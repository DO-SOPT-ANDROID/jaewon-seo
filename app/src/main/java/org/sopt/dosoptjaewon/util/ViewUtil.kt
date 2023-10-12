package org.sopt.dosoptjaewon.util

import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar


fun View.snackbar(
    message: CharSequence,
    duration: Int = Snackbar.LENGTH_SHORT,
    view: View = this.findViewById(android.R.id.content)
) {
    Snackbar.make(view, message, duration).show()
}