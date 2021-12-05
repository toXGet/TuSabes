package com.example.tusabes

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.annotation.RequiresApi
import com.example.tusabes.Convertidores.Companion.toInstant
import com.example.tusabes.database.TuSabesDB
import com.example.tusabes.databinding.FragmentNuevaPruebaBinding
import com.example.tusabes.model.Pregunta
import com.example.tusabes.model.Prueba
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.sql.Timestamp
import kotlin.random.Random

class NuevaPruebaFragment : Fragment() {
    private var _binding: FragmentNuevaPruebaBinding? = null
    private val binding get() = _binding!!

    private var avanzadas = 0
    private var todasCategorias = 1
    private var numeroPreguntas = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNuevaPruebaBinding.inflate(inflater,container,false)

        binding.btnGenerarNuevaPrueba.setOnClickListener { generarPrueba() }
        binding.swOpcionesAvanzadas.setOnClickListener { activarOpcionesAvanzadas() }
        binding.cbTodasCategorias.setOnClickListener { activarTodasCategorias() }



        return binding.root
    }

    private fun activarTodasCategorias() {
        if (!binding.cbTodasCategorias.isChecked){
            binding.tvTituloCategorias.visibility = View.VISIBLE
            binding.spinNuevaPruebaCategorias.visibility = View.VISIBLE
            todasCategorias = 0
        }else{
            binding.tvTituloCategorias.visibility = View.GONE
            binding.spinNuevaPruebaCategorias.visibility = View.GONE
            todasCategorias = 1
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
            0,
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
        var arraySubproceso = emptyList<Pregunta>()
        var preguntas = ""

        runBlocking(Dispatchers.IO) {
            val context = activity?.applicationContext
            val database = context?.let { TuSabesDB.getDataBase(it) }
            listaPreguntas = database?.PreguntasDAO()?.getAllAsync()!!
        }

        for (i in 1..numeroPreguntas){
            var indiceRandom = Random.nextInt(1, listaPreguntas.count())
            arraySubproceso.toMutableSet().add(listaPreguntas.get(indiceRandom))
            listaPreguntas.toMutableSet().remove(listaPreguntas.get(indiceRandom))
            println("NÚMERO: ${i}")
        }
        for (i in listaPreguntas){
            preguntas = preguntas + i.id.toString() + ":" + i.respuesta.toString() + ","
        }

        /*val database = TuSabesDB.getDataBase(binding.root.context)
        if (database != null){
            database.PreguntasDAO().getAll().observe({ lifecycle }, {
                listaPreguntas=it

                cantidadPreguntas = listaPreguntas.count()
                var listaIds = generadorRandom(numeroPreguntas, cantidadPreguntas)
                for (i in listaIds){
                    listaPreguntas.random()
                }
                for (i in listaPreguntas){
                    preguntas = preguntas + i.id.toString() + ":" + i.respuesta.toString() + ","
                }

            })
        }*/



        return preguntas
    }

    private fun generadorRandom(limite: Int, cantidad: Int): List<Int>{
        val listaNumeros = List(limite){ Random.nextInt(0,cantidad) }
        return listaNumeros
    }

    private fun cerrarVista() {
        val lvPruebas = activity?.findViewById<ListView>(R.id.lvListaPruebas)
        lvPruebas?.visibility = View.VISIBLE
        val btnAnadir = activity?.findViewById<Button>(R.id.btnGenerarPrueba)
        btnAnadir?.visibility = View.VISIBLE

        parentFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }


}