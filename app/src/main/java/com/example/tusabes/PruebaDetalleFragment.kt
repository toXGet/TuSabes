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
import androidx.core.content.ContextCompat
import com.example.tusabes.Convertidores.Companion.toInstant
import com.example.tusabes.database.TuSabesDB
import com.example.tusabes.databinding.FragmentPruebaDetalleBinding
import com.example.tusabes.model.Pregunta
import com.example.tusabes.model.Prueba
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class PruebaDetalleFragment : Fragment() {
    private var _binding: FragmentPruebaDetalleBinding? = null
    private val binding get() = _binding!!

    private var paramBundle = Bundle()

    private lateinit var prueba : Prueba

    private var listaInicialPreguntas = mutableListOf<String>()
    private var listaPreguntasResueltas = mutableListOf<String>()

    private var listaRespuestasUsuario = mutableListOf<String>()

    private var calificacion = 0
    private var listaPreguntas = mutableListOf<Pregunta>()
    private var preguntaActual = 0

    private var notaPregunta = 0

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
        if (paramBundle.getString("rol") == "Estudiante") {
            binding.swOpAvanzadasPrueba.visibility = View.GONE
        }

        cargarPrueba()

        binding.btnPresentarPrueba.setOnClickListener { comenzarAresponder() }

        binding.btnCancelarPrueba.setOnClickListener { cancelarYvolver() }

        binding.btnCalificarPregunta.setOnClickListener {
            if (binding.swOpAvanzadasPrueba.isChecked) { mostrarRespuesta() }
            procesarPregunta()
        }

        binding.btnCalificarPrueba.setOnClickListener { chequeaPregContestadas() }

        binding.btnSiguientePregunta.setOnClickListener {
            navegacionPreguntas("ffw")
            if (binding.swOpAvanzadasPrueba.isChecked) { mostrarRespuesta() }
        }
        binding.btnAnteriorPregunta.setOnClickListener {
            navegacionPreguntas("rew")
            if (binding.swOpAvanzadasPrueba.isChecked) { mostrarRespuesta() }
        }
        binding.rgGrupoOpciones.setOnCheckedChangeListener { group, checkedId ->
            chequeaRadioGroup()
        }

        binding.swOpAvanzadasPrueba.setOnClickListener { mostrarRespuesta() }

        return binding.root
    }

    private fun procesarPregunta(){
        if (binding.rgGrupoOpciones.checkedRadioButtonId == -1){
            Toast.makeText(binding.root.context, "Debes escoger una Respuesta", Toast.LENGTH_LONG).show()
        }else {
            notasPreguntas()
            if (preguntaActual == listaPreguntas.lastIndex) {
                binding.btnCalificarPrueba.visibility = View.VISIBLE
                binding.btnCalificarPregunta.visibility = View.GONE
            } else {
                navegacionPreguntas("ffw")
            }
        }
    }

    private fun comenzarAresponder(){
        binding.btnCalificarPregunta.visibility = View.VISIBLE
        binding.llNavegacion.visibility = View.GONE
        binding.rgGrupoOpciones.clearCheck()
        preguntaActual = 0
        var cuenta = preguntaActual
        binding.tvPruebaPreguntaCuenta.text = "Pregunta ${cuenta + 1} de ${listaPreguntas.count()}"
        binding.tvEnunciadoPreguntaPrueba.text = listaPreguntas[preguntaActual].enunciado
        binding.rOpcion1.text = listaPreguntas[preguntaActual].opcion1
        binding.rOpcion2.text = listaPreguntas[preguntaActual].opcion2
        binding.rOpcion3.text = listaPreguntas[preguntaActual].opcion3
        binding.rOpcion4.text = listaPreguntas[preguntaActual].opcion4
        binding.rOpcion5.text = listaPreguntas[preguntaActual].opcion5
    }

    private fun mostrarRespuesta() {
        if (binding.swOpAvanzadasPrueba.isChecked){
            binding.rOpcion1.setBackgroundColor(0)
            binding.rOpcion2.setBackgroundColor(0)
            binding.rOpcion3.setBackgroundColor(0)
            binding.rOpcion4.setBackgroundColor(0)
            binding.rOpcion5.setBackgroundColor(0)
            when (listaPreguntas[preguntaActual].respuesta){
                1 -> binding.rOpcion1.setBackgroundColor(ContextCompat.getColor(binding.root.context,
                    R.color.teal_100))
                2 -> binding.rOpcion2.setBackgroundColor(ContextCompat.getColor(binding.root.context,
                    R.color.teal_100))
                3 -> binding.rOpcion3.setBackgroundColor(ContextCompat.getColor(binding.root.context,
                    R.color.teal_100))
                4 -> binding.rOpcion4.setBackgroundColor(ContextCompat.getColor(binding.root.context,
                    R.color.teal_100))
                5 -> binding.rOpcion5.setBackgroundColor(ContextCompat.getColor(binding.root.context,
                    R.color.teal_100))
            }
        }else{
            binding.rOpcion1.setBackgroundColor(0)
            binding.rOpcion2.setBackgroundColor(0)
            binding.rOpcion3.setBackgroundColor(0)
            binding.rOpcion4.setBackgroundColor(0)
            binding.rOpcion5.setBackgroundColor(0)
        }
    }

    private fun navegacionPreguntas(sentido: String) {
        binding.rgGrupoOpciones.clearCheck()
        when (sentido){
            "ffw" -> {
                if (preguntaActual == listaPreguntas.lastIndex){
                    preguntaActual = 0
                }else{
                    preguntaActual += 1
                }
            }
            "rew" -> {
                if (preguntaActual == 0){
                    preguntaActual = listaPreguntas.lastIndex
                }else{
                    preguntaActual -= 1
                }
            }
        }
        var cuenta = preguntaActual
        binding.tvPruebaPreguntaCuenta.text = "Pregunta ${cuenta + 1} de ${listaPreguntas.count()}"
        binding.tvEnunciadoPreguntaPrueba.text = listaPreguntas[preguntaActual].enunciado
        binding.rOpcion1.text = listaPreguntas[preguntaActual].opcion1
        binding.rOpcion2.text = listaPreguntas[preguntaActual].opcion2
        binding.rOpcion3.text = listaPreguntas[preguntaActual].opcion3
        binding.rOpcion4.text = listaPreguntas[preguntaActual].opcion4
        binding.rOpcion5.text = listaPreguntas[preguntaActual].opcion5
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun chequeaPregContestadas() {
        calificacion /= listaPreguntas.count()
        if (calificacion < 3) { binding.tvPruebaResultado
            .setTextColor(ContextCompat.getColor(binding.root.context,R.color.red_700))
        }else { binding.tvPruebaResultado
            .setTextColor(ContextCompat.getColor(binding.root.context,R.color.teal_700))}
        binding.tvPruebaResultado.text = "Calificación: $calificacion"
        completaPruebaGrabaYsale()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun completaPruebaGrabaYsale() {
        prueba.preguntas = listaRespuestasUsuario.toString()
            .replace(" ", "")
            .replace("[", "")
            .replace("]","")
        prueba.fecha = toInstant(System.currentTimeMillis())
        prueba.resultado = calificacion
        runBlocking(Dispatchers.IO) {
            val context = activity?.applicationContext
            val database = context?.let { TuSabesDB.getDataBase(it) }
            database?.PruebasDAO()?.actualizar(prueba)
        }
        val botonAceptar = { _: DialogInterface, _: Int -> cancelarYvolver() }
        AlertDialog.Builder(binding.root.context)
            .setTitle("PRUEBA COMPLETADA")
            .setMessage("Has completado la prueba ${prueba.id}.\n Tu calificación es: ${prueba.resultado}")
            .setPositiveButton("ACEPTAR", botonAceptar)
            .create()
            .show()
    }

    private fun chequeaRadioGroup(){
        when (binding.rgGrupoOpciones.checkedRadioButtonId){
            binding.rOpcion1.id -> { notaPregunta = 1 }
            binding.rOpcion2.id -> { notaPregunta = 2 }
            binding.rOpcion3.id -> { notaPregunta = 3 }
            binding.rOpcion4.id -> { notaPregunta = 4 }
            binding.rOpcion5.id -> { notaPregunta = 5 }
            -1 -> notaPregunta = 0
        }
    }

    private fun notasPreguntas() {
        if (notaPregunta == listaPreguntas[preguntaActual].respuesta) {
            calificacion += 5
        } else {
            calificacion += 1
        }
        listaRespuestasUsuario.add("${listaPreguntas[preguntaActual].id}:${notaPregunta}")
        listaPreguntasResueltas.add("${listaPreguntas[preguntaActual].id}")
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
        binding.tvIdPruebaDetalle.text = " ${prueba.id}"
        binding.tvPruebaFecha.text = "${prueba.fecha.toString()}"
        if (prueba.resultado.toString().isNullOrEmpty() || prueba.resultado == 0){
            binding.tvPruebaResultado.text = "No Resuelta"
        }else{
            if (prueba.resultado < 3) { binding.tvPruebaResultado
                .setTextColor(ContextCompat.getColor(binding.root.context,R.color.red_700))}
            else { binding.tvPruebaResultado
                .setTextColor(ContextCompat.getColor(binding.root.context,R.color.teal_700))}
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
        //listaRespuestasCorrectas = preguntas.split(Regex("(^[0-9]{1,2}:)|(,[0-9]{1,2}:)")).toMutableList()
        //listaRespuestasCorrectas.removeAt(0)
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
        var cuenta = preguntaActual
        binding.tvPruebaPreguntaCuenta.text = "Pregunta ${cuenta + 1} de ${listaPreguntas.count()}"
        binding.tvEnunciadoPreguntaPrueba.text = listaPreguntas[preguntaActual].enunciado
        binding.rOpcion1.text = listaPreguntas[preguntaActual].opcion1
        binding.rOpcion2.text = listaPreguntas[preguntaActual].opcion2
        binding.rOpcion3.text = listaPreguntas[preguntaActual].opcion3
        binding.rOpcion4.text = listaPreguntas[preguntaActual].opcion4
        binding.rOpcion5.text = listaPreguntas[preguntaActual].opcion5

    //println(listaPreguntas)

    }


}