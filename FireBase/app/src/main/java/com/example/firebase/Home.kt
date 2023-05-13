package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val db = Firebase.firestore

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        // Agrega un espaciado de 16dp entre cada elemento
        recycler.addItemDecoration(ItemRecycler(16))

        val myList = arrayListOf<Data>()

        db.collection("students")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val student = document.data
                    myList.add(Data(
                        "Nombre: "+student["nombre"],
                        "Edad: "+student["edad"].toString(),
                        "Grado: "+student["grado"]
                    )
                    )
                    recycler.adapter = Adaptador(myList)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Consulta de datos fallida: $exception",Toast.LENGTH_LONG).show()
            }



        val regresar = findViewById<Button>(R.id.returnButton)
        regresar.setOnClickListener {
            val intent = Intent(this, Activity2::class.java)
            startActivity(intent)
        }

    }
}