package com.example.tusabes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.example.tusabes.databinding.FragmentProfesorBinding
import com.google.android.material.tabs.TabLayout

class ProfesorFragment : Fragment() {
    private var _binding: FragmentProfesorBinding? = null
    private val binding get() = _binding!!
    var fragmento: Fragment? = null
    var argumentos: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfesorBinding.inflate(inflater,container,false)

        argumentos = requireArguments()
        binding.tvNombreProfesor.text = argumentos!!.getString("usuario")
        binding.tvRolProfesor.text = argumentos!!.getString("rol")

        parentFragmentManager.beginTransaction().replace(R.id.fragmentContenedorProfesor,
            ListaPreguntasFragment::class.java, argumentos,"preguntas")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        binding.tabContainer.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position){
                    0 -> fragmento = ListaPreguntasFragment()
                    1 -> fragmento = ListaCategoriasFragment()
                    2 -> fragmento = ListaPruebasFragment()
                }
                parentFragmentManager.beginTransaction().replace(R.id.fragmentContenedorProfesor,
                    fragmento!!::class.java, argumentos,"cambio fragmentos")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.btnSalirProfesor.setOnClickListener { activity?.finish() }

        return binding.root
    }
}