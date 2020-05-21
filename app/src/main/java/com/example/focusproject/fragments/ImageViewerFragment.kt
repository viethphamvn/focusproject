package com.example.focusproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.example.focusproject.R
import kotlinx.android.synthetic.main.fragment_image_viewer.view.*

private const val IMAGE_URL = "param1"

class ImageViewerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var imgUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imgUrl = it.getString(IMAGE_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_image_viewer, container, false)
        Glide.with(this)
            .load(imgUrl)
            .centerCrop()
            .into(view.workoutImageView)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(imgUrl: String) =
            ImageViewerFragment().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_URL, imgUrl)
                }
            }
    }
}
