package com.dongbin.maple6thcalculator.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import org.jetbrains.annotations.NotNull

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: UserInfo)

    @Query("SELECT * FROM user WHERE name = :name")
    fun getUserByName(name: String): UserInfo

    @Query("DELETE FROM user")
    fun deleteAll()

    @Query("SELECT * FROM user")
    fun getUser(): List<UserInfo>
}