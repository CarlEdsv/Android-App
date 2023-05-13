package com.example.firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import kotlin.math.log

enum class ProviderType{
    BASIC
}

class Activity2 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity2)

        title = "Inicio"

        val db = Firebase.firestore
        auth = Firebase.auth

        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        var count = sharedPrefs.getInt("count", 0)

        val correo = findViewById<TextView>(R.id.txt1)
        val proveedor = findViewById<TextView>(R.id.txt2)

        val bundle: Bundle? = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        correo.text = email
        proveedor.text = provider

        val name = findViewById<EditText>(R.id.nameText)
        val age = findViewById<EditText>(R.id.ageText)
        val grade = findViewById<Spinner>(R.id.spinner)

        val grades = listOf(
            "Grados", "Primero", "Segundo", "Tercero", "Cuarto",
            "Quinto", "Sexto", "Séptimo", "Octavo", "Noveno"
        )

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, grades)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        grade.adapter = spinnerAdapter

        val logOut = findViewById<Button>(R.id.logOutButton)
        logOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val add = findViewById<Button>(R.id.addButton)
        add.setOnClickListener {

            val agenum = age.text.toString().toIntOrNull()
            // Cambiar a si selecciona el primer elemento
            val opcion = grade.selectedItem.toString()

            if (name.text.isEmpty() || age.text.isEmpty() || opcion.isEmpty()) {
                Toast.makeText(this, "Por favor Ingrese todos los Campos", Toast.LENGTH_LONG).show()

            } else if (agenum == null) {
                Toast.makeText(this, "Ingrese una edad correcta", Toast.LENGTH_LONG).show()
            }

            else{

                //val studentRef = db.collection("students")
                //studentRef.document("1")
                val student = hashMapOf(
                    "nombre" to name.text.toString(),
                    "edad" to agenum,
                    "grado" to opcion
                )

                count++
                sharedPrefs.edit().putInt("count", count).apply()

                db.collection("students").document("$count").set(student).addOnSuccessListener {
                    Toast.makeText(this,"Datos guardados existosamente", Toast.LENGTH_LONG).show()
                }
                    .addOnFailureListener{e ->
                        Toast.makeText(this,"Error al añadir estudiante: $e", Toast.LENGTH_LONG).show()
                    }
            }

        }

        val show = findViewById<Button>(R.id.showButtton)
        show.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }



        }
}