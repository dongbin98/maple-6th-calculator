package com.dongbin.maple6thcalculator.data

import androidx.annotation.WorkerThread

class UserRepository(private val userDao: UserDao) {

    lateinit var user: UserInfo

    @WorkerThread
    fun insert(user: UserInfo) {
        userDao.deleteAll()
        userDao.insertUser(user)
    }

    @WorkerThread
    fun select(name: String) {
        user = userDao.getUserByName(name)
    }

    @WorkerThread
    fun selectAll(): String? {
        val list = userDao.getUser()
        return if (list.isNotEmpty()) list[0].name
        else null
    }
}