package com.example.tusabes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class PaginasLegales : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paginas_legales)

        val intLlegada: Intent = intent
        val tipo:String = intLlegada.getStringExtra("tipo").toString()

        val tvTitulo = findViewById<TextView>(R.id.tvTitulo)
        val tvContenido = findViewById<TextView>(R.id.tvContenido)
        val btnAceptar = findViewById<Button>(R.id.btnAceptar)

        if (tipo == "declaracion"){
            tvTitulo.text = getString(R.string.declaracionPrivacidadTitulo)
            tvContenido.text = getString(R.string.declaracionPrivacidad)
        }
        if (tipo == "terminos"){
            tvTitulo.text = getString(R.string.condicionesUsoTitulo)
            tvContenido.text = getString(R.string.condicionesUso)
        }

        btnAceptar.setOnClickListener { finish() }
    }
}