package com.android.urfruits

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.api.ApiClient
import com.android.api.ApiService
import com.android.api.UpdateRequest
import com.android.api.UpdateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var apiService: ApiService
    private lateinit var inputName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var btnSave: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputName = view.findViewById(R.id.input_nama)
        inputEmail = view.findViewById(R.id.input_email)
        inputPassword = view.findViewById(R.id.input_password)
        btnSave = view.findViewById(R.id.editButton)

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        ApiClient.init(requireContext())
        apiService = ApiClient.apiService

        // Retrieve user ID from SharedPreferences
        val userId = sharedPreferences.getString("USER_ID", "")

        // Retrieve current user data from SharedPreferences
        val currentName = sharedPreferences.getString("USER_NAME", "")
        val currentEmail = sharedPreferences.getString("USER_EMAIL", "")

        // Set default values to EditTexts
        inputName.setText(currentName)
        inputEmail.setText(currentEmail)

        btnSave.setOnClickListener {
            val newName = inputName.text.toString()
            val newEmail = inputEmail.text.toString()
            val newPassword = inputPassword.text.toString()

            if (userId != null) {
                // Check if there are changes
                val nameChanged = newName != currentName
                val emailChanged = newEmail != currentEmail
                val passwordChanged = newPassword.isNotEmpty() // Check if password is not empty

                if (!nameChanged && !emailChanged && !passwordChanged) {
                    Toast.makeText(context, "Tidak ada perubahan", Toast.LENGTH_SHORT).show()
                } else {
                    updateUser(userId, newName, newEmail, if (passwordChanged) newPassword else null)
                }
            } else {
                Toast.makeText(context, "User ID is null", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUser(userId: String, name: String?, email: String?, password: String?) {
        val user = UpdateRequest(
            name = if (name != sharedPreferences.getString("USER_NAME", "")) name else null,
            email = if (email != sharedPreferences.getString("USER_EMAIL", "")) email else null,
            password = password
        )
        val call = apiService.updateUser("", userId, user)  // The token will be added by the interceptor
        call.enqueue(object : Callback<UpdateResponse> {
            override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    // Update SharedPreferences with new data
                    with(sharedPreferences.edit()) {
                        putString("USER_NAME", name)
                        putString("USER_EMAIL", email)
                        apply()
                    }
                    // Navigate back to ProfileFragment or update UI accordingly
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleError(response: Response<UpdateResponse>) {
        when (response.code()) {
            401 -> {
                Toast.makeText(context, "Invalid token, please log in again.", Toast.LENGTH_SHORT).show()
                // Handle unauthorized access, e.g., redirect to login screen
            }
            else -> {
                Toast.makeText(context, "Failed to update profile: ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
