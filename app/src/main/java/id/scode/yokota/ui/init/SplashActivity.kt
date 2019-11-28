/*
 * Copyright (c) 2019. Scodeid | yogithesymbian | Yogi Arif Widodo. 
 * Android Studio 3.5.1
 * Build #AI-191.8026.42.35.5900203, built on September 26, 2019
 * JRE: 1.8.0_202-release-1483-b49-5587405 amd64
 * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
 * Linux 5.2.0-kali3-amd64
 */

package id.scode.yokota.ui.init

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.Intent
import android.view.WindowManager
import id.scode.yokota.R


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_splash)

        /*
            Fullscreen View On Layout
         */
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // initialize delay
        val delayMiles = 2000
        // create Thread Handler with runnable object
        Handler().postDelayed(object : Runnable {
            override fun run() {
                // intent activity | move activity
                startActivity(Intent(this@SplashActivity, SliderActivity::class.java))
                this.finish()
            }

            // anonymous function
            private fun finish() {
                //TODO
            }
        }, delayMiles.toLong())

    }
}
