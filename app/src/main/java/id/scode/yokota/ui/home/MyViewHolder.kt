/*
 * Copyright (c) 2020. Scodeid | yogithesymbian | Yogi Arif Widodo.
 * Android Studio 3.5.1
 * Build #AI-191.8026.42.35.5900203, built on September 26, 2019
 * JRE: 1.8.0_202-release-1483-b49-5587405 amd64
 * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
 * Linux 5.2.0-kali3-amd64
 */

package id.scode.yokota.ui.home

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import id.scode.yokota.R

/**
 * @Authors scodeid | Yogi Arif Widodo
 * Created on 14 1/14/20 12:28 PM 2020
 * id.scode.yokota.ui.home
 * https://github.com/yogithesymbian
 * Android Studio 3.5.3
 * Build #AI-191.8026.42.35.6010548, built on November 15, 2019
 * JRE: 1.8.0_202-release-1483-b49-5587405 amd64
 * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
 * Linux 5.3.0-kali3-amd64
 */

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var distance: TextView = itemView.findViewById(R.id.txt_distance)
    var txtStatus: TextView = itemView.findViewById(R.id.txt_status)
    var location: TextView = itemView.findViewById(R.id.txt_desc_location)
    var cardLeft: CardView = itemView.findViewById(R.id.card_left)
    var cardRight: CardView = itemView.findViewById(R.id.card_right)

}

