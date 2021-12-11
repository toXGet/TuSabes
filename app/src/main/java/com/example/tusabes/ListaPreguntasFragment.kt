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
    var paramBundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListaPreguntasBinding.inflate(inflater,container,false)

        paramBundle = requireArguments()
        if (paramBundle.getString("rol") == "estudiante") {
            binding.btnAnadirPregunta.visibility = View.GONE
        }
        mostrarPreguntas()
        paramBundle.putInt("preguntaId", 0)

        binding.btnAnadirPregunta.setOnClickListener {
            ocultarLista()
            childFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentPreguntaNueva,
                    AddPreguntaFragment::class.java, paramBundle,"pregunta")
                .commit()
            childFragmentManager.setFragmentResult("requestKey", bundleOf("key" to "nueva"))
        }

        binding.lvListaPreguntas.setOnItemClickListener { parent, view, position, id ->
            ocultarLista()
            paramBundle.putInt("preguntaId", listaPreguntas[position].id)
            childFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentPreguntaNueva,AddPreguntaFragment::class.java, paramBundle,"pregunta")
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
        runBlocking(Dispatchers.IO) {
            val context = activity?.applicationContext
            val database = context?.let { TuSabesDB.getDataBase(it) }
            listaCategorias = database?.CategoriasDAO()?.getAllAsync()!!
        }
        return listaCategorias
    }
}