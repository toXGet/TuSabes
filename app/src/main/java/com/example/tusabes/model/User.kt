package com.example.tusabes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var usuario: String,
    var clave: String,
    var email: String,
    var nombres: String,
    var apellidos: String,
    var rol: String
)
