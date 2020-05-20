package com.example.focusproject.tools

import com.example.focusproject.models.Exercise
import com.google.firebase.firestore.DocumentSnapshot

class CreateExercise {
    companion object {
        fun createExercise(it: DocumentSnapshot): Exercise{
            var name = it.get("name") as String
            var type = it.get("type") as String
            var duration = it.get("duration") as Long
            var equipmentNeeded = it.get("equipmentNeeded") as Boolean
            var img = it.get("img") as String
            var isRestTime = it.get("isRestTime") as Boolean
            var isTime = it.get("isTimed") as Boolean
            var rep = it.get("rep") as Long
            var weight = it.get("weight") as Long
            var vidId = it.get("vidId") as String
            var createdBy = it.get("createdBy") as String
            var desc = it.get("desc") as String
            var uid = it.get("uid") as String
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
            var name = it.get("name") as String
            var type = it.get("type") as String
            var duration = it.get("duration") as Long
            var equipmentNeeded = it.get("equipmentNeeded") as Boolean
            var img = it.get("img") as String
            var isRestTime = it.get("isRestTime") as Boolean
            var isTime = it.get("isTimed") as Boolean
            var rep = it.get("rep") as Long
            var weight = it.get("weight") as Long
            var vidId = it.get("vidId") as String
            var createdBy = it.get("createdBy") as String
            var desc = it.get("desc") as String
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
    }
}