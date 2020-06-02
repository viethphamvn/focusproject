package com.example.focusproject

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.M
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.security.Permission
import java.util.jar.Manifest

private const val REQUEST_TAKE_IMAGE_CODE = 250
private const val REQUEST_PICK_IMAGE_CODE = 270
private const val READ_EX_STORAGE_PERMISSION_CODE = 1000

class UserInfoActivity : AppCompatActivity() {
    private lateinit var imageView : ImageView
    private lateinit var profilePictureView: ImageView
    private lateinit var welcomeTextView: TextView
    private var username = FirebaseAuth.getInstance().currentUser?.displayName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        imageView = findViewById(R.id.imageView1)
        profilePictureView = findViewById(R.id.user_profile_picture)
        welcomeTextView = findViewById(R.id.welcomeTextView)

        findViewById<Button>(R.id.changeProfilePictureBtn).setOnClickListener {
            showDialog()
        }
        setUpViews()
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.image_source_pick_dialog)
        val cameraButton = dialog.findViewById(R.id.cameraBtn) as Button
        val picker = dialog.findViewById(R.id.imagePickerBtn) as Button
        cameraButton.setOnClickListener {
            takePictureIntent()
            dialog.cancel()
        }
        picker.setOnClickListener {
            pickImageIntent()
            dialog.cancel()
        }
        dialog.show()
    }

    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureCaptureIntent ->
            pictureCaptureIntent.resolveActivity(this.packageManager).also {
                startActivityForResult(pictureCaptureIntent, REQUEST_TAKE_IMAGE_CODE)
            }
        }
    }

    private fun pickImageIntent(){
        //Check permission if user want to pick their own picture
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            //Permission Deny
            val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            requestPermissions(permissions, READ_EX_STORAGE_PERMISSION_CODE)
        } else {
            Intent(Intent.ACTION_PICK).also { picturePickIntent ->
                picturePickIntent.setType("image/*").also {
                    startActivityForResult(picturePickIntent, REQUEST_PICK_IMAGE_CODE)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode){
            READ_EX_STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission granted
                    pickImageIntent()
                } else {
                    //Permission Deny
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_TAKE_IMAGE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        val imageBitmap = data.extras?.get("data") as Bitmap
                        if (imageBitmap != null) {
                            uploadProfilePicture(imageBitmap)
                        }
                    }
                }
            }

            REQUEST_PICK_IMAGE_CODE -> {
                if (data != null) {
                    val imagePath = data.data
                    if (imagePath != null) {
                        val imageSource = ImageDecoder.createSource(this.contentResolver, imagePath)
                        val imageBitmap = ImageDecoder.decodeBitmap(imageSource)
                        if (imageBitmap != null) {
                            uploadProfilePicture(imageBitmap)
                        }
                    }
                }
            }

            else -> {
                return
            }
        }
    }

    private fun uploadProfilePicture(imageBitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("profilePics/${FirebaseAuth.getInstance().currentUser?.uid}")
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val compressedImage = byteArrayOutputStream.toByteArray()
        storageRef.putBytes(compressedImage).addOnSuccessListener {task ->
            if (task.task.isSuccessful){
                storageRef.downloadUrl.addOnCompleteListener {
                    it.result?.let { uri ->
                        setProfileImage(uri)
                    }
                    Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {e->
            println(e.message)
        }
    }

    private fun setProfileImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .into(profilePictureView)
    }

    private fun setUpViews() {
        if (username != null){
            welcomeTextView.text = "Welcome $username, get ready to stay #focus"
        } else {
//            Toast.makeText(this, "Sorry, an error has occurred!", Toast.LENGTH_LONG).show()
//            FirebaseAuth.getInstance().signOut()
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
        }

        //Set up welcome imageview
        val imageLink = "https://media.giphy.com/media/xThtama8b8ZGtnMeuQ/giphy.gif"
        Glide.with(this)
            .load(imageLink)
            .fitCenter()
            .into(imageView)
    }
}