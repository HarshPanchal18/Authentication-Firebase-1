package com.example.firebaseauthenticationtries

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.editMail
import kotlinx.android.synthetic.main.activity_forgot_password.tinput_mail
import kotlinx.android.synthetic.main.activity_sign_in.*

class ForgotPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth= FirebaseAuth.getInstance()

        forgotbtn.setOnClickListener {
            val mail=editMail.text.toString()
            if(checkFields()){
                auth.sendPasswordResetEmail(mail).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this,"Reset link is sent to the mail",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,SignInActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    private fun checkFields(): Boolean{
        if(editMail.text.toString()== ""){
            tinput_mail.error="Required field"
            return false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(editMail.text.toString()).matches()){
            tinput_mail.error="Invalid mail format"
            return false
        }
        return true
    }
}
