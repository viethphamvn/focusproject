package com.example.focusproject.tools

import com.example.focusproject.models.Routine
import com.google.firebase.firestore.DocumentSnapshot

class CreateRoutine {
    companion object {
        fun createRoutine(it: DocumentSnapshot): Routine{
            var routine = Routine()
            routine.createdBy = it.get("createdBy").toString()
            routine.createdOn = it.get("createdOn") as Long
            routine.name = it.get("name").toString()
            routine.exerciseList = it.get("exercises") as ArrayList<String>
            return routine
        }
    }
}