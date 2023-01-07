package com.example.firebaseauthenticationtries

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        // to put delay
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        },3000)
    }
}
