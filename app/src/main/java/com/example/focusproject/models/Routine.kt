package com.example.focusproject.models

import java.io.Serializable

class Routine : Comparable<Routine>, Serializable {
    var exerciseList: ArrayList<String> = ArrayList()
    var createdOn: Long = 0
    var name: String = ""
    var createdBy: String = ""
    var id: String = ""

    override fun compareTo(other: Routine): Int {
        return COMPARATOR.compare(this, other)
    }

    companion object {
        var COMPARATOR: Comparator<Routine> =
            Comparator.comparingLong{it.createdOn}
    }
}