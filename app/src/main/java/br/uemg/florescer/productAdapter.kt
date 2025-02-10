package br.uemg.florescer

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import br.uemg.florescer.model.Product


class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    data class Product(
        val id: Long,
        val name: String,
        val imageResId: Bitmap // ID do recurso da imagem
    )

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        fun mostrarDialogoConfirmacao(context: Context, mensagem: String, onConfirm: () -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Confirmação")
            builder.setMessage(mensagem)

            // Botão "Sim"
            builder.setPositiveButton("Sim") { _, _ ->
                onConfirm() // Executa a ação se o usuário confirmar
            }

            // Botão "Não"
            builder.setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss() // Apenas fecha o diálogo
            }

            val dialog = builder.create()
            dialog.show()
        }
        val product = productList[position]

        holder.productName.text = product.name
        holder.productImage.setImageBitmap(product.imageResId)
        val dbHelper = DatabaseHelper(holder.itemView.context)

        holder.editButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EditProductActivity::class.java).apply {
                putExtra("produto_id", product.id)
                val cargo = "admin"
                putExtra("cargo", cargo)
            }
            context.startActivity(intent)
        }


        holder.deleteButton.setOnClickListener {
            mostrarDialogoConfirmacao(holder.itemView.context, "Tem certeza que deseja excluir este produto?"){
                val sucesso = dbHelper.deletarProduto(product.id)
                if (sucesso) {
                    Toast.makeText(holder.itemView.context, "Produto excluído com sucesso!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(holder.itemView.context, CatalogoActivityAdmin::class.java)
                    holder.itemView.context.startActivity(intent)
            } else {
                Toast.makeText(holder.itemView.context, "Erro ao excluir o produto.", Toast.LENGTH_SHORT).show()
            }
                }
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }


}
