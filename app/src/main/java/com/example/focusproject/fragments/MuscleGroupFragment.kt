package com.example.focusproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.GridLayoutManager
import com.example.focusproject.CreateExcerciseActivity

import com.example.focusproject.R
import com.example.focusproject.RoutineEditActivity
import com.example.focusproject.adapters.ExcerciseRecyclerViewAdapter
import com.example.focusproject.models.Excercise
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_arms.view.excerciseItemRecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val MUSCL_GRP = "param1"

class MuscleGroupFragment : Fragment() {
    private var doc_name: String = ""
    private lateinit var excerciseRecyclerViewAdapter: ExcerciseRecyclerViewAdapter
    private var Excercises: ArrayList<Excercise> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            doc_name = it.getString(MUSCL_GRP)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Get data
        getItemFromDatabase()
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_arms, container, false)
        view.excerciseItemRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            excerciseRecyclerViewAdapter = ExcerciseRecyclerViewAdapter(Excercises) { item -> doClick(item)}
            adapter = excerciseRecyclerViewAdapter
        }
        return view
    }

    fun doClick(item: Excercise){
        if (activity is CreateExcerciseActivity){
            (activity as CreateExcerciseActivity).onItemClick(item)
        } else if (activity is RoutineEditActivity){
            (activity as RoutineEditActivity).onItemClick(item)
        }
    }

    private fun getItemFromDatabase(){
        var firestore = FirebaseFirestore.getInstance()
        firestore.collection(doc_name)
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    var excercise = Excercise(
                        document.data.get("name").toString(),
                    document.data.get("uid").toString(),
                    document.data.get("duration") as Long,
                        document.data.get("img").toString(),
                        document.data.get("vidId").toString(),
                        document.data.get("isRestTime") as Boolean,
                        document.data.get("isTimed") as Boolean,
                        document.data.get("rep") as Long,
                        document.data.get("equipmentNeeded") as Boolean,
                        document.data.get("weight") as Long)
                    Excercises.add(excercise)
                    excerciseRecyclerViewAdapter.notifyItemInserted(Excercises.size-1)
                }
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(doc_name: String) =
            MuscleGroupFragment().apply {
                arguments = Bundle().apply {
                    putString(MUSCL_GRP, doc_name)
                }
            }
    }
}
