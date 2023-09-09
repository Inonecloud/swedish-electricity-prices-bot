package com.yelmanov.service

import com.yelmanov.domain.User
import com.yelmanov.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun saveUser(user: User) {
        userRepository.save(user)
    }

    fun getUserByChatId(chatId: Long): User {
        return userRepository.getUserByChatId(chatId)
    }

}