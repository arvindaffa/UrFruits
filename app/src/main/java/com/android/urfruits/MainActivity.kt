package com.android.urfruits

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Ambil data dari Intent
        val userId = intent.getStringExtra("USER_ID")
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val userName = intent.getStringExtra("USER_NAME")
        val userToken = intent.getStringExtra("USER_TOKEN")

        // Tampilkan data di UI
        val userIdTextView: TextView = findViewById(R.id.userIdTextView)
        val userEmailTextView: TextView = findViewById(R.id.userEmailTextView)
        val userNameTextView: TextView = findViewById(R.id.userNameTextView)
        val userTokenTextView: TextView = findViewById(R.id.userTokenTextView)

        userIdTextView.text = "User ID: $userId"
        userEmailTextView.text = "Email: $userEmail"
        userNameTextView.text = "Name: $userName"
        userTokenTextView.text = "Token: $userToken"

        // Tampilkan informasi pengguna dalam toast
        Toast.makeText(
            this,
            "ID: $userId\nEmail: $userEmail\nName: $userName\nToken: $userToken",
            Toast.LENGTH_LONG
        ).show()
    }
}