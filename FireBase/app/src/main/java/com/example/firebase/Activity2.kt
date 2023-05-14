package com.example.firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

        val bundle: Bundle? = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        val logData = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        logData.putString("email",email)
        logData.putString("provider",provider)
        logData.apply()

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

        val add = findViewById<Button>(R.id.addButton)

        add.setOnClickListener {

            val agenum = age.text.toString().toIntOrNull()
            val opcion = grade.selectedItem.toString()
            // selectedItemId devuelve un dato de tipo Long
            val itemSelected = grade.selectedItemId

            if (name.text.isBlank() || age.text.isBlank() || itemSelected == 0L) {
                Toast.makeText(this, "Por favor Ingrese todos los Campos", Toast.LENGTH_LONG).show()

            } else if (agenum == null) {
                Toast.makeText(this, "Ingrese una edad correcta", Toast.LENGTH_LONG).show()
            }

            else{
                val student = hashMapOf(
                    "nombre" to name.text.toString(),
                    "edad" to agenum,
                    "grado" to opcion
                )

                val countRef = db.collection("students").document("Contador")
                countRef.update("Valor", FieldValue.increment(1))
                    .addOnSuccessListener {
                        countRef.get().addOnSuccessListener { document ->
                            if (document != null) {
                                val inf = document.data
                                val valor = inf?.get("Valor").toString().toInt()
                                db.collection("students").document("$valor").set(student)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Datos guardados existosamente", Toast.LENGTH_LONG).show()
                                        name.text.clear()
                                        age.text.clear()
                                        grade.setSelection(0)
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Error al añadir estudiante: $e", Toast.LENGTH_LONG).show()
                                    }
                            } else {
                                Toast.makeText(this, "No se encuentra el nuevo contador", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Problemas de tipo: $it", Toast.LENGTH_LONG).show()
                    }
            }

        }

        val show = findViewById<Button>(R.id.showButtton)
        show.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        val logOut = findViewById<Button>(R.id.logOutButton)
        logOut.setOnClickListener {
            val infoLog = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            infoLog.clear()
            infoLog.apply()

            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}