package br.uemg.florescer
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val dbHelper = DatabaseHelper(this)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val nome = findViewById<EditText>(R.id.nome).text.toString().trim()
            val endereco = findViewById<EditText>(R.id.endereco).text.toString().trim()
            val telefone = findViewById<EditText>(R.id.telefone).text.toString().trim()
            val email = findViewById<EditText>(R.id.email).text.toString().trim()
            val password = findViewById<EditText>(R.id.password).text.toString().trim()
            val confirmPassword = findViewById<EditText>(R.id.confirmPassword).text.toString().trim()

            if (password == confirmPassword) {
                dbHelper.inserirUsuario(nome, email, password, telefone, endereco, "Cliente")
                Toast.makeText(this, "Usuario Cadastrado!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show()

            }
        }


    }

}