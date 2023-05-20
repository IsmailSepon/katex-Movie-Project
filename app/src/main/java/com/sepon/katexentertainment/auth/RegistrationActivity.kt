package com.sepon.katexentertainment.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.babu.smartlock.sessionManager.UserSessionManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.sepon.katexentertainment.MainActivity
import com.sepon.katexentertainment.R

class RegistrationActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    lateinit var   userSession : UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_login)
        supportActionBar!!.hide();


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        userSession = UserSessionManager(this)

        val createAcc : Button? = findViewById(R.id.createAccountBtn);
        val email : TextInputEditText? = findViewById(R.id.inputEmailEt);
        val password : TextInputEditText? = findViewById(R.id.inputPassword);
        val confirmPassword : TextInputEditText? = findViewById(R.id.inputConfirmPassword);
        createAcc!!.setOnClickListener{

            val emailtxt : String = email!!.text.toString()
            val pass : String  = password!!.text.toString()
            val conPass : String  = confirmPassword!!.text.toString()
            if (emailtxt == ""){
                email.error = "Input Required"
            }else if (pass == "" || pass.length < 6){
                password.error = "Password Required"
            }else if (conPass == "" || conPass.length < 6){
                confirmPassword.error = "Password Required"
            }else if (pass != conPass){
                confirmPassword.error = "Password Not matched"
            }else{
                goForReg(emailtxt, pass)
            }

        }

    }

    private fun goForReg(emailtxt: String, pass: String) {
        mAuth!!.createUserWithEmailAndPassword(emailtxt, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val account = mAuth!!.currentUser
                    userSession.userName = account?.email.toString()
                    userSession.userEmail = account?.email.toString()
                    //userSession.userPic = account?.photoUrl.toString()
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finish()
                   // updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }


}