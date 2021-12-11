package com.example.tusabes

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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
    private var listaInicialPreguntas = emptyList<String>()
    private var listaPreguntas = emptyList<Pregunta>()


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

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cargarPrueba() {
        var prueba = Prueba(0,0,0,toInstant(0),"")
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
        var original = prueba.preguntas.split(Regex(",[0-9]{1,2}:"))
        listaInicialPreguntas = prueba.preguntas.split(Regex(":[0-9],"))


        /*listaInicialPreguntas = prueba.preguntas.split(Regex(":[0-9],"))
        listaInicialPreguntas = prueba.preguntas.split(',')
        for (i in listaInicialPreguntas){
            i = i.substring(0,)
        }*/

        println("$listaInicialPreguntas Y SON ${listaInicialPreguntas.count()} ORIGINAL ES $original - ${original.count()}")
    }


}