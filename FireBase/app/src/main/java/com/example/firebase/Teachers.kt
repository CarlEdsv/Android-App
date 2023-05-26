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
import androidx.viewbinding.ViewBinding
import com.example.firebase.databinding.ActivityTeachersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator

class Teachers : AppCompatActivity() {

    private lateinit var documento : Documento

    private lateinit var auth: FirebaseAuth

    private lateinit var binding : ActivityTeachersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTeachersBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = "Registrar estudiante"


        auth = Firebase.auth

        documento = Documento()

        binding.btnScanner.setOnClickListener {
            initScanner()
        }

        val bundle: Bundle? = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        val logData = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        logData.putString("email",email)
        logData.putString("provider",provider)
        logData.apply()

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

    private fun initScanner() {
            val integrator = IntentIntegrator(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.setPrompt("Registrar alumnos")
            integrator.setTorchEnabled(false)
            integrator.setBeepEnabled(true)
            integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "El valor escaneado es: ${result.contents}" + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}