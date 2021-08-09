package id.ias.calculationwaterdebit.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModelFactory
import id.ias.calculationwaterdebit.database.viewmodel.PengambilanDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.PengambilanDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityAnalisisBinding
import kotlin.math.log10

class AnalisisActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityAnalisisBinding

    private val baseDataViewModel: BaseDataViewModel by viewModels {
        BaseDataViewModelFactory((application as Application).baseDataRepository)
    }

    private val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory((application as Application).pengambilanDataRepository)
    }

    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0
    var detailBangunan: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAnalisisBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                detailBangunan = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
                idBaseData = it.getLongExtra("id_base_data", 0)
            }
        }

        setViewModel()
    }

    private fun setViewModel() {
        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            LegacyTableView.insertLegacyTitle(
                "Variasi air Ke-",
                "H1 (m)",
                "Q Pengukuran (m3/s)",
                "Q Pengukuran (I/s",
                "Q Bangunan (m3/s)",
                " Q Bangunan (I/s)",
                "Log Q. Pengukuran",
                "Log Q. Bangunan",
                "Residual",
                "%Rresidual",
                "Median E",
                "Abs(%Residual)",
                "MAD",
                "Zmod"
            )

            val arrayItem: ArrayList<Array<String>> = ArrayList()

            for (i in it.indices) {
                val h1: String = String.format("%.3f", it[i].h1)
                val qPengukuran: String = String.format("%.3f", it[i].qPengukuran)
                val qPengukuran1000: String = String.format(
                    "%.3f",
                    (it[i].qPengukuran!! * 1000)
                )
                val qBangunan: String = String.format("%.3f", it[i].qBangunan)
                val qBangunan1000: String = String.format("%.3f", (it[i].qBangunan!! * 1000))
                val logQPengukuran: String = String.format("%.3f", log10(qPengukuran1000.toFloat()))
                val logQBangunan: String = String.format("%.3f", log10(qBangunan1000.toFloat()))
                val residual: String = String.format(
                    "%.3f",
                    logQPengukuran.toFloat() - logQBangunan.toFloat()
                )
                val percentageResidual: String = String.format(
                    "%.3f",
                    residual.toFloat() / logQPengukuran.toFloat()
                )

                arrayItem.add(
                    arrayOf(
                        h1, qPengukuran, qPengukuran1000, qBangunan, qBangunan1000, logQPengukuran,
                        logQBangunan, residual, percentageResidual, "", ""
                    )
                )
            }
        })
    }
}