package com.example.tusabes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.example.tusabes.database.TuSabesDB
import com.example.tusabes.databinding.FragmentAddPreguntaBinding
import com.example.tusabes.model.Categoria
import com.example.tusabes.model.Pregunta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddPreguntaFragment : Fragment() {
    private var _binding: FragmentAddPreguntaBinding? = null
    private val binding get() = _binding!!

    private lateinit var listaCategorias: ArrayList<Categoria>
    private lateinit var categoriasAdapter: ArrayAdapter<Categoria>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPreguntaBinding.inflate(inflater,container,false)

        listaCategorias = ArrayList<Categoria>()

        binding.btnGuardarPregunta.setOnClickListener { guardarPreguntaNueva() }
        binding.btnCancelarPregunta.setOnClickListener { cerrarVista() }

        binding.btnActualizarPregunta.setOnClickListener { actualizarPregunta() }
        binding.btnEliminarPregunta.setOnClickListener { eliminarPregunta() }
        binding.btnVolverPregunta.setOnClickListener { cerrarVista() }

        childFragmentManager.setFragmentResultListener("requestKey",this){
                key, bundle ->
            when (bundle.getString("key")){
                "nueva" -> {binding.btnGuardarPregunta.visibility = View.VISIBLE
                            binding.btnCancelarPregunta.visibility = View.VISIBLE
                            binding.btnActualizarPregunta.visibility = View.GONE
                            binding.btnEliminarPregunta.visibility = View.GONE
                            binding.btnVolverPregunta.visibility = View.GONE}
                "actualizar" -> {binding.btnGuardarPregunta.visibility = View.GONE
                                binding.btnCancelarPregunta.visibility = View.GONE
                                binding.btnActualizarPregunta.visibility = View.VISIBLE
                                binding.btnEliminarPregunta.visibility = View.VISIBLE
                                binding.btnVolverPregunta.visibility = View.VISIBLE}
            }
        }

        //setSpinnerData()


        return binding.root
    }

   /* private fun setSpinnerData() {
        val database = TuSabesDB.getDataBase(binding.root.context)
        if (database != null){
            database.CategoriasDAO().getAll().observe(viewLifecycleOwner ,{
                listaCategorias=it
                categoriasAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                binding.spinPreguntaCategoria.adapter = categoriasAdapter
                binding.spinPreguntaCategoria.onItemSelectedListener


            })
        }

    }*/

    private fun guardarPreguntaNueva() {
        val context = activity?.applicationContext
        val pregunta = Pregunta(0,
            binding.edtPreguntaEnunciado.text.toString(),
            binding.edtPreguntaOp1.text.toString(),
            binding.edtPreguntaOp2.text.toString(),
            binding.edtPreguntaOp3.text.toString(),
            binding.edtPreguntaOp4.text.toString(),
            binding.edtPreguntaOp5.text.toString(),
            binding.edtPreguntaRespuesta.text.toString().toInt(),
            1//binding.spinPreguntaCategoria.selectedItemPosition
        )
        CoroutineScope(Dispatchers.IO).launch {
            val database = context?.let { TuSabesDB.getDataBase(it)}
            if (database != null) {
                database.PreguntasDAO().insert(pregunta)
            }
        }
        cerrarVista()
    }

    private fun eliminarPregunta() {
        TODO("Not yet implemented")
    }

    private fun actualizarPregunta() {
        TODO("Not yet implemented")
    }

    private fun cerrarVista() {
        val lvPreguntas = activity?.findViewById<ListView>(R.id.lvListaPreguntas)
        lvPreguntas?.visibility = View.VISIBLE
        val btnAnadir = activity?.findViewById<Button>(R.id.btnAnadirPregunta)
        btnAnadir?.visibility = View.VISIBLE

        parentFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }
}