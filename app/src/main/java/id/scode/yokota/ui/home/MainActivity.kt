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
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import id.scode.yokota.R
import id.scode.yokota.model.AirModel
import kotlinx.android.synthetic.main.activity_main_content.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    lateinit var databaseReference: DatabaseReference
    lateinit var showProgress: ProgressBar
    private val TAG_LOG: String = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        showProgress = findViewById(R.id.progress_bar_rv)
        databaseReference = FirebaseDatabase.getInstance().reference.child("water-level-sensor/ultrasonic")

        recyclerView.layoutManager = LinearLayoutManager(this)
        Log.d(TAG_LOG, "onCreate")

        val option = FirebaseRecyclerOptions.Builder<AirModel>()
            .setQuery(databaseReference, AirModel::class.java)
            .build()

        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<AirModel, MyViewHolder>(option) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                return MyViewHolder(LayoutInflater.from(this@MainActivity).inflate(R.layout.item_air, parent, false))
            }

            override fun onBindViewHolder(holder: MyViewHolder, p1: Int, airModel: AirModel) {
                val refid = getRef(p1).key.toString()
                Log.d(TAG_LOG, "data id " + refid)

                databaseReference.child(refid).addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.d(TAG_LOG, "i get error $p0")
                        Toast.makeText(this@MainActivity, "Error Occurred " + p0.toException(), Toast.LENGTH_SHORT).show()
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(p0: DataSnapshot) {
                        showProgress.visibility = if (itemCount == 0) View.VISIBLE else View.GONE
                        holder.distance.text = "${airModel.distance}"
                        val kedalaman = holder.distance.text
                        if (kedalaman <= "2") {
                        }
                        holder.location.text = "${airModel.location}"
                        Log.d(TAG_LOG, " data : " + airModel.distance)
                    }
                })
            }
        }
        recyclerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()

        spinner_cat1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selected: Any = parent.getItemAtPosition(position)
                Log.d(TAG_LOG, "onItemSelected : $selected")
//                alreadyChooseCat1(selected) // query to show result (category 2) after choose on category 1
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var distance: TextView = itemView.findViewById(R.id.txt_distance)
        var location: TextView = itemView.findViewById(R.id.txt_status)
    }
}
