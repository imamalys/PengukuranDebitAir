package id.ias.calculationwaterdebit.ui

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
import com.gkemon.XMLtoPDF.PdfGenerator
import com.gkemon.XMLtoPDF.PdfGeneratorListener
import com.gkemon.XMLtoPDF.model.FailureResponse
import com.gkemon.XMLtoPDF.model.SuccessResponse
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityReportBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import kotlin.math.pow


class ReportDetailActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    private lateinit var mBinding: ActivityReportBinding

    private val baseDataViewModel: BaseDataViewModel by viewModels {
        BaseDataViewModelFactory((application as Application).baseDataRepository)
    }

    var idBaseData: Long = 0
    lateinit var baseData: BaseDataModel
    var grafikData: ArrayList<Array<String>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            idBaseData = it.getLongExtra("id_base_data", 0)
        }

        loading.show(this)
        setViewModel()
    }

    private fun setView() {
        mBinding.tvTitle.text = "Hasil Kalibrasi Bangunan Ukur Debit\nDi Daerah Irigasi ${baseData.namaDaerahIrigasi}"
        mBinding.tvNamaBangunanValue.text = "Daerah Irigasi ${baseData.namaDaerahIrigasi}"
        mBinding.tvTanggalKalibrasi.text = baseData.tanggal
        mBinding.tvDetail.text = "a) Acuan Kalibrasi: Pengukuran Debit menggunakan Current Meter\n" +
                "b) Rentang Tinggi di Ambang/Pengontrol: ${baseData.minH2} - ${baseData.maxH2} meter\n" +
                "c) Rentang Debit Kalibrasi: ${baseData.minDebitSaluran} - ${baseData.maxDebitSaluran} m3/s\n" +
                "d) Deviasi/MAPE: ${baseData.mape}%"
        mBinding.tvDetailRumus1.text = ": ${baseData.k} x H1"
        mBinding.tvDetailRumusN1.text = baseData.n
        mBinding.tvDetailRumus2.text = ": ${baseData.c} x B x H1"
        mBinding.tvDetailRumusN2.text = baseData.n
        mBinding.tvTabelDebit.text = "Tabel Debit Bangunan Ukur di Daerah Irigasi ${baseData.namaDaerahIrigasi}"
        mBinding.tvKeterangan.text = baseData.keterangan
        mBinding.tvNilaiResult.text = if (baseData.nilaiKeterangan >= 5)
            "SIBIMA Tidak Merekomendasikan Melakukan Kalibrasi Berdasarkan Keterangan Di Atas" else
                "SIBIMA Merekomendasikan Melakukan Kalibrasi Berdasarkan Keterangan Di Atas"

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
            first = String.format("%.2f", first.toFloat() + 0.01.toFloat())
            second = String.format("%.2f", second.toFloat() + 0.01.toFloat())
            third = String.format("%.2f", third.toFloat() + 0.01.toFloat())
            fourth = String.format("%.2f", fourth.toFloat() + 0.01.toFloat())

            val qFirst: String = String.format(
                "%.3f", baseData.k!!.toFloat() * first.toFloat().pow(
                    baseData.n!!.toFloat()
                )
            )
            val qSecond: String = String.format(
                "%.3f", baseData.k!!.toFloat() * second.toFloat().pow(
                    baseData.n!!.toFloat()
                )
            )
            val qThird: String = String.format(
                "%.3f", baseData.k!!.toFloat() * third.toFloat().pow(
                    baseData.n!!.toFloat()
                )
            )
            val qFourth: String = String.format(
                "%.3f", baseData.k!!.toFloat() * fourth.toFloat().pow(
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
        mBinding.qTable1.setTitle(readTitle)
        mBinding.qTable1.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.qTable1.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.qTable1.setZoomEnabled(true)
        mBinding.qTable1.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.qTable1.build()

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
            first = String.format("%.2f", first.toFloat() + 0.01.toFloat())
            second = String.format("%.2f", second.toFloat() + 0.01.toFloat())
            third = String.format("%.2f", third.toFloat() + 0.01.toFloat())
            fourth = String.format("%.2f", fourth.toFloat() + 0.01.toFloat())

            val qFirst: String = String.format(
                "%.3f", baseData.k!!.toFloat() * first.toFloat().pow(
                    baseData.n!!.toFloat()
                )
            )
            val qSecond: String = String.format(
                "%.3f", baseData.k!!.toFloat() * second.toFloat().pow(
                    baseData.n!!.toFloat()
                )
            )
            val qThird: String = String.format(
                "%.3f", baseData.k!!.toFloat() * third.toFloat().pow(
                    baseData.n!!.toFloat()
                )
            )
            val qFourth: String = String.format(
                "%.3f", baseData.k!!.toFloat() * fourth.toFloat().pow(
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
        mBinding.qTable2.setTitle(readTitle)
        mBinding.qTable2.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.qTable2.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.qTable2.setZoomEnabled(true)
        mBinding.qTable2.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.qTable2.build()

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

        cartesian.title("Lengkung Debit Bangunan Ukur di Daerah Irigasi ${baseData.namaDaerahIrigasi}")

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

        loading.dialog.dismiss()

        mBinding.btnNext.setOnClickListener {
            mBinding.btnNext.visibility = android.view.View.GONE
            PdfGenerator.getBuilder()
                .setContext(this@ReportDetailActivity)
                .fromViewSource()
                .fromView(mBinding.root)
                .setFileName("Report")
                .setFolderName("Debit")
                .openPDFafterGeneration(true)
                .build(object : PdfGeneratorListener() {
                    override fun onFailure(failureResponse: FailureResponse) {
                        super.onFailure(failureResponse)
                    }

                    override fun showLog(log: String) {
                        super.showLog(log)
                    }

                    override fun onStartPDFGeneration() {
                        /*When PDF generation begins to start*/
                    }

                    override fun onFinishPDFGeneration() {
                        /*When PDF generation is finished*/
                    }

                    override fun onSuccess(response: SuccessResponse) {
                        super.onSuccess(response)
                    }
                })
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.btnNext.visibility = android.view.View.VISIBLE
    }
    private fun setViewModel() {
        baseDataViewModel.getBaseDataById(idBaseData.toInt())
        baseDataViewModel.baseDataById.observe(this, {
            baseData = it
            setView()
        })
    }
}