package com.example.tusabes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tusabes.model.Prueba

@Dao
interface PruebaDAO {
    // (C)rud
    @Insert
    fun insert(prueba: Prueba)

    // c(R)ud
    @Query("SELECT * FROM prueba")
    fun getAll() : LiveData<List<Prueba>> // No se va a usar al final en la app es una función administrativa para saber cuántas pruebas se han hecho.

    @Query("SELECT * FROM prueba WHERE id = :id")
    fun getPrueba(id: Int) : Prueba

    @Query("SELECT * FROM prueba WHERE idUser = :idUser")
    fun getPruebaByUser(idUser: Int) : List<Prueba>

    // cr(U)d
    @Update
    fun actualizar(prueba: Prueba)

    // cru(D)
    @Delete
    fun eliminar(prueba: Prueba)
}