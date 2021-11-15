package com.example.tusabes

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

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
                                }
                                else{
                                    AlertDialog.Builder(this)
                                        .setTitle("Aceptar Terminos y Condiciones")
                                        .setMessage("Debe Aceptar los terminos y condiciones para continuar")
                                        .create()
                                        .show()
                                }
                            else noCumpleClave()
                        else noCumple("Número de Teléfono")
                    else noCumple("E-mail")
                else noCumple("Apellidos")
            else noCumple("Nombres")


            
            if (validarContrasena(edtPass.text.toString()))
                listaUsuarios.add(Usuario(edtUsuario.text.toString(), edtPass.text.toString()))
            else
                Toast.makeText(this, "No cumple con las politicas", Toast.LENGTH_LONG).show()

        }


    }

    private fun noCumpleClave() {

    }

    private fun validarContrasena(clave: String): Boolean {

    }

    private fun validarNumero(numero: String): Boolean {

    }

    private fun validarCorreo(correo: String): Boolean {

    }

    private fun validarTexto(cadena: String): Boolean {

    }
    private fun noCumple(cadena: String){
        var mensaje : String = "Debe diligenciar correctamente el campo " + cadena
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

}