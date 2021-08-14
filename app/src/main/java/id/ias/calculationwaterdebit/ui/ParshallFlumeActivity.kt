package id.ias.calculationwaterdebit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.ias.calculationwaterdebit.databinding.ActivityParshallFlumeBinding

class ParshallFlumeActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityParshallFlumeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityParshallFlumeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}