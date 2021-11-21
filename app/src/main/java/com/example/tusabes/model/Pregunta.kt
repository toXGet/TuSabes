package com.example.tusabes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pregunta")
data class Pregunta(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var enunciado: String,
    var opcion1: String,
    var opcion2: String,
    var opcion3: String,
    var opcion4: String,
    var opcion5: String,
    var respuesta: Int,
    var idCategoria: Int
)
