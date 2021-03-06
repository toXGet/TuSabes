package com.example.tusabes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.tusabes.database.TuSabesDB
import com.example.tusabes.databinding.FragmentEstudianteBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstudianteFragment : Fragment() {
    private var _binding: FragmentEstudianteBinding? = null
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

        _binding = FragmentEstudianteBinding.inflate(inflater,container,false)

        argumentos = requireArguments()
        binding.tvNombreEstudiante.text = argumentos!!.getString("usuario")
        binding.tvRolEstudiante.text = argumentos!!.getString("rol")

        parentFragmentManager.beginTransaction().replace(R.id.fragmentContenedorEstudiante,
            ListaPreguntasFragment::class.java, argumentos,"preguntas")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        binding.tabContainer.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position){
                    0 -> fragmento = ListaPreguntasFragment()
                    1 -> fragmento = ListaPruebasFragment()
                }
                parentFragmentManager.beginTransaction().replace(R.id.fragmentContenedorEstudiante,
                    fragmento!!::class.java, argumentos,"cambio fragmentos")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.btnSalirEstudiante.setOnClickListener { activity?.finish() }

        /*binding.tvNombreEstudiante.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContenedorEstudiante,
                ListaCategoriasFragment::class.java,null,"categorias")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }*/

        return binding.root
    }

}