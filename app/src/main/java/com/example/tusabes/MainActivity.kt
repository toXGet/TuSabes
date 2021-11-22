package com.example.tusabes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       supportFragmentManager.beginTransaction().setReorderingAllowed(true)
           .replace(R.id.fragmentContenedorPrincipal, BienvenidaFragment::class.java,null,"bienvenida")
           .commit()

   }
}