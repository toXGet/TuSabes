package com.example.tusabes

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.tusabes.Convertidores.Companion.toInstant
import com.example.tusabes.databinding.FragmentNuevaPruebaBinding
import com.example.tusabes.model.Prueba
import java.sql.Timestamp

class NuevaPruebaFragment : Fragment() {
    private var _binding: FragmentNuevaPruebaBinding? = null
    private val binding get() = _binding!!

    private var avanzadas = 0
    private var todasCategorias = 1

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
            "")
    }


}