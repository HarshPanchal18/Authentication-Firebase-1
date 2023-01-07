package com.example.firebaseauthenticationtries

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.editPass
import kotlinx.android.synthetic.main.activity_home.tinput_pass
import kotlinx.android.synthetic.main.activity_sign_up.*

class HomeActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.hide()
        auth=FirebaseAuth.getInstance()

        signoutbtn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }

        update_pass_btn.setOnClickListener {
            val user=auth.currentUser
            val password=editPass.text.toString()
            if(checkFields()){
                if(user!=null){
                    user.updatePassword(password).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this,"Password updated successfully",Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this,it.exception?.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }
    private fun checkFields():Boolean{
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
