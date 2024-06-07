package com.android.urfruits.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.urfruits.R
import com.android.urfruits.signin.SigninActivity
import com.android.urfruits.signup.SignupActivity

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val signinButton: Button = findViewById(R.id.signinButton)
        val signupButton: Button = findViewById(R.id.signupButton)

        signupButton.setOnClickListener(this)
        signinButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.signinButton -> {
                val moveToSignIn = Intent(this@WelcomeActivity, SigninActivity::class.java)
                startActivity(moveToSignIn)
            }
            R.id.signupButton -> {
                val moveToSignUp = Intent(this@WelcomeActivity, SignupActivity::class.java)
                startActivity(moveToSignUp)
            }
        }
    }
}