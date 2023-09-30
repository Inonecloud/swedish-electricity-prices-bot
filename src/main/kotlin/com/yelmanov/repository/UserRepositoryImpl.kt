package com.yelmanov.repository

import com.yelmanov.domain.User
import org.springframework.stereotype.Component


@Component
class UserRepositoryImpl : UserRepository {
    val users: MutableList<User> = mutableListOf()
    override fun getUserByChatId(chatId: Long): User {
        return users.first { it.chatId == chatId }
    }

    override fun save(user: User) {
        synchronized(users) {
            for (u: User in users) {
                if (user.chatId == u.chatId) {
                    users.remove(u)
                    break
                }
            }
            users.add(user)
        }
    }

    override fun findAll(): List<User> {
        synchronized(users) {
            return users.toList()
        }
    }
}