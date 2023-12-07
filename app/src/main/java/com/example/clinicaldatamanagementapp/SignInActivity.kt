package com.example.clinicaldatamanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.clinicaldatamanagementapp.database.ClinicalDataManagementDatabase
import com.example.clinicaldatamanagementapp.database.DatabaseBuilder
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val signInButton: Button = findViewById(R.id.signInButton)

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            lifecycleScope.launch {
                val database = DatabaseBuilder.getDatabase(context = this@SignInActivity)

                val user = database.userDao().getUser(email, password)
                if (user != null) {
                    val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SignInActivity, "User name or password is wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val signUpLink: TextView = findViewById(R.id.signUpLink)
        signUpLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}