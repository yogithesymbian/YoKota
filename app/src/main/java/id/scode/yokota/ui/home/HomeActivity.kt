package id.scode.yokota.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.scode.yokota.R
import id.scode.yokota.ui.auth.RegisterActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn_register.setOnClickListener{
            startActivity(Intent(this@HomeActivity, RegisterActivity::class.java))
        }
    }
}
