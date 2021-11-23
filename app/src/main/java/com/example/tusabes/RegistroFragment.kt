package com.example.tusabes

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.tusabes.database.TuSabesDB
import com.example.tusabes.model.User
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegistroFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmento = inflater.inflate(R.layout.fragment_registro, container, false)

        val edtNombres = fragmento.findViewById<TextInputEditText>(R.id.edtNombres)
        val edtApellidos = fragmento.findViewById<TextInputEditText>(R.id.edtApellidos)
        val edtEmail = fragmento.findViewById<TextInputEditText>(R.id.edtEmail)
        val edtNick = fragmento.findViewById<TextInputEditText>(R.id.edtNombreUsuario)
        val edtClave = fragmento.findViewById<TextInputEditText>(R.id.edtPassword)
        val edtClaveConf = fragmento.findViewById<TextInputEditText>(R.id.edtConfirmaPasswd)
        val chkRol = fragmento.findViewById<MaterialCheckBox>(R.id.chbRol)
        val chkTerminos = fragmento.findViewById<MaterialCheckBox>(R.id.chbAceptarTyC)
        val tvDeclaracion = fragmento.findViewById<TextView>(R.id.tvDeclaracion)
        val tvTerminos = fragmento.findViewById<TextView>(R.id.tvTerminos)
        val btnRegistro = fragmento.findViewById<Button>(R.id.btnRegistro)

        btnRegistro.setOnClickListener {
            if (validarInfo(edtNombres.text.toString(), "texto"))
                if (validarInfo(edtApellidos.text.toString(), "texto"))
                    if (validarInfo(edtEmail.text.toString(), "correo"))
                        if (validarInfo(edtNick.text.toString(), "nick"))
                            if (validarInfo(edtClave.text.toString(), "clave"))
                                if (edtClaveConf.text.toString() == edtClave.text.toString())
                                    if (chkTerminos.isChecked){
                                        val rol : String
                                        rol = when (chkRol.isChecked){
                                            true -> "Profesor"
                                            false -> "Estudiante"
                                        }
                                        val usuario = User(0,"${edtNick.text}","${edtClave.text}",
                                            "${edtEmail.text}","${edtNombres.text}",
                                            "${edtApellidos.text}", rol)
                                        grabarDatos(usuario, fragmento)
                                    }else{noCumple("terminos")}
                                else noCumple("mismaClave")
                            else noCumple("clave")
                        else noCumple("nick")
                    else noCumple("correo")
                else noCumple("apellidos")
            else noCumple("nombres")
        }

        return fragmento
    }

    private fun grabarDatos(usuario: User, vista: View) {
        val context = activity?.applicationContext
        CoroutineScope(Dispatchers.IO).launch{
            val database = context?.let { TuSabesDB.getDataBase(it)}
            if (database != null){
                database.UsersDAO().insert(usuario)
            }
        }
        val boton = {_:DialogInterface,_:Int -> iniciar(usuario.rol)}
        AlertDialog.Builder(vista.context)
            .setTitle("${usuario.usuario} Registrado")
            .setMessage("Tus datos han sido registrados de manera correcta")
            .setPositiveButton("OK", boton)
            .create()
            .show()
    }

    private fun iniciar(rol: String) {
       if (rol == "Profesor"){

        }else {
            activity?.supportFragmentManager?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.replace(
                    R.id.fragmentContenedorPrincipal,
                    EstudianteFragment::class.java,
                    null,
                    "estudiante"
                )
                ?.commit()
        }
    }

    private fun noCumple(tipoDato: String) {
        var mensaje: String
        when (tipoDato){
            "clave" -> mensaje="La contraseña debe ser de mínimo 8 caracteres, contener numeros, letras (mayusculas y minusculas) y caracteres especiales"
            "mismaClave" -> mensaje="La contraseña no concuerda con su comprobación"
            "correo" -> mensaje="El correo debe ser válido del tipo correo@servidor.com"
            "nick" -> mensaje="Ese nombre de usuario ya está en uso, por favor escoja otro"
            "terminos" -> mensaje = "Debe Aceptar los terminos y condiciones para continuar"
            else -> mensaje = "Debe diligenciar correctamente el campo " + tipoDato
        }
        Toast.makeText(activity?.applicationContext, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun validarInfo(entrada: String, tipoDato: String) : Boolean{
        val context = activity?.applicationContext
        var existe: Int = 0
        if (tipoDato == "nick"){
            CoroutineScope(Dispatchers.IO).launch{
                val database = context?.let { TuSabesDB.getDataBase(it)}
                if (database != null){
                    existe = database.UsersDAO().getUserByNick(entrada).id
                    println("El numero es $existe")
                }
            }
            return existe == 0
        }
        else{
            var regularExp: String
            var patron: Pattern
            var comparador: Matcher

            when (tipoDato){
                "texto" -> regularExp = "^[\\w ]+$"
                "correo" -> regularExp = "^[-a-z0-9~!\$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!\$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?\$"
                "clave" -> regularExp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@!%*?&;:+])(?=\\S+$).{8,}$"
                else -> regularExp = ""
            }
            patron = Pattern.compile(regularExp)
            comparador = patron.matcher(entrada)
            return comparador.matches()
        }
    }
}