package id.ias.calculationwaterdebit.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import id.ias.calculationwaterdebit.R

class LoadingDialogUtil {

    lateinit var dialog: CustomDialog

    fun isShow(): Boolean {
        return if (::dialog.isInitialized) {
            dialog.isShowing
        } else {
            false
        }
    }
    fun show(context: Context, isCancelable: Boolean = false): Dialog {
        return show(context, null, isCancelable)
    }

    fun show(context: Context, title: CharSequence?, isCancelable: Boolean): Dialog {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.loading_dialog, null)
        dialog = CustomDialog(context)
        dialog.setContentView(view)
        dialog.setCancelable(isCancelable)
        dialog.show()
        return dialog
    }

    class CustomDialog(context: Context) : Dialog(context) {
        init {
            // Set Semi-Transparent Color for Dialog Background
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                window?.decorView?.setOnApplyWindowInsetsListener { _, insets ->
                    insets.consumeSystemWindowInsets()
                }
            }
        }
    }
}