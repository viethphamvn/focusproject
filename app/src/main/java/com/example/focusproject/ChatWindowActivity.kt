package com.example.focusproject

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.focusproject.fragments.RoutineAndExercisePickerFragment
import com.example.focusproject.models.Chat
import com.example.focusproject.models.Exercise
import com.example.focusproject.models.Routine
import com.example.focusproject.models.User
import com.example.focusproject.tools.CreateExercise
import com.example.focusproject.tools.CreateRoutine
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class ChatWindowActivity : AppCompatActivity() {
    private var recipient : User? = null
    private var chatList = ArrayList<Chat>()
    lateinit var chatListener : ChildEventListener
    val realtimeDatabase = FirebaseDatabase.getInstance()
    private lateinit var conversationRecyclerView : RecyclerView
    private lateinit var conversationRecyclerViewAdapter: GroupAdapter<GroupieViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_window)

        conversationRecyclerViewAdapter = GroupAdapter<GroupieViewHolder>()
        conversationRecyclerView = findViewById(R.id.conversation_list_recycler_view)
        conversationRecyclerView.adapter = conversationRecyclerViewAdapter
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        conversationRecyclerView.layoutManager = linearLayoutManager

        val messageEditText = findViewById<EditText>(R.id.messageEditText)
        val showRoutineButton = findViewById<ImageButton>(R.id.moreRoutineButton).setOnClickListener {
            val routineAndExercisePickerFragment =
                supportFragmentManager.findFragmentByTag("pickerFragment")
            if (routineAndExercisePickerFragment != null) {
                supportFragmentManager.beginTransaction()
                    .remove(routineAndExercisePickerFragment)
                    .commit()
                val display = windowManager.defaultDisplay
                val layout = findViewById<FrameLayout>(R.id.exercisePickerContainer)
                var screenHeight = display.height
                screenHeight = 0
                val parms = layout.layoutParams
                parms.height = screenHeight
                layout.layoutParams = parms
            } else {
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.exercisePickerContainer,
                        RoutineAndExercisePickerFragment.newInstance(),
                        "pickerFragment"
                    )
                    .commit()

                val display = windowManager.defaultDisplay
                val layout = findViewById<FrameLayout>(R.id.exercisePickerContainer)
                var screen_height = display.height
                screen_height = (0.60 * screen_height).toInt()
                val parms = layout.layoutParams
                parms.height = screen_height
                layout.layoutParams = parms
            }
        }

        messageEditText.setOnClickListener {
            //Collapse Exercise Picker when Kayboard is activiated

            val excercisePickerFragment =
                supportFragmentManager.findFragmentByTag("pickerFragment")
            if (excercisePickerFragment != null) {
                supportFragmentManager.beginTransaction()
                    .remove(excercisePickerFragment)
                    .commit()
                val display = windowManager.defaultDisplay
                val layout = findViewById<FrameLayout>(R.id.exercisePickerContainer)
                var screen_height = display.height
                screen_height = 0
                val parms = layout.layoutParams
                parms.height = screen_height
                layout.layoutParams = parms
            }
        }

        //Get User Info from intent
        if (intent.getSerializableExtra("user") != null){
            recipient = intent.getSerializableExtra("user") as User
        }

        //Change TextView to display user's name
        if (recipient != null){
            findViewById<TextView>(R.id.username_textview).text = recipient!!.username

            //Get chat
            fetchChat()
        }

        //handle send button
        findViewById<MaterialButton>(R.id.sendButton).setOnClickListener {
            //Get sent message
            val message = Chat(System.currentTimeMillis(), User.currentUser.id, recipient!!.id, messageEditText.text.toString())
            //clear text in edittext
            messageEditText.text.clear()
            sendMessage(message)
        }


    }

    private fun sendMessage(message : Chat){
        //send message to database
        realtimeDatabase.getReference("/inbox/${recipient!!.id}/${User.currentUser.id}").push().setValue(message)
        realtimeDatabase.getReference("/inbox/${User.currentUser.id}/${recipient!!.id}").push().setValue(message)

        //Update Latest Message for current user and the recipient
        //For Current User
        realtimeDatabase.getReference("/latest/${User.currentUser.id}/${recipient!!.id}").setValue(message)
        //For Recipient
        realtimeDatabase.getReference("/latest/${recipient!!.id}/${User.currentUser.id}").setValue(message)
    }

    private fun fetchChat() {
        //Fetch chat
        chatListener = realtimeDatabase.getReference("/inbox/${User.currentUser.id}/${recipient!!.id}")
            .addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                println("here")
                val chat = p0.getValue(Chat::class.java)
                if (chat != null) {
                    chatList.add(chat)
                    if (chat.sender == recipient!!.id){
                        conversationRecyclerViewAdapter.add(ChatInItem(recipient!!.profilePictureUri, this@ChatWindowActivity, chat))
                    } else {
                        conversationRecyclerViewAdapter.add(ChatOutItem(User.currentUser.profilePictureUri, this@ChatWindowActivity, chat))
                    }
                    conversationRecyclerView.smoothScrollToPosition(conversationRecyclerViewAdapter.itemCount)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
    }

    fun itemOnClick(item : Exercise){
        //extract info to create a chat object
        var message = Chat()
        message.timeStamp = System.currentTimeMillis()
        message.sender = User.currentUser.id
        message.recipient = recipient!!.id
        message.content = "${Chat.EXERCISE_TAG}${item.uid}${Chat.EXERCISE_TAG}"
        sendMessage(message)
    }

    fun itemOnClick(item : Routine){
        //extract info to create a chat object
        var message = Chat()
        message.timeStamp = System.currentTimeMillis()
        message.sender = User.currentUser.id
        message.recipient = recipient!!.id
        message.content = "${Chat.ROUTINE_TAG}${item.id}${Chat.ROUTINE_TAG}"
        sendMessage(message)
    }

    override fun onStop() {
        super.onStop()
        realtimeDatabase.getReference("/inbox/${User.currentUser.id}/${recipient!!.id}").removeEventListener(chatListener)
    }
}

