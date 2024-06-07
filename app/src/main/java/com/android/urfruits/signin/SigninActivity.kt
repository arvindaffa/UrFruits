package com.android.urfruits.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.urfruits.R
import com.android.urfruits.signup.SignupActivity

class SigninActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val signup : TextView = findViewById(R.id.signup_text)

        signup.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.signup_text -> {
                val moveToSignIn = Intent(this@SigninActivity, SignupActivity::class.java)
                startActivity(moveToSignIn)
            }
        }
    }
}