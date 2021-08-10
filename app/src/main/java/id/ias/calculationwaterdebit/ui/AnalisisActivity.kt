package id.ias.calculationwaterdebit.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModelFactory
import id.ias.calculationwaterdebit.database.viewmodel.PengambilanDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.PengambilanDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityAnalisisBinding
import org.apache.commons.math3.stat.descriptive.summary.Sum
import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

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
    var b: String = ""
    private lateinit var baseData: BaseDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAnalisisBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                detailBangunan = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
                idBaseData = it.getLongExtra("id_base_data", 0)
                b = it.getStringExtra("b")!!
            }
        }

        setViewModel()
    }

    private fun setAnalisisBangunanTable1(it: List<PengambilanDataModel>) {
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

        setAnalisisBangunanTable2(arrayItem, arrayZmod)
    }

    private fun setAnalisisBangunanTable2(arrayItem: ArrayList<Array<String>>, arrayZmod: ArrayList<String>) {
        LegacyTableView.insertLegacyTitle(
                "Variasi air Ke-",
                "H1 (m)",
                "Q Pengukuran (m3/s)",
                "Q Pengukuran (I/s",
                "Q Bangunan (m3/s)",
                " Q Bangunan (I/s)",
                "APE", "E", "dE"
        )

        val newArrayItem: ArrayList<Array<String>> = ArrayList()
        val e: ArrayList<String> = ArrayList()
        var apeSum: Float = (0.0).toFloat()
        var apeSumNotZero: Float = (0.0).toFloat()
        var apeSumNotZeroCount: Int = 0
        var eSum: Float = (0.0).toFloat()
        for (i in arrayItem.indices) {
            val h1: String = arrayItem[i][0]
            val qPengukuran: String = arrayItem[i][1]
            val qPengukuran1000: String = arrayItem[i][2]
            val qBangunan: String = if (String.format("%.1f", arrayZmod[i].toFloat()).toFloat() > (3.5).toFloat() ||
                    String.format("%.1f", arrayZmod[i].toFloat()).toFloat() < (-3.5).toFloat())
                        "0" else arrayItem[i][3]
            val qBangunan1000: String = if (qBangunan == "0") "0" else arrayItem[i][3]
            val ape: String = if (qBangunan == "0") "0.00" else String.format("%.2f",
                    abs(qPengukuran1000.toFloat() - qBangunan1000.toFloat()) / qBangunan1000.toFloat() * 100)
            val eValue: String = if (qBangunan == "0") "0.00" else String.format("%.2f",
                    (qPengukuran.toFloat() - qBangunan.toFloat()) / qBangunan.toFloat() * 100)
            newArrayItem.add(arrayOf(h1, qPengukuran, qPengukuran1000, qBangunan, qBangunan1000, ape))
            apeSum += ape.toFloat()
            eSum += eValue.toFloat()
            e.add(eValue)

            if (qBangunan != "0") {
                apeSumNotZeroCount += 1
                apeSumNotZero += ape.toFloat()
            }
        }

        val dE: ArrayList<String> = ArrayList()
        for (i in newArrayItem.indices) {
            val deValue = if (newArrayItem[i][3] == "0") "0.00" else String.format("%.2f",
                    (newArrayItem[i][3].toFloat() - eSum).pow(2))
            dE.add(deValue)
        }

        for (i in newArrayItem.indices) {
            LegacyTableView.insertLegacyContent((i + 1).toString(), newArrayItem[i][0], newArrayItem[i][1],
                    newArrayItem[i][2], newArrayItem[i][3], newArrayItem[i][4], newArrayItem[i][5], e[i], dE[i])
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.analisisPertamaTable2.setTitle(readTitle)
        mBinding.analisisPertamaTable2.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.analisisPertamaTable2.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.analisisPertamaTable2.setZoomEnabled(true)
        mBinding.analisisPertamaTable2.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.analisisPertamaTable2.build()

        val mape = String.format("%.3f", (apeSumNotZero / apeSumNotZeroCount))
        mBinding.analisisPertamaResult.text = "Mape: $mape%"

        setAnalisisTableKe1()
    }

    private fun setAnalisisTableKe1() {
        baseDataViewModel.getBaseDataById(idBaseData.toInt())
        baseDataViewModel.baseDataById.observe(this, {
            baseData = it

            LegacyTableView.insertLegacyTitle(
                    "Variasi air Ke-",
                    "H1 (m)",
                    "Q Pengukuran (m3/s)",
                    "Q Pengukuran (I/s",
                    "Q Tabel (m3/s)",
                    " Q Tabel (I/s)",
                    "Log Q. Pengukuran",
                    "Log Q. Tabel",
                    "Residual",
                    "%Rresidual",
                    "Median E",
                    "Abs(%Residual)",
                    "MAD",
                    "Zmod"
            )

            val arrayItem: ArrayList<Array<String>> = ArrayList()
            val arrayPercentageResidual: ArrayList<String> = ArrayList()
            for (i in pengambilanDataList.indices) {
                val h1: String = String.format("%.3f", pengambilanDataList[i].h1)
                val qPengukuran: String = String.format("%.3f", pengambilanDataList[i].qPengukuran)
                val qPengukuran1000: String = String.format("%.3f", pengambilanDataList[i].qPengukuran!! * 1000)
                val qTabel: String = String.format("%.3f", (baseData.variablePertama!!.toFloat() * b.toFloat() * h1.toFloat()).pow(baseData.n!!.toFloat()))
                val qTabel1000: String = String.format("%.3f", (
                        baseData.variablePertama!!.toFloat() * b.toFloat() * h1.toFloat()).pow(baseData.n!!.toFloat()) * 1000)
                val logQPengukuran: String = String.format("%.3f", log10(qPengukuran1000.toFloat()))
                val logQBangunan: String = String.format("%.3f", log10(qTabel1000.toFloat()))
                val residual: String = String.format("%.3f", logQPengukuran.toFloat() - logQBangunan.toFloat())
                val percentageResidual: String = String.format("%.4f", residual.toFloat() / logQPengukuran.toFloat())

                arrayItem.add(
                        arrayOf(
                                h1, qPengukuran, qPengukuran1000, qTabel, qTabel1000, logQPengukuran,
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

            for (i in pengambilanDataList.indices) {
                LegacyTableView.insertLegacyContent((i + 1).toString(), arrayItem[i][0], arrayItem[i][1], arrayItem[i][2], arrayItem[i][3],
                        arrayItem[i][4], arrayItem[i][5], arrayItem[i][6], arrayItem[i][7], arrayPercentageResidual[i], median, arrayAbs[i],
                        mad, arrayZmod[i])
            }

            val readTitle = LegacyTableView.readLegacyTitle()
            val readBody = LegacyTableView.readLegacyContent()
            mBinding.analisisKeduaTable1.setTitle(readTitle)
            mBinding.analisisKeduaTable1.setContent(readBody)

            //depending on the phone screen size default table scale is 100
            //you can change it using this method
            //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

            //if you want a smaller table, change the padding setting
            mBinding.analisisKeduaTable1.setTablePadding(7);

            //to enable users to zoom in and out:
            mBinding.analisisKeduaTable1.setZoomEnabled(true)
            mBinding.analisisKeduaTable1.setShowZoomControls(true)

            //remember to build your table as the last step
            mBinding.analisisKeduaTable1.build()

            setAnalisisTableKe2(arrayItem, arrayZmod)
        })
    }

    private fun setAnalisisTableKe2(arrayItem: ArrayList<Array<String>>, arrayZmod: ArrayList<String>) {
        LegacyTableView.insertLegacyTitle(
                "Variasi air Ke-",
                "H1 (m)",
                "Q Pengukuran (m3/s)",
                "Q Pengukuran (I/s",
                "Q Bangunan (m3/s)",
                " Q Bangunan (I/s)",
                "APE", "E", "dE"
        )

        val newArrayItem: ArrayList<Array<String>> = ArrayList()
        val e: ArrayList<String> = ArrayList()
        var apeSum: Float = (0.0).toFloat()
        var apeSumNotZero: Float = (0.0).toFloat()
        var apeSumNotZeroCount: Int = 0
        var eSum: Float = (0.0).toFloat()
        for (i in arrayItem.indices) {
            val h1: String = arrayItem[i][0]
            val qPengukuran: String = arrayItem[i][1]
            val qPengukuran1000: String = arrayItem[i][2]
            val qTabel: String = if (String.format("%.1f", arrayZmod[i].toFloat()).toFloat() > (3.5).toFloat() ||
                    String.format("%.1f", arrayZmod[i].toFloat()).toFloat() < (-3.5).toFloat())
                "0" else arrayItem[i][3]
            val qTabel1000: String = if (qTabel == "0") "0" else arrayItem[i][3]
            val ape: String = if (qTabel == "0") "0.00" else String.format("%.2f",
                    abs(qPengukuran1000.toFloat() - qTabel1000.toFloat()) / qTabel1000.toFloat() * 100)
            val eValue: String = if (qTabel == "0") "0.00" else String.format("%.2f",
                    (qPengukuran.toFloat() - qTabel.toFloat()) / qTabel.toFloat() * 100)
            newArrayItem.add(arrayOf(h1, qPengukuran, qPengukuran1000, qTabel, qTabel1000, ape))
            apeSum += ape.toFloat()
            eSum += eValue.toFloat()
            e.add(eValue)

            if (qTabel != "0") {
                apeSumNotZeroCount += 1
                apeSumNotZero += ape.toFloat()
            }
        }

        val dE: ArrayList<String> = ArrayList()
        for (i in newArrayItem.indices) {
            val deValue = if (newArrayItem[i][3] == "0") "0.00" else String.format("%.2f",
                    (newArrayItem[i][3].toFloat() - eSum).pow(2))
            dE.add(deValue)
        }

        for (i in newArrayItem.indices) {
            LegacyTableView.insertLegacyContent((i + 1).toString(), newArrayItem[i][0], newArrayItem[i][1],
                    newArrayItem[i][2], newArrayItem[i][3], newArrayItem[i][4], newArrayItem[i][5], e[i], dE[i])
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.analisisKeduaTable2.setTitle(readTitle)
        mBinding.analisisKeduaTable2.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.analisisKeduaTable2.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.analisisKeduaTable2.setZoomEnabled(true)
        mBinding.analisisKeduaTable2.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.analisisKeduaTable2.build()

        val mape = String.format("%.3f", (apeSumNotZero / apeSumNotZeroCount))
        mBinding.analisisKeduaResult.text = "Mape: $mape%"

        setAnalisisRegresi()
    }

    private fun setAnalisisRegresi() {
        for (i in pengambilanDataList.indices) {
            val h1: String = String.format("%.3f", pengambilanDataList[i].h1)
            val qPengukuran: String = String.format("%.3f", pengambilanDataList[i].qPengukuran)
            val h1CarretnQPengukuran = String.format("%.3f",
                    h1.toFloat().pow(baseData.n!!.toFloat()) * qPengukuran.toFloat())
            val h1N2 = String.format("%.3f", h1.toFloat().pow(baseData.n!!.toFloat() * 2))
        }
    }


    private fun setViewModel() {
        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            pengambilanDataList = it
            setAnalisisBangunanTable1(it)
        })
    }
}