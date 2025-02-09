package br.uemg.florescer

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.uemg.florescer.ui.theme.FlorescerTheme

class debugImg : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_img)
        // Função para exibir os dados do produto na interface
        val dbHelper = DatabaseHelper(this)
        fun exibirProduto(produtoId: Long) {
            val produto = dbHelper.obterProdutoPorId(produtoId)
            if (produto != null) {
                // Exibir os dados do produto na interface
                val nomeProduto = produto.nome
                val descricaoProduto = produto.descricao
                val precoProduto = produto.preco
                val estoqueProduto = produto.estoque
                val caminhoImagem = produto.imagem

                // Atualizar os campos da interface (exemplo)
                findViewById<TextView>(R.id.textNomeProduto).text = nomeProduto
                findViewById<TextView>(R.id.textDescricaoProduto).text = descricaoProduto
                findViewById<TextView>(R.id.textPrecoProduto).text = precoProduto.toString()
                findViewById<TextView>(R.id.textEstoqueProduto).text = estoqueProduto.toString()

                // Função para carregar a imagem na ImageView
                fun carregarImagemDoProduto(caminhoImagem: String) {
                    val imageViewSampleAvatar = findViewById<ImageView>(R.id.imageViewSampleAvatar)
                    val bitmap = BitmapFactory.decodeFile(caminhoImagem)
                    imageViewSampleAvatar.setImageBitmap(bitmap)
                }
                // Exibir a imagem na ImageView
                if (caminhoImagem != null) {
                    carregarImagemDoProduto(caminhoImagem)
                } else {
                    Toast.makeText(this, "Imagem não disponível.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Produto não encontrado.", Toast.LENGTH_SHORT).show()
            }
        }
        exibirProduto(5)
    }
}
