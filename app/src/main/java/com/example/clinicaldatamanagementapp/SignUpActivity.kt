package com.example.clinicaldatamanagementapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.clinicaldatamanagementapp.database.ClinicalDataManagementDatabase
import com.example.clinicaldatamanagementapp.database.DatabaseBuilder
import com.example.clinicaldatamanagementapp.database.UserEntity
import com.example.clinicaldatamanagementapp.databinding.ActivitySignUpBinding
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (validateForm(email, password, confirmPassword)) {
                signUpUser(email, password)
            }
        }

        binding.alreadyHaveAccountTextView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }

    private fun validateForm(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validate password strength (example: minimum 6 characters)
        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        return true
    }

    private fun signUpUser(email: String, password: String) {
        lifecycleScope.launch {
            val newUser = UserEntity(email = email, password = password)
                val database = DatabaseBuilder.getDatabase(context = this@SignUpActivity)

            database.userDao().insertUsers(listOf(newUser))
            runOnUiThread {
                Toast.makeText(this@SignUpActivity, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                startActivity(intent)
            }
        }
    }
}