package com.example.tusabes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.tusabes.model.Categoria

class CategoriasSpinnerAdapter(private val mContext: Context, val listaCategorias: List<Categoria>)
    : BaseAdapter(){
    override fun getCount(): Int {
        return listaCategorias.count()
    }

    override fun getItem(position: Int): Any {
        return listaCategorias[position]
    }

    override fun getItemId(position: Int): Long {
        return listaCategorias[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layout = LayoutInflater.from(mContext)
            .inflate(R.layout.categorias_list_item,parent,false)
        val categoria = listaCategorias[position]
        val tvIdCat = layout.findViewById<TextView>(R.id.tvIdCat)
        val tvNomCat = layout.findViewById<TextView>(R.id.tvNomCat)

        tvIdCat.text = categoria.id.toString()
        tvNomCat.text = categoria.nombre

        return layout
    }
}