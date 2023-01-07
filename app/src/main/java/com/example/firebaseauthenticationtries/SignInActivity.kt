package com.example.firebaseauthenticationtries

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()

        signinbtn.setOnClickListener {
            val mail=editMail.text.toString()
            val password=editPass.text.toString()
            if(checkFields()){
                auth.signInWithEmailAndPassword(mail,password).addOnCompleteListener{
                    if(it.isSuccessful){
                        startActivity(Intent(this,HomeActivity::class.java))
                        Toast.makeText(this,"Signed in successfully",Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val snackbar=Snackbar.make(signinActivity,"User not found!!",Snackbar.ANIMATION_MODE_SLIDE)
                        snackbar.setAction("Register"){
                            startActivity(Intent(this,SignUpActivity::class.java))
                        }.setActionTextColor(Color.YELLOW).setBackgroundTint(Color.BLACK).show()
                    }
                }
            }
        }
    }

    private fun checkFields():Boolean{
        if(editMail.text.toString()== ""){
            tinput_mail.error="Required field"
            return false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(editMail.text.toString()).matches()){
            tinput_mail.error="Invalid mail format"
            return false
        }

        if(editPass.text.toString()== ""){
            tinput_pass.error="Required field"
            tinput_pass.errorIconDrawable=null
            return false
        }

        if(editPass.length() < 8){
            tinput_pass.error="Password should at least 8 digits long"
            tinput_pass.errorIconDrawable=null
            return false
        }
        return true
    }
}
