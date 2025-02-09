package br.uemg.florescer

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class RegisterProductActivity : AppCompatActivity() {

    private var caminhoImagem: String? = null  // Variável para armazenar o caminho da imagem
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var dbHelper: DatabaseHelper  // Propriedade da classe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_product)

        dbHelper = DatabaseHelper(this)
        val btnGravarProduto = findViewById<Button>(R.id.gravarProduto)
        val btnImgProduto = findViewById<Button>(R.id.imgProduto)
        val spinnerCategoria = findViewById<Spinner>(R.id.spinnerCategoria)
        val produto = findViewById<EditText>(R.id.produto)
        val preco = findViewById<EditText>(R.id.preco)
        val estoque = findViewById<EditText>(R.id.estoque)
        val descricao = findViewById<EditText>(R.id.descricao)

        // 🔹 Buscar categorias no banco e preencher o Spinner
        val categorias: Array<String> = dbHelper.getCategorias()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapter

        // Abrir galeria ao clicar no botão
        btnImgProduto.setOnClickListener {
            abrirGaleria()
        }

        // Salvar o produto ao clicar no botão
        btnGravarProduto.setOnClickListener {
            val nomeProduto = produto.text.toString().trim()
            val precoProduto = preco.text.toString().trim().toDoubleOrNull() ?: 0.0
            val estoqueProduto = estoque.text.toString().trim().toIntOrNull() ?: 0
            val categoriaSelecionada = spinnerCategoria.selectedItem?.toString()
            val descricaoProduto = descricao.text.toString().trim()

            if (nomeProduto.isNotEmpty() && categoriaSelecionada != null && caminhoImagem != null) {
                // Obter o ID da categoria selecionada
                val categoriaId = dbHelper.getIdCategoria(categoriaSelecionada)

                // Inserir o produto no banco de dados, incluindo o caminho da imagem
                if (dbHelper.inserirProduto(nomeProduto, descricaoProduto, precoProduto, estoqueProduto, caminhoImagem!!, categoriaId)) {
                    Toast.makeText(this, "Produto Cadastrado!", Toast.LENGTH_SHORT).show()
                    Log.d("RegisterProductActivity", "Produto cadastrado com sucesso")
                    val intent = Intent(this, CatalogoPlaceHolder::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao cadastrar produto!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos e adicione uma imagem!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun salvarImagemNoBanco(caminhoImagem: String): Boolean {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("imagem", caminhoImagem)  // Armazenando o caminho da imagem
        }

        val newRowId = db.insert(DatabaseHelper.TABLE_PRODUTOS, null, values)
        db.close()

        return newRowId != -1L  // Retorna true se a imagem foi salva com sucesso
    }

    private fun salvarImagemInternamente(imageUri: Uri): String? {
        return try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)

            // Salva a imagem internamente no dispositivo
            val file = File(filesDir, "product_img_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }

            // Retorna o caminho da imagem salva
            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao salvar a imagem", Toast.LENGTH_SHORT).show()
            null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            if (imageUri != null) {
                caminhoImagem = salvarImagemInternamente(imageUri)  // Salva a imagem e recebe o caminho
                Log.d("RegisterProductActivity", "Caminho da imagem: $caminhoImagem")
            }
        }
    }
}
