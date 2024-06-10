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
import com.auth0.android.jwt.JWT
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

        ApiClient.init(this)
        // Initialize Retrofit
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
                    Log.d("SigninActivity", "Response: $loginResponse")

                    if (loginResponse.status == "success") {
                        val user = loginResponse.user
                        val token = user.token
                        val userId = user.id
                        val userEmail = user.email
                        val userName = user.name

                        // Log the token and other values to check if they are not null
                        Log.d("SigninActivity", "Token: $token, User ID: $userId, User Email: $userEmail, User Name: $userName")

                        // Check if token is null
                        if (token != null) {
                            // Store token in SharedPreferences
                            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("JWT_TOKEN", token)
                            editor.putString("USER_ID", userId)
                            editor.putString("USER_EMAIL", userEmail)
                            editor.putString("USER_NAME", userName)
                            editor.apply()

                            Toast.makeText(this@SigninActivity, loginResponse.message, Toast.LENGTH_SHORT).show()

                            // Handle login success and pass data to MainActivity
                            val intent = Intent(this@SigninActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@SigninActivity, "Login failed: Token is null", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Handle failed login attempt
                        val errorMessage = loginResponse.message
                        if (errorMessage.contains("Invalid email or password", true)) {
                            // Display only the specific error message
                            Toast.makeText(this@SigninActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            // Display general error message
                            Toast.makeText(this@SigninActivity, "Login failed: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val errorResponse = response.errorBody()?.string()
                    Log.d("SigninActivity", "Login failed: $errorResponse")
                    Toast.makeText(this@SigninActivity, errorResponse, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("SigninActivity", "Login error: ${t.message}", t)
                Toast.makeText(this@SigninActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}