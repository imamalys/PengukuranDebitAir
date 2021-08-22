package id.ias.calculationwaterdebit.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import id.ias.calculationwaterdebit.databinding.MessageDialogBinding

class MessageDialogUtil {

    lateinit var dialog: CustomDialog
    lateinit var binding: MessageDialogBinding

    fun show(context: Context, dialogListener: DialogListener) {
        show(context, "Apakah anda yakin ingin membatalkan?",
        "Ya", "Tidak", dialogListener)
    }

    fun show(context: Context, title: String = "Apakah anda yakin ingin membatalkan?",
             yes: String = "Ya", no: String = "Tidak", dialogListener: DialogListener): Dialog {
        val inflater = (context as Activity).layoutInflater
        binding = MessageDialogBinding.inflate(inflater)
        dialog = CustomDialog(context)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)

        binding.tvTitle.text = title
        binding.tvYa.text = yes
        binding.tvTidak.text = no
        binding.tvYa.setOnClickListener {
            dialog.dismiss()
            dialogListener.onYes(true)
        }

        binding.tvTidak.setOnClickListener {
            dialog.dismiss()
            dialogListener.onYes(false)
        }
        dialog.show()
        return dialog
    }

    class CustomDialog(context: Context) : Dialog(context) {
        init {
            // Set Semi-Transparent Color for Dialog Background
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    interface DialogListener {
        fun onYes(action: Boolean)
    }
}