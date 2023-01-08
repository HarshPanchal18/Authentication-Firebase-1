package com.example.firebaseauthenticationtries

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    companion object{ private val RC_SIGN_IN=120 }

    private lateinit var auth:FirebaseAuth
    private lateinit var googleSignInClient:GoogleSignInClient
    private lateinit var mGoogleApiClient: GoogleApiClient
    val RC_SIGN_IN=123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        supportActionBar?.hide()
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient=GoogleSignIn.getClient(this,gso)
        auth = FirebaseAuth.getInstance()

        signin_mail_img.setOnClickListener {
            signIn()
        }

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
                        val snackbar=Snackbar.make(
                            signinActivity,
                            "Account/Password is not correct!!",
                            Snackbar.ANIMATION_MODE_SLIDE
                        )
                        snackbar.setAction("Register"){
                            startActivity(Intent(this,SignUpActivity::class.java))
                        }.setActionTextColor(Color.YELLOW).setBackgroundTint(Color.BLACK).show()
                    }
                }
            }
        }

        forgot_p_text.setOnClickListener {
            startActivity(Intent(this,ForgotPassword::class.java))
        }

        create_acc_text.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }

    private fun signIn() {
        val intent= googleSignInClient.signInIntent
        startActivityForResult(intent,RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==RC_SIGN_IN){
            val task =GoogleSignIn.getSignedInAccountFromIntent(data)
            val except=task.exception
            if(task.isSuccessful) {
                try {
                    val acc=task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(acc.idToken!!)
                    startActivity(Intent(this, HomeActivity::class.java))
                } catch (e: ApiException) {
                    Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, except?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"Successfully logged in",Toast.LENGTH_LONG).show()
                    Log.e("Complete","signInWithCredential:on_complete: " + task.isSuccessful)
                    val intent = Intent(this,HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("Error","signInWithCredential: " + task.exception?.message)
                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
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

    override fun onStart() {
        super.onStart()
        val user=auth.currentUser
        if(user!=null)
            startActivity(Intent(this,HomeActivity::class.java))
    }
}
