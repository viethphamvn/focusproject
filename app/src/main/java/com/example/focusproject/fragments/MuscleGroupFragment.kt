package com.example.focusproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.focusproject.ChatWindowActivity
import com.example.focusproject.CreateRoutineActivity

import com.example.focusproject.R
import com.example.focusproject.RoutineEditActivity
import com.example.focusproject.adapters.ExerciseRecyclerViewAdapter
import com.example.focusproject.models.Exercise
import com.example.focusproject.models.Routine
import com.example.focusproject.models.User
import com.example.focusproject.tools.CreateExercise
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_muscle_group.view.*

private const val MUSCL_GRP = "param1"
private const val SELF_CREATED = "param2"

class MuscleGroupFragment : Fragment() {
    private var docName: String? = ""
    private var self: Boolean? = false
    private lateinit var exerciseRecyclerViewAdapter: ExerciseRecyclerViewAdapter
    private var exercises: ArrayList<Exercise> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            docName = if (it.getString(MUSCL_GRP) != null) it.getString(MUSCL_GRP) else ""
            self = if (it.getBoolean(SELF_CREATED) != null) it.getBoolean(SELF_CREATED) else false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Get data
        getItemFromDatabase()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_muscle_group, container, false)
        view.exerciseItemRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            exerciseRecyclerViewAdapter = ExerciseRecyclerViewAdapter(exercises) { item -> doClick(item)}
            adapter = exerciseRecyclerViewAdapter
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        getItemFromDatabase()
    }


    //If user select an exercise
    private fun doClick(item: Exercise){
        when (activity) {
            is CreateRoutineActivity -> {
                (activity as CreateRoutineActivity).onItemClick(item)
            }
            is RoutineEditActivity -> {
                (activity as RoutineEditActivity).onItemClick(item)
            }
            is ChatWindowActivity -> {
                (activity as ChatWindowActivity).itemOnClick(item)
            }
        }
    }

    private fun getItemFromDatabase(){
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("Exercise")
            .get()
            .addOnSuccessListener { result ->
                exercises.clear()
                exerciseRecyclerViewAdapter.notifyDataSetChanged()
                for (document in result){
                    if (document.get("type") as String == docName && !self!!) {
                        val exercise = CreateExercise.createExercise(document)
                        exercises.add(exercise)
                        exerciseRecyclerViewAdapter.notifyItemInserted(exercises.size - 1)
                    } else if (self!! && document.get("createdBy") as String == User.currentUser.id && !(document.get("isRestTime") as Boolean)){
                        val exercise = CreateExercise.createExercise(document)
                        exercises.add(exercise)
                        exerciseRecyclerViewAdapter.notifyItemInserted(exercises.size - 1)
                    }
                }
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(doc_name: String, self: Boolean) =
            MuscleGroupFragment().apply {
                arguments = Bundle().apply {
                    putString(MUSCL_GRP, doc_name)
                    putBoolean(SELF_CREATED, self)
                }
            }
    }
}
