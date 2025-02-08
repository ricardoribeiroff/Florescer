package br.uemg.florescer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class RegisterProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_product)

        val btnGravarProduto = findViewById<Button>(R.id.gravarProduto)
        val dbHelper = DatabaseHelper(this)

        btnGravarProduto.setOnClickListener {
            val produto = findViewById<EditText>(R.id.produto).text.toString().trim()
            val preco = findViewById<EditText>(R.id.preco).text.toString().trim().toDouble()
            val estoque = findViewById<EditText>(R.id.estoque).text.toString().trim().toInt()
            dbHelper.inserirProduto(produto, "descricao", preco, estoque, "imagem", 1)
            Toast.makeText(this, "Produto Cadastrado!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

}
