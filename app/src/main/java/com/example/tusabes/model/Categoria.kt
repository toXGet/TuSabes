package com.example.tusabes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categoria")
data class Categoria(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var nombre: String
)
