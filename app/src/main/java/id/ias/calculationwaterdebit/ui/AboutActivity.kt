package id.ias.calculationwaterdebit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.ias.calculationwaterdebit.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}