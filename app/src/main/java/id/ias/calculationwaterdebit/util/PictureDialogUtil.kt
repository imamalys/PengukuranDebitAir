package id.ias.calculationwaterdebit.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.ViewGroup
import com.bumptech.glide.Glide
import id.ias.calculationwaterdebit.databinding.DialogPictureBinding

class PictureDialogUtil {

    lateinit var dialog: CustomDialog
    lateinit var binding: DialogPictureBinding

    fun show(context: Context, image: Int): Dialog {
        val inflater = (context as Activity).layoutInflater
        binding = DialogPictureBinding.inflate(inflater)
        dialog = CustomDialog(context)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)


        Glide.with(context)
            .load(image)
            .into(binding.zoomimg)

        binding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        return dialog
    }

    class CustomDialog(context: Context) : Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
        init {
            // Set Semi-Transparent Color for Dialog Background
            window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
}