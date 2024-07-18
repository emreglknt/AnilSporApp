package com.example.recipeapppaparaproject.data.model

import java.io.Serializable


data class Player(
    val name: String = "",
    val surname: String = "",
    val age: String = "",
    val position: String = "",
    val homeTown: String = "",
    val formanumber:String ="",
    val goals: String ="",
    val pp : String =""
): Serializable
