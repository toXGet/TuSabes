package com.example.tusabes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tusabes.model.Categoria

@Dao
interface CategoriaDAO {
    // (C)rud
    @Insert
    fun insert(categoria: Categoria)

    // c(R)ud
    @Query("SELECT * FROM categoria")
    fun getAll() : LiveData<List<Categoria>>

    @Query("SELECT * FROM categoria WHERE id = :id")
    fun getCategoria(id: Int) : Categoria

    @Query("SELECT * FROM categoria WHERE nombre = :nombre")
    fun getCategoriaPorNombre(nombre: String) : Categoria

    // cr(U)d
    @Update
    fun actualizar(categoria: Categoria)

    // cru(D)
    @Delete
    fun eliminar(categoria: Categoria)

    // Transacción para definir las relaciones con preguntas
    //@Transaction
}