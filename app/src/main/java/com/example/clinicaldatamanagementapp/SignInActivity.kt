package com.example.clinicaldatamanagementapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.clinicaldatamanagementapp.application.ClinicalDataManagementApp.Companion.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignInActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var rememberMeCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox)
        val signInButton: Button = findViewById(R.id.signInButton)
        val signUpLink: TextView = findViewById(R.id.signUpLink)

        loadCredentialsIfRemembered()

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            lifecycleScope.launch(Dispatchers.IO) {
                val user = database.userDao().getUser(email, password)
                if (user != null) {
                    if (rememberMeCheckBox.isChecked) {
                        saveCredentials(email, password)
                    }
                    val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    runOnUiThread {
                        Toast.makeText(this@SignInActivity, "User name or password is wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        signUpLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveCredentials(email: String, password: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    private fun loadCredentialsIfRemembered() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val savedEmail = sharedPreferences.getString("email", "")
        val savedPassword = sharedPreferences.getString("password", "")
        if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            emailEditText.setText(savedEmail)
            passwordEditText.setText(savedPassword)
            rememberMeCheckBox.isChecked = true
        }
    }
}