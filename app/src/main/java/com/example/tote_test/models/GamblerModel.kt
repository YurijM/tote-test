package com.example.tote_test.models

import com.example.tote_test.utils.EMPTY

data class GamblerModel(
    val id: String = "",
    val email: String = "",
    val nickname: String = "",
    val family: String = "",
    val name: String = "",
    val gender: String = "",
    val photoUrl: String = EMPTY, // для Picasso поле не должно быть пустым
    val stake: Int = 0,
    val points: Double = 0.00,
    val placePrev: Int = 0,
    val place: Int = 1,
    val isAdmin: Boolean = false,
)