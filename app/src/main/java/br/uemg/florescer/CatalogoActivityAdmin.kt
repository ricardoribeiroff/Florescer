package br.uemg.florescer

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import br.uemg.florescer.adapter.ProductAdapter
import br.uemg.florescer.databinding.ActivityCatalogoAdminBinding
import br.uemg.florescer.listitems.Products
import br.uemg.florescer.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

class CatalogoActivityAdmin : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogoAdminBinding
    private lateinit var productAdapter: ProductAdapter
    private val products = Products()
    private val productList: MutableList<Product> = mutableListOf()
    var clicked = false

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#E0E0E0")

        CoroutineScope(Dispatchers.IO).launch {
            products.getProducts().collectIndexed { index, value ->
                for (p in value) {
                    productList.add(p)
                }
            }
        }
        val recyclerViewProducts = binding.recyclerViewProducts
        recyclerViewProducts.layoutManager = GridLayoutManager(this, 2)
        recyclerViewProducts.setHasFixedSize(true)
        productAdapter = ProductAdapter(this, productList)
        recyclerViewProducts.adapter = productAdapter

        binding.btAll.setOnClickListener {
            clicked = true
            if (clicked) {
                binding.btAll.setBackgroundResource(R.drawable.bg_button_enabled)
                binding.btAll.setTextColor(Color.WHITE)
                binding.btFlores.setBackgroundResource(R.drawable.bg_button_disabled)
                binding.btFlores.setTextColor(R.color.dark_gray)
                binding.btPlanta.setBackgroundResource(R.drawable.bg_button_disabled)
                binding.btPlanta.setTextColor(R.color.dark_gray)
                binding.btJardinagem.setBackgroundResource(R.drawable.bg_button_disabled)
                binding.btJardinagem.setTextColor(R.color.dark_gray)
                binding.recyclerViewProducts.visibility = View.INVISIBLE
                binding.txtListTitle.text = "Todos"
            }
        }

        binding.btFlores.setOnClickListener {
            clicked = true
            if (clicked) {
                binding.btFlores.setBackgroundResource(R.drawable.bg_button_enabled)
                binding.btFlores.setTextColor(Color.WHITE)
                binding.btAll.setBackgroundResource(R.drawable.bg_button_disabled)
                binding.btAll.setTextColor(R.color.dark_gray)
                binding.btPlanta.setBackgroundResource(R.drawable.bg_button_disabled)
                binding.btPlanta.setTextColor(R.color.dark_gray)
                binding.btJardinagem.setBackgroundResource(R.drawable.bg_button_disabled)
                binding.btJardinagem.setTextColor(R.color.dark_gray)
                binding.recyclerViewProducts.visibility = View.VISIBLE
                binding.txtListTitle.text = "Flores"
            }
        }

        binding.btPlanta.setOnClickListener {
            clicked = true
            if (clicked) {
                binding.btPlanta.setBackgroundResource(R.drawable.bg_button_enabled)
                binding.btPlanta.setTextColor(Color.WHITE)
                binding.btAll.setBackgroundResource(R.drawable.bg_button_disabled)
                binding.btAll.setTextColor(R.color.dark_gray)
                binding.btFlores.setBackgroundResource(R.drawable.bg_button_disabled)
                binding.btFlores.setTextColor(R.color.dark_gray)
                binding.btJardinagem.setBackgroundResource(R.drawable.bg_button_disabled)
                binding.btJardinagem.setTextColor(R.color.dark_gray)
                binding.recyclerViewProducts.visibility = View.INVISIBLE
                binding.txtListTitle.text = "Plantas"
            }
        }

        binding.btJardinagem.setOnClickListener {
            clicked = true
            if (clicked) {
                binding.btJardinagem.setBackgroundResource(R.drawable.bg_button_enabled)
                binding.btJardinagem.setTextColor(Color.WHITE)
                binding.btAll.setBackgroundResource(R.drawable.bg_button_disabled)
                binding.btAll.setTextColor(R.color.dark_gray)
                binding.btFlores.setBackgroundResource(R.drawable.bg_button_disabled)
                binding.btFlores.setTextColor(R.color.dark_gray)
                binding.btPlanta.setBackgroundResource(R.drawable.bg_button_disabled)
                binding.btPlanta.setTextColor(R.color.dark_gray)
                binding.recyclerViewProducts.visibility = View.INVISIBLE
                binding.txtListTitle.text = "Jardinagem"
            }
        }

        binding.btEditarProdutos.setOnClickListener{
            val intent = Intent(this, ListProductsActivity::class.java)
            startActivity(intent)
        }
    }
}