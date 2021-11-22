package com.example.tusabes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class BienvenidaFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmento = inflater.inflate(R.layout.fragment_bienvenida, container, false)

        val btnLogin = fragmento.findViewById<Button>(R.id.btnLogin)
        val btnRegistro = fragmento.findViewById<Button>(R.id.btnRegistro)

        btnLogin.setOnClickListener {
            childFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentPantallaBienvenida, LoginFragment::class.java,null,"login")
                .commit()
            hideButtons(btnLogin, btnRegistro)
        }

        btnRegistro.setOnClickListener {
            childFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentPantallaBienvenida, RegistroFragment::class.java,null,"registro")
                .commit()
            hideButtons(btnLogin, btnRegistro)
        }

        return fragmento

    }

    private fun hideButtons(btn1: Button, btn2: Button) {
        btn1.visibility = View.GONE
        btn2.visibility = View.GONE
    }
}