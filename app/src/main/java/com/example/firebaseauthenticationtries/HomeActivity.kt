package com.example.firebaseauthenticationtries

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.math.sign

class HomeActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.hide()
        auth=FirebaseAuth.getInstance()
        user= auth.currentUser!!

        //val signinAcc:GoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)!!
        //loggeduser.text= signinAcc.displayName

        /*val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val gsc=GoogleSignIn.getClient(this,gso)*/
        val account=GoogleSignIn.getLastSignedInAccount(this)
        loggeduser.text=account?.displayName

        //loggeduser.text=user.email

        signoutbtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }

        update_pass_btn.setOnClickListener {
            val password=editPass.text.toString()
            if(checkPasswordFields()){
                user.updatePassword(password).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this,"Password updated successfully",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this,it.exception?.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        update_mail_btn.setOnClickListener {
            val mail=editMail.text.toString()
            if(checkMailFields()){
                user.updateEmail(mail).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this,"Mail Updated Successfully",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,SignInActivity::class.java))
                    } else {
                        Toast.makeText(this,it.exception?.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        delete_acc_btn.setOnClickListener {

            val builder=AlertDialog.Builder(this)
            builder.setTitle("Delete User Account")
                .setMessage("Are you sure you want to delete your account permanently?\nThis action can't be undone.")
                .setPositiveButton("Yes, I understand"){ _, _ ->
                    user.delete().addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this,"You're Kicked out of this",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,SignInActivity::class.java))
                        finish()
                    }
                    else
                        Toast.makeText(this,it.exception?.message,Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No"){ _, _ -> }
            val alertDialog=builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    private fun checkPasswordFields():Boolean{
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

    private fun checkMailFields():Boolean{
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