class ChatInItem(private val imageUrl: String, val context: Context, val chat : Chat) : Item<GroupieViewHolder>(){
    var exercise : Exercise? = null
    var routine : Routine? = null
    var layoutType = -1

    override fun getLayout(): Int {
        //Identify chat type (Text or Media)
        return when {
            chat.content.startsWith(Chat.EXERCISE_TAG) -> {
                layoutType = 1
                R.layout.exercise_item_simplified_left
            }
            chat.content.startsWith(Chat.ROUTINE_TAG) -> {
                layoutType = 2
                R.layout.routine_item_simplified_left
            }
            else -> {
                layoutType = 3
                R.layout.inchat
            }
        }
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if (layoutType < 3){
            var source = ""
            source = if (layoutType == 1){
                "Exercise"
            } else {
                "Routines"
            }
            val id = chat.content.substring(3, chat.content.length - 3)
            println(id)
            FirebaseFirestore.getInstance().collection(source).document(id)
                .get()
                .addOnSuccessListener {
                    if (it.data != null){
                        if (layoutType == 1){
                            exercise = CreateExercise.createExercise(it)
                            //Display
                            var url = ""
                            if (exercise!!.img != ""){
                                url = exercise!!.img
                            } else if (exercise!!.vidId != ""){
                                url = "https://img.youtube.com/vi/" + exercise!!.vidId + "/0.jpg"
                            }

                            Glide.with(context)
                                .load(url)
                                .centerCrop()
                                .into(viewHolder.itemView.findViewById<CircleImageView>(R.id.exerciseImageHolder))
                            viewHolder.itemView.findViewById<TextView>(R.id.exerciseNameTextView).text = exercise!!.name
                        } else {
                            routine = CreateRoutine.createRoutine(it)
                            viewHolder.itemView.findViewById<TextView>(R.id.routine_name).text = routine!!.name
                        }
                    }
                }
        } else {
            Glide.with(context)
                .load(imageUrl)
                .centerCrop()
                .into(viewHolder.itemView.findViewById<CircleImageView>(R.id.user_profile_picture))

            viewHolder.itemView.findViewById<TextView>(R.id.chatTextView).text = chat.content
        }
    }
}

class ChatOutItem(private val imageUrl: String, val context: Context, val chat : Chat) : Item<GroupieViewHolder>(){
    var exercise : Exercise? = null
    var routine : Routine? = null
    var layoutType = -1

    override fun getLayout(): Int {
        //Identify chat type (Text or Media)
        return when {
            chat.content.startsWith(Chat.EXERCISE_TAG) -> {
                layoutType = 1
                R.layout.exercise_item_simplified_right
            }
            chat.content.startsWith(Chat.ROUTINE_TAG) -> {
                layoutType = 2
                R.layout.routine_item_simplified_right
            }
            else -> {
                layoutType = 3
                R.layout.outchat
            }
        }
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if (layoutType < 3){
            var source = ""
            source = if (layoutType == 1){
                "Exercise"
            } else {
                "Routines"
            }
            val id = chat.content.substring(3, chat.content.length - 3)
            println(id)
            FirebaseFirestore.getInstance().collection(source).document(id)
                .get()
                .addOnSuccessListener {
                    if (it.data != null){
                        if (layoutType == 1){
                            exercise = CreateExercise.createExercise(it)
                            //Display
                            var url = ""
                            if (exercise!!.img != ""){
                                url = exercise!!.img
                            } else if (exercise!!.vidId != ""){
                                url = "https://img.youtube.com/vi/" + exercise!!.vidId + "/0.jpg"
                            }

                            Glide.with(context)
                                .load(url)
                                .centerCrop()
                                .into(viewHolder.itemView.findViewById<CircleImageView>(R.id.exerciseImageHolder))
                            viewHolder.itemView.findViewById<TextView>(R.id.exerciseNameTextView).text = exercise!!.name
                        } else {
                            routine = CreateRoutine.createRoutine(it)
                            viewHolder.itemView.findViewById<TextView>(R.id.routine_name).text = routine!!.name
                        }
                    }
                }
        } else {
            Glide.with(context)
                .load(imageUrl)
                .centerCrop()
                .into(viewHolder.itemView.findViewById<CircleImageView>(R.id.user_profile_picture))

            viewHolder.itemView.findViewById<TextView>(R.id.chatTextView).text = chat.content
        }
    }
}