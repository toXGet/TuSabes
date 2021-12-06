package com.example.tusabes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import com.example.tusabes.database.TuSabesDB
import com.example.tusabes.databinding.FragmentListaPruebasBinding
import com.example.tusabes.model.Prueba

class ListaPruebasFragment : Fragment() {
    private var _binding: FragmentListaPruebasBinding? = null
    private val binding get() = _binding!!

    var listaPruebas = emptyList<Prueba>()
    private lateinit var pruebasAdapter: ArrayAdapter<Prueba>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaPruebasBinding.inflate(inflater,container,false)

        mostrarPruebas()

        binding.btnGenerarPrueba.setOnClickListener {
            ocultarlista()
            childFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentPruebas,
                    NuevaPruebaFragment::class.java, bundleOf("id" to 0),"generar prueba")
                .commit()
        }

        binding.lvListaPruebas.setOnItemClickListener { parent, view, position, id ->
            ocultarlista()
            childFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentPruebas,PruebaDetalleFragment::class.java,null,"pruebas")
                .commit()
        }



        return binding.root
    }

    private fun mostrarPruebas() {
        val database = TuSabesDB.getDataBase(binding.root.context)
        if (database != null){
            database.PruebasDAO().getAll().observe({ lifecycle }, {
                listaPruebas=it
                pruebasAdapter = PruebasAdapter(binding.root.context, listaPruebas)
                binding.lvListaPruebas.adapter = pruebasAdapter
            })
        }
    }

    private fun ocultarlista() {
        binding.btnGenerarPrueba.visibility = View.GONE
        binding.lvListaPruebas.visibility = View.GONE
    }

}