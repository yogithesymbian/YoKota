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
        R.drawable.ic_logo_1,
        R.drawable.ic_logo_1,
        R.drawable.ic_logo_1
    )

    private var slideHeading = arrayOf("Service Government", "Medical Service", "Move Point Service")
    private var slideDescription = arrayOf("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Adipisci atque dolores doloribus eaque eos exercitationem, omnis pariatur repellendus vitae voluptatem. Dolorum eaque iste magni nemo nobis, quo totam? Aut, hic.", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Adipisci atque dolores doloribus eaque eos exercitationem, omnis pariatur repellendus vitae voluptatem. Dolorum eaque iste magni nemo nobis, quo totam? Aut, hic.", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Adipisci atque dolores doloribus eaque eos exercitationem, omnis pariatur repellendus vitae voluptatem. Dolorum eaque iste magni nemo nobis, quo totam? Aut, hic.")

    override fun getCount(): Int {
        return slideImage.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view: View? = null
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
