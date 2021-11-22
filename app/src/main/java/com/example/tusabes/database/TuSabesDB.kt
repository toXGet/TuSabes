package com.example.tusabes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tusabes.Convertidores
import com.example.tusabes.dao.CategoriaDAO
import com.example.tusabes.dao.PreguntaDAO
import com.example.tusabes.dao.PruebaDAO
import com.example.tusabes.dao.UserDAO
import com.example.tusabes.model.Categoria
import com.example.tusabes.model.Pregunta
import com.example.tusabes.model.Prueba
import com.example.tusabes.model.User

@Database(entities = [User::class, Pregunta::class, Categoria::class, Prueba::class], version = 1)
@TypeConverters(Convertidores::class)
abstract class TuSabesDB : RoomDatabase() {
    // Las operaciones que voy a tener en la DB
    abstract fun UsersDAO() : UserDAO
    abstract fun PreguntasDAO() : PreguntaDAO
    abstract fun CategoriasDAO() : CategoriaDAO
    abstract fun PruebasDAO() : PruebaDAO

    // Instanciamiento de la DB
    companion object{
        @Volatile
        private var INSTANCE : TuSabesDB? = null

        fun getDataBase(context: Context) : TuSabesDB{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TuSabesDB::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}