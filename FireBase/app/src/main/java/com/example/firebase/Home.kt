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

    lateinit var sqliteHelper:SqliteHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sqliteHelper = SqliteHelper(this)

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)

        // Agrega un espaciado de 16dp entre cada elemento
        recycler.addItemDecoration(ItemRecycler(16))
        recycler.adapter = Adaptador(sqliteHelper.getTarea())

        val regresar = findViewById<Button>(R.id.returnButton)
        regresar.setOnClickListener {
            val intent = Intent(this, Students::class.java)
            startActivity(intent)
        }

        val nuevo = findViewById<Button>(R.id.añadir)
        nuevo.setOnClickListener {
            val intent = Intent(this, Tarea::class.java)
            startActivity(intent)
        }
    }

}