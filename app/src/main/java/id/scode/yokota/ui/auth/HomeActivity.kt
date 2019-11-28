/*
 * Copyright (c) 2019. Scodeid | yogithesymbian | Yogi Arif Widodo.
 * Android Studio 3.5.1
 * Build #AI-191.8026.42.35.5900203, built on September 26, 2019
 * JRE: 1.8.0_202-release-1483-b49-5587405 amd64
 * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
 * Linux 5.2.0-kali3-amd64
 */

package id.scode.yokota.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import id.scode.yokota.R
import id.scode.yokota.ui.home.MainActivity
import kotlinx.android.synthetic.main.activity_home.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException



class HomeActivity : AppCompatActivity() {

    companion object{
        private const val REQUEST_SIGN_GOOGLE: Int = 1
    }

    // declare google lib
    private lateinit var gsc: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions

    // declare for facebook lib
    private var firebaseAuth: FirebaseAuth ? = null
    private var callBackManager: CallbackManager ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        verifyUserLoggedIn()


//        printHashKey() // for facebook app {hash}

        /**
         * google initialize for dialog builder client
         */
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(
            this ,
            gso
        )

        /**
         * facebook initialize
         */
        firebaseAuth = FirebaseAuth.getInstance()
        callBackManager = CallbackManager.Factory.create()
        btn_fb_login.setReadPermissions("email")


        /**
         * Listener onClick
         */
        btn_register.setOnClickListener{
            startActivity(Intent(this@HomeActivity, RegisterActivity::class.java))
        }

        google_sign.setOnClickListener{
            signInGoogle()
        }
        btn_fb_login.setOnClickListener {
            signIn()
        }


    }

    private fun verifyUserLoggedIn() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    private fun signIn() {
        btn_fb_login.registerCallback(callBackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {

            }

        })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        // Get Credential
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        firebaseAuth?.signInWithCredential(credential)
            ?.addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_LONG)
                    .show()
            }
            ?.addOnSuccessListener { result ->
                // Get Email
                val email = result.user?.email
                Toast.makeText(this, "You logged with email : $email", Toast.LENGTH_LONG)
                    .show()
            }
    }


//    @SuppressLint("PackageManagerGetSignatures")
//    private fun printHashKey() {
//        Log.d("RUN","print")
//        try {
//            Log.d("RUN","try print")
//            val info : PackageInfo = packageManager.getPackageInfo("id.scode.yokota", PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures){
//
//                val md : MessageDigest = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.e("KEY_HASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//                Log.d("RUN","for print")
//
//            }
//        } catch (e: PackageManager.NameNotFoundException){
//
//        } catch (e: NoSuchAlgorithmException){
//
//        }
//    }

    private fun signInGoogle() {
        val intentSignGoogle = gsc.signInIntent
        startActivityForResult(intentSignGoogle, REQUEST_SIGN_GOOGLE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SIGN_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
        callBackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleResult(task: Task<GoogleSignInAccount>?) {
        try {
            val account = task?.getResult(ApiException::class.java)
            updateUi(account)
        } catch (e: ApiException){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun updateUi(account: GoogleSignInAccount?) {
        Toast.makeText(this, account?.displayName.toString(), Toast.LENGTH_LONG)
            .show()
        startActivity(Intent(this@HomeActivity, MainActivity::class.java))
    }
}
