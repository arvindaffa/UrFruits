package com.android.urfruits.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.urfruits.MainActivity
import com.android.urfruits.R
import com.android.urfruits.api.ApiClient
import com.android.urfruits.api.ApiService
import com.android.urfruits.api.LoginRequest
import com.android.urfruits.api.ApiResponse
import com.android.urfruits.signup.SignupActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var signinButton: Button
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        inputEmail = findViewById(R.id.input_email)
        inputPassword = findViewById(R.id.input_password)
        signinButton = findViewById(R.id.signinButton)
        val signup: TextView = findViewById(R.id.signup_text)

        // Inisialisasi Retrofit
        apiService = ApiClient.apiService

        signinButton.setOnClickListener(this)
        signup.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.signup_text -> {
                val moveToSignUp = Intent(this@SigninActivity, SignupActivity::class.java)
                startActivity(moveToSignUp)
            }
            R.id.signinButton -> {
                val email = inputEmail.text.toString()
                val password = inputPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    performLogin(email, password)
                } else {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun performLogin(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        val call = apiService.loginUser(loginRequest)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    Toast.makeText(this@SigninActivity, loginResponse.message, Toast.LENGTH_SHORT).show()

                    // Handle login success and pass data to MainActivity
                    val intent = Intent(this@SigninActivity, MainActivity::class.java).apply {
                        putExtra("USER_ID", loginResponse.user.id)
                        putExtra("USER_EMAIL", loginResponse.user.email)
                        putExtra("USER_NAME", loginResponse.user.name)
                        putExtra("USER_TOKEN", loginResponse.token)
                    }
                    // Display user details in toast
                    Toast.makeText(
                        this@SigninActivity,
                        "ID: ${loginResponse.user.id}\nEmail: ${loginResponse.user.email}\nName: ${loginResponse.user.name}\nToken: ${loginResponse.token}",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(intent)
                    finish()
                } else {
                    Log.d("SigninActivity", "Login failed: ${response.errorBody()?.string()}")
                    Toast.makeText(this@SigninActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("SigninActivity", "Login error: ${t.message}", t)
                Toast.makeText(this@SigninActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
