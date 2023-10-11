package org.sopt.dosopttemplate.util

import android.app.Activity
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar


fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Activity.snackbar(
    message: CharSequence,
    duration: Int = Snackbar.LENGTH_SHORT,
    view: View = this.findViewById(android.R.id.content)
) {
    Snackbar.make(view, message, duration).show()
}