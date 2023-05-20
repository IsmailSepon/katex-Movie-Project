package com.sepon.katexentertainment.splashActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sepon.katexentertainment.auth.LoginActivity
import com.babu.smartlock.sessionManager.UserSessionManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.sepon.katexentertainment.MainActivity
import com.sepon.katexentertainment.R


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    var btSignIn: SignInButton? = null
    var googleSignInClient: GoogleSignInClient? = null
    var firebaseAuth: FirebaseAuth? = null

    lateinit var   userSession : UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        userSession = UserSessionManager(this)
        btSignIn=findViewById(R.id.google_button);


        if (userSession.userName != ""){

            Handler().postDelayed({
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 2000)
        }else{

            Handler().postDelayed({
                val i = Intent(this, LoginActivity::class.java
                )
                startActivity(i)
                finish()
            }, 2000)
        }


        val googleSignInOptions  = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

//        val i = Intent(this, MainActivity::class.java)
//            startActivity(i)

        btSignIn!!.setOnClickListener {

            val intent = googleSignInClient!!.signInIntent
            startActivityForResult(intent, 100)


        }




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