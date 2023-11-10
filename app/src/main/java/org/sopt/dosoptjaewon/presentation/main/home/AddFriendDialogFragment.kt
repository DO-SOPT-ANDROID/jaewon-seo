package org.sopt.dosoptjaewon.presentation.main.home

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import org.sopt.dosoptjaewon.databinding.DialogFriendAddBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddFriendDialogFragment : DialogFragment() {

    interface AddFriendDialogListener {
        fun onDialogPositiveClick(name: String, dob: LocalDate)
    }

    private var listener: AddFriendDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            val builder = AlertDialog.Builder(activity)
            val binding = DialogFriendAddBinding.inflate(activity.layoutInflater)

            builder.setView(binding.root)
                .setPositiveButton("Add") { _, _ ->
                    val name = binding.etAddFriendName.text.toString()
                    val dobString = binding.etAddFriendBrithday.text.toString()
                    val dob = LocalDate.parse(dobString, DateTimeFormatter.ISO_DATE)
                    listener?.onDialogPositiveClick(name, dob)
                }
                .setNegativeButton("Cancel", null)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setAddFriendDialogListener(listener: AddFriendDialogListener) {
        this.listener = listener
    }
}
