package id.scode.yokota.ui.init

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager
import id.scode.yokota.ui.home.HomeActivity
import id.scode.yokota.R

class SliderActivity : AppCompatActivity() {

    /**
     * add indicator dot in linearLayout
     */
    internal lateinit var mDots: Array<TextView?>
    internal lateinit var dotLayout: LinearLayout
    internal lateinit var btnBack: Button
    internal lateinit var btnNext: Button
    internal lateinit var btnFinish: Button
    private var currentPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_slider)

        /*
          Fullscreen View On Layout
         */
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val viewPager = findViewById<View>(R.id.slideViewPagerFirst) as ViewPager
        dotLayout = findViewById(R.id.dotSlide)
        btnBack = findViewById(R.id.btnBack)
        btnNext = findViewById(R.id.btnNext)
        btnFinish = findViewById(R.id.btnFinish)

        /*
        Set Adapter View Pager
         */
        val sliderAdapterFirstView = SliderAdapter(this)
        viewPager.adapter = sliderAdapterFirstView

        /*
        display Indicators
         */
        addDotsIndicator(0)

        //set listener
        viewPager.addOnPageChangeListener(viewListener)

        /*
        on Click Listener
         */
        btnBack.setOnClickListener { viewPager.currentItem = currentPage - 1 }

        btnNext.setOnClickListener { viewPager.currentItem = currentPage + 1 }
        btnFinish.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        val isFirstRun = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getBoolean("isFirstRun", false)
        if (isFirstRun) {
            //show start activity
            startActivity(Intent(this@SliderActivity, HomeActivity::class.java))
        }
        getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit().putBoolean("isFirstRun", true).apply()
    }

    fun addDotsIndicator(position: Int) {
        mDots = arrayOfNulls(3)
        dotLayout.removeAllViews()

        for (i in mDots.indices) {
            mDots[i] = TextView(this)
            mDots[i]!!.text = Html.fromHtml(" &#8226 ")
            mDots[i]!!.textSize = 40f
            mDots[i]!!.setTextColor(ResourcesCompat.getColor(resources,
                R.color.colorTransparentWhite, null))

            dotLayout.addView(mDots[i])
        }
        if (mDots.isNotEmpty()) {
            mDots[position]!!.setTextColor(ResourcesCompat.getColor(resources,
                R.color.white, null))
        }
        if (0 >= position) {
            guideText()
        }

    }

    private fun guideText() {

//        FancyShowCaseView.Builder(this)
//            .title("\n\n\n\n\n\n\n\n Swipe Screen \n FOR \n Next | Back")
//            .titleStyle(R.style.FancyShowCaseDefaultTitleStyle, Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL or Gravity.CENTER)
//            .build()
//            .show()
    }


    internal var viewListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {

            addDotsIndicator(position)

            currentPage = position

            if (position == 0)
            //1
            {
                btnBack.isEnabled = false
                btnNext.isEnabled = true
                btnFinish.isEnabled = false
                btnFinish.visibility = View.INVISIBLE
                btnBack.visibility = View.INVISIBLE
                btnNext.visibility = View.INVISIBLE

                btnBack.text = ""
                btnNext.text = "Next"
            } else if (position == mDots.size - 1)
            //3
            {
                btnBack.isEnabled = true
                btnNext.isEnabled = false
                btnFinish.isEnabled = true
                btnBack.visibility = View.VISIBLE
                btnNext.visibility = View.INVISIBLE
                btnFinish.visibility = View.VISIBLE

                btnBack.text = "Back"
                btnNext.text = ""
            } else
            //2
            {
                btnBack.isEnabled = true
                btnNext.isEnabled = true
                btnFinish.isEnabled = false
                btnNext.visibility = View.VISIBLE
                btnBack.visibility = View.VISIBLE
                btnFinish.visibility = View.INVISIBLE

                btnBack.text = "Back"
                btnNext.text = "Next"
            }
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }
}

