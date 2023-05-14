package com.example.firebase

import android.content.Context
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
            if (email.text.isEmpty()||password.text.isEmpty()){
                Toast.makeText(this,"Ingrese los datos",Toast.LENGTH_LONG).show()
            }else{
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            activity2(it.result?.user?.email?: "", ProviderType.BASIC)
                        } else {
                            showAlert()
                        }
                    }

            }
        }

        val log = findViewById<Button>(R.id.loginButton)
        log.setOnClickListener {
            if (email.text.isEmpty()||password.text.isEmpty()){
                Toast.makeText(this,"Ingrese los datos",Toast.LENGTH_LONG).show()
            }else{
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            activity2(it.result?.user?.email?: "", ProviderType.BASIC)
                        } else {
                            showAlert()
                        }
                    }

            }
        }

        //session()
    }

    override fun onStart() {
        super.onStart()
        val authLayout = findViewById<LinearLayout>(R.id.authLayout)
        authLayout.visibility = View.VISIBLE
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, Activity2::class.java))
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

    private fun activity2(email : String, provider: ProviderType){
        val intent = Intent(this, Activity2::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(intent)
    }

    private fun session(){
        val logData = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = logData.getString("email",null)
        val provider = logData.getString("provider",null)
        if(email != null && provider != null){
            val authLayout = findViewById<LinearLayout>(R.id.authLayout)
            authLayout.visibility = View.INVISIBLE
            activity2(email,ProviderType.BASIC)
        }
    }
}



