package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.database.viewmodel.AmbangLebarPengontrolSegiempatViewModel
import id.ias.calculationwaterdebit.database.viewmodel.AmbangLebarPengontrolSegiempatViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainMenuBinding
    private val ambangLebarPengontrolSegiempatViewModel: AmbangLebarPengontrolSegiempatViewModel by viewModels {
        AmbangLebarPengontrolSegiempatViewModelFactory((application as Application).alpsRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setAction()

        Glide.with(this)
                .load(R.drawable.logo_app)
                .circleCrop()
                .into(mBinding.ivTopLeft)
    }

    private fun setAction() {
        mBinding.ivIcon.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, DataInformasiActivity::class.java)
            startActivity(intent)
        }

        mBinding.ivIcon2.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, ReportActivity::class.java)
            startActivity(intent)
        }
    }
}