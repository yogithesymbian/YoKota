/*
 * Copyright (c) 2019. Scodeid | yogithesymbian | Yogi Arif Widodo.
 * Android Studio 3.5.1
 * Build #AI-191.8026.42.35.5900203, built on September 26, 2019
 * JRE: 1.8.0_202-release-1483-b49-5587405 amd64
 * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
 * Linux 5.2.0-kali3-amd64
 */

package id.scode.yokota.ui.auth

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.widget.TooltipCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import id.scode.yokota.R
import id.scode.yokota.hide
import id.scode.yokota.show
import id.scode.yokota.snackbar
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private var firebaseAuth: FirebaseAuth ?= null
    private var firebaseStorage: FirebaseStorage ?= null
    private var firebaseDatabase: FirebaseDatabase ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        Handler().postDelayed({
            btn_select_photo?.performLongClick()
        }, 2000)
        TooltipCompat.setTooltipText(btn_select_photo, "Add Photo")

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        // permission storage for load image's and contact for email
        @Suppress("ControlFlowWithEmptyBody")
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS)


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        /**
         * Listener onClick
         */
        btn_select_photo.setOnClickListener{

            Log.d(TAG_LOG, "Try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }

        btn_sign_up.setOnClickListener{
            performRegister(it)
        }

    }

    private fun performRegister(view: View) {
        progress_bar.show()

        val userName = ip_edt_username.text.toString()
        val userMail = ip_edt_email.text.toString()
        val userPass = ip_edt_pass.text.toString()

        if (userMail.isEmpty() || userPass.isEmpty() || userName.isEmpty() || selectedPhotoUri == null) {
            Log.d(TAG_LOG,"error : CRD0x000 ")
            root_layout_register.snackbar("Please Fill The Credential | (C) yoKata")
            return
        }

        Log.d(TAG_LOG,"""
            \n
            UserName : $userName
            Email is : $userMail/***
            Pass is : $userPass/***
        """.trimIndent())

        firebaseAuth?.createUserWithEmailAndPassword(userMail, userPass)
            ?.addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d(TAG_LOG,"Successfully created user With uid : ${it.result?.user?.uid}")
                root_layout_register.snackbar("Applying the image . . . ")

                // TODO store account to database
                uploadImageToFirbaseStorage(it.result?.user?.email)
            }
            ?.addOnFailureListener {
                Log.d(TAG_LOG,"Failed to create user : ${it.message} ")
                root_layout_register.snackbar("Failed to create user : ${it.message}")
//                finish()
//                overridePendingTransition(0,0)
//                startActivity(intent)
//                overridePendingTransition(0,0)
            }
    }

    private fun uploadImageToFirbaseStorage(email: String?) {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = firebaseStorage?.getReference("/images_yokota/$email/$filename")
        TooltipCompat.setTooltipText(progress_bar,"Loading . . .")

        ref?.putFile(selectedPhotoUri!!)
            ?.addOnSuccessListener {
                Log.d(TAG_LOG,"successfully uploading image : ${it.metadata?.path}")

                ref.downloadUrl
                    .addOnSuccessListener { url ->
                        Log.d(TAG_LOG,"File image location at : $url")
                        saveUserToFirebaseDatabase(url.toString())
                    }
            }
            ?.addOnFailureListener{
                Log.d(TAG_LOG,"Failed uploading image : ${it.message}")
                root_layout_register.snackbar("Failed uploading image : ${it.message}")

            }

    }

    private fun saveUserToFirebaseDatabase(urlImage: String) {
        val uid = firebaseAuth?.currentUser?.uid
        val ref = firebaseDatabase?.getReference("/users/$uid")

        val user = uid?.let {
            User(it, ip_edt_username.text.toString(), urlImage)
        }

        ref?.setValue(user)
            ?.addOnSuccessListener {

                Log.d(TAG_LOG,"Register successfully $it")
                root_layout_register.snackbar("Register successfully $it")
                Handler().postDelayed({
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }, 2000)

            }

    }

    @Suppress("ControlFlowWithEmptyBody")
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        progress_bar.show()

        if ( requestCode == 0 && resultCode == Activity.RESULT_OK && data !=null ){
            //proceed and check what the selected image was ...
            Log.d(TAG_LOG,"Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            select_circle_image.setImageBitmap(bitmap)
            btn_select_photo.alpha = 0f
            progress_bar.hide()
        }
    }

    companion object {

        private val TAG_LOG: String = RegisterActivity::class.java.simpleName
        private var selectedPhotoUri: Uri? = null
        private const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1

    }

    // inner class
     class User(val uid: String, val username: String, val profileImageUrl: String) {
        constructor() : this("","","")
    }
}
