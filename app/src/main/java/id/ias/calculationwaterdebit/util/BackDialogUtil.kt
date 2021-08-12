package id.ias.calculationwaterdebit.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.databinding.BackDialogBinding

class BackDialogUtil {

    lateinit var dialog: CustomDialog
    lateinit var binding: BackDialogBinding

    fun show(context: Context, dialogListener: DialogListener): Dialog {
        val inflater = (context as Activity).layoutInflater
        binding = BackDialogBinding.inflate(inflater)
        dialog = CustomDialog(context)
        dialog.setContentView(binding.root)

        binding.tvYa.setOnClickListener {
            dialog.dismiss()
            dialogListener.onYes(true)
        }

        binding.tvTidak.setOnClickListener {
            dialog.dismiss()
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