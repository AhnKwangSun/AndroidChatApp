package com.kwang.androidchatapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button.setOnClickListener {
            performRegister()
        }

        already_account.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }
    }

    private fun performRegister(){
        val email = email_register.text.toString()
        var password = password_register.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email 또는 Password를 입력해주세요.", Toast.LENGTH_LONG).show()
            return
        }
        if (password.length < 6) {
            Toast.makeText(this, "비밀번호는 6자리 이상 입력해주세요.", Toast.LENGTH_LONG).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener

                    // else if successful
                    Log.d("Main","Successfully created user with uid: ${it.result.user.uid}")
                }
                .addOnFailureListener {
                    Log.d("Main","Failed to create user: ${it.message}")
                    Toast.makeText(this, "Email 형식이 맞지 않습니다.", Toast.LENGTH_LONG).show()
                }
    }
}
