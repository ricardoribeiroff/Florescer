package br.uemg.florescer
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues


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
        private const val DATABASE_VERSION = 2

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
                preco DECIMAL NOT NULL,
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
                valor_total DECIMAL NOT NULL,
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
                preco_unitario DECIMAL NOT NULL,
                fk_pedido INTEGER NOT NULL,
                fk_produto INTEGER NOT NULL,
                FOREIGN KEY (fk_pedido) REFERENCES pedidos(id),
                FOREIGN KEY (fk_produto) REFERENCES produtos(id)
            );
        """
    }
    fun verificarLogin(email: String, senha: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USUARIOS WHERE email = ? AND senha = ?"
        val cursor = db.rawQuery(query, arrayOf(email, senha))

        val usuarioExiste = cursor.count > 0
        cursor.close()
        db.close()

        return usuarioExiste
    }
    fun inserirUsuario(nome: String, email: String, senha: String, telefone: String?, endereco: String?, cargo: String?): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nome", nome)
            put("email", email)
            put("senha", senha)  //
            put("telefone", telefone)
            put("endereco", endereco)
            put("cargo", cargo)
        }

        val resultado = db.insert(TABLE_USUARIOS, null, values)
        db.close()

        return resultado != -1L  // Retorna `true` se a inserção foi bem-sucedida
    }
    fun inserirProduto(nome: String, descricao: String?, preco: Double, estoque: Int, imagem: String?, fkCategoria: Int?): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nome", nome)
            put("descricao", descricao)
            put("preco", preco)
            put("estoque", estoque)
            put("imagem", imagem)
            put("fk_categoria", fkCategoria)
        }

        val resultado = db.insert(TABLE_PRODUTOS, null, values)
        db.close()

        return resultado != -1L // Retorna `true` se a inserção foi bem-sucedida
    }


}
