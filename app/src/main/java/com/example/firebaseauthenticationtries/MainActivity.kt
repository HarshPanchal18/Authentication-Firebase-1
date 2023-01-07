package com.example.firebaseauthenticationtries

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        auth=FirebaseAuth.getInstance()
        val user =auth.currentUser

        // to put delay
        Handler(Looper.getMainLooper()).postDelayed({
            if(user!=null)
                startActivity(Intent(this,HomeActivity::class.java))
            else
                startActivity(Intent(this,SignInActivity::class.java))
            finish()
        },3000)
    }
}
