package com.example.tusabes.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoriaPregunta(
    @Embedded val categoria: Categoria,
    @Relation(
        parentColumn = "id",
        entityColumn = "idCategoria"
    )
    val pregunta: List<Pregunta>
)
