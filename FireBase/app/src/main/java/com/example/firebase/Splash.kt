package com.example.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.*

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        GlobalScope.launch {
            performAction()
        }
    }
    suspend fun performAction() {
        delay(5000) // Esperar 5 segundos
        val intent = Intent(this@Splash, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}


