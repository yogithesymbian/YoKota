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
