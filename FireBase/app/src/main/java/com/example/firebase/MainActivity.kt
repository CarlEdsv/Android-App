package com.example.firebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        firebaseAnalytics = Firebase.analytics

        title = "Autenticación"

        val email = findViewById<EditText>(R.id.emailText)
        val password = findViewById<EditText>(R.id.passwText)

        val reg = findViewById<Button>(R.id.signupButton)
        reg.setOnClickListener {
            signUp(email, password)
        }

        val log = findViewById<Button>(R.id.loginButton)
        log.setOnClickListener {
            logIn(email, password,Students::class.java,ProviderType.BASIC)
        }

        ///////////////////////////////////////////////////////////////////////////////////////////

        val email2 = findViewById<EditText>(R.id.emailText2)
        val password2 = findViewById<EditText>(R.id.passwText2)

        val reg2 = findViewById<Button>(R.id.signupButton2)
        reg2.setOnClickListener {
            signUp(email2,password2)
        }
        val log2 = findViewById<Button>(R.id.loginButton2)
        log2.setOnClickListener {
            logIn(email2, password2, Teachers::class.java, ProviderType.BASIC)
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

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error al autenticar el usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun onActivity(context: Class<out Activity>) {
        val intent = Intent(this, context)
        startActivity(intent)
    }

    private fun signUp(email:EditText, password: EditText){
        if (email.text.isBlank()||password.text.isBlank()){
            Toast.makeText(this,"Ingrese los datos",Toast.LENGTH_LONG).show()
        }else{
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) {
                    if(it.isSuccessful){
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show()
                    }else{
                        showAlert()
                    }

                }
        }
    }

    private fun logIn(email:EditText, password: EditText, activity:Class<out Activity>, provider: ProviderType){
        if (email.text.isBlank()||password.text.isBlank()){
            Toast.makeText(this,"Ingrese los datos",Toast.LENGTH_LONG).show()
        }else{
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        val intent = Intent(this, activity).apply {
                            putExtra("email", email.text.toString())
                            putExtra("provider",provider.name)
                        }
                        startActivity(intent)
                    } else {
                        showAlert()
                    }
                }

        }

    }
}



