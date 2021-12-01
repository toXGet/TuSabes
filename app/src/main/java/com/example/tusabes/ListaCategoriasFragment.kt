package com.example.tusabes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.tusabes.database.TuSabesDB
import com.example.tusabes.databinding.FragmentListaCategoriasBinding
import com.example.tusabes.model.Categoria
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import java.lang.Thread.sleep

class ListaCategoriasFragment : Fragment() {
    private var _binding: FragmentListaCategoriasBinding? = null
    private val binding get() = _binding!!

    private var listaCategorias = emptyList<Categoria>()
    private lateinit var categoriasAdapter: ArrayAdapter<Categoria>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaCategoriasBinding.inflate(inflater,container,false)

        mostrarListaCategorias()

        binding.btnNuevaCategoria.setOnClickListener { crearCategoria() }
        binding.btnActualizarCategoria.setOnClickListener { actualizarCategoria() }
        binding.btnEliminarCategoria.setOnClickListener { eliminarCategoria() }
        binding.btnVolverCategoria.setOnClickListener { volverFragmentoAnterior() }

        binding.swEditarCategoria.setOnClickListener { activarEdicion() }

        binding.lvListaCategorias.setOnItemClickListener { parent, view, position, id ->
            var categoria = Categoria(0,"")
            CoroutineScope(Dispatchers.IO).launch {
                val context = activity?.applicationContext
                val database = context?.let { TuSabesDB.getDataBase(it)}
                categoria = database?.CategoriasDAO()?.getCategoria(listaCategorias[position].id)!!
            }
            sleep(250)
            binding.edtIdCategoria.setText(categoria.id.toString())
            binding.edtNombreCategoria.setText(categoria.nombre)
        }



        binding.spinnerCategoriaHid.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var categoria = Categoria(0,"")
                CoroutineScope(Dispatchers.IO).launch {
                    val context = activity?.applicationContext
                    val database = context?.let { TuSabesDB.getDataBase(it)}
                    categoria = database?.CategoriasDAO()?.getCategoria(listaCategorias[position].id)!!
                }
                sleep(250)
                binding.edtIdCategoria.setText(categoria.id.toString())
                binding.edtNombreCategoria.setText(categoria.nombre)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {


            }

        }


        /*var categorias = ArrayList<String>()
        var adaptador = ArrayAdapter<String>(binding.root.context,
            R.layout.support_simple_spinner_dropdown_item,categorias)*/

        return binding.root
    }

    private fun activarEdicion() {
        binding.edtNombreCategoria.isEnabled = (binding.swEditarCategoria.isChecked)
    }

    private fun crearCategoria() {
        val context = activity?.applicationContext
        val categoria = Categoria(0, binding.edtNombreCategoria.text.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val database = context?.let { TuSabesDB.getDataBase(it)}
            if (database != null){
                database.CategoriasDAO().insert(categoria)

            }
        }
    }

    private fun actualizarCategoria() {
        CoroutineScope(Dispatchers.IO).launch {
            val categoria = Categoria(
                binding.edtIdCategoria.text.toString().toInt(),
                binding.edtNombreCategoria.text.toString()
            )
            val database = context?.let { TuSabesDB.getDataBase(it) }
            database?.CategoriasDAO()?.actualizar(categoria)
        }
    }

    private fun eliminarCategoria() {
        CoroutineScope(Dispatchers.IO).launch {
            val context = activity?.applicationContext
            val database = context?.let { TuSabesDB.getDataBase(it) }
            val categoria = Categoria(
                binding.edtIdCategoria.text.toString().toInt(),""
            )
            database?.CategoriasDAO()?.eliminar(categoria)
        }
    }

    private fun volverFragmentoAnterior() {

    }

    private fun mostrarListaCategorias() {
        val database = TuSabesDB.getDataBase(binding.root.context)
        if (database != null){
            database.CategoriasDAO().getAll().observe({ lifecycle },{
                listaCategorias = it
                categoriasAdapter = CategoriasAdapter(binding.root.context,listaCategorias)
                binding.lvListaCategorias.adapter = categoriasAdapter

                //binding.spinnerCategoriaHid.adapter = categoriasAdapter
                //var adaptadorSpin = ArrayAdapter<Categoria>(binding.root.context,R.layout.categorias_list_item,R.id.tvNomCat,listaCategorias)
                //binding.spinnerCategoriaHid.adapter = adaptadorSpin
                var adaptadorSpin = CategoriasSpinnerAdapter(binding.root.context, listaCategorias)
                binding.spinnerCategoriaHid.adapter = adaptadorSpin
            })
        }
    }

}