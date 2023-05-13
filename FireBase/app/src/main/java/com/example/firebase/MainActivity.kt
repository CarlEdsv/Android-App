package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

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
                            nextActivity(it.result?.user?.email?: "", ProviderType.BASIC)
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
                            nextActivity(it.result?.user?.email?: "", ProviderType.BASIC)
                        } else {
                            showAlert()
                        }
                    }

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

    private fun nextActivity(email : String, provider: ProviderType){
        val intent = Intent(this, Activity2::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(intent)
    }
}



