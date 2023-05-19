package com.example.firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {

    private val db = Firebase.firestore

    private lateinit var documento: Documento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        // Agrega un espaciado de 16dp entre cada elemento
        recycler.addItemDecoration(ItemRecycler(16))

        documento = Documento()

        val extras = intent.extras
        if (extras != null) {
            if (extras.containsKey("cero")) {
                documento.getDocuments(this, recycler, "students", "nombre", "edad", "grado")


            } else {
                documento.getDocuments(this, recycler, "teachers", "nombre", "edad", "asignatura")
            }

            val regresar = findViewById<Button>(R.id.returnButton)
            regresar.setOnClickListener {
                val extras = intent.extras
                if (extras != null) {
                    if (extras.containsKey("cero")) {
                        val intent = Intent(this, Students::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, Teachers::class.java)
                        startActivity(intent)
                    }
                }
            }

        }
    }
}