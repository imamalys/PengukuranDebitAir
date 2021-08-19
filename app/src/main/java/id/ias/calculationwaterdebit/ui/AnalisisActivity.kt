package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Line
import com.anychart.data.Mapping
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModelFactory
import id.ias.calculationwaterdebit.database.viewmodel.PengambilanDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.PengambilanDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityAnalisisBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow


class AnalisisActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
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
    var kRegresi: String = ""
    var mapeBangunan: String = ""
    var mapeTabel: String = ""
    var mapeRegresi: String = ""
    var kUse: String = ""
    var cUse: String = ""
    var mapeTerkecil: String = ""
    private lateinit var baseData: BaseDataModel
    var grafikData: ArrayList<Array<String>> = ArrayList()

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

        loading.show(this)
        setViewModel()
        setAction()
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            loading.show(this)
            kUse = kUse.replace(",", ".")
            cUse = cUse.replace(",", ".")
            mapeTerkecil = mapeTerkecil.replace(",", ".")
            baseDataViewModel.updateAnalisis(idBaseData.toInt(), kUse, cUse, mapeTerkecil)
        }
    }

    private fun setAnalisisBangunanTable1(it: List<PengambilanDataModel>) {
        LegacyTableView.insertLegacyTitle(
            "Variasi air Ke-",
            "H1 (m)",
            "Q Pengukuran (m3/s)",
            "Q Pengukuran (I/s",
            "Q Bangunan (m3/s)",
            "Q Bangunan (I/s)",
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
        val arrayPercentageResidual = FloatArray(it.size)
        for (i in it.indices) {
            val h1: String = String.format(Locale.ENGLISH,"%.3f", it[i].h1)
            val qPengukuran: String = String.format(Locale.ENGLISH,"%.3f", it[i].qPengukuran)
            val qPengukuran1000: String = String.format(Locale.ENGLISH,"%.3f", it[i].qPengukuran!! * 1000)
            val qBangunan: String = if (it[i].qBangunan == null) "0" else String.format(Locale.ENGLISH,"%.3f", it[i].qBangunan)
            val qBangunan1000: String = if (it[i].qBangunan== null) "0" else String.format(Locale.ENGLISH,"%.3f", (it[i].qBangunan!! * 1000))
            val logQPengukuran: String = String.format(Locale.ENGLISH,"%.3f", log10(qPengukuran1000.toFloat()))
            var logQBangunan: String = String.format(Locale.ENGLISH,"%.3f", log10(qBangunan1000.toFloat()))
            if (logQBangunan.contains("infinity", true)) {
                logQBangunan = "0.0"
            }
            val residual: String = String.format(
                Locale.ENGLISH,"%.3f",
                logQPengukuran.toFloat() - logQBangunan.toFloat()
            )
            val percentageResidual: Float = residual.toFloat() / logQPengukuran.toFloat()

            arrayItem.add(
                arrayOf(
                    h1, qPengukuran, qPengukuran1000, qBangunan, qBangunan1000, logQPengukuran,
                    logQBangunan, residual
                )
            )

            arrayPercentageResidual[i] = percentageResidual
        }

        Arrays.sort(arrayPercentageResidual)
        val median: String = if (arrayPercentageResidual.size %2 == 0)
            String.format(
                Locale.ENGLISH,"%.4f",
                arrayPercentageResidual[arrayPercentageResidual.size / 2] +
                        arrayPercentageResidual[arrayPercentageResidual.size / 2 - 1] / 2
            )
        else String.format(
            Locale.ENGLISH,"%.4f",
                arrayPercentageResidual[arrayPercentageResidual.size / 2]
        )

        val arrayAbs = FloatArray(arrayPercentageResidual.size)
        for (i in arrayPercentageResidual.indices) {
            val abs = abs(arrayPercentageResidual[i] - median.toFloat())
            arrayAbs[i] = abs
        }

        Arrays.sort(arrayAbs)
        val mad: String = if (arrayAbs.size %2 == 0)
            String.format(
                Locale.ENGLISH,"%.6f",
                (arrayAbs[arrayAbs.size / 2]) + (arrayAbs[arrayAbs.size / 2 - 1] / 2.0)
            ) else String.format(
            Locale.ENGLISH,"%.6f",
            (arrayAbs[arrayAbs.size / 2])
        )

        val arrayZmod: ArrayList<String> = ArrayList()

        for (i in arrayAbs.indices) {
            val zmod = String.format(
                Locale.ENGLISH,"%.3f",
                0.6745.toFloat() * arrayPercentageResidual[i] / mad.toFloat()
            )
            arrayZmod.add(zmod)
        }

        for (i in it.indices) {
            LegacyTableView.insertLegacyContent(
                (i + 1).toString(),
                arrayItem[i][0],
                arrayItem[i][1],
                arrayItem[i][2],
                arrayItem[i][3],
                arrayItem[i][4],
                arrayItem[i][5],
                arrayItem[i][6],
                arrayItem[i][7],
                String.format(Locale.ENGLISH,"%.4f", arrayPercentageResidual[i]),
                median,
                String.format(Locale.ENGLISH,"%.6f", arrayAbs[i]),
                mad,
                arrayZmod[i]
            )
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

    private fun setAnalisisBangunanTable2(
        arrayItem: ArrayList<Array<String>>,
        arrayZmod: ArrayList<String>
    ) {
        LegacyTableView.insertLegacyTitle(
            "Variasi air Ke-",
            "H1 (m)",
            "Q Pengukuran (m3/s)",
            "Q Pengukuran (I/s",
            "Q Bangunan (m3/s)",
            "Q Bangunan (I/s)",
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
            val qBangunan: String = if (String.format(Locale.ENGLISH,"%.1f", arrayZmod[i].toFloat()).toFloat() > (3.5).toFloat() ||
                    String.format(Locale.ENGLISH,"%.1f", arrayZmod[i].toFloat()).toFloat() < (-3.5).toFloat())
                        "0" else arrayItem[i][3]
            val qBangunan1000: String = if (qBangunan == "0") "0" else arrayItem[i][4]
            val ape: String = if (qBangunan == "0") "0.00" else String.format(
                Locale.ENGLISH,"%.2f",
                abs(qPengukuran1000.toFloat() - qBangunan1000.toFloat()) / qBangunan1000.toFloat() * 100
            )
            val eValue: String = if (qBangunan == "0") "0.00" else String.format(
                Locale.ENGLISH,"%.2f",
                (qPengukuran.toFloat() - qBangunan.toFloat()) / qBangunan.toFloat() * 100
            )
            newArrayItem.add(
                arrayOf(
                    h1,
                    qPengukuran,
                    qPengukuran1000,
                    qBangunan,
                    qBangunan1000,
                    ape
                )
            )
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
            val deValue = if (newArrayItem[i][3] == "0") "0.00" else String.format(
                Locale.ENGLISH,"%.2f",
                (newArrayItem[i][3].toFloat() - eSum).pow(2)
            )
            dE.add(deValue)
        }

        for (i in newArrayItem.indices) {
            LegacyTableView.insertLegacyContent(
                (i + 1).toString(),
                newArrayItem[i][0],
                newArrayItem[i][1],
                newArrayItem[i][2],
                newArrayItem[i][3],
                newArrayItem[i][4],
                newArrayItem[i][5],
                e[i],
                dE[i]
            )
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

        mapeBangunan = String.format(Locale.ENGLISH,"%.3f", (apeSumNotZero / apeSumNotZeroCount))
        mBinding.analisisPertamaResult.text = "Mape: $mapeBangunan%"

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
                "Q Tabel (I/s)",
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
            val arrayPercentageResidual = FloatArray(pengambilanDataList.size)
            for (i in pengambilanDataList.indices) {
                val h1: String = String.format(Locale.ENGLISH,"%.3f", pengambilanDataList[i].h1)
                val qPengukuran: String = String.format(Locale.ENGLISH,"%.3f", pengambilanDataList[i].qPengukuran)
                val qPengukuran1000: String = String.format(
                    Locale.ENGLISH,"%.3f",
                    pengambilanDataList[i].qPengukuran!! * 1000
                )
                val qTabel: String = String.format(
                    Locale.ENGLISH, "%.3f", (baseData.variablePertama!!.toFloat() * b.toFloat() * h1.toFloat()).pow(
                        baseData.n!!.toFloat()
                    )
                )
                val qTabel1000: String = String.format(
                    Locale.ENGLISH,"%.3f", (
                            baseData.variablePertama!!.toFloat() * b.toFloat() * h1.toFloat()).pow(
                        baseData.n!!.toFloat()
                    ) * 1000
                )
                val logQPengukuran: String = String.format(Locale.ENGLISH,"%.3f", log10(qPengukuran1000.toFloat()))
                val logQBangunan: String = String.format(Locale.ENGLISH,"%.3f", log10(qTabel1000.toFloat()))
                val residual: String = String.format(
                    Locale.ENGLISH, "%.3f",
                    logQPengukuran.toFloat() - logQBangunan.toFloat()
                )
                val percentageResidual: Float = residual.toFloat() / logQPengukuran.toFloat()

                arrayItem.add(
                    arrayOf(
                        h1, qPengukuran, qPengukuran1000, qTabel, qTabel1000, logQPengukuran,
                        logQBangunan, residual
                    )
                )

                arrayPercentageResidual[i] = percentageResidual
            }

            Arrays.sort(arrayPercentageResidual)
            val median: String = if (arrayPercentageResidual.size % 2 == 0)
                String.format(
                    Locale.ENGLISH,"%.4f",
                    arrayPercentageResidual[arrayPercentageResidual.size / 2] +
                            arrayPercentageResidual[arrayPercentageResidual.size / 2 - 1] / 2
                )
            else String.format(
                Locale.ENGLISH,"%.4f",
                    arrayPercentageResidual[arrayPercentageResidual.size / 2]
            )

            val arrayAbs = FloatArray(arrayPercentageResidual.size)
            for (i in arrayPercentageResidual.indices) {
                val abs = arrayPercentageResidual[i] - median.toFloat()
                arrayAbs[i] = abs
            }

            Arrays.sort(arrayAbs)
            val mad: String = if (arrayAbs.size % 2 == 0)
                String.format(
                    Locale.ENGLISH,  "%.6f",
                    arrayAbs[arrayAbs.size / 2] + arrayAbs[arrayAbs.size / 2 - 1] / 2
                )
            else String.format(
                Locale.ENGLISH, "%.6f",
                    arrayAbs[arrayAbs.size / 2]
            )

            val arrayZmod: ArrayList<String> = ArrayList()

            for (i in arrayAbs.indices) {
                val zmod = String.format(
                    Locale.ENGLISH,   "%.3f",
                    0.6745.toFloat() * arrayPercentageResidual[i].toFloat() / mad.toFloat()
                )
                arrayZmod.add(zmod)
            }

            for (i in pengambilanDataList.indices) {
                LegacyTableView.insertLegacyContent(
                    (i + 1).toString(),
                    arrayItem[i][0],
                    arrayItem[i][1],
                    arrayItem[i][2],
                    arrayItem[i][3],
                    arrayItem[i][4],
                    arrayItem[i][5],
                    arrayItem[i][6],
                    arrayItem[i][7],
                    String.format(Locale.ENGLISH, "%.4f", arrayPercentageResidual[i]),
                    median,
                    String.format(Locale.ENGLISH, "%.6f", arrayAbs[i]),
                    mad,
                    arrayZmod[i]
                )
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

    private fun setAnalisisTableKe2(
        arrayItem: ArrayList<Array<String>>,
        arrayZmod: ArrayList<String>
    ) {
        LegacyTableView.insertLegacyTitle(
            "Variasi air Ke-",
            "H1 (m)",
            "Q Pengukuran (m3/s)",
            "Q Pengukuran (I/s",
            "Q Tabel (m3/s)",
            " Q Tabel (I/s)",
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
            val qTabel: String = if (String.format(Locale.ENGLISH,"%.1f", arrayZmod[i].toFloat()).toFloat() > (3.5).toFloat() ||
                    String.format(Locale.ENGLISH,"%.1f", arrayZmod[i].toFloat()).toFloat() < (-3.5).toFloat())
                "0" else arrayItem[i][3]
            val qTabel1000: String = if (qTabel == "0") "0" else arrayItem[i][4]
            val ape: String = if (qTabel == "0") "0.00" else String.format(
                Locale.ENGLISH, "%.2f",
                abs(qPengukuran1000.toFloat() - qTabel1000.toFloat()) / qTabel1000.toFloat() * 100
            )
            val eValue: String = if (qTabel == "0") "0.00" else String.format(
                Locale.ENGLISH, "%.2f",
                (qPengukuran.toFloat() - qTabel.toFloat()) / qTabel.toFloat() * 100
            )
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
            val deValue = if (newArrayItem[i][3] == "0") "0.00" else String.format(
                Locale.ENGLISH,  "%.2f",
                (newArrayItem[i][3].toFloat() - eSum).pow(2)
            )
            dE.add(deValue)
        }

        for (i in newArrayItem.indices) {
            LegacyTableView.insertLegacyContent(
                (i + 1).toString(),
                newArrayItem[i][0],
                newArrayItem[i][1],
                newArrayItem[i][2],
                newArrayItem[i][3],
                newArrayItem[i][4],
                newArrayItem[i][5],
                e[i],
                dE[i]
            )
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

        mapeTabel = String.format(Locale.ENGLISH,"%.3f", (apeSumNotZero / apeSumNotZeroCount))
        mBinding.analisisKeduaResult.text = "Mape: $mapeTabel%"

        setAnalisisRegresi()
    }

    private fun setAnalisisRegresi() {
        LegacyTableView.insertLegacyTitle(
            "Variasi air Ke-",
            "H1 (m)",
            "Q Pengukuran (m3/s)",
            "(H1^n)*Q Pengukuran",
            "H1^(n*2)",
            "Q Regresi Pengukuran"
        )

        var sumH1CarretnQPengukuran:Float = (0.0).toFloat()
        var sumH1N2: Float = (0.0).toFloat()

        val arrayItem: ArrayList<Array<String>> = ArrayList()
        for (i in pengambilanDataList.indices) {
            val h1: String = String.format(Locale.ENGLISH,"%.3f", pengambilanDataList[i].h1)
            val qPengukuran: String = String.format(Locale.ENGLISH,"%.3f", pengambilanDataList[i].qPengukuran)
            val h1CarretnQPengukuran = String.format(
                Locale.ENGLISH, "%.3f",
                h1.toFloat().pow(baseData.n!!.toFloat()) * qPengukuran.toFloat()
            )
            val h1N2 = String.format(Locale.ENGLISH,"%.3f", h1.toFloat().pow(baseData.n!!.toFloat() * 2))
            sumH1CarretnQPengukuran += h1CarretnQPengukuran.toFloat()
            sumH1N2 += h1N2.toFloat()
            arrayItem.add(arrayOf(h1, qPengukuran, h1CarretnQPengukuran, h1N2))
        }

        kRegresi = String.format(Locale.ENGLISH,"%.2f", sumH1CarretnQPengukuran / sumH1N2)
        val c = String.format(Locale.ENGLISH,"%.2f", kRegresi.toFloat() / b.toFloat())

        val qRegresiPengukuran: ArrayList<Float> = ArrayList()
        for (i in arrayItem.indices) {
            val qRegresiPengukuranValue: Float = c.toFloat() * b.toFloat() *
                    arrayItem[i][0].toFloat().pow(baseData.n!!.toFloat())
            qRegresiPengukuran.add(qRegresiPengukuranValue)

            LegacyTableView.insertLegacyContent(
                (i + 1).toString(), arrayItem[i][0], arrayItem[i][1],
                arrayItem[i][2], arrayItem[i][3], String.format(Locale.ENGLISH,"%.3f", qRegresiPengukuranValue)
            )
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.analisisKetigaRegresi.setTitle(readTitle)
        mBinding.analisisKetigaRegresi.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.analisisKetigaRegresi.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.analisisKetigaRegresi.setZoomEnabled(true)
        mBinding.analisisKetigaRegresi.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.analisisKetigaRegresi.build()

        mBinding.analisisRegresiResult.text = "Jumlah (H1^n)*Q Pengukuran: $sumH1CarretnQPengukuran\n" +
                "Jumlah H1^(n*2): $sumH1N2\nK: $kRegresi\nC: $c"

        setAnalisisRegresiTable1(qRegresiPengukuran)
    }

    private fun setAnalisisRegresiTable1(qRegresiPengukuran: ArrayList<Float> = ArrayList()) {
        LegacyTableView.insertLegacyTitle(
            "Variasi air Ke-",
            "H1 (m)",
            "Q Pengukuran (m3/s)",
            "Q Pengukuran (I/s",
            "Q Regresi (m3/s)",
            " Q Regresi (I/s)",
            "Log Q. Pengukuran",
            "Log Q. Regresi",
            "Residual",
            "%Rresidual",
            "Median E",
            "Abs(%Residual)",
            "MAD",
            "Zmod"
        )

        val arrayItem: ArrayList<Array<String>> = ArrayList()
        val arrayPercentageResidual = FloatArray(pengambilanDataList.size)
        for (i in pengambilanDataList.indices) {
            val h1: String = String.format(Locale.ENGLISH,"%.3f", pengambilanDataList[i].h1)
            val qPengukuran: String = String.format(Locale.ENGLISH,"%.3f", pengambilanDataList[i].qPengukuran)
            val qPengukuran1000: String = String.format(
                Locale.ENGLISH,"%.3f",
                pengambilanDataList[i].qPengukuran!! * 1000
            )
            val qRegresi: String = String.format(Locale.ENGLISH,"%.3f", qRegresiPengukuran[i])
            val qRegresi1000: String = String.format(Locale.ENGLISH,"%.3f", (qRegresiPengukuran[i] * 1000))
            val logQPengukuran: String = String.format(Locale.ENGLISH,"%.3f", log10(qPengukuran1000.toFloat()))
            val logQBangunan: String = String.format(Locale.ENGLISH,"%.3f", log10(qRegresi1000.toFloat()))
            val residual: String = String.format(
                Locale.ENGLISH,   "%.3f",
                logQPengukuran.toFloat() - logQBangunan.toFloat()
            )
            val percentageResidual: Float = residual.toFloat() / logQPengukuran.toFloat()

            arrayItem.add(
                arrayOf(
                    h1, qPengukuran, qPengukuran1000, qRegresi, qRegresi1000, logQPengukuran,
                    logQBangunan, residual
                )
            )

            arrayPercentageResidual[i] = percentageResidual
        }

        Arrays.sort(arrayPercentageResidual)
        val median: String = if (arrayPercentageResidual.size %2 == 0)
            String.format(
                Locale.ENGLISH,  "%.4f",
                arrayPercentageResidual[arrayPercentageResidual.size / 2].toFloat() +
                        arrayPercentageResidual[arrayPercentageResidual.size / 2 - 1].toFloat() / 2
            )
        else String.format(
            Locale.ENGLISH, "%.4f",
            arrayPercentageResidual[arrayPercentageResidual.size / 2].toFloat()
        )

        val arrayAbs = FloatArray(arrayPercentageResidual.size)
        for (i in arrayPercentageResidual.indices) {
            val abs: Float = arrayPercentageResidual[i] - median.toFloat()
            arrayAbs[i] = abs
        }

        Arrays.sort(arrayAbs)
        val mad: String = if (arrayAbs.size %2 == 0)
            String.format(
                Locale.ENGLISH,  "%.6f",
                arrayAbs[arrayAbs.size / 2] + arrayAbs[arrayAbs.size / 2 - 1] / 2
            )
        else String.format(
            Locale.ENGLISH,  "%.6f",
                arrayAbs[arrayAbs.size / 2]
        )

        val arrayZmod: ArrayList<String> = ArrayList()

        for (i in arrayAbs.indices) {
            val zmod = String.format(
                Locale.ENGLISH,  "%.3f",
                0.6745.toFloat() * arrayPercentageResidual[i].toFloat() / mad.toFloat()
            )
            arrayZmod.add(zmod)
        }

        for (i in pengambilanDataList.indices) {
            LegacyTableView.insertLegacyContent(
                (i + 1).toString(),
                arrayItem[i][0],
                arrayItem[i][1],
                arrayItem[i][2],
                arrayItem[i][3],
                arrayItem[i][4],
                arrayItem[i][5],
                arrayItem[i][6],
                arrayItem[i][7],
                String.format(Locale.ENGLISH,  "%.4f",arrayPercentageResidual[i]),
                median,
                String.format(Locale.ENGLISH,"%.6f", arrayAbs[i]),
                mad,
                arrayZmod[i]
            )
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.analisisKetigaTable1.setTitle(readTitle)
        mBinding.analisisKetigaTable1.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.analisisKetigaTable1.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.analisisKetigaTable1.setZoomEnabled(true)
        mBinding.analisisKetigaTable1.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.analisisKetigaTable1.build()

        setAnalisisRegresiTable2(arrayItem, arrayZmod)
    }

    private fun setAnalisisRegresiTable2(
        arrayItem: ArrayList<Array<String>>,
        arrayZmod: ArrayList<String>
    ) {
        LegacyTableView.insertLegacyTitle(
            "Variasi air Ke-",
            "H1 (m)",
            "Q Pengukuran (m3/s)",
            "Q Pengukuran (I/s",
            "Q Regresi (m3/s)",
            " Q Regresi (I/s)",
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
            val qRegresi: String = if (String.format(Locale.ENGLISH,"%.1f", arrayZmod[i].toFloat()).toFloat() > (3.5).toFloat() ||
                    String.format(Locale.ENGLISH,"%.1f", arrayZmod[i].toFloat()).toFloat() < (-3.5).toFloat())
                "0" else arrayItem[i][3]
            val qRegresi1000: String = if (qRegresi == "0") "0" else arrayItem[i][4]
            val ape: String = if (qRegresi == "0") "0.00" else String.format(
                Locale.ENGLISH,"%.2f",
                abs(qPengukuran1000.toFloat() - qRegresi1000.toFloat()) / qRegresi1000.toFloat() * 100
            )
            val eValue: String = if (qRegresi == "0") "0.00" else String.format(
                Locale.ENGLISH, "%.2f",
                (qPengukuran.toFloat() - qRegresi.toFloat()) / qRegresi.toFloat() * 100
            )
            newArrayItem.add(arrayOf(h1, qPengukuran, qPengukuran1000, qRegresi, qRegresi1000, ape))
            apeSum += ape.toFloat()
            eSum += eValue.toFloat()
            e.add(eValue)

            if (qRegresi != "0") {
                apeSumNotZeroCount += 1
                apeSumNotZero += ape.toFloat()
            }
        }

        val dE: ArrayList<String> = ArrayList()
        for (i in newArrayItem.indices) {
            val deValue = if (newArrayItem[i][3] == "0") "0.00" else String.format(
                Locale.ENGLISH, "%.2f",
                (newArrayItem[i][3].toFloat() - eSum).pow(2)
            )
            dE.add(deValue)
        }

        for (i in newArrayItem.indices) {
            LegacyTableView.insertLegacyContent(
                (i + 1).toString(),
                newArrayItem[i][0],
                newArrayItem[i][1],
                newArrayItem[i][2],
                newArrayItem[i][3],
                newArrayItem[i][4],
                newArrayItem[i][5],
                e[i],
                dE[i]
            )
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.analisisKetigaTable2.setTitle(readTitle)
        mBinding.analisisKetigaTable2.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.analisisKetigaTable2.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.analisisKetigaTable2.setZoomEnabled(true)
        mBinding.analisisKetigaTable2.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.analisisKetigaTable2.build()

        mapeRegresi = String.format(Locale.ENGLISH,"%.3f", (apeSumNotZero / apeSumNotZeroCount))
        mBinding.analisisKetigaResult.text = "Mape: $mapeRegresi%"

        setAnalisaResult()
    }

    private fun setAnalisaResult() {
        mapeTerkecil = if (mapeBangunan.toFloat() < mapeTabel.toFloat()) if
                (mapeBangunan.toFloat() < mapeRegresi.toFloat()) mapeBangunan else mapeRegresi else if
                        (mapeTabel.toFloat() < mapeRegresi.toFloat()) mapeTabel else mapeRegresi
        kUse = if (mapeTerkecil == mapeRegresi) kRegresi else baseData.variablePertama!!
        cUse = String.format(Locale.ENGLISH,"%.3f", kUse.toFloat() / b.toFloat())
        val n: String = baseData.n!!

        mBinding.analisisResult.text = "Yang digunakan\nMAPA TERKECIL: $mapeTerkecil%\nK: $kUse\nC: $cUse\nN: $n"

        setAnalisisResultTabel1()
    }

    private fun setAnalisisResultTabel1() {
        LegacyTableView.insertLegacyTitle(
            "H1(m)",
            "Q(m3/s)",
            "H1(m)",
            "Q(m3/s)",
            "H1(m)",
            "Q(m3/s)",
            "H1(m)",
            "Q(m3/s)"
        )

        var first = "0.00"
        var second = "0.25"
        var third = "0.50"
        var fourth = "0.75"

        val arrayFirst: ArrayList<Array<String>> = ArrayList()
        val arraySecond: ArrayList<Array<String>> = ArrayList()
        val arrayThird: ArrayList<Array<String>> = ArrayList()
        val arrayFourth: ArrayList<Array<String>> = ArrayList()
        for (i in 1..25) {
            first = String.format(Locale.ENGLISH,"%.2f", first.toFloat() + 0.01.toFloat())
            second = String.format(Locale.ENGLISH,"%.2f", second.toFloat() + 0.01.toFloat())
            third = String.format(Locale.ENGLISH,"%.2f", third.toFloat() + 0.01.toFloat())
            fourth = String.format(Locale.ENGLISH,"%.2f", fourth.toFloat() + 0.01.toFloat())

            val qFirst: String = String.format(
                Locale.ENGLISH,  "%.3f",
                kUse.toFloat() * first.toFloat().pow(baseData.n!!.toFloat())
            )
            val qSecond: String = String.format(
                Locale.ENGLISH, "%.3f", kUse.toFloat() * second.toFloat().pow(
                    baseData.n!!.toFloat()
                )
            )
            val qThird: String = String.format(
                Locale.ENGLISH,  "%.3f",
                kUse.toFloat() * third.toFloat().pow(baseData.n!!.toFloat())
            )
            val qFourth: String = String.format(
                Locale.ENGLISH,   "%.3f", kUse.toFloat() * fourth.toFloat().pow(
                    baseData.n!!.toFloat()
                )
            )

            arrayFirst.add(arrayOf(first, qFirst))
            arraySecond.add(arrayOf(second, qSecond))
            arrayThird.add(arrayOf(third, qThird))
            arrayFourth.add(arrayOf(fourth, qFourth))

            LegacyTableView.insertLegacyContent(
                first,
                qFirst,
                second,
                qSecond,
                third,
                qThird,
                fourth,
                qFourth
            )
        }

        grafikData.addAll(arrayFirst)
        grafikData.addAll(arraySecond)
        grafikData.addAll(arrayThird)
        grafikData.addAll(arrayFourth)

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.analisisResultTable1.setTitle(readTitle)
        mBinding.analisisResultTable1.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.analisisResultTable1.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.analisisResultTable1.setZoomEnabled(true)
        mBinding.analisisResultTable1.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.analisisResultTable1.build()

        setAnalisisResultTabel2()
    }

    private fun setAnalisisResultTabel2() {
        LegacyTableView.insertLegacyTitle(
            "H1(m)",
            "Q(m3/s)",
            "H1(m)",
            "Q(m3/s)",
            "H1(m)",
            "Q(m3/s)",
            "H1(m)",
            "Q(m3/s)"
        )

        var first = "1.00"
        var second = "1.25"
        var third = "1.50"
        var fourth = "1.75"
        fourth.toFloat()

        val arrayFirst: ArrayList<Array<String>> = ArrayList()
        val arraySecond: ArrayList<Array<String>> = ArrayList()
        val arrayThird: ArrayList<Array<String>> = ArrayList()
        val arrayFourth: ArrayList<Array<String>> = ArrayList()
        for (i in 1..25) {
            first = String.format(Locale.ENGLISH,"%.2f", first.toFloat() + 0.01.toFloat())
            second = String.format(Locale.ENGLISH,"%.2f", second.toFloat() + 0.01.toFloat())
            third = String.format(Locale.ENGLISH,"%.2f", third.toFloat() + 0.01.toFloat())
            fourth = String.format(Locale.ENGLISH,"%.2f", fourth.toFloat() + 0.01.toFloat())

            val qFirst: String = String.format(
                Locale.ENGLISH,  "%.3f",
                kUse.toFloat() * first.toFloat().pow(baseData.n!!.toFloat())
            )
            val qSecond: String = String.format(
                Locale.ENGLISH,  "%.3f", kUse.toFloat() * second.toFloat().pow(
                    baseData.n!!.toFloat()
                )
            )
            val qThird: String = String.format(
                Locale.ENGLISH, "%.3f",
                kUse.toFloat() * third.toFloat().pow(baseData.n!!.toFloat())
            )
            val qFourth: String = String.format(
                Locale.ENGLISH, "%.3f", kUse.toFloat() * fourth.toFloat().pow(
                    baseData.n!!.toFloat()
                )
            )

            arrayFirst.add(arrayOf(first, qFirst))
            arraySecond.add(arrayOf(second, qSecond))
            arrayThird.add(arrayOf(third, qThird))
            arrayFourth.add(arrayOf(fourth, qFourth))

            LegacyTableView.insertLegacyContent(
                first,
                qFirst,
                second,
                qSecond,
                third,
                qThird,
                fourth,
                qFourth
            )
        }

        grafikData.addAll(arrayFirst)
        grafikData.addAll(arraySecond)
        grafikData.addAll(arrayThird)
        grafikData.addAll(arrayFourth)

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.analisisResultTable2.setTitle(readTitle)
        mBinding.analisisResultTable2.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.analisisResultTable2.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.analisisResultTable2.setZoomEnabled(true)
        mBinding.analisisResultTable2.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.analisisResultTable2.build()

        loading.dialog.dismiss()

        setGrafik()
    }

    private fun setGrafik() {
        val cartesian: Cartesian = AnyChart.line()

        cartesian.animation(true)

        cartesian.padding(10.0, 20.0, 5.0, 20.0)

        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
            .yLabel(true) // TODO ystroke
            .yStroke(null as Stroke?, null, null, null as String?, null as String?)
            .xLabel(true)
            .xStroke(null as Stroke?, null, null, null as String?, null as String?)

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)

        cartesian.title("Lengkung Debit Bangunan Ukur")

        cartesian.yAxis(0).title("(m)")
        cartesian.xAxis(0).title("(m3/s)")
        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

        val seriesData: ArrayList<DataEntry> = ArrayList()
        for (i in grafikData.indices) {
            seriesData.add(ValueDataEntry(grafikData[i][1], grafikData[i][0].toFloat()))
        }

        val set: Set = Set.instantiate()
        set.data(seriesData)
        val series1Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value' }")

        val series1: Line = cartesian.line(series1Mapping)
        series1.name("")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series1.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)
        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)

        mBinding.chart.setChart(cartesian)
    }

    private fun setViewModel() {
        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            pengambilanDataList = it
            setAnalisisBangunanTable1(it)
        })

        baseDataViewModel.baseDataUpdate.observe(this, {
            if (it != 0) {
                loading.dialog.dismiss()
                val alps = Intent(this@AnalisisActivity, ReportDetailActivity::class.java)
                alps.putExtra("id_base_data", idBaseData)
                startActivity(alps)
                finish()
            }
        })
    }
}