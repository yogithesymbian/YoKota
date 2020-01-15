/*
 * Copyright (c) 2019. Scodeid | yogithesymbian | Yogi Arif Widodo.
 * Android Studio 3.5.1
 * Build #AI-191.8026.42.35.5900203, built on September 26, 2019
 * JRE: 1.8.0_202-release-1483-b49-5587405 amd64
 * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
 * Linux 5.2.0-kali3-amd64
 */

package id.scode.yokota.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import id.scode.yokota.data.model.AirModel
import id.scode.yokota.ui.map.DeviceLocationActivity
import id.scode.yokota.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_bar.*
import kotlinx.android.synthetic.main.activity_main_content.*
import id.scode.yokota.R
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG_LOG: String = MainActivity::class.java.simpleName
    }

    lateinit var databaseReference: DatabaseReference
    lateinit var linearLayoutManager: LinearLayoutManager

    //menu favorite
    private var menuItem: Menu? = null

    // weather
    val CITY: String = "Samarinda"
    val API: String = "bce3713362b4fb9df79300b585714554"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // weather
        WeatherTask().execute()

        // recycler view
        loadWaterRecycler(rv_water_level_sensor)

        fb_map_view.setOnClickListener {
            startActivity(Intent(this@MainActivity, DeviceLocationActivity::class.java))
        }

        card_weather.setOnClickListener {
            startActivity(Intent(this@MainActivity, WeatherActivity::class.java))
            overridePendingTransition(R.anim.intent_zoom_in, R.anim.intent_zoom_in)
        }

        spinnerListener()

        bottom_app_bar.replaceMenu(R.menu.drawer_home)
        bottom_app_bar.setNavigationOnClickListener {
            drawer_home.openDrawer(GravityCompat.START)
        }
        bottom_app_bar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.nav_user -> {
                    Log.d(TAG_LOG, "soon")
                    Toast.makeText(this, "soon", Toast.LENGTH_SHORT)
                        .show()
                    true
                }
                else -> false
            }
        }
    }

    private fun spinnerListener() {
        spinner_cat1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selected: Any = parent.getItemAtPosition(position)
                Log.d(TAG_LOG, "onItemSelected : $selected")
//                alreadyChooseCat1(selected) // query to show result (category 2) after choose on category 1
            }
        }
    }

    private fun loadWaterRecycler(recyclerView: RecyclerView) {
        databaseReference =
            FirebaseDatabase.getInstance().reference.child("water-level-sensor/ultrasonic")


        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager
//        recyclerView.layoutManager = LinearLayoutManager(this)
        Log.d(TAG_LOG, "onCreate")

        val option = FirebaseRecyclerOptions.Builder<AirModel>()
            .setQuery(databaseReference, AirModel::class.java)
            .build()

        val firebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<AirModel, MyViewHolder>(option) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                    return MyViewHolder(
                        LayoutInflater.from(this@MainActivity).inflate(
                            R.layout.item_air,
                            parent,
                            false
                        )
                    )
                }

                override fun onBindViewHolder(holder: MyViewHolder, p1: Int, airModel: AirModel) {
                    val refId = getRef(p1).key.toString()
                    Log.d(TAG_LOG, "data id $refId")

                    databaseReference.child(refId)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Log.d(TAG_LOG, "i get error $p0")
                                Toast.makeText(
                                    this@MainActivity,
                                    "Error Occurred " + p0.toException(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            @SuppressLint("SetTextI18n")
                            override fun onDataChange(p0: DataSnapshot) {

                                progress_bar_rv.visibility =
                                    if (itemCount == 0) View.VISIBLE else View.GONE

                                airModel.distance?.let { data ->
                                    Log.d(TAG_LOG, "length distance : ${data.length} = $data")
                                    holder.distance.let { txt ->
                                        when (data.length) {
                                            3 -> {
                                                txt.text = "00$data"
                                            }
                                            4 -> {
                                                txt.text = "0$data"
                                            }
                                            else -> {
                                                txt.text = data
                                            }
                                        }
                                    }
                                }

//                         = holder.distance.text.toString()
                                val arrayList = arrayListOf(airModel)

                                for (i in arrayList.indices) {
                                    Log.d(TAG_LOG, " data ${arrayList[i].distance}")

                                    holder.location.text = "${arrayList[i].location}"

                                    val distanceAir =
                                        arrayList[i].distance.toString().replace("\r\n", "")
                                    Log.d(TAG_LOG, "init distanceAir : $distanceAir")
                                    when {
                                        distanceAir == "" -> {
                                            Log.d(TAG_LOG, "distanceAir : $distanceAir set UNKNOW")
                                            holder.cardLeft.setCardBackgroundColor(
                                                resources.getColor(
                                                    R.color.colorPrimaryDark
                                                )
                                            )
                                            holder.cardRight.setCardBackgroundColor(
                                                resources.getColor(
                                                    R.color.colorPrimaryDark
                                                )
                                            )
                                            holder.txtStatus.text = "unknow"
                                            TooltipCompat.setTooltipText(
                                                holder.cardRight,
                                                "tidak diketahui"
                                            )
                                        }
                                        distanceAir.toInt() in 0..14 -> {
                                            Log.d(TAG_LOG, "distanceAir : $distanceAir set GREEN")
                                            holder.cardLeft.setCardBackgroundColor(
                                                resources.getColor(
                                                    R.color.green
                                                )
                                            )
                                            holder.cardRight.setCardBackgroundColor(
                                                resources.getColor(
                                                    R.color.green
                                                )
                                            )
                                            holder.txtStatus.text = "safe"
                                            TooltipCompat.setTooltipText(
                                                holder.cardRight,
                                                "boleh dilewati"
                                            )
                                        }
                                        distanceAir.toInt() in 15..22 -> {
                                            Log.d(TAG_LOG, "distanceAir : $distanceAir set YELLOW")
                                            holder.cardLeft.setCardBackgroundColor(
                                                resources.getColor(
                                                    R.color.yellow
                                                )
                                            )
                                            holder.cardRight.setCardBackgroundColor(
                                                resources.getColor(
                                                    R.color.yellow
                                                )
                                            )
                                            holder.txtStatus.text = "warning"
                                            TooltipCompat.setTooltipText(
                                                holder.cardRight,
                                                "boleh dilewati, tapi hati-hati"
                                            )
                                        }
                                        else -> {
                                            Log.d(TAG_LOG, "distanceAir : $distanceAir set RED")
                                            holder.cardLeft.setCardBackgroundColor(
                                                resources.getColor(
                                                    R.color.red
                                                )
                                            )
                                            holder.cardRight.setCardBackgroundColor(
                                                resources.getColor(
                                                    R.color.red
                                                )
                                            )
                                            holder.txtStatus.text = "danger"
                                            TooltipCompat.setTooltipText(
                                                holder.cardRight,
                                                "berbahaya untuk dilewati"
                                            )
                                        }
                                    }
                                }
                            }
                        })
                }

            }

        firebaseRecyclerAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                val friendlyMessageCount: Int = firebaseRecyclerAdapter.itemCount
                val lastVisiblePosition: Int = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                    positionStart >= friendlyMessageCount - 1 &&
                    lastVisiblePosition == positionStart - 1
                ) {
                    recyclerView.scrollToPosition(positionStart)
                }
            }
        })

        recyclerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.drawer_home, menu)
        menuItem = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.nav_user -> {
                Log.d(TAG_LOG, "soon")
                Toast.makeText(this, "soon", Toast.LENGTH_SHORT)
                    .show()
                true
            }
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // weather
    @SuppressLint("StaticFieldLeak")
    inner class WeatherTask : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
            findViewById<ProgressBar>(R.id.pg_loader).visibility = View.VISIBLE
            findViewById<TextView>(R.id.txt_error).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            return try {
                URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
            } catch (e: Exception) {
                null
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt: Long = jsonObj.getLong("dt")
                val updatedAtText =
                    "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updatedAt * 1000)
                    )
                val temp = main.getString("temp") + "°C"
                val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
                val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name") + ", " + sys.getString("country")

                /* Populating extracted data into our views */
                findViewById<TextView>(R.id.txt_address).text = address
                findViewById<TextView>(R.id.txt_updated_at).text = updatedAtText
                findViewById<TextView>(R.id.txt_status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.txt_temp).text = temp
                findViewById<TextView>(R.id.txt_temp_min).text = tempMin
                findViewById<TextView>(R.id.txt_temp_max).text = tempMax
                findViewById<TextView>(R.id.sunrise).text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                findViewById<TextView>(R.id.sunset).text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById<ProgressBar>(R.id.pg_loader).visibility = View.GONE

            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.pg_loader).visibility = View.GONE
                findViewById<TextView>(R.id.txt_error).visibility = View.VISIBLE
            }

        }
    }
}