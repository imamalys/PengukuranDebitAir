package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolSegiempatModel
import id.ias.calculationwaterdebit.database.viewmodel.AmbangLebarPengontrolSegiempatViewModel
import id.ias.calculationwaterdebit.database.viewmodel.AmbangLebarPengontrolSegiempatViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityTipeBangunanUkurBinding
import id.ias.calculationwaterdebit.viewmodel.TipeBangunanUkurViewModel
import id.ias.calculationwaterdebit.viewmodel.TipeBangunanUkurViewModelFactory

class TipeBangunanUkurActivity : AppCompatActivity() {

    var idBaseData: Long = 0
    lateinit var mBinding: ActivityTipeBangunanUkurBinding
    private val tipeBangunanUkurViewModel: TipeBangunanUkurViewModel by viewModels {
        TipeBangunanUkurViewModelFactory()
    }
    private val alpsViewModel: AmbangLebarPengontrolSegiempatViewModel by viewModels {
        AmbangLebarPengontrolSegiempatViewModelFactory((application as Application).alpsRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityTipeBangunanUkurBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("id_base_data")) {
                idBaseData = it.getLongExtra("id_base_data", 0)
            }
        }

        setSpinner()
        setViewModel()
        setAction()
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            val intent = Intent(this@TipeBangunanUkurActivity, DetailBangunanActivity::class.java)
            when(tipeBangunanUkurViewModel.tipeBangunan.value!!) {
                "Ambang Lebar Pengontrol Segiempat" -> {
                    val alpsData = AmbangLebarPengontrolSegiempatModel(
                        null,
                        idBaseData.toInt(),
                        null,
                        "0".toFloat(), "0".toFloat(), "0".toFloat(), "0".toFloat(), "0".toFloat(),"0".toFloat(),
                        "0".toFloat())
                    alpsViewModel.insert(alpsData)
                    intent.putExtra("tipe_bangunan", tipeBangunanUkurViewModel.tipeBangunan.value)
                    intent.putExtra("id_tipe_bangunan", alpsViewModel.idTipeBangunan)
                }
            }

            startActivity(intent)
            finish()
        }
    }

    private fun setSpinner() {
        var list = resources.getStringArray(R.array.bangunan_ukur)
        val adapter = ArrayAdapter(applicationContext, R.layout.spinner_item, list)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        mBinding.spTipeBangunan.adapter = adapter
        mBinding.spTipeBangunan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                tipeBangunanUkurViewModel.tipeBangunan.value = list[position]
            }
        }

    }

    private fun setViewModel() {
        tipeBangunanUkurViewModel.tipeBangunan.observe(this) {
            mBinding.ivImageBangunan.setImageDrawable(theme.getDrawable(tipeBangunanUkurViewModel.getImage(it)))
            mBinding.ivRumus.setImageDrawable(theme.getDrawable(tipeBangunanUkurViewModel.getRumus(it)))
            mBinding.tvKeteranganIsi.text = tipeBangunanUkurViewModel.getKeterangan(it)
        }
    }
}