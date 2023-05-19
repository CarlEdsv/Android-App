package com.example.firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

enum class ProviderType{
    BASIC
}

class Students : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var documento : Documento

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.students)

        title = "Inicio"

        auth = Firebase.auth

        documento = Documento()

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

            //regDialog(this)

            val opcion = grade.selectedItem.toString()
            val agenum = age.text.toString().toIntOrNull()?: 0

            val student = hashMapOf<String, Any>(
                "nombre" to name.text.toString(),
                "edad" to agenum,
                "grado" to opcion
            )

            documento.adDocument(name, age,grade,"students",student,this)
        }

        val show = findViewById<Button>(R.id.showButtton)
        show.setOnClickListener {
            val intent = Intent(this, Home::class.java).apply {
                putExtra("cero",0)
            }
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

    /*private fun regDialog(contexto: Context) {

        val grades2 = listOf(
            "Grados", "Primero", "Segundo", "Tercero", "Cuarto",
            "Quinto", "Sexto", "Séptimo", "Octavo", "Noveno"
        )

        val dialogView = LayoutInflater.from(contexto).inflate(R.layout.dialog_layout, null)

        val builder = AlertDialog.Builder(contexto)
            .setView(dialogView)
            .setTitle("ingresar Datos")
            .setCancelable(false)
            .setPositiveButton("Guardar") { dialog, which ->

                val text1 = findViewById<EditText>(R.id.edit_text_1)
                val text2 = findViewById<EditText>(R.id.edit_text_2)
                val spinnerDialog = findViewById<Spinner>(R.id.DialogSpinner)

                val spinDiagAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, grades2)

                spinDiagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerDialog.adapter = spinDiagAdapter

                val opcion = spinnerDialog.selectedItem.toString()
                val agenum = text2.text.toString().toIntOrNull()?: 0

                val student = hashMapOf<String, Any>(
                    "nombre" to text1.text.toString(),
                    "edad" to agenum,
                    "grado" to opcion
                )

                documento.adDocument(text1, text2,spinnerDialog,"students",student,contexto)
            }
            .setNegativeButton("Salir") { dialog, which ->
                dialog.dismiss()
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }*/
}






