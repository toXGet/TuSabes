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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class ListaPruebasFragment : Fragment() {
    private var _binding: FragmentListaPruebasBinding? = null
    private val binding get() = _binding!!

    var listaPruebas = emptyList<Prueba>()
    private lateinit var pruebasAdapter: ArrayAdapter<Prueba>
    private var paramsBundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaPruebasBinding.inflate(inflater,container,false)

        paramsBundle = requireArguments()

        mostrarPruebas()

        binding.btnGenerarPrueba.setOnClickListener {
            ocultarlista()
            childFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentPruebas,
                    NuevaPruebaFragment::class.java, paramsBundle,"generar prueba")
                .commit()
        }

        binding.lvListaPruebas.setOnItemClickListener { parent, view, position, id ->
            ocultarlista()
            paramsBundle.putInt("idPrueba", listaPruebas[position].id)
            childFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentPruebas,PruebaDetalleFragment::class.java,paramsBundle,"pruebas")
                .commit()
        }



        return binding.root
    }

    private fun mostrarPruebas() {
        /*runBlocking(Dispatchers.IO) {
            val context = activity?.applicationContext
            val database = context?.let { TuSabesDB.getDataBase(it) }
            listaPruebas = database?.PruebasDAO()?.getPruebaByUser(paramsBundle.getInt("id"))!!
        }*/
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
        binding.tvTituloPruebas.visibility = View.GONE
        binding.lvListaPruebas.visibility = View.GONE
    }

}