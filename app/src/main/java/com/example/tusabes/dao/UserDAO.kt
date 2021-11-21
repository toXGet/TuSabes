package com.example.tusabes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tusabes.model.User

@Dao
interface UserDAO {
    // (C)rud
    @Insert
    fun insert(user: User)

    // c(R)ud
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>> //No se va a usar al final en la app es una función administrativa para saber cuántos usuarios hay

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Int): User

    @Query("SELECT * FROM user WHERE usuario = :usuario")
    fun getUserByNick(usuario: String): User

    // cr(U)d
    @Update
    fun actualizar(user: User)

    // cru(D)
    @Delete
    fun eliminar(user: User)
}