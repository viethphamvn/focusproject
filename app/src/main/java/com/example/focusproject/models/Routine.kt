package com.example.focusproject.models

class Routine : Comparable<Routine> {
    var exerciseList: ArrayList<String> = ArrayList()
    var createdOn: Long = 0
    var name: String = ""
    var createdBy: String = ""

    override fun compareTo(other: Routine): Int {
        return COMPARATOR.compare(this, other)
    }

    companion object {
        var COMPARATOR: Comparator<Routine> =
            Comparator.comparingLong{it.createdOn}
    }
}