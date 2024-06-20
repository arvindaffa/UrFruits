package com.android.urfruits

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Retrieve user data from SharedPreferences
        val userName = sharedPreferences.getString("USER_NAME", "")
        val userEmail = sharedPreferences.getString("USER_EMAIL", "")

        // Update UI with user data from SharedPreferences
        updateUI(userName, userEmail)
    }

    private fun updateUI(userName: String?, userEmail: String?) {
        // Update UI elements with user details
        view?.findViewById<TextView>(R.id.input_nama)?.text = userName ?: ""
        view?.findViewById<TextView>(R.id.input_email)?.text = userEmail ?: ""
    }
}
