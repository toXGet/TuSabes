package com.example.tusabes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.tusabes.databinding.FragmentBienvenidaBinding

class BienvenidaFragment : Fragment() {
    private var _binding: FragmentBienvenidaBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBienvenidaBinding.inflate(inflater,container,false)

        binding.btnLogin.setOnClickListener {
            childFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentPantallaBienvenida, LoginFragment::class.java,null,"login")
                .commit()
            hideButtons("login")
        }

        binding.btnRegistro.setOnClickListener {
            childFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentPantallaBienvenida, RegistroFragment::class.java,null,"registro")
                .commit()
            hideButtons("registro")
        }

        return binding.root

    }

    fun hideButtons(caso: String) {
        if (binding.btnLogin.visibility == View.VISIBLE) { binding.btnLogin.visibility = View.GONE }
        if (binding.btnRegistro.visibility == View.VISIBLE) { binding.btnRegistro.visibility = View.GONE }
        if (caso == "registro"){
            binding.imgBienvenida.visibility = View.GONE
            binding.tvBienvenida.visibility = View.GONE
        }
    }

    fun cerrar(){
        activity?.getSupportFragmentManager()?.beginTransaction()?.remove(this)?.commit()
    }
}