package com.example.tusabes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tusabes.model.Pregunta

@Dao
interface PreguntaDAO {
    // (C)rud
    @Insert
    fun insert(pregunta: Pregunta)

    // c(R)ud
    @Query("SELECT * FROM pregunta")
    fun getAll() : LiveData<List<Pregunta>>

    @Query("SELECT * FROM pregunta WHERE id = :id")
    fun getPregunta(id: Int) : Pregunta

    @Query("SELECT * FROM pregunta WHERE idCategoria = :idCategoria")
    fun getPreguntaPorCategoria(idCategoria: Int) : List<Pregunta>

    // cr(U)d
    @Update
    fun actualizar(pregunta: Pregunta)

    // cru(D)
    @Delete
    fun eliminar(pregunta: Pregunta)
}