package com.example.focusproject.models

import com.bumptech.glide.Glide.init
import java.io.Serializable
import java.lang.reflect.Array.get
import java.lang.reflect.Array.set

class Excercise(name: String, code: String, duration: Int,  img: String, vidUrl: String, isRestTime: Boolean, isTimed: Boolean, set:Int) : Serializable{
    var name: String
    var code: String
    var img: String
    var vidUrl: String
    var isRestTime: Boolean
    var isTimed: Boolean
    var set: Int
//    var weight: HashMap<Int, Int>
    var duration: Long

    init{
        this.name = name
        this.code = code
        this.img = img
        this.isRestTime = isRestTime
        this.vidUrl = vidUrl
        this.set = set
//        this.weight = HashMap(weight)
        this.duration = (duration*1000).toLong()
        this.isTimed = isTimed
    }
}