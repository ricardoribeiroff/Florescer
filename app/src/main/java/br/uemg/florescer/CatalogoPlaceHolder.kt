package br.uemg.florescer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button

class CatalogoPlaceHolder : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)
        val btnCatalogo = findViewById<Button>(R.id.registrarProduto)

        btnCatalogo.setOnClickListener {
            val intent = Intent(this, RegisterProductActivity::class.java)
            startActivity(intent)
        }


    }
}