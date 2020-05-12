package com.example.focusproject.models

import java.io.Serializable

class Excercise(name: String, type: String, uid: String, duration: Long,  img: String, vidId: String, isRestTime: Boolean,
                isTimed: Boolean, rep:Long, equipmentNeeded: Boolean, weight: Long) : Serializable{
    var name: String = name
    var uid: String = uid
    var img: String = img
    var vidId: String = vidId
    var isRestTime: Boolean = isRestTime
    var isTimed: Boolean = isTimed
    var rep: Long = rep
    var equipmentNeeded: Boolean = equipmentNeeded
    var weight: Long = weight
    var duration: Long = duration
    var type: String = type
}