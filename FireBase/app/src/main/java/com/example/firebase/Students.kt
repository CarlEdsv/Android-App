package com.example.firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.firebase.databinding.ActivityTeachersBinding
import com.example.firebase.databinding.StudentsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

enum class ProviderType{
    BASIC
}

class Students : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var documento : Documento
    private lateinit var binding: StudentsBinding

    private val db = Firebase.firestore

    lateinit var sqliteHelper:SqliteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StudentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sqliteHelper = SqliteHelper(this)

        title = "Inicio"

        auth = Firebase.auth

        documento = Documento()

        val bundle: Bundle? = intent.extras
        val email = bundle?.getString("email")
        val pref = getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("email", email)
        editor.apply()

        binding.logOutButton.setOnClickListener {
            editor.clear()
            editor.apply()

            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.tarea.setOnClickListener{
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        binding.txTarea.setOnClickListener{
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        binding.contador.text = sqliteHelper.numTareas()
    }

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth

        binding.stLayout.visibility = View.VISIBLE
        val currentUser = auth.currentUser

        val email = currentUser?.let {
            it.email.toString()
        }
        if (email != null) {
            showQr(email)
            binding.email.text=email.toString()
        }
    }

    private fun showQr(email: String) {
        val preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val savedImageUrl = preferences.getString("qrImageUrl", null)

        if (savedImageUrl != null) {
            Glide.with(this)
                .load(savedImageUrl)
                .into(binding.qrImage)
        } else {
            db.collection("students").document(email).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val inf = document.data
                        val urlImageQr = inf?.get("URL").toString()

                        val editor = preferences.edit()
                        editor.putString("qrImageUrl", urlImageQr)
                        editor.apply()

                        // Cargar la imagen desde la URL
                        Glide.with(this)
                            .load(urlImageQr)
                            .into(binding.qrImage)
                    } else {
                        Toast.makeText(this, "Qr no encontrado", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Problemas de tipo: $exception", Toast.LENGTH_LONG).show()
                }
        }
    }


}






