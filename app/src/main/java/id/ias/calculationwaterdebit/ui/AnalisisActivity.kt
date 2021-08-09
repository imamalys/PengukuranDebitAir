package id.ias.calculationwaterdebit.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
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
    var pengambilanDataList: List<PengambilanDataModel> = ArrayList()

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

    private fun setAnalisisBangunan(it: List<PengambilanDataModel>) {
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
        val arrayPercentageResidual: ArrayList<String> = ArrayList()
        for (i in it.indices) {
            val h1: String = String.format("%.3f", it[i].h1)
            val qPengukuran: String = String.format("%.3f", it[i].qPengukuran)
            val qPengukuran1000: String = String.format("%.3f", it[i].qPengukuran!! * 1000)
            val qBangunan: String = String.format("%.3f", it[i].qBangunan)
            val qBangunan1000: String = String.format("%.3f", (it[i].qBangunan!! * 1000))
            val logQPengukuran: String = String.format("%.3f", log10(qPengukuran1000.toFloat()))
            val logQBangunan: String = String.format("%.3f", log10(qBangunan1000.toFloat()))
            val residual: String = String.format("%.3f", logQPengukuran.toFloat() - logQBangunan.toFloat())
            val percentageResidual: String = String.format("%.4f", residual.toFloat() / logQPengukuran.toFloat())

            arrayItem.add(
                    arrayOf(
                            h1, qPengukuran, qPengukuran1000, qBangunan, qBangunan1000, logQPengukuran,
                            logQBangunan, residual
                    )
            )

            arrayPercentageResidual.add(percentageResidual)
        }

        val median: String = if (arrayPercentageResidual.size %2 == 0)
            String.format("%.4f",
                    arrayPercentageResidual[arrayPercentageResidual.size/2].toFloat() +
                            arrayPercentageResidual[arrayPercentageResidual.size/2 - 1].toFloat() / 2)
        else String.format("%.4f",
                arrayPercentageResidual[arrayPercentageResidual.size/2].toFloat())

        val arrayAbs: ArrayList<String> = ArrayList()
        for (i in arrayPercentageResidual.indices) {
            val abs = String.format("%.6f", arrayPercentageResidual[i].toFloat() - median.toFloat())
            arrayAbs.add(abs)
        }

        val mad: String = if (arrayAbs.size %2 == 0)
            String.format("%.6f",
                    arrayAbs[arrayAbs.size/2].toFloat() + arrayAbs[arrayAbs.size/2 - 1].toFloat() / 2)
        else String.format("%.6f",
                arrayAbs[arrayAbs.size/2].toFloat())

        val arrayZmod: ArrayList<String> = ArrayList()

        for (i in arrayAbs.indices) {
            val zmod = String.format("%.3f", 0.6745.toFloat() * arrayPercentageResidual[i].toFloat() / mad.toFloat())
            arrayZmod.add(zmod)
        }

        for (i in it.indices) {
            LegacyTableView.insertLegacyContent((i + 1).toString(), arrayItem[i][0], arrayItem[i][1], arrayItem[i][2], arrayItem[i][3],
                    arrayItem[i][4], arrayItem[i][5], arrayItem[i][6], arrayItem[i][7], arrayPercentageResidual[i], median, arrayAbs[i],
                    mad, arrayZmod[i])
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.analisisPertamaTable1.setTitle(readTitle)
        mBinding.analisisPertamaTable1.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.analisisPertamaTable1.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.analisisPertamaTable1.setZoomEnabled(true)
        mBinding.analisisPertamaTable1.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.analisisPertamaTable1.build()
    }

    private fun setViewModel() {
        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            pengambilanDataList = it
            setAnalisisBangunan(it)
        })
    }
}