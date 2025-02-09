package br.uemg.florescer


import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import android.widget.Button
import android.content.Intent
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.text
import androidx.compose.ui.tooling.preview.Preview
import br.uemg.florescer.ui.theme.FlorescerTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val dbHelper = DatabaseHelper(this)

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextSenha = findViewById<EditText>(R.id.editTextSenha)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val cadastrar = findViewById<Button>(R.id.cadastrar)

        btnLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val senha = editTextSenha.text.toString().trim()

            if(email.isNotEmpty() && senha.isNotEmpty()) {
                val usuarioExiste = dbHelper.verificarLogin(email, senha)
                if(usuarioExiste) {
                    Toast.makeText(this, "Login bem sucedido!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CatalogoActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "E-mail ou senha incorretos!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
        cadastrar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val text = "Não tem uma conta? \n<b>Cadastre-se</b>"
        val spannedText: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text)
        }
        cadastrar.text = spannedText
    }
}


