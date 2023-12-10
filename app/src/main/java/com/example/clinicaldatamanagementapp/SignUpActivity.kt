package com.example.clinicaldatamanagementapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.clinicaldatamanagementapp.application.ClinicalDataManagementApp.Companion.database
import com.example.clinicaldatamanagementapp.database.ClinicalDataManagementDatabase
import com.example.clinicaldatamanagementapp.database.UserEntity
import com.example.clinicaldatamanagementapp.databinding.ActivitySignUpBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignUpActivity : AppCompatActivity() {

    private val userEntities = ArrayList<UserEntity>()

    private lateinit var binding: ActivitySignUpBinding

    companion object {
        val users = ArrayList<User>()

        init {
            users.add(User(email = "admin@gmail.com", password = "123456"))
        }
    }

    data class User(
        val email: String,
        val password: String
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

//    private fun signUpUser(email: String, password: String) {
//        val newUser = User(email, password)
//        users.add(newUser)
//
//        Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
//        val intent = Intent(this, SignInActivity::class.java)
//        startActivity(intent)
//    }

    private fun signUpUser(email: String, password: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val newUser = UserEntity(email = email, password = password)
            database.userDao().insertUsers(listOf(newUser))

            runOnUiThread {
                Toast.makeText(this@SignUpActivity, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                startActivity(intent)
            }
        }
    }
}