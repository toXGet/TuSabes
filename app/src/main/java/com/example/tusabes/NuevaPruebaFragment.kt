package com.example.tusabes

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.tusabes.Convertidores.Companion.toInstant
import com.example.tusabes.database.TuSabesDB
import com.example.tusabes.databinding.FragmentNuevaPruebaBinding
import com.example.tusabes.model.Categoria
import com.example.tusabes.model.Pregunta
import com.example.tusabes.model.Prueba
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.sql.Timestamp
import java.util.*
import kotlin.random.Random

class NuevaPruebaFragment : Fragment() {
    private var _binding: FragmentNuevaPruebaBinding? = null
    private val binding get() = _binding!!

    private var avanzadas = 0
    private var todasCategorias = 1
    private var numeroPreguntas = 5
    private var cantidadPreguntas = 5
    private var categoria = 1
    private var idUsuario = 0


    private var listaCategorias = emptyList<Categoria>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNuevaPruebaBinding.inflate(inflater,container,false)

        idUsuario = requireArguments().getInt("id")

        getCantidadPreguntas()
        binding.tiTituloCantidad.hint = "${binding.tiTituloCantidad.hint} hasta ${cantidadPreguntas}"

        binding.btnGenerarNuevaPrueba.setOnClickListener {
            if (avanzadas == 1){
                numeroPreguntas = binding.edtCantidadPreguntas.text.toString().toInt()
                if (numeroPreguntas <= 0 || numeroPreguntas >= cantidadPreguntas){
                    Toast.makeText(binding.root.context,
                        "La cantidad de preguntas no puede ser menos de 0 ni más de ${cantidadPreguntas}",
                        Toast.LENGTH_LONG).show()
                    binding.edtCantidadPreguntas.setText("")
                }else{ generarPrueba() }
            }
            if (avanzadas == 0){ generarPrueba() }
        }
        binding.swOpcionesAvanzadas.setOnClickListener { activarOpcionesAvanzadas() }
        binding.cbTodasCategorias.setOnClickListener { activarTodasCategorias() }




        return binding.root
    }

    private fun getCantidadPreguntas() {
        runBlocking(Dispatchers.IO) {
            val context = activity?.applicationContext
            val database = context?.let { TuSabesDB.getDataBase(it) }
            cantidadPreguntas = database?.PreguntasDAO()?.getAllAsync()!!.count()
        }
    }

    private fun activarTodasCategorias() {
        if (!binding.cbTodasCategorias.isChecked){
            binding.tvTituloCategorias.visibility = View.VISIBLE
            binding.spinNuevaPruebaCategorias.visibility = View.VISIBLE
            todasCategorias = 0
            mostrarCategorias()
        }else{
            binding.tvTituloCategorias.visibility = View.GONE
            binding.spinNuevaPruebaCategorias.visibility = View.GONE
            todasCategorias = 1
        }
    }

    private fun mostrarCategorias() {
        val database = TuSabesDB.getDataBase(binding.root.context)
        if (database != null) {
            database.CategoriasDAO().getAll().observe({ lifecycle }, {
                listaCategorias = it
                var adaptadorSpin = CategoriasSpinnerAdapter(binding.root.context, listaCategorias)
                binding.spinNuevaPruebaCategorias.adapter = adaptadorSpin

            })
        }
        binding.spinNuevaPruebaCategorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoria = binding.spinNuevaPruebaCategorias.adapter.getItemId(position).toInt()

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    private fun activarOpcionesAvanzadas() {
        if (binding.swOpcionesAvanzadas.isChecked){
            binding.llOpcionesAvanzadas.visibility = View.VISIBLE
            avanzadas = 1
        }else{
            binding.llOpcionesAvanzadas.visibility = View.GONE
            avanzadas = 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generarPrueba() {
        val context = activity?.applicationContext
        val prueba = Prueba(
            0,
            0,
            idUsuario,
            toInstant(System.currentTimeMillis()),
            getPreguntasYresultados())
        println(prueba)
        CoroutineScope(Dispatchers.IO).launch {
            val database = context?.let { TuSabesDB.getDataBase(it)}
            if (database != null) {
                database.PruebasDAO().insert(prueba)
            }
        }
        cerrarVista()
    }

    private fun getPreguntasYresultados(): String{
        var listaPreguntas = emptyList<Pregunta>()
        var arraySubproceso = mutableListOf<Pregunta>()
        var preguntas = ""

        if (todasCategorias == 1) {
            runBlocking(Dispatchers.IO) {
                val context = activity?.applicationContext
                val database = context?.let { TuSabesDB.getDataBase(it) }
                listaPreguntas = database?.PreguntasDAO()?.getAllAsync()!!
            }
        }
        if (todasCategorias == 0){
            runBlocking(Dispatchers.IO) {
                val context = activity?.applicationContext
                val database = context?.let { TuSabesDB.getDataBase(it) }
                listaPreguntas = database?.PreguntasDAO()?.getPreguntaPorCategoria(categoria)!!
            }
        }
        if(numeroPreguntas > listaPreguntas.count()){
            numeroPreguntas = listaPreguntas.count()
        }
        while (arraySubproceso.count() != numeroPreguntas) {
            var indiceRandom = Random.nextInt(1, listaPreguntas.count())
            if (listaPreguntas[indiceRandom] != null || listaPreguntas[indiceRandom].id != 0) {
                if (!arraySubproceso.contains(listaPreguntas[indiceRandom])) {
                    arraySubproceso.add(listaPreguntas[indiceRandom])
                }
            }
            println("NUMERO RANDOM: ${indiceRandom} el array es ${arraySubproceso}")
        }
        var cadenaPreguntas = mutableListOf<String>()
        for (i in arraySubproceso){
            cadenaPreguntas.add("${i.id}:${i.respuesta}")
            //preguntas = preguntas + i.id.toString() + ":" + i.respuesta.toString() + ","
        }
        preguntas = cadenaPreguntas.toString().replace(" ","")
        return preguntas
    }

    private fun cerrarVista() {
        val lvPruebas = activity?.findViewById<ListView>(R.id.lvListaPruebas)
        lvPruebas?.visibility = View.VISIBLE
        val btnAnadir = activity?.findViewById<Button>(R.id.btnGenerarPrueba)
        btnAnadir?.visibility = View.VISIBLE
        val tvTituloGenerar = activity?.findViewById<TextView>(R.id.tvTituloPruebas)
        tvTituloGenerar?.visibility = View.VISIBLE

        parentFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

}