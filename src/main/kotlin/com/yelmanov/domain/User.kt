package com.yelmanov.domain

data class User(
    val chatId: Long,
    val username: String
){
    lateinit var region:Regions
}
