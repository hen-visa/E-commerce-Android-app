package com.example.ecommerce.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseAuth



class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Skip if already signed in
        if (auth.currentUser != null) goToDashboard()

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener { login() }
        binding.btnRegister.setOnClickListener { register() }
        binding.tvToggle.setOnClickListener { toggleMode() }
    }

    private var isLoginMode = true

    private fun toggleMode() {
        isLoginMode = !isLoginMode
        binding.tilName.visibility = if (isLoginMode) View.GONE else View.VISIBLE
        binding.tvToggle.text = if (isLoginMode) "Don't have an account? Register" else "Have an account? Login"
        binding.btnLogin.visibility = if (isLoginMode) View.VISIBLE else View.GONE
        binding.btnRegister.visibility = if (isLoginMode) View.GONE else View.VISIBLE
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        setLoading(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { goToDashboard() }
            .addOnFailureListener { e ->
                setLoading(false)
                Toast.makeText(this, e.message ?: "Login failed", Toast.LENGTH_LONG).show()
            }
    }

    private fun register() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        setLoading(true)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                result.user?.updateProfile(
                    com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(name).build()
                )
                goToDashboard()
            }
            .addOnFailureListener { e ->
                setLoading(false)
                Toast.makeText(this, e.message ?: "Registration failed", Toast.LENGTH_LONG).show()
            }
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !loading
        binding.btnRegister.isEnabled = !loading
    }

    private fun goToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}