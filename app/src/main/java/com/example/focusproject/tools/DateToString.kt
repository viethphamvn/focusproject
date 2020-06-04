package com.example.focusproject.tools

class DateToString {

    companion object {
        fun toString(date : Int):String{
            return when (date) {
                2 -> "mon"
                3 -> "tue"
                4 -> "wed"
                5 -> "thu"
                6 -> "fri"
                7 -> "sat"
                else -> "sun"
            }
        }
    }

}