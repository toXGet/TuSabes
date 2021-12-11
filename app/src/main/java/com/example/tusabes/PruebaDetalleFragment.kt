package com.example.tusabes

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.tusabes.Convertidores.Companion.toInstant
import com.example.tusabes.database.TuSabesDB
import com.example.tusabes.databinding.FragmentPruebaDetalleBinding
import com.example.tusabes.model.Pregunta
import com.example.tusabes.model.Prueba
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PruebaDetalleFragment : Fragment() {
    private var _binding: FragmentPruebaDetalleBinding? = null
    private val binding get() = _binding!!

    private var paramBundle = Bundle()

    private lateinit var prueba : Prueba

    private var listaInicialPreguntas = mutableListOf<String>()
    private var listaPreguntasResueltas = mutableListOf<String>()

    private var listaRespuestasCorrectas = mutableListOf<String>()
    private var listaRespuestasUsuario = mutableListOf<String>()

    private var calificacion = 0
    private var listaPreguntas = mutableListOf<Pregunta>()
    private var preguntaActual = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPruebaDetalleBinding.inflate(inflater,container,false)

        paramBundle = requireArguments()

        cargarPrueba()

        binding.btnCancelarPrueba.setOnClickListener { cancelarYvolver() }


        binding.btnPresentarPrueba.setOnClickListener {
            //chequeaRespuestaRadioGroup()
            chequeaPregContestadas()

        }


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun chequeaPregContestadas() {
        if (listaPreguntasResueltas.sort() == listaInicialPreguntas.sort()){
            calificacion /= listaPreguntas.count()
            completaPruebaGrabaYsale()
        }else{
            Toast.makeText(binding.root.context, "Para Completar la Prueba debes Contestar Todas las preguntas", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun completaPruebaGrabaYsale() {
        prueba.preguntas = listaRespuestasUsuario.toString().replace(" ", "")
        prueba.fecha = toInstant(System.currentTimeMillis())
        prueba.resultado = calificacion
        runBlocking(Dispatchers.IO) {
            val context = activity?.applicationContext
            val database = context?.let { TuSabesDB.getDataBase(it) }
            database?.PruebasDAO()?.insert(prueba)
        }
        val botonAceptar = { _: DialogInterface, _: Int -> cancelarYvolver() }
        AlertDialog.Builder(binding.root.context)
            .setTitle("PRUEBA COMPLETADA")
            .setMessage("Has completado la prueba ${prueba.id}")
            .setPositiveButton("ACEPTAR", botonAceptar)
            .create()
            .show()
    }

    private fun chequeaRespuestaRadioGroup(){
        var nota = 0
        when (binding.rgGrupoOpciones.checkedRadioButtonId){
            -1 -> Toast.makeText(binding.root.context, "Debes escoger una Respuesta", Toast.LENGTH_LONG).show()
            binding.rOpcion1.id -> {
                listaRespuestasUsuario.add("${listaPreguntas[preguntaActual].id}:1")
                nota = 1
            }
            binding.rOpcion2.id -> {
                listaRespuestasUsuario.add("${listaPreguntas[preguntaActual].id}:2")
                nota = 2
            }
            binding.rOpcion3.id -> {
                listaRespuestasUsuario.add("${listaPreguntas[preguntaActual].id}:3")
                nota = 3
            }
            binding.rOpcion4.id -> {
                listaRespuestasUsuario.add("${listaPreguntas[preguntaActual].id}:4")
                nota = 4
            }
            binding.rOpcion5.id -> {
                listaRespuestasUsuario.add("${listaPreguntas[preguntaActual].id}:5")
                nota = 5
            }
        }
        listaPreguntasResueltas.add("${listaPreguntas[preguntaActual].id}")
        if (nota == listaPreguntas[preguntaActual].respuesta){
            calificacion += 5
        }else{
            calificacion += 1
        }

    }

    private fun cancelarYvolver() {
        val btnGenerarPrueba = activity?.findViewById<Button>(R.id.btnGenerarPrueba)
        val tvTituloPrueba = activity?.findViewById<TextView>(R.id.tvTituloPruebas)
        val lvVistaPrueba = activity?.findViewById<ListView>(R.id.lvListaPruebas)
        btnGenerarPrueba?.visibility = View.VISIBLE
        tvTituloPrueba?.visibility = View.VISIBLE
        lvVistaPrueba?.visibility = View.VISIBLE
        parentFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cargarPrueba() {
        //var prueba = Prueba(0,0,0,toInstant(0),"")
        runBlocking(Dispatchers.IO) {
            val context = activity?.applicationContext
            val database = context?.let { TuSabesDB.getDataBase(it) }
            prueba = database?.PruebasDAO()?.getPrueba(paramBundle.getInt("idPrueba"))!!
        }
        binding.tvIdPruebaDetalle.text = "${prueba.id}"
        binding.tvPruebaFecha.text = "${prueba.fecha.toString()}"
        if (prueba.resultado.toString().isNullOrEmpty() || prueba.resultado == 0){
            binding.tvPruebaResultado.text = "No Resuelta"
        }else{
            binding.tvPruebaResultado.text = "Calificación: ${prueba.resultado}"
        }
        cargarPreguntas(prueba.preguntas)

        // Test Función Preguntas
        /*var original = prueba.preguntas.split(",").toMutableList()
        var resp = prueba.preguntas.split(Regex("(^[0-9]{1,2}:)|(,[0-9]{1,2}:)")).toMutableList()
        var pregs = prueba.preguntas.split(Regex("(:[0-9],)|(:[0-9]$)")).toMutableList()
        resp.removeAt(0)
        pregs.removeAt(pregs.lastIndex)
        println("ORIGINAL: ${original} - ${original.count()}")
        println("PREGUNTAS: ${pregs} - ${pregs.count()}")
        println("RESPUESTAS: ${resp} - ${resp.count()}")*/

    }

    private fun cargarPreguntas(preguntas: String) {
        listaRespuestasCorrectas = preguntas.split(Regex("(^[0-9]{1,2}:)|(,[0-9]{1,2}:)")).toMutableList()
        listaRespuestasCorrectas.removeAt(0)
        //listaRespuestasCorrectas[listaRespuestasCorrectas.lastIndex] = listaRespuestasCorrectas.elementAt(listaRespuestasCorrectas.lastIndex).replace(",","")

        listaInicialPreguntas = preguntas.split(Regex("(:[0-9],)|(:[0-9]$)")).toMutableList()
        listaInicialPreguntas.removeAt(listaInicialPreguntas.lastIndex)

        for (i in listaInicialPreguntas){
            runBlocking(Dispatchers.IO) {
                val context = activity?.applicationContext
                val database = context?.let { TuSabesDB.getDataBase(it) }
                listaPreguntas.add(database?.PreguntasDAO()?.getPregunta(i.toInt())!!)
            }
            //println("EN FOR ${listaPreguntas.count()}: $listaPreguntas")
        }
        binding.tvPruebaPreguntaCuenta.text = "Pregunta ${preguntaActual + 1} de ${listaPreguntas.count()}"
        binding.tvEnunciadoPreguntaPrueba.text = listaPreguntas[preguntaActual].enunciado
        binding.rOpcion1.text = listaPreguntas[preguntaActual].opcion1
        binding.rOpcion2.text = listaPreguntas[preguntaActual].opcion2
        binding.rOpcion3.text = listaPreguntas[preguntaActual].opcion3
        binding.rOpcion4.text = listaPreguntas[preguntaActual].opcion4
        binding.rOpcion5.text = listaPreguntas[preguntaActual].opcion5

    //println(listaPreguntas)

    }


}