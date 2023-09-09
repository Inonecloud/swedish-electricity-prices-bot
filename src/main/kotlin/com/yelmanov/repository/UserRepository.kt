package com.yelmanov.repository

import com.yelmanov.domain.User


interface UserRepository {

    fun getUserByChatId(chatId: Long): User

    fun save(user: User)

    fun findAll():List<User>
}