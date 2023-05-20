package com.sepon.katexentertainment.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.babu.smartlock.sessionManager.UserSessionManager
import com.sepon.katexentertainment.MainActivity
import com.sepon.katexentertainment.R


class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private lateinit var auth: FirebaseAuth
    var googleSignInClient: GoogleSignInClient? = null
    lateinit var   userSession : UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide();

        mAuth = FirebaseAuth.getInstance()
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        userSession = UserSessionManager(this)

        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)


        val createAcc: TextView? = findViewById(R.id.createAcc);
        val loginbtn: Button? = findViewById(R.id.loginbtn);
        val googleLoginBtn: SignInButton? = findViewById(R.id.google_login)

        createAcc!!.setOnClickListener {
            val i = Intent(this, RegistrationActivity::class.java)
            startActivity(i)
        }

        googleLoginBtn!!.setOnClickListener {
            val intent = googleSignInClient!!.signInIntent
            startActivityForResult(intent, 100)

        }

        val email: TextInputEditText? = findViewById(R.id.inputUsername1)
        val password: TextInputEditText? = findViewById(R.id.inputPassword1)
        loginbtn!!.setOnClickListener {

            val emailtxt: String = email!!.text.toString()
            val pass: String = password!!.text.toString()
            if (emailtxt.equals("")) {
                email.setError("Input Required")
            } else if (pass.equals("") || pass.length < 6) {
                password.setError("Password Required")
            } else {
                goForLogin(emailtxt, pass)
            }

        }

    }

    private fun goForLogin(emailtxt: String, pass: String) {
        mAuth!!.signInWithEmailAndPassword(emailtxt, pass)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val account: FirebaseUser? = mAuth?.currentUser

                        userSession.userName = account?.email.toString()
                        userSession.userEmail = account?.email.toString()
//                        userSession.userPic = account?.photoUrl.toString()
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                        finish()

                        Toast.makeText(this, "Authentication success.",
                            Toast.LENGTH_SHORT
                        ).show()
                       // updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        //updateUI(null)
                    }
                })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (requestCode == 100) {

                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                if (result!!.isSuccess) {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = result.signInAccount
                    userSession.userName = account?.displayName.toString()
                    userSession.userEmail = account?.email.toString()
                    userSession.userPic = account?.photoUrl.toString()
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finish()
                    // Toast.makeText(this, "success"+userSession.userName+" / "+userSession.userPic, Toast.LENGTH_SHORT).show()
                } else {
                    // Google Sign In failed, update UI appropriately
                    Toast.makeText(this, "faild", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}