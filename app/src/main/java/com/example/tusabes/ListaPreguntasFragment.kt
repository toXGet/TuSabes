package com.example.tusabes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.example.tusabes.database.TuSabesDB
import com.example.tusabes.databinding.FragmentListaPreguntasBinding
import com.example.tusabes.model.Categoria
import com.example.tusabes.model.Pregunta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.security.acl.Owner
import java.util.*
import kotlin.collections.ArrayList

class ListaPreguntasFragment : Fragment() {
    private var _binding: FragmentListaPreguntasBinding? = null
    private val binding get() = _binding!!

    //private lateinit var listaPreguntas: ArrayList<Pregunta>
    var listaPreguntas = emptyList<Pregunta>()
    var listaCategorias = emptyList<Categoria>()
    private lateinit var preguntasAdapter: ArrayAdapter<Pregunta>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListaPreguntasBinding.inflate(inflater,container,false)

        mostrarPreguntas()

        binding.btnAnadirPregunta.setOnClickListener {
            ocultarLista()
            childFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentPreguntaNueva,
                    AddPreguntaFragment::class.java, bundleOf("id" to 0),"pregunta")
                .commit()
            childFragmentManager.setFragmentResult("requestKey", bundleOf("key" to "nueva"))
        }

        binding.lvListaPreguntas.setOnItemClickListener { parent, view, position, id ->
            ocultarLista()
            val pregunta = Bundle()
            pregunta.putInt("id", listaPreguntas[position].id)
            childFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentPreguntaNueva,AddPreguntaFragment::class.java, pregunta,"pregunta")
                .commit()
            childFragmentManager.setFragmentResult("requestKey", bundleOf("key" to "actualizar"))

        }

        return binding.root
    }

    private fun ocultarLista() {
        binding.lvListaPreguntas.visibility = View.GONE
        binding.btnAnadirPregunta.visibility = View.GONE
    }

    private fun mostrarPreguntas() {
        var categorias = categoriasParaLista()
        println("MOSTRAR PREGUNTAS ${categorias}")
        val database = TuSabesDB.getDataBase(binding.root.context)
        if (database != null){
            database.PreguntasDAO().getAll().observe({ lifecycle }, {
                listaPreguntas=it
                preguntasAdapter = PreguntasAdapter(binding.root.context, listaPreguntas, categorias)
                binding.lvListaPreguntas.adapter = preguntasAdapter
            })
        }
    }
    private fun categoriasParaLista() : List<Categoria>{
        /*val database = TuSabesDB.getDataBase(binding.root.context)
        if (database != null){
            listaCategorias = database.CategoriasDAO().getAll().
            println("${listaCategorias}")
        }*/
        runBlocking(Dispatchers.IO) {
            val context = activity?.applicationContext
            val database = context?.let { TuSabesDB.getDataBase(it) }
            listaCategorias = database?.CategoriasDAO()?.getAllAsync()!!
            println("Dentro de la corrutina ${listaCategorias}")
        }
        println("Fuera de la corrutina ${listaCategorias}")
        return listaCategorias


    }
}