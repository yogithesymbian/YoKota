/*
 * Copyright (c) 2019. Scodeid | yogithesymbian | Yogi Arif Widodo. 
 * Android Studio 3.5.1
 * Build #AI-191.8026.42.35.5900203, built on September 26, 2019
 * JRE: 1.8.0_202-release-1483-b49-5587405 amd64
 * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
 * Linux 5.2.0-kali3-amd64
 */

package id.scode.yokota.ui.init

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import id.scode.yokota.R


class SliderAdapter(private val context: Context) : PagerAdapter() {

    private var slideImage = intArrayOf(
        R.drawable.ic_bg_sl_1,
        R.drawable.ic_logo_1,
        R.drawable.ic_logo_1,
        R.drawable.ic_logo_1
    )

    private var slideHeading =
        arrayOf("YoKota","Banjir", "Reboisasi", "Hutan Gundul/ Penebangan Liar")
    private var slideDescription = arrayOf(
        "aplikasi cerdas yang dibuat atas kesadaran dari lokasi yang rawan terhadap banjir akibat berkurangnya jumlah pohon-pohon sebagai penyerap air jika intensitas hujan terus menerus",
        "salah satu bencana alam yang sering timbul salah satu factornya adalah karena berkurangnya jumlah pohon dan pembangunan gedung- gedung tinggi pencakar langit",
        "penghijauan atau penanaman hutan kembali agar dapat menyerap jumlah air yang berlebihan sehingga tidak terjadi bencana banjir di tempat rawan banjir",
        "kondisi lahan yang berkurang ditumbuhi pohon- pohon atau tanaman yang secara tidak langsung jika tidak dilakukan penanaman kembali (reboisasi) maka dapat mengakibatkan banjir jika intensitas hujan berlebihan"
    )

    override fun getCount(): Int {
        return slideImage.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View?
        view = layoutInflater.inflate(R.layout.activity_slider, container, false)

        assert(view != null)
        val imageLogo = view!!.findViewById<View>(R.id.logoFirstView) as ImageView
        val txtTitle = view.findViewById<View>(R.id.title_first_view) as TextView
        val txtDescprition = view.findViewById<View>(R.id.desc_view) as TextView
        val cardView1 = view.findViewById<View>(R.id.cardViewFirstView) as CardView
        val cardView2 = view.findViewById<View>(R.id.cardViewFirstView2) as CardView

        cardView1.setBackgroundColor(Color.TRANSPARENT)

        cardView2.setBackgroundColor(Color.TRANSPARENT)

        imageLogo.setImageResource(slideImage[position])
        txtTitle.text = slideHeading[position]
        txtDescprition.text = slideDescription[position]

        container.addView(view)

        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}
