package com.example.tusabes

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.view.iterator
import com.example.tusabes.database.TuSabesDB
import com.example.tusabes.databinding.FragmentAddPreguntaBinding
import com.example.tusabes.model.Categoria
import com.example.tusabes.model.Pregunta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep

class AddPreguntaFragment : Fragment() {
    private var _binding: FragmentAddPreguntaBinding? = null
    private val binding get() = _binding!!

    private var listaCategorias = emptyList<Categoria>()
    private var categoriaDesdeSpinner = 0
    private var idPregunta: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPreguntaBinding.inflate(inflater,container,false)

        mostrarCategorias()

        idPregunta = requireArguments().getInt("id")
        if (idPregunta != 0){ verPregunta(idPregunta) }

        parentFragmentManager.setFragmentResultListener("requestKey",this) { key, bundle ->
            when (bundle.getString("key")) {
                "nueva" -> {
                    binding.llBotonesCrear.visibility = View.VISIBLE
                    binding.llBotonesActualizar.visibility = View.GONE
                }
                "actualizar" -> {

                    binding.llBotonesCrear.visibility = View.GONE
                    binding.llBotonesActualizar.visibility = View.VISIBLE
                }
            }
        }

        binding.spinPreguntaCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoriaDesdeSpinner = binding.spinPreguntaCategoria.adapter.getItemId(position).toInt()
                println("ON ITEM SELECTED ${binding.spinPreguntaCategoria.selectedItem}")

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //binding.spinPreguntaCategoria.setSelection(2)

            }

        }

        binding.btnGuardarPregunta.setOnClickListener { guardarPreguntaNueva() }
        binding.btnCancelarPregunta.setOnClickListener { cerrarVista() }

        binding.btnActualizarPregunta.setOnClickListener { actualizarPregunta(idPregunta) }
        binding.btnEliminarPregunta.setOnClickListener { dialogoEliminar(idPregunta) }
        binding.btnVolverPregunta.setOnClickListener { cerrarVista() }

        binding.swEditarPregunta.setOnClickListener { activarEdicion() }

        return binding.root
    }

    private fun mostrarCategorias() {
        val database = TuSabesDB.getDataBase(binding.root.context)
        if (database != null){
            database.CategoriasDAO().getAll().observe({ lifecycle },{
                listaCategorias = it
                var adaptadorSpin = CategoriasSpinnerAdapter(binding.root.context, listaCategorias)
                binding.spinPreguntaCategoria.adapter = adaptadorSpin

            })
        }
       /* binding.spinPreguntaCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoriaDesdeSpinner = binding.spinPreguntaCategoria.adapter.getItemId(position).toInt()
                println("ON ITEM SELECTED ${binding.spinPreguntaCategoria.selectedItem}")

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }*/
    }

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
            categoriaDesdeSpinner
        )
        CoroutineScope(Dispatchers.IO).launch {
            val database = context?.let { TuSabesDB.getDataBase(it)}
            if (database != null) {
                database.PreguntasDAO().insert(pregunta)
            }
        }
        cerrarVista()
    }

    private fun verPregunta(idPregunta: Int) {
        binding.spinPreguntaCategoria.isEnabled = false
        binding.edtPreguntaEnunciado.isEnabled = false
        binding.edtPreguntaOp1.isEnabled = false
        binding.edtPreguntaOp2.isEnabled = false
        binding.edtPreguntaOp3.isEnabled = false
        binding.edtPreguntaOp4.isEnabled = false
        binding.edtPreguntaOp5.isEnabled = false
        binding.edtPreguntaRespuesta.isEnabled = false

        var pregunta = Pregunta(0,"","","","","","",0,0)

        runBlocking(Dispatchers.IO){
            val context = activity?.applicationContext
            val database = context?.let { TuSabesDB.getDataBase(it)}
            pregunta = database?.PreguntasDAO()?.getPregunta(idPregunta)!!
        }

        binding.spinPreguntaCategoria.setSelection(pregunta.idCategoria)
        //categoriaDesdeSpinner = pregunta.idCategoria
        binding.edtPreguntaEnunciado.setText(pregunta.enunciado)
        binding.edtPreguntaOp1.setText(pregunta.opcion1)
        binding.edtPreguntaOp2.setText(pregunta.opcion2)
        binding.edtPreguntaOp3.setText(pregunta.opcion3)
        binding.edtPreguntaOp4.setText(pregunta.opcion4)
        binding.edtPreguntaOp5.setText(pregunta.opcion5)
        binding.edtPreguntaRespuesta.setText(pregunta.respuesta.toString())
        println("VER PREGUNTA LA CATEGORIA ES ${pregunta.idCategoria} Y EL SPINNER ESTA EN ${binding.spinPreguntaCategoria.selectedItem}")
    }

    private fun eliminarPregunta(idPregunta: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val context = activity?.applicationContext
            val database = context?.let { TuSabesDB.getDataBase(it) }
            val pregunta = Pregunta(
                idPregunta, "", "", "", "",
                "", "", 0, 0
            )
            println(database?.PreguntasDAO()?.getPregunta(idPregunta))
            database?.PreguntasDAO()?.eliminar(pregunta)
            println(pregunta)
        }
        Toast.makeText(binding.root.context,"Pregunta Eliminada", Toast.LENGTH_LONG).show()
        cerrarVista()
    }

    private fun dialogoEliminar(idPregunta: Int){
        val botonCancelar = { _: DialogInterface, _: Int -> }
        val botonEliminar = { _: DialogInterface, _: Int -> eliminarPregunta(idPregunta)}
        AlertDialog.Builder(binding.root.context)
            .setTitle("ATENCION")
            .setMessage("Quieres eliminar la pregunta?")
            .setPositiveButton("ELIMINAR", botonEliminar)
            .setNegativeButton("CANCELAR", botonCancelar)
            .create()
            .show()
    }

    private fun actualizarPregunta(idPregunta: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val pregunta = Pregunta(idPregunta,
                "${binding.edtPreguntaEnunciado.text}",
                "${binding.edtPreguntaOp1.text}",
                "${binding.edtPreguntaOp2.text}",
                "${binding.edtPreguntaOp3.text}",
                "${binding.edtPreguntaOp4.text}",
                "${binding.edtPreguntaOp5.text}",
                binding.edtPreguntaRespuesta.text.toString().toInt(),
                categoriaDesdeSpinner
            )
            val database = context?.let { TuSabesDB.getDataBase(it) }
            database?.PreguntasDAO()?.actualizar(pregunta)
        }
        Toast.makeText(binding.root.context,"Pregunta Actualizada", Toast.LENGTH_LONG).show()
        cerrarVista()
    }

    private fun activarEdicion() {
        binding.spinPreguntaCategoria.isEnabled = (binding.swEditarPregunta.isChecked)
        binding.edtPreguntaEnunciado.isEnabled = (binding.swEditarPregunta.isChecked)
        binding.edtPreguntaOp1.isEnabled = (binding.swEditarPregunta.isChecked)
        binding.edtPreguntaOp2.isEnabled = (binding.swEditarPregunta.isChecked)
        binding.edtPreguntaOp3.isEnabled = (binding.swEditarPregunta.isChecked)
        binding.edtPreguntaOp4.isEnabled = (binding.swEditarPregunta.isChecked)
        binding.edtPreguntaOp5.isEnabled = (binding.swEditarPregunta.isChecked)
        binding.edtPreguntaRespuesta.isEnabled = (binding.swEditarPregunta.isChecked)
        if (binding.swEditarPregunta.isChecked){
            binding.btnActualizarPregunta.visibility = View.VISIBLE
        }else{
            binding.btnActualizarPregunta.visibility = View.GONE
        }
    }

    private fun cerrarVista() {
        val lvPreguntas = activity?.findViewById<ListView>(R.id.lvListaPreguntas)
        lvPreguntas?.visibility = View.VISIBLE
        val btnAnadir = activity?.findViewById<Button>(R.id.btnAnadirPregunta)
        btnAnadir?.visibility = View.VISIBLE

        parentFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    /*private fun setValorCategoriaSpinner(idCategoria: Int){
        println("ANTES DEL FOR: items en spinner : ${adaptadorSpinner.count}")
        println("ANTES DEL FOR: Cual es el numero de la categoria al comienzo: ${categoriaDesdeSpinner}")

        for (i in 1..adaptadorSpinner.count){
            if (idCategoria == binding.spinPreguntaCategoria.adapter.getItemId(i).toInt()){
                binding.spinPreguntaCategoria.setSelection(i,true)
                println("SET VALOR ID ES: ${idCategoria}, ITERADOR ES: ${i}, SPINNER QUEDA EN: ${binding.spinPreguntaCategoria.selectedItem}")
            }else{
                println("NO ESTÁ HACIENDO NADA")
            }
            println("EN EL FOR: ID AT POSITION: ${binding.spinPreguntaCategoria.getItemIdAtPosition(i)}")
            println("EN EL FOR: POSITION: ${binding.spinPreguntaCategoria.getItemAtPosition(i)}")
        }

    }*/
}