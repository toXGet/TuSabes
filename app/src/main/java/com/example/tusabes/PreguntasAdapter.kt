package com.example.tusabes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.tusabes.model.Categoria
import com.example.tusabes.model.Pregunta

class PreguntasAdapter(private val mContext: Context, val listaPreguntas: List<Pregunta>,
                        val listaCategorias: List<Categoria>)
    : ArrayAdapter<Pregunta>(mContext,0,listaPreguntas){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext)
            .inflate(R.layout.preguntas_list_item,parent,false)
        val pregunta = listaPreguntas[position]

        val tvTexto = layout.findViewById<TextView>(R.id.tvTextoPregunta)
        val tvCategoria = layout.findViewById<TextView>(R.id.tvCategoriaPregunta)

        if (pregunta.enunciado.length > 20) {
            tvTexto.text = "${pregunta.enunciado.subSequence(0..20)}..."
        }else {
            tvTexto.text = pregunta.enunciado
        }
        for (iter in listaCategorias){
            if (iter.id == pregunta.idCategoria){
                tvCategoria.text = "${iter.nombre}"
            }
        }
        return layout
    }
}