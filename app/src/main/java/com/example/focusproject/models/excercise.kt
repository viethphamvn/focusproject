package com.example.focusproject.models

class excercise {
    private var name: String
    private var code: String

    get() = field

    set(value) {
        field = value
    }

    constructor(name:String, code:String){
        this.name = name
        this.code = code
    }
}