package com.android.urfruits.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.urfruits.R
import com.android.urfruits.signin.SigninActivity

class SignupActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signin : TextView = findViewById(R.id.signup_text)

        signin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.signup_text -> {
                val moveToSignUp = Intent(this@SignupActivity, SigninActivity::class.java)
                startActivity(moveToSignUp)
            }
        }
    }
}