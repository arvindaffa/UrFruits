package com.android.urfruits

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.urfruits.signin.SigninActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Retrieve user details from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("USER_ID", "")
        val userEmail = sharedPreferences.getString("USER_EMAIL", "")
        val userName = sharedPreferences.getString("USER_NAME", "")
        val userToken = sharedPreferences.getString("JWT_TOKEN", "")

        Log.d(TAG, "User Token: $userToken")

        // Initialize TextViews
        val userIdTextView: TextView = findViewById(R.id.userIdTextView)
        val userEmailTextView: TextView = findViewById(R.id.userEmailTextView)
        val userNameTextView: TextView = findViewById(R.id.userNameTextView)
        val userTokenTextView: TextView = findViewById(R.id.userTokenTextView)

        // Set user details to TextViews
        userIdTextView.text = "User ID: $userId"
        userEmailTextView.text = "Email: $userEmail"
        userNameTextView.text = "Name: $userName"
        userTokenTextView.text = "Token: $userToken"

        val profileButton: Button = findViewById(R.id.profile)
        profileButton.setOnClickListener {
            // Create an Intent to navigate to ProfileActivity
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
