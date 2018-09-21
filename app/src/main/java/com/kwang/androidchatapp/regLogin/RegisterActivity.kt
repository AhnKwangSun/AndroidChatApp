package com.kwang.androidchatapp.regLogin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kwang.androidchatapp.R
import com.kwang.androidchatapp.classes.User
import com.kwang.androidchatapp.message.LatestMessagesActivity
import kotlinx.android.synthetic.main.activity_main.*

class RegisterActivity : AppCompatActivity() {

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
                    Log.d("RegisterActivity","Successfully created user with uid: ${it.result.user.uid}")

                    saveUsertoFirebaseDatabase()

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener {
                                if(!it.isSuccessful) return@addOnCompleteListener

                                Log.d("Main","Successfully created user with uid: ${it.result.user.uid}")
                                val intent = Intent(this, LatestMessagesActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)

                            }
                            .addOnFailureListener {
                                Log.d("Main","Failed to create user: ${it.message}")
                            }
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity","Failed to create user: ${it.message}")
                    Toast.makeText(this, "Email이 중복됩니다.", Toast.LENGTH_LONG).show()
                }
    }

    private fun saveUsertoFirebaseDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, name_register.text.toString(), email_register.text.toString())

        ref.setValue(user).addOnSuccessListener {
            Log.d("RegisterActivity", "파이어베이스 데이터베이스 저장 완료")
        }


    }

}
