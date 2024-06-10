package com.android.urfruits

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Retrieve user data from SharedPreferences
        val userName = sharedPreferences.getString("USER_NAME", "")
        val userEmail = sharedPreferences.getString("USER_EMAIL", "")

        // Check if user data is not null or empty
        if (!userName.isNullOrEmpty() && !userEmail.isNullOrEmpty()) {
            // Update UI with user data from SharedPreferences
            updateUI(userName, userEmail)
        } else {
            Log.e(TAG, "User data is null or empty")
        }
    }

    private fun updateUI(userName: String, userEmail: String) {
        // Update UI elements with user details
        val userNameTextView: TextView = findViewById(R.id.input_nama)
        val userEmailTextView: TextView = findViewById(R.id.input_email)

        userNameTextView.text = "$userName"
        userEmailTextView.text = "$userEmail"
    }

    companion object {
        private const val TAG = "ProfileActivity"
    }
}
