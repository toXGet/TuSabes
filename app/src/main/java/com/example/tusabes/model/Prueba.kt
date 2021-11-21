package com.example.tusabes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "prueba")
data class Prueba(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var resultado: Int,
    val idUser: Int,
    var fecha: Instant,
    var preguntas: String
)
