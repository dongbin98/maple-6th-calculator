package com.dongbin.maple6thcalculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserInfo(
    @PrimaryKey
    val name: String,
    val level: Int,
    val job: String,
    val image: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserInfo

        if (name != other.name) return false
        if (level != other.level) return false
        if (job != other.job) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + level
        result = 31 * result + job.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }

    override fun toString(): String {
        return super.toString()
    }
}