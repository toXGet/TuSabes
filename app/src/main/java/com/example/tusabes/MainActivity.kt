package com.example.tusabes

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private var listaUsuarios: MutableList<Usuario> = mutableListOf(
        Usuario( "Carlos", "Zabala", "correo@mail.com",
                "miclave*123","2253168"),
        Usuario( "Minerva", "Dea", "mine@mail.com",
            "Clave*123","232253168"),
        Usuario( "Juan de Arco", "Juana", "jojovita@mail.com",
            "1234clave++123","2342534168")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAcepta = findViewById<Button>(R.id.btnAceptar)
        btnAcepta.setOnClickListener {
            val edtNombre = findViewById<EditText>(R.id.edtNombres)
            val edtApellido = findViewById<EditText>(R.id.edtApellidos)
            val edtCorreo = findViewById<EditText>(R.id.edtEmail)
            val edtClave = findViewById<EditText>(R.id.edtPassword)
            val edtTelefono = findViewById<EditText>(R.id.edtPhone)
            val cbCheck = findViewById<CheckBox>(R.id.cbCheck)

            if (validarTexto(edtNombre.text.toString()))
                if (validarTexto(edtApellido.text.toString()))
                    if (validarCorreo(edtCorreo.text.toString()))
                        if (validarNumero(edtTelefono.text.toString()))
                            if (validarContrasena(edtClave.text.toString()))
                                if(cbCheck.isChecked){
                                    listaUsuarios.add(Usuario(edtNombre.text.toString(),
                                        edtApellido.text.toString(),
                                        edtCorreo.text.toString(),
                                        edtClave.text.toString(),
                                        edtTelefono.text.toString()))
                                    AlertDialog.Builder(this)
                                        .setTitle("Usuario Registrado")
                                        .setMessage("Sus datos han sido registrados de manera correcta")
                                        .create()
                                        .show()
                                    println(listaUsuarios)
                                }
                                else{
                                    AlertDialog.Builder(this)
                                        .setTitle("Aceptar Terminos y Condiciones")
                                        .setMessage("Debe Aceptar los terminos y condiciones para continuar")
                                        .create()
                                        .show()
                                }
                            else noCumple("clave")
                        else noCumple("telefono")
                    else noCumple("e-mail")
                else noCumple("Apellidos")
            else noCumple("Nombres")

        }


    }

    private fun validarContrasena(clave: String): Boolean {
        var pattern: Pattern
        var matcher: Matcher

        val PATRON_CONTRASENA = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@!%*?&;:+])(?=\\S+$).{8,}$"

        pattern = Pattern.compile(PATRON_CONTRASENA)
        matcher = pattern.matcher(clave)

        return matcher.matches();

    }

    private fun validarNumero(numero: String): Boolean {
        var patron: Pattern
        var comparador: Matcher
        val expresion = "^\\d+\$"
        patron = Pattern.compile(expresion)
        comparador = patron.matcher(numero)
        return comparador.matches()
    }

    private fun validarCorreo(correo: String): Boolean {
        var patron: Pattern
        var comparador: Matcher
        val regAseguir = "^[-a-z0-9~!\$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!\$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?\$"
        patron = Pattern.compile(regAseguir)
        comparador = patron.matcher(correo)
        return comparador.matches()
    }

    private fun validarTexto(cadena: String): Boolean {
        var patron: Pattern
        var comparador: Matcher
        val expresion = "^[\\w ]+$"
        patron = Pattern.compile(expresion)
        comparador = patron.matcher(cadena)
        return comparador.matches()
    }
    private fun noCumple(cadena: String){
        var mensaje: String
        when (cadena){
            "clave" -> mensaje="La contraseña debe ser de mínimo 8 caracteres, contener numeros, letras (mayusculas y minusculas) y caracteres especiales"
            "e-mail" -> mensaje="El correo debe ser válido del tipo correo@servidor.com"
            "telefono" -> mensaje="El teléfono deben ser sólo números sin guiones, espacios o caracteres especiales"
            else -> mensaje = "Debe diligenciar correctamente el campo " + cadena
        }
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

}