package com.android.urfruits

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.api.ApiClient
import com.android.api.ApiService
import com.android.api.ProfileResponse
import com.android.urfruits.signin.SigninActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var apiService: ApiService

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
        ApiClient.init(requireContext())
        apiService = ApiClient.apiService

        // Retrieve user ID from SharedPreferences
        val userId = sharedPreferences.getString("USER_ID", "")

        if (userId != null && userId.isNotEmpty()) {
            Log.d("ProfileFragment", "User ID: $userId")
            getUserData(userId)
        } else {
            Toast.makeText(context, "User ID is null or empty", Toast.LENGTH_SHORT).show()
        }

        // Setup onClickListener to navigate to EditProfileFragment
        view.findViewById<TextView>(R.id.editButton)?.setOnClickListener {
            navigateToEditProfile()
        }
    }

    private fun navigateToEditProfile() {
        val editProfileFragment = EditProfileFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, editProfileFragment)
        transaction.addToBackStack(null)  // Optional, untuk menambahkan ke back stack
        transaction.commit()
    }


    private fun getUserData(userId: String) {
        val call = apiService.getUser("", userId)
        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()?.data
                    if (userResponse != null) {
                        updateUI(userResponse.name, userResponse.email)
                    } else {
                        Toast.makeText(context, "User data is null", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleError(response: Response<ProfileResponse>) {
        when (response.code()) {
            401 -> {
                // Token mungkin tidak valid atau telah kadaluarsa
                Toast.makeText(context, "Invalid token, please log in again.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, "Failed to retrieve user data: ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(userName: String?, userEmail: String?) {
        // Update UI elements with user details
        view?.findViewById<TextView>(R.id.input_nama)?.text = userName ?: ""
        view?.findViewById<TextView>(R.id.input_email)?.text = userEmail ?: ""
    }
}
