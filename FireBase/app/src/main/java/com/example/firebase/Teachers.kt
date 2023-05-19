package com.example.firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Teachers : AppCompatActivity() {

    private lateinit var documento : Documento

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teachers)

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

        val name2 = findViewById<EditText>(R.id.nameText2)
        val age2 = findViewById<EditText>(R.id.ageText2)

        val subject = findViewById<Spinner>(R.id.spinner2)

        val subjects = listOf(
            "Materias", "Lenguaje", "Sociales", "Matemáticas", "Ciencias",
            "Educación Física", "Artes"
        )

        val spinnerAdapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, subjects)

        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subject.adapter = spinnerAdapter2

        val add2 = findViewById<Button>(R.id.addButton2)
        add2.setOnClickListener {

            val opcion2 = subject.selectedItem.toString()
            val agenum2 = age2.text.toString().toIntOrNull()?: 0

            val teacher = hashMapOf<String, Any>(
                "nombre" to name2.text.toString(),
                "edad" to agenum2,
                "asignatura" to opcion2
            )

            documento.adDocument(name2, age2,subject,"teachers",teacher,this)

        }

        val show2 = findViewById<Button>(R.id.showButtton2)
        show2.setOnClickListener {
            val intent = Intent(this, Home::class.java).apply {
                putExtra("uno", 1)
            }
            startActivity(intent)
        }



        val logOut2 = findViewById<Button>(R.id.logOutButton2)
        logOut2.setOnClickListener {
            val infoLog = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            infoLog.clear()
            infoLog.apply()

            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}