package com.example.tusabes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.tusabes.databinding.FragmentPaginasLegalesBinding

class PaginasLegalesFragment : Fragment() {
    private var _binding : FragmentPaginasLegalesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPaginasLegalesBinding.inflate(inflater,container,false)

        parentFragmentManager.setFragmentResultListener("requestKey",this){
            key, bundle ->
            val resultado = bundle.getString("data")
            if (resultado == "declaracion"){
                binding.tvTitulo.text = getString(R.string.declaracionPrivacidadTitulo)
                binding.tvContenido.text = getString(R.string.declaracionPrivacidad)
            }
            if (resultado == "terminos"){
                binding.tvTitulo.text = getString(R.string.condicionesUsoTitulo)
                binding.tvContenido.text = getString(R.string.condicionesUso)
            }

        }

        binding.btnAceptar.setOnClickListener { cerrarVentana() }

        return binding.root
    }

    private fun cerrarVentana() {
        parentFragmentManager.popBackStack()
    }
}