package com.example.focusproject.models

import java.io.Serializable

class Exercise(name: String, type: String, uid: String, duration: Long, img: String, vidId: String, isRestTime: Boolean,
               isTimed: Boolean, rep:Long, equipmentNeeded: Boolean, weight: Long, createdBy: String, desc: String) : Serializable{
    var name = name
    var uid = uid
    var img = img
    var vidId = vidId
    var isRestTime = isRestTime
    var isTimed = isTimed
    var rep = rep
    var equipmentNeeded = equipmentNeeded
    var weight = weight
    var duration = duration
    var type = type
    var createdBy = createdBy
    var desc = desc
}