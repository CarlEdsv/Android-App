package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.firebase.databinding.ActivityTareaBinding

class Tarea : AppCompatActivity() {
    private lateinit var binding: ActivityTareaBinding
    lateinit var sqliteHelper:SqliteHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTareaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sqliteHelper = SqliteHelper(this)

        binding.regresar.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        /*var n = 0
          binding.entrega.addTextChangedListener {
            val input = binding.entrega.text.toString()

            if (binding.entrega.length() == 2 && n == 0 && input.endsWith("/").not()) {
                binding.entrega.append("/")
                n++
            }

            if (binding.entrega.length() < 5 && binding.entrega.length() >= 4) {
                n = 0
            }

            if (binding.entrega.length() == 6 && input.endsWith("/")) {
                binding.entrega.text.delete(binding.entrega.length() - 1, binding.entrega.length())
            }
        }*/

        binding.entrega.addTextChangedListener(object :TextWatcher{
            private var currentText: String = ""
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString() != currentText) {
                    val input = s.toString()
                    val filteredInput = applyDateFormat(input)
                    currentText = filteredInput
                    binding.entrega.setText(filteredInput)
                    binding.entrega.setSelection(filteredInput.length)
                }
            }

        })

        binding.guardar.setOnClickListener {
            if (binding.titulo.text.isBlank() || binding.descripcion.text.isBlank() || binding.entrega.text.isBlank()) {
                Toast.makeText(this, "Por favor rellene todos los campos", Toast.LENGTH_LONG).show()
            } else {
                sqliteHelper.addTareas(
                    binding.titulo.text.toString(),
                    binding.descripcion.text.toString(),
                    binding.entrega.text.toString()
                )
                binding.titulo.text.clear()
                binding.descripcion.text.clear()
                binding.entrega.text.clear()
                Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_LONG).show()
            }

        }
    }
}
private fun applyDateFormat(input: String): String {
    val strippedInput = input.replace("/", "")

    val formattedInput = buildString {
        for (i in strippedInput.indices) {
            append(strippedInput[i])
            if (i == 1 || i == 3) {
                append('/')
            }
        }
    }

    return formattedInput
}