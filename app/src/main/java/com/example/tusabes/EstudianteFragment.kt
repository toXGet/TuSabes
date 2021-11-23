package com.example.tusabes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tusabes.database.TuSabesDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstudianteFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmento = inflater.inflate(R.layout.fragment_estudiante, container, false)

        val context = activity?.applicationContext
        CoroutineScope(Dispatchers.IO).launch{
            val database = context?.let { TuSabesDB.getDataBase(it)}
            if (database != null){
                val datos = database.UsersDAO().getAll()
                println(datos)
            }
        }
        return fragmento
    }

}