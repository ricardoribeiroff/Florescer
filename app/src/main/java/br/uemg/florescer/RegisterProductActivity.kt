package br.uemg.florescer

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
        val categorias = dbHelper.getCategorias()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner = findViewById<Spinner>(R.id.spinnerCategorias)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val categoriaSelecionada = parent.getItemAtPosition(position).toString()
                Toast.makeText(
                    applicationContext,
                    "Selecionado: $categoriaSelecionada",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}

        }
    }
}
