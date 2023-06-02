package com.example.firebase

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.viewbinding.ViewBinding
import com.example.firebase.databinding.ActivityTeachersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class Teachers : AppCompatActivity() {

    private lateinit var documento : Documento

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityTeachersBinding

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
        logData.putString("email", email)
        logData.putString("provider", provider)
        logData.apply()

        binding.logOutButton2.setOnClickListener {
            val infoLog = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            infoLog.clear()
            infoLog.apply()

            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



        /*binding.gSheet.setOnClickListener {
            val webPage: Uri = Uri.
            parse("https://docs.google.com/spreadsheets/d/1LLVaT-4_xt38BfiKLBJL3JPEuudJApLmTHkTOqB6bGE/edit#gid=0")
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }else{
                Toast.makeText(this, "Error al cargar la página", Toast.LENGTH_LONG).show()
            }
        }*/

        binding.refrescar.setOnClickListener {
            recreate()
        }

        binding.sheet.webChromeClient = object : WebChromeClient(){

        }

        binding.sheet.webViewClient = object : WebViewClient(){
            /*override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                binding.swipeRefresh.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.swipeRefresh.isRefreshing = false
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                Log.d("Error","Error de tipo: $error")
            }*/
        }
        val shSettings = binding.sheet.settings
        shSettings.javaScriptEnabled = true
        binding.sheet.loadUrl("https://docs.google.com/spreadsheets/d/1LLVaT-4_xt38BfiKLBJL3JPEuudJApLmTHkTOqB6bGE/edit?usp=sharing")

        /*val pref = getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
        val url = pref.getString("url",null)
        if(url!=null){
            //val json = getJson(url)
            Toast.makeText(this, "Dato devuelto: $url",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Error al obtener la URL",Toast.LENGTH_LONG).show()
        }*/
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
                Toast.makeText(this, "Registro exitoso, diríjase al sheet para ver los datos", Toast.LENGTH_LONG).show()

                binding.WebView.isInvisible = true
                binding.WebView.isEnabled = false
                binding.WebView.webChromeClient = object : WebChromeClient(){

                }
                binding.WebView.webViewClient = object : WebViewClient(){

                }
                val settings = binding.WebView.settings
                settings.javaScriptEnabled = false
                binding.WebView.loadUrl(result.contents.toString())

                /*val pref = getSharedPreferences("Preferencias", Context.MODE_PRIVATE).edit()
                pref.putString("url",result.contents.toString())
                pref.apply()*/
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    /*private fun getJson(url: String): String?{

        var jsonString:String? = ""
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback{

            override fun onFailure(call: Call, e: IOException) {
                val error = "Error de tipo :$e"
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    jsonString = response.body?.string()
                } else {
                    val error = "Error de tipo :${response.code}"
                }
            }

        })

        return jsonString
    }*/


}