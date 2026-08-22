package com.mafiagame.freemium.model

data class Player(
    val id: String,
    val name: String,
    var isAlive: Boolean = true,
    var role: Role? = null,
    var isProtected: Boolean = false,
    var isDoused: Boolean = false, // for Arsonist
    var votes: Int = 0
)