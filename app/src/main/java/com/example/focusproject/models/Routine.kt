package com.example.focusproject.models

class Routine : Comparable<Routine> {
    var exerciseList: ArrayList<String> = ArrayList()
    var createdOn: Long = 0

    override fun compareTo(other: Routine): Int {
        return COMPARATOR.compare(this, other)
    }

    companion object {
        private var COMPARATOR =
            Comparator.comparingLong<Routine>{it.createdOn}
    }
}