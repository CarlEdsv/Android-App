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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val name = findViewById<EditText>(R.id.nameText)
        val age = findViewById<EditText>(R.id.ageText)

        val id = findViewById<EditText>(R.id.idText)

        val grade = findViewById<Spinner>(R.id.spinner)

        val grades = listOf("Grados","Primero","Segundo","Tercero","Cuarto",
            "Quinto","Sexto","Séptimo","Octavo","Noveno")

        val spinerAdaptater = ArrayAdapter(this, android.R.layout.simple_spinner_item, grades)

        spinerAdaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        grade.adapter = spinerAdaptater

        val save = findViewById<Button>(R.id.saveButton)
        val show = findViewById<Button>(R.id.showButton)
        val delete = findViewById<Button>(R.id.deleteButton)
        val data = findViewById<Button>(R.id.dataButton)

        val db = Firebase.firestore

        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        var count = sharedPrefs.getInt("count", 0)

        save.setOnClickListener{

            val agenum = age.text.toString().toIntOrNull()
            val opcion = grade.selectedItem.toString()


            if(name.text.isEmpty()||age.text.isEmpty()||opcion.isEmpty()){
                Toast.makeText(this,"Por favor Ingrese todos los Campos",Toast.LENGTH_LONG).show()
            }

            if (agenum == null){
                Toast.makeText(this,"Ingrese una edad correcta",Toast.LENGTH_LONG).show()
            }

            else{
                val student = hashMapOf(
                    "nombre" to name.text.toString(),
                    "edad" to agenum,
                    "grado" to opcion
                )
                if(id.text.isEmpty()){
                    count++
                    sharedPrefs.edit().putInt("count", count).apply()
                    db.collection("students").document("$count").set(student).addOnSuccessListener {
                        Toast.makeText(this,"Datos guardados existosamente",Toast.LENGTH_LONG).show()
                    }
                        .addOnFailureListener{e ->
                            Toast.makeText(this,"Error al añadir estudiante: $e",Toast.LENGTH_LONG).show()
                        }
                }else{
                    //Si el documento no existe, se creará.
                    // Si el documento existe, su contenido se reemplazará por los datos proporcionados
                    db.collection("students").document(id.text.toString()).set(student).addOnSuccessListener {
                        Toast.makeText(this,"Datos guardados existosamente",Toast.LENGTH_LONG).show()
                    }
                        .addOnFailureListener{e ->
                            Toast.makeText(this,"Error al añadir estudiante: $e",Toast.LENGTH_LONG).show()
                        }
                }

            }

        }

        show.setOnClickListener {
            if(id.text.isEmpty()){
                Toast.makeText(this,"Ingrese un Id",Toast.LENGTH_LONG).show()
            }else{
                val docRef = db.collection("students").document(id.text.toString())
                docRef.get().addOnSuccessListener { document ->
                    //Si el documento no existe
                    if (!document.exists()) {
                        Toast.makeText(this,"No se encontraron datos con ese Id",Toast.LENGTH_LONG).show()
                    } else {
                        val student = document.data
                        name.setText(student?.get("nombre").toString())
                        age.setText(student?.get("edad").toString())

                        val position = grades.indexOf(student?.get("grado").toString())
                        grade.setSelection(position)
                    }
                }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this,"Consulta de datos fallida: $exception",Toast.LENGTH_LONG).show()
                    }

            }
        }

        delete.setOnClickListener {
            if(id.text.isEmpty()){
                Toast.makeText(this,"Ingrese un Id",Toast.LENGTH_LONG).show()
            }else{
                db.collection("students").document(id.text.toString()).delete()
                    .addOnSuccessListener {
                        Toast.makeText(this,"Documento eliminado exitosamente",Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener{e->
                        Toast.makeText(this,"Error al tratar de eliminar:$e ",Toast.LENGTH_LONG).show()
                    }
            }

        }

        data.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}

