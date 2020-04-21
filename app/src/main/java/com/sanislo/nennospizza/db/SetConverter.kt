package com.sanislo.nennospizza.db

import androidx.room.TypeConverter

class SetConverter {

    @TypeConverter
    fun toSet(setString: String): Set<Int> {
        return setString.split(",")
            .map { it.toInt() }
            .toSet()
    }

    @TypeConverter
    fun fromSet(set: Set<Int>): String {
        return set.joinToString(",")
    }
}