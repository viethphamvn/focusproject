package com.example.focusproject.models

import java.io.Serializable

class Excercise(name: String, uid: String, duration: Long,  img: String, vidId: String, isRestTime: Boolean,
                isTimed: Boolean, rep:Long, equipmentNeeded: Boolean, weight: Long) : Serializable{
    var name: String
    var uid: String
    var img: String
    var vidId: String
    var isRestTime: Boolean
    var isTimed: Boolean
    var rep: Long
    var equipmentNeeded: Boolean
    var weight: Long
    var duration: Long

    init{
        this.name = name
        this.uid = uid
        this.img = img
        this.isRestTime = isRestTime
        this.vidId = vidId
        this.rep = rep
        this.weight = weight
        this.duration = duration
        this.isTimed = isTimed
        this.equipmentNeeded = equipmentNeeded
    }
}