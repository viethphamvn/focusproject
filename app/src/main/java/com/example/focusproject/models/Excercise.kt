package com.example.focusproject.models

import com.bumptech.glide.Glide.init
import java.io.Serializable
import java.lang.reflect.Array.get
import java.lang.reflect.Array.set

class Excercise(name: String, code: String, duration: Int,  img: String, isRestTime: Boolean) : Serializable{
    var name: String
    var code: String
    var img: String
    var isRestTime: Boolean
//    var setnrep: HashMap<Int, Int>
//    var weight: HashMap<Int, Int>
    var duration: Int

    init{
        this.name = name
        this.code = code
        this.img = img
        this.isRestTime = isRestTime
//        this.setnrep = HashMap<Int, Int>(setnrep)
//        this.weight = HashMap(weight)
        this.duration = duration
    }
}