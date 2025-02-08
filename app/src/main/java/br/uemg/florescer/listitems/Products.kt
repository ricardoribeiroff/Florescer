package br.uemg.florescer.listitems

import br.uemg.florescer.R
import br.uemg.florescer.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Products {

    private val _productList = MutableStateFlow<MutableList<Product>>(mutableListOf())
    private val productListFlow: StateFlow<MutableList<Product>> = _productList

    fun getProducts(): Flow<MutableList<Product>> {
        val productList: MutableList<Product> = mutableListOf(
            Product(
                imgProduct = R.drawable.rosa,
                name = "Rosa Vermelha",
                price = "96.00"

            ),

            Product(
                imgProduct = R.drawable.rosaamarela,
                name = "Rosa Amarela",
                price = "90.00"

            ),

            Product(
                imgProduct = R.drawable.rosabranca,
                name = "Rosa Branca",
                price = "95.00"

            ),

            Product(
                imgProduct = R.drawable.vermelhobranco,
                name = "Vermelhas e Brancas",
                price = "130.00"

            ),

            Product(
                imgProduct = R.drawable.rosalaranja,
                name = "Rosa Laranja",
                price = "75.00"

            ),

            Product(
                imgProduct = R.drawable.rosarosa,
                name = "Rosa ",
                price = "75.00"

            )
        )
        _productList.value = productList
        return productListFlow
    }
}