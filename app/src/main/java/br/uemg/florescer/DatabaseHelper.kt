package br.uemg.florescer

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_USUARIOS)
        db.execSQL(CREATE_TABLE_CATEGORIAS)
        db.execSQL(CREATE_TABLE_PRODUTOS)
        db.execSQL(CREATE_TABLE_PEDIDOS)
        db.execSQL(CREATE_TABLE_ITENS_PEDIDO)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ITENS_PEDIDO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PEDIDOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUTOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "loja.db"
        private const val DATABASE_VERSION = 2 // Aumente se fizer alterações nas tabelas

        // Tabela Usuarios
        const val TABLE_USUARIOS = "usuarios"
        private const val CREATE_TABLE_USUARIOS = """
            CREATE TABLE $TABLE_USUARIOS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                senha TEXT NOT NULL,
                telefone TEXT,
                endereco TEXT,
                cargo TEXT
            );
        """

        // Tabela Categorias
        const val TABLE_CATEGORIAS = "categorias"
        private const val CREATE_TABLE_CATEGORIAS = """
            CREATE TABLE $TABLE_CATEGORIAS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                descricao TEXT
            );
        """

        // Tabela Produtos
        const val TABLE_PRODUTOS = "produtos"
        private const val CREATE_TABLE_PRODUTOS = """
            CREATE TABLE $TABLE_PRODUTOS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                descricao TEXT,
                preco REAL NOT NULL,  -- Alterado para REAL para suportar valores decimais
                estoque INTEGER NOT NULL,
                imagem TEXT,
                fk_categoria INTEGER,
                FOREIGN KEY (fk_categoria) REFERENCES categorias(id)
            );
        """

        // Tabela Pedidos
        const val TABLE_PEDIDOS = "pedidos"
        private const val CREATE_TABLE_PEDIDOS = """
            CREATE TABLE $TABLE_PEDIDOS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                valor_total REAL NOT NULL,
                status TEXT NOT NULL,
                fk_cliente INTEGER NOT NULL,
                fk_usuario INTEGER NOT NULL,
                FOREIGN KEY (fk_cliente) REFERENCES clientes(id),
                FOREIGN KEY (fk_usuario) REFERENCES usuarios(id)
            );
        """

        // Tabela Itens Pedido
        const val TABLE_ITENS_PEDIDO = "itens_pedido"
        private const val CREATE_TABLE_ITENS_PEDIDO = """
            CREATE TABLE $TABLE_ITENS_PEDIDO (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                quantidade INTEGER NOT NULL,
                preco_unitario REAL NOT NULL,
                fk_pedido INTEGER NOT NULL,
                fk_produto INTEGER NOT NULL,
                FOREIGN KEY (fk_pedido) REFERENCES pedidos(id),
                FOREIGN KEY (fk_produto) REFERENCES produtos(id)
            );
        """
    }

    // 🔹 Verifica se o usuário existe no banco (LOGIN)
    fun verificarLogin(email: String, senha: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USUARIOS WHERE email = ? AND senha = ?"
        val cursor = db.rawQuery(query, arrayOf(email, senha))

        val usuarioExiste = cursor.count > 0
        cursor.close()
        db.close()

        return usuarioExiste
    }

    // 🔹 Insere um novo usuário no banco
    fun inserirUsuario(nome: String, email: String, senha: String, telefone: String?, endereco: String?, cargo: String?): Boolean {
        val db = this.writableDatabase
        return try {
            val values = ContentValues().apply {
                put("nome", nome)
                put("email", email)
                put("senha", senha)
                put("telefone", telefone)
                put("endereco", endereco)
                put("cargo", cargo)
            }
            val resultado = db.insert(TABLE_USUARIOS, null, values)
            resultado != -1L
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Erro ao inserir usuário: ${e.message}")
            false
        } finally {
            db.close()
        }
    }

    // 🔹 Insere um novo produto no banco
    fun inserirProduto(nome: String, descricao: String?, preco: Double, estoque: Int, imagem: String?, fkCategoria: Int?): Boolean {
        val db = this.writableDatabase
        return try {
            val values = ContentValues().apply {
                put("nome", nome)
                put("descricao", descricao)
                put("preco", preco)
                put("estoque", estoque)
                put("imagem", imagem)
                put("fk_categoria", fkCategoria)
            }
            val resultado = db.insert(TABLE_PRODUTOS, null, values)
            resultado != -1L
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Erro ao inserir produto: ${e.message}")
            false
        } finally {
            db.close()
        }
    }
    // 🔹 Coleta os nomes das categorias em um array
    fun getCategorias(): Array<String> {
        val listaCategorias = ArrayList<String>()
        val db = this.readableDatabase
        val query = "SELECT nome FROM $TABLE_CATEGORIAS"

        try {
            val cursor = db.rawQuery(query, null)

            if (cursor.moveToFirst()) {
                do {
                    val nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"))
                    listaCategorias.add(nome)
                } while (cursor.moveToNext())
            }

            cursor.close()
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Erro ao coletar categorias: ${e.message}")
        } finally {
            db.close()
        }

        return listaCategorias.toTypedArray()
    }
    fun getIdCategoria(nomeCategoria: String): Int? {
        val db = this.readableDatabase
        val query = "SELECT id FROM $TABLE_CATEGORIAS WHERE nome = ?"
        val cursor = db.rawQuery(query, arrayOf(nomeCategoria))

        var categoriaId: Int? = null
        if (cursor.moveToFirst()) {
            categoriaId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }

        cursor.close()
        db.close()

        return categoriaId
    }
    fun obterProdutoPorId(produtoId: Long): Produto? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PRODUTOS,
            null,
            "id = ?",
            arrayOf(produtoId.toString()),
            null,
            null,
            null
        )

        var produto: Produto? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"))
            val descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"))
            val preco = cursor.getDouble(cursor.getColumnIndexOrThrow("preco"))
            val estoque = cursor.getInt(cursor.getColumnIndexOrThrow("estoque"))
            val imagem = cursor.getString(cursor.getColumnIndexOrThrow("imagem"))
            val categoriaId = cursor.getLong(cursor.getColumnIndexOrThrow("fk_categoria"))

            produto = Produto(id, nome, descricao, preco, estoque, imagem, categoriaId)
        }
        cursor.close()
        db.close()

        return produto
    }

    // Classe de dados para representar um produto
    data class Produto(
        val id: Long,
        val nome: String,
        val descricao: String,
        val preco: Double,
        val estoque: Int,
        val imagem: String?,
        val categoriaId: Long)

}
