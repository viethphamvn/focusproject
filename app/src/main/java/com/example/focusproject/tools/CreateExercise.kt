package com.example.focusproject.tools

import com.example.focusproject.models.Exercise
import com.google.firebase.firestore.DocumentSnapshot

class CreateExercise {
    companion object {
        fun createExercise(it: DocumentSnapshot): Exercise{
            val name = it.get("name") as String
            val type = it.get("type") as String
            val duration = it.get("duration") as Long
            val equipmentNeeded = it.get("equipmentNeeded") as Boolean
            val img = it.get("img") as String
            val isRestTime = it.get("isRestTime") as Boolean
            val isTime = it.get("isTimed") as Boolean
            val rep = it.get("rep") as Long
            val weight = it.get("weight") as Long
            val vidId = it.get("vidId") as String
            val createdBy = it.get("createdBy") as String
            val desc = it.get("desc") as String
            val uid = it.get("uid") as String
            return Exercise(
                name,
                type,
                uid,
                duration,
                img,
                vidId,
                isRestTime,
                isTime,
                rep,
                equipmentNeeded,
                weight,
                createdBy,
                desc
            )
        }

        fun createExercise(it: DocumentSnapshot, uid: String): Exercise{
            val name = it.get("name") as String
            val type = it.get("type") as String
            val duration = it.get("duration") as Long
            val equipmentNeeded = it.get("equipmentNeeded") as Boolean
            val img = it.get("img") as String
            val isRestTime = it.get("isRestTime") as Boolean
            val isTime = it.get("isTimed") as Boolean
            val rep = it.get("rep") as Long
            val weight = it.get("weight") as Long
            val vidId = it.get("vidId") as String
            val createdBy = it.get("createdBy") as String
            val desc = it.get("desc") as String
            return Exercise(
                name,
                type,
                uid,
                duration,
                img,
                vidId,
                isRestTime,
                isTime,
                rep,
                equipmentNeeded,
                weight,
                createdBy,
                desc
            )
        }

        fun createReadyExercise(duration: Long, upcomingExercise: Exercise): Exercise{
            return Exercise("GET READY","","",duration,upcomingExercise.img,upcomingExercise.vidId,true,true,upcomingExercise.rep, false, upcomingExercise.weight, upcomingExercise.createdBy, upcomingExercise.desc)
        }
    }
}