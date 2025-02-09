package br.uemg.florescer


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import br.uemg.florescer.databinding.ActivityProductDetailsBinding

class ProductDetails : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private var amount = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        //window.statusBarColor = Color.parseColor("#E0E0E0")

        val imgProduct = intent.extras!!.getInt("imgProduct")
        val name = intent.extras!!.getString("name")
        var price = intent.extras!!.getString("price")!!.toDouble()
        var newPrice = price
        val decimalFormat = java.text.DecimalFormat.getCurrencyInstance()

        binding.imgProduct.setBackgroundResource(imgProduct)
        binding.txtProductName.text = "$name"
        binding.txtProductPrice.text = decimalFormat.format(price)

        binding.btBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btToIncrease.setOnClickListener {
            if (amount == 1) {

                binding.txtAmount.text = "2"
                newPrice += price
                amount = 2

            }else if (amount == 2){

                binding.txtAmount.text = "3"
                newPrice += price
                amount = 3

            }
            binding.txtProductPrice.text = decimalFormat.format(newPrice)
            }

        binding.btToDecrease.setOnClickListener {
            if (amount == 3) {

                binding.txtAmount.text = "2"
                newPrice -= price
                amount = 2

            }else if (amount == 2){

                binding.txtAmount.text = "1"
                newPrice -= price
                amount = 1

            }
            binding.txtProductPrice.text = decimalFormat.format(newPrice)
        }

        binding.btConfirm.setOnClickListener {

            val buque = binding.btBuque
            val cesta = binding.btCesta
            val chocolate = binding.btChocolate

            val options = when {
                buque.isChecked -> {
                    "Buque"
                }
                cesta.isChecked -> {
                    "Buque/cesta de café"
                }
                chocolate.isChecked -> {
                    "Buque/Chocolate"
                }


                else -> {
                    ""
                }
            }

            val intent = Intent(this,Payment::class.java)
            intent.putExtra("name", name)
            intent.putExtra("amount", amount)
            intent.putExtra("price", newPrice)
            intent.putExtra("options", options)
            startActivity(intent)
        }

    }

}

