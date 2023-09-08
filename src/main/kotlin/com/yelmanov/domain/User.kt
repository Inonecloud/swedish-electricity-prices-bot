package com.yelmanov.domain

data class User(
    val chatId: String,
    val username: String
){
    lateinit var region:Regions
}
