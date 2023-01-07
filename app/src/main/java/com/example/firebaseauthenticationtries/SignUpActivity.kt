package com.example.firebaseauthenticationtries

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()
        auth=FirebaseAuth.getInstance()

        signupbtn.setOnClickListener {
            val mail=editMail.text.toString()
            val password=editPass.text.toString()
            if(checkFields()){
                auth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener{
                    if(it.isSuccessful){
                        auth.signOut()
                        Toast.makeText(this,"Created",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,SignInActivity::class.java))
                        finish()
                    }
                    else
                        Toast.makeText(this,it.exception?.message,Toast.LENGTH_SHORT).show()
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

        if(editCPass.text.toString()== ""){
            tinput_cpass.error="Required field"
            tinput_cpass.errorIconDrawable=null
            return false
        }

        if(editPass.text.toString()!=editCPass.text.toString()){
            tinput_pass.error="Password doesn't matched"
            return false
        }
        return true
    }
}
