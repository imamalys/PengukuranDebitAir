package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.ias.calculationwaterdebit.databinding.ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setAction()
    }

    private fun setAction() {
        mBinding.ivIcon.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, DataInformasiActivity::class.java)
            startActivity(intent)
        }
    }
}