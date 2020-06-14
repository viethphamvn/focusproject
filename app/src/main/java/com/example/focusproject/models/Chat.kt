package com.example.focusproject.models

class Chat (var timeStamp : Long, var sender: String, var recipient: String, var content: String) : Comparable<Chat>{
    constructor() : this(-1,"","","")

    override fun compareTo(other: Chat): Int {
        return COMPARATOR.compare(this, other)
    }

    companion object {
        var COMPARATOR: Comparator<Chat> =
            Comparator.comparingLong{it.timeStamp}
    }
}