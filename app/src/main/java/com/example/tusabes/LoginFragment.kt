package com.example.tusabes

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import com.example.tusabes.database.TuSabesDB
import com.example.tusabes.databinding.FragmentLoginBinding
import com.example.tusabes.model.User
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import java.lang.Thread.sleep

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater,container,false)

        binding.btnIngresar.setOnClickListener {
            validar(binding.edtNombreUsuario.text.toString(), binding.edtPassword.text.toString())
        }

        return binding.root
    }

    private fun validar(usuario: String, clave: String) {
        val context = activity?.applicationContext
        if (usuario.isNullOrEmpty() || clave.isNullOrEmpty()){
            Toast.makeText(context,"Debes ingresar tu nombre de usuario y tu clave", Toast.LENGTH_LONG).show()
        }else{
            var loginUser: User? = null
            runBlocking(Dispatchers.IO){
                val database = context?.let { TuSabesDB.getDataBase(it)}
                if (database != null){
                    loginUser = database.UsersDAO().getUserByNick(usuario)
                    println("segunda entrada usuario es $loginUser")
                }
            }

            if (loginUser == null) {
                    println("tercera entrada usuario es $loginUser")
                    val botonCancelar = { _: DialogInterface, _: Int ->
                        binding.edtNombreUsuario.setText("")
                        binding.edtPassword.setText("")
                    }
                    val botonRegistro = { _: DialogInterface, _: Int -> registrarse(usuario) }
                    AlertDialog.Builder(binding.root.context)
                        .setTitle("$usuario No está registrado")
                        .setMessage("No te encuentras registrado en la app")
                        .setPositiveButton("REGISTRARSE", botonRegistro)
                        .setNegativeButton("CANCELAR", botonCancelar)
                        .create()
                        .show()
            }else if (loginUser!!.clave != clave) {
                    println("quinta entrada usuario es $loginUser")
                    Toast.makeText(
                        context,
                        "La clave ingresada está errada, verifica y vuelve a ingresarla",
                        Toast.LENGTH_LONG
                    ).show()
            }else{
                    ingresar(loginUser!!)
            }
        }
    }

    private fun ingresar(loginUser: User) {
        val bundleUser = bundleOf(
            "id" to loginUser.id,
            "usuario" to loginUser.usuario,
            "rol" to loginUser.rol
        )
        if (loginUser.rol == "Profesor"){
            activity?.supportFragmentManager?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.replace(R.id.fragmentContenedorPrincipal,
                    ProfesorFragment::class.java,bundleUser,"profesor")
                ?.commit()
            //activity?.supportFragmentManager?.setFragmentResult("requestKey", bundleUser)
        }else {
            activity?.supportFragmentManager?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.replace(R.id.fragmentContenedorPrincipal,
                    EstudianteFragment::class.java,bundleUser,"estudiante")
                ?.commit()
            //activity?.supportFragmentManager?.setFragmentResult("requestKey", bundleUser)
        }
        BienvenidaFragment().cerrar()

    }

    private fun registrarse(nombre: String) {
        val bundleUser = bundleOf("usuario" to nombre)
        parentFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragmentPantallaBienvenida,RegistroFragment::class.java,bundleUser,"registro_login")
            .commit()
        //parentFragmentManager.setFragmentResult("requestKey", bundleUser)

        //BienvenidaFragment().hideButtons("registro")
    }
}