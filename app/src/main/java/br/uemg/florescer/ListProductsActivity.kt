package br.uemg.florescer

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListProductsActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_products)
        val btnNewProduct = findViewById<Button>(R.id.btnNewProduct)
        btnNewProduct.setOnClickListener {
            val intent = Intent(this, RegisterProductActivity::class.java)
            val cargo = "admin"
            intent.putExtra("cargo", cargo)
            startActivity(intent)
        }


        fun recyclePopulate() {
            recyclerView = findViewById(R.id.recyclerViewProducts)
            recyclerView.layoutManager = LinearLayoutManager(this)


            val dbHelper = DatabaseHelper(this)

            // Lista de produtos
            val productList = mutableListOf<ProductAdapter.Product>()

            // Começando do ID 5
            var productId = 8
            var product = dbHelper.obterProdutoPorId(productId.toLong())

            // Enquanto o produto não for null, continua adicionando à lista
            while (product != null) {
                val bitmap = BitmapFactory.decodeFile(product.imagem)

                // Adiciona o produto à lista
                productList.add(ProductAdapter.Product(product.id, product.nome, bitmap))

                // Incrementa o ID para o próximo produto
                productId++
                product = dbHelper.obterProdutoPorId(productId.toLong())
            }

            // Cria o adaptador e configura o RecyclerView
            productAdapter = ProductAdapter(productList)
            recyclerView.adapter = productAdapter
        }
        recyclePopulate()


    }
}