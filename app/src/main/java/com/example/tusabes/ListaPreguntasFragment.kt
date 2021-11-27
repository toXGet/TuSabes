package com.example.tusabes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.example.tusabes.database.TuSabesDB
import com.example.tusabes.databinding.FragmentListaPreguntasBinding
import com.example.tusabes.model.Pregunta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.acl.Owner
import java.util.*
import kotlin.collections.ArrayList

class ListaPreguntasFragment : Fragment() {
    private var _binding: FragmentListaPreguntasBinding? = null
    private val binding get() = _binding!!

    private lateinit var listaPreguntas: ArrayList<Pregunta>
    private lateinit var preguntasAdapter: ArrayAdapter<Pregunta>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListaPreguntasBinding.inflate(inflater,container,false)

        listaPreguntas = ArrayList<Pregunta>()

        mostrarPreguntas()


        return binding.root
    }

    private fun mostrarPreguntas() {

            val database = TuSabesDB.getDataBase(binding.root.context)
            if (database != null){
                database.PreguntasDAO().getAll().observe({ lifecycle }, {
                    listaPreguntas
                    preguntasAdapter = PreguntasAdapter(binding.root.context,listaPreguntas)
                    binding.lvListaPreguntas.adapter = preguntasAdapter
                })


            }

        /*val database = TuSabesDB.getDataBase(binding.root.context)
        database.PreguntasDAO().getAll().observe(this, Observer {
            listaPreguntas = it
            preguntasAdapter = PreguntasAdapter(this,listaPreguntas)
            binding.lvListaPreguntas.adapter = preguntasAdapter
        })*/

    }

}