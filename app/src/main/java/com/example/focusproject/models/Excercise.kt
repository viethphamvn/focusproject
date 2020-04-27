package com.example.focusproject.models

import com.bumptech.glide.Glide.init
import java.lang.reflect.Array.get
import java.lang.reflect.Array.set

class Excercise(name: String, code: String, img: String){
    var name: String
    var code: String
    var img: String

    init{
        this.name = name
        this.code = code
        this.img = img
    }
}