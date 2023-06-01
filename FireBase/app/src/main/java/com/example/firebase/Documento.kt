package com.example.firebase

import android.content.Context
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Documento {
    private val db = Firebase.firestore
    fun adDocument(name: EditText, age: EditText, spinner: Spinner, collectionPath: String, documentMap: HashMap<String, Any>, contexto: Context){

        if (name.text.isBlank() || age.text.isBlank() || spinner.selectedItem == 0L) {
            Toast.makeText(contexto, "Por favor Ingrese todos los Campos", Toast.LENGTH_LONG).show()
        }

        else if (age.text.toString().toIntOrNull() == null) {
            Toast.makeText(contexto, "Ingrese una edad correcta", Toast.LENGTH_LONG).show()

        }else{

            val countRef = db.collection(collectionPath).document("Contador")
            countRef.update("Valor", FieldValue.increment(1))
                .addOnSuccessListener {
                    countRef.get().addOnSuccessListener { document ->
                        if (document != null) {
                            val inf = document.data
                            val valor = inf?.get("Valor").toString().toInt()
                            db.collection(collectionPath).document("$valor").set(documentMap)
                                .addOnSuccessListener {
                                    Toast.makeText(contexto, "Datos guardados existosamente", Toast.LENGTH_LONG).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(contexto, "Error al añadir el registro: $e", Toast.LENGTH_LONG).show()
                                }
                        } else {
                            Toast.makeText(contexto, "No se encuentra el nuevo contador", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(contexto, "Problemas de tipo: $it", Toast.LENGTH_LONG).show()
                }


        }

    }

    fun getDocuments(contexto: Context, recycler:RecyclerView, collectionPath: String,field1:String, field2:String, field3:String){
        val myList = arrayListOf<Data>()
        db.collection(collectionPath)
            .get()
            .addOnSuccessListener { result ->
                /*for (document in result) {
                    val doc = document.data
                    if (doc.containsKey(field1) && doc.containsKey(field2) && doc.containsKey(field3)) {
                        myList.add(Data(
                            field1+": "+doc[field1],
                            field2+": "+doc[field2].toString(),
                            field3+": "+doc[field3]
                        )
                        )
                    }
                    recycler.adapter = Adaptador(myList)
                }*/
            }
            .addOnFailureListener { exception ->
                Toast.makeText(contexto,"Consulta de datos fallida: $exception",Toast.LENGTH_LONG).show()
            }
    }
}