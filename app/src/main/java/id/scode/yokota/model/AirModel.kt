/*
 * Copyright (c) 2019. Scodeid | yogithesymbian | Yogi Arif Widodo.
 * Android Studio 3.5.1
 * Build #AI-191.8026.42.35.5900203, built on September 26, 2019
 * JRE: 1.8.0_202-release-1483-b49-5587405 amd64
 * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
 * Linux 5.2.0-kali3-amd64
 */

package id.scode.yokota.model

import kotlinx.android.parcel.Parcelize

/**
 * @Authors scode | Yogi Arif Widodo
 * Created on 18 11/18/19 7:42 AM 2019
 * id.scode.yokota.model
 * https://github.com/yogithesymbian
 * Android Studio 3.5.1
 * Build #AI-191.8026.42.35.5900203, built on September 26, 2019
 * JRE: 1.8.0_202-release-1483-b49-5587405 amd64
 * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
 * Linux 5.2.0-kali3-amd64
 */

//water-level-sensor
//        ultrasonic
//ultrasonic
//-LpA86y3LooTkDxt_uzh
//date:
//"20/09/2019"
//distance:
//"31in, 79cm\r\n"
//location:
//"Samarinda"
//time:
//"04:08:27"

class AirModel {
    var date: String? = null
    var distance: String? = null
    var location: String? = null
    var time: String? = null

    constructor():this("","","",""){

    }

    constructor(date: String?, distance: String?, location: String?, time: String?) {
        this.date = date
        this.distance = distance
        this.location = location
        this.time = time
    }
}