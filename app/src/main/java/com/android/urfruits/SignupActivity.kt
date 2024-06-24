package com.android.urfruits

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.api.ApiClient
import com.android.api.ApiResponse
import com.android.api.User
import com.android.urfruits.signin.SigninActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var inputUsername: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize ApiClient here
        ApiClient.init(this)

        inputUsername = findViewById(R.id.input_username)
        inputEmail = findViewById(R.id.input_email)
        inputPassword = findViewById(R.id.input_password)
        signupButton = findViewById(R.id.signupButton)
        val signin: TextView = findViewById(R.id.signin_text)

        signupButton.setOnClickListener(this)
        signin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.signupButton -> {
                val name = inputUsername.text.toString().trim()
                val email = inputEmail.text.toString().trim()
                val password = inputPassword.text.toString().trim()

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                } else {
                    registerUser(name, email, password)
                }
            }

            R.id.signin_text -> {
                val moveToSignIn = Intent(this@SignupActivity, SigninActivity::class.java)
                startActivity(moveToSignIn)
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        val user = User(name, email, password)
        val call = ApiClient.apiService.registerUser(user)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == "success") {
                        val userId = apiResponse.user.id
                        val message = "Status: ${apiResponse.status}\nMessage: ${apiResponse.message}\nUser ID: $userId"
                        Toast.makeText(this@SignupActivity, message, Toast.LENGTH_LONG).show()
                        val intent = Intent(this@SignupActivity, SigninActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val message = "Status: ${apiResponse?.status}\nMessage: ${apiResponse?.message}"
                        Toast.makeText(this@SignupActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignupActivity, "Registration Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
