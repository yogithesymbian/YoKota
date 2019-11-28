/*
 * Copyright (c) 2019. Scodeid | yogithesymbian | Yogi Arif Widodo.
 * Android Studio 3.5.1
 * Build #AI-191.8026.42.35.5900203, built on September 26, 2019
 * JRE: 1.8.0_202-release-1483-b49-5587405 amd64
 * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
 * Linux 5.2.0-kali3-amd64
 */

package id.scode.yokota

import android.view.View
import android.widget.ProgressBar
import com.google.android.material.snackbar.Snackbar

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun View.snackbar(message: String) {

    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Action", null)
    }.show()
//    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
//        snackbar.setAction("Ok") {
//            snackbar.dismiss()
//        }
//    }.show()

}