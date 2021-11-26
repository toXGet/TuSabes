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
import androidx.core.os.bundleOf
import com.example.tusabes.database.TuSabesDB
import com.example.tusabes.databinding.FragmentRegistroBinding
import com.example.tusabes.model.User
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegistroFragment : Fragment() {
    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegistroBinding.inflate(inflater,container,false)

        binding.btnRegistro.setOnClickListener {
            registrar()
        }

        binding.tvDeclaracion.setOnClickListener {
            println("Click en Declaración de Privacidad")
            parentFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentPantallaBienvenida,PaginasLegalesFragment::class.java
                    , bundleOf("tipo" to "declaracion"),"legales")
                .addToBackStack("legales")
                .commit()
        }

        binding.tvTerminos.setOnClickListener {
            println("Click en Términos y condiciones")
            parentFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentPantallaBienvenida,PaginasLegalesFragment::class.java
                    , bundleOf("tipo" to "terminos"),"legales")
                .addToBackStack("legales")
                .commit()
        }

        return binding.root
    }

    private fun grabarDatos(usuario: User) {
        val context = activity?.applicationContext
        CoroutineScope(Dispatchers.IO).launch{
            val database = context?.let { TuSabesDB.getDataBase(it)}
            if (database != null){
                database.UsersDAO().insert(usuario)
            }
        }
        sleep(500)
        val boton = {_:DialogInterface,_:Int -> iniciar(usuario.rol)}
        AlertDialog.Builder(binding.root.context)
            .setTitle("${usuario.usuario} Registrado")
            .setMessage("Tus datos han sido registrados de manera correcta")
            .setPositiveButton("OK", boton)
            .create()
            .show()
    }

    private fun iniciar(rol: String) {
        if (rol == "Profesor"){
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContenedorPrincipal,ProfesorFragment::class.java,null,"profesor")
                ?.commit()
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
        BienvenidaFragment().cerrar()
    }

    private fun noCumple(tipoDato: String) {
        var mensaje: String
        when (tipoDato){
            "clave" -> mensaje="La contraseña debe ser de mínimo 8 caracteres, contener numeros, letras (mayusculas y minusculas) y caracteres especiales"
            "mismaClave" -> mensaje="La contraseña no concuerda con su comprobación"
            "correo" -> mensaje="El correo debe ser válido del tipo correo@servidor.com"
            "nick" -> mensaje="Ese nombre de usuario ya está en uso, por favor escoja otro"
            "terminos" -> mensaje = "Debe Aceptar los terminos y condiciones para continuar"
            "todo" -> mensaje= "Debes llenar todos los campos, no pueden quedar campos vacíos"
            else -> mensaje = "Debe diligenciar correctamente el campo " + tipoDato
        }
        Toast.makeText(activity?.applicationContext, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun registrar() : Boolean{
        if (binding.edtNombres.text.isNullOrEmpty()
            ||binding.edtApellidos.text.isNullOrEmpty()
            ||binding.edtEmail.text.isNullOrEmpty()
            ||binding.edtNombreUsuario.text.isNullOrEmpty()
            ||binding.edtPassword.text.isNullOrEmpty()
            ||binding.edtConfirmaPasswd.text.isNullOrEmpty()){
                noCumple("todo")
        }else if (!validarInfo(binding.edtNombres.text.toString(),"texto")){
            noCumple("Nombres")
            return false
        }else if (!validarInfo(binding.edtApellidos.text.toString(),"texto")) {
            noCumple("Apellidos")
            return false
        }else if (!validarInfo(binding.edtEmail.text.toString(),"correo")) {
            noCumple("correo")
            return false
        }else if (!validarInfo(binding.edtNombreUsuario.text.toString(),"nick")) {
            noCumple("nick")
            return false
        }else if (!validarInfo(binding.edtPassword.text.toString(),"clave")) {
            noCumple("clave")
            return false
        }else if (binding.edtConfirmaPasswd.text.toString() != binding.edtPassword.text.toString()) {
            noCumple("mismaClave")
            return false
        }else if (!binding.chbAceptarTyC.isChecked) {
            noCumple("terminos")
            return false
        }else {
            val rol: String = when (binding.chbRol.isChecked) {
                true -> "Profesor"
                false -> "Estudiante"
            }
            val usuario = User(
                0,
                "${binding.edtNombreUsuario.text}",
                "${binding.edtPassword.text}",
                "${binding.edtEmail.text}",
                "${binding.edtNombres.text}",
                "${binding.edtApellidos.text}",
                rol
            )
            grabarDatos(usuario)

        }
        return true
    }

    private fun validarInfo(entrada: String, tipoDato: String) : Boolean{

        if (tipoDato == "nick") {
            val context = activity?.applicationContext
            var usuario: User
            var comparadorId = 0
            CoroutineScope(Dispatchers.IO).launch {
                val database = context?.let { TuSabesDB.getDataBase(it) }
                if (database != null) {
                    usuario = database.UsersDAO().getUserByNick(entrada)
                    if (usuario == null){ comparadorId = 0}else{ comparadorId = usuario.id }
                    println("EL DATO ES $usuario")
                    print(database.UsersDAO().getAll())
                }
            }
            sleep(500)
            return comparadorId == 0
        } else {
            var regularExp: String
            var patron: Pattern
            var comparador: Matcher

            when (tipoDato) {
                "texto" -> regularExp = "^[\\w ]+$"
                "correo" -> regularExp =
                    "^[-a-z0-9~!\$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!\$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?\$"
                "clave" -> regularExp =
                    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@!%*?&;:+])(?=\\S+$).{8,}$"
                else -> regularExp = ""
            }
            patron = Pattern.compile(regularExp)
            comparador = patron.matcher(entrada)
            return comparador.matches()
        }

    }
}