package com.example.firebase

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.firebase.databinding.ActivityMainBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        /*Thread.sleep(2000)
        setTheme(R.style.Theme_FireBase)*/
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth

        firebaseAnalytics = Firebase.analytics

        title = "Autenticación"

        binding.loginButton.setOnClickListener {
            if (binding.emailText.text.contains("@ugb.edu.sv")) {
                binding.emailText.error = "Está tratando de ingresar con un correo docente"
            } else {
                logIn(binding.emailText, binding.passwText, Students::class.java, ProviderType.BASIC)
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////////////

        binding.loginButton2.setOnClickListener {
            if (binding.emailText2.text.contains("@ugb.edu.sv")) {
                logIn(binding.emailText2, binding.passwText2, Teachers::class.java, ProviderType.BASIC)
            } else {
                binding.emailText2.error = "Debe ingresar con un correo docente"
            }
        }

        var passVisibility = false
        binding.showPass.setOnClickListener {
            passVisibility = !passVisibility // Invertir el estado de visibilidad

            if (passVisibility) {
                // Mostrar la contraseña
                binding.passwText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                // Ocultar la contraseña
                binding.passwText.transformationMethod = PasswordTransformationMethod.getInstance()
            }

        }

        binding.showpass2.setOnClickListener {
            passVisibility = !passVisibility // Invertir el estado de visibilidad

            if (passVisibility) {
                // Mostrar la contraseña
                binding.passwText2.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                // Ocultar la contraseña
                binding.passwText2.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val authLayout = findViewById<LinearLayout>(R.id.authLayout)
        authLayout.visibility = View.VISIBLE
        val currentUser = auth.currentUser
        currentUser?.let {
            val email = it.email.toString()
            if (email.contains("@ugb.edu.sv")){
                onActivity(Teachers::class.java)
            }else{
                onActivity(Students::class.java)
            }
        }
    }

    private fun showAlert(error: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un erroral autenticar el usuario")
        builder.setMessage("Se ha producido un error: $error")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun onActivity(context: Class<out Activity>) {
        val intent = Intent(this, context)
        startActivity(intent)
    }
    private fun logIn(email:EditText, password: EditText, activity:Class<out Activity>, provider: ProviderType){
        if (email.text.isBlank()||password.text.isBlank()){
            Toast.makeText(this,"Ingrese los datos",Toast.LENGTH_LONG).show()
        }else{

            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnSuccessListener {
                    val intent = Intent(this, activity).apply {
                        putExtra("email", email.text.toString())
                        putExtra("provider",provider.name)
                    }
                    startActivity(intent)
                }
                .addOnFailureListener{e->
                    showAlert(e.toString())
                }


        }

    }

    private fun showPass(passText: EditText, button: Button){
        var passVisibility = false
        button.setOnClickListener {
            passVisibility = !passVisibility // Invertir el estado de visibilidad

            if (passVisibility) {
                // Mostrar la contraseña
                passText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                // Ocultar la contraseña
                passText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

    }

    /*private fun signUp(email: EditText, password: EditText) {
        if (email.text.isBlank() || password.text.isBlank()) {
            Toast.makeText(this, "Ingrese los datos", Toast.LENGTH_LONG).show()
        } else if(password.text.length < 6){
            password.error = "Ingrese una contraseña mayor de 6 dígitos"
        }
        else {
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show()
                    } else {
                        showAlert()
                    }
                }
        }
    }*/

}



