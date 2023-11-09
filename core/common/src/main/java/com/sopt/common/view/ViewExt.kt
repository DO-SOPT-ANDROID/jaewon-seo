package com.sopt.common.view

import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun View.snackBar(messageResId: Int) {
    Snackbar.make(this, context.getString(messageResId), Snackbar.LENGTH_SHORT).show()
}

fun View.toast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

