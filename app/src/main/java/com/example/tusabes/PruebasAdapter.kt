package com.example.tusabes

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.tusabes.model.Prueba

class PruebasAdapter(private val mContext: Context, val listaPruebas: List<Prueba>)
    : ArrayAdapter<Prueba>(mContext,0,listaPruebas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext)
            .inflate(R.layout.pruebas_list_item,parent,false)
        val prueba = listaPruebas[position]

        val tvIdPrueba = layout.findViewById<TextView>(R.id.tvIdPrueba)
        val tvFecha = layout.findViewById<TextView>(R.id.tvFechaHoraPrueba)
        val tvResuelto = layout.findViewById<TextView>(R.id.tvResuelto)
        val tvNota = layout.findViewById<TextView>(R.id.tvNota)

        if (prueba.resultado.toString().isNullOrEmpty() || prueba.resultado == 0){
            tvResuelto.text = "No Resuelta"
            tvResuelto.setTextColor(ContextCompat.getColor(mContext, R.color.blue_700))
            tvNota.visibility = GONE
        }else{
            tvResuelto.text = "Resuelta"
            tvResuelto.setTextColor(ContextCompat.getColor(mContext,R.color.teal_700))
            tvNota.text = "Calificación: ${prueba.resultado}"
            if (prueba.resultado < 3){
                tvNota.setTextColor(ContextCompat.getColor(mContext,R.color.red_700))
            }else{
                tvNota.setTextColor(ContextCompat.getColor(mContext,R.color.teal_700))
            }
        }

        tvIdPrueba.text = "${prueba.id}"
        tvFecha.text = "${prueba.fecha.toString()}"

        return layout
    }
}