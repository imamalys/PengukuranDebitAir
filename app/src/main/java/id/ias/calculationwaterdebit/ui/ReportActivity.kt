package id.ias.calculationwaterdebit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityReportBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import kotlin.math.pow

class ReportActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    private lateinit var mBinding: ActivityReportBinding

    private val baseDataViewModel: BaseDataViewModel by viewModels {
        BaseDataViewModelFactory((application as Application).baseDataRepository)
    }
    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0
    lateinit var baseData: BaseDataModel
    var detailBangunan: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                detailBangunan = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
                idBaseData = it.getLongExtra("id_base_data", 0)
            }
        }

        loading.show(this)
        setViewModel()
    }

    private fun setView() {
        mBinding.tvTitle.text = "Hasil Kalibrasi Bangunan Ukur Debit Di Daerah Irigasi ${baseData.namaDaerahIrigasi}"
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
        mBinding.tvLengkungDebit.text = "Lengkung Debit Bangunan Ukur Debit Daerah Irigasi ${baseData.namaDaerahIrigasi}"

        setAnalisisResultTabel1()
    }

    private fun setAnalisisResultTabel1() {
        LegacyTableView.insertLegacyTitle("H1(m)", "Q(m3/s)", "H1(m)", "Q(m3/s)", "H1(m)", "Q(m3/s)", "H1(m)", "Q(m3/s)")

        var first = "1.01"
        var second = "1.25"
        var third = "1.50"
        var fourth = "1.75"

        val arrayFirst: ArrayList<Array<String>> = ArrayList()
        val arraySecond: ArrayList<Array<String>> = ArrayList()
        val arrayThird: ArrayList<Array<String>> = ArrayList()
        val arrayFourth: ArrayList<Array<String>> = ArrayList()
        for (i in 1..25) {
            first = String.format("%.2f", first.toFloat() + 0.01.toFloat())
            second = String.format("%.2f", second.toFloat() + 0.01.toFloat())
            third = String.format("%.2f", third.toFloat() + 0.01.toFloat())
            fourth = String.format("%.2f", fourth.toFloat() + 0.01.toFloat())

            val qFirst: String = String.format("%.3f", baseData.k!!.toFloat() * first.toFloat().pow(baseData.n!!.toFloat()))
            val qSecond: String = String.format("%.3f", baseData.k!!.toFloat() * second.toFloat().pow(baseData.n!!.toFloat()))
            val qThird: String = String.format("%.3f", baseData.k!!.toFloat() * third.toFloat().pow(baseData.n!!.toFloat()))
            val qFourth: String = String.format("%.3f", baseData.k!!.toFloat() * fourth.toFloat().pow(baseData.n!!.toFloat()))

            arrayFirst.add(arrayOf(first, qFirst))
            arraySecond.add(arrayOf(second, qSecond))
            arrayThird.add(arrayOf(third, qThird))
            arrayFourth.add(arrayOf(fourth, qFourth))

            LegacyTableView.insertLegacyContent(first, qFirst, second, qSecond, third, qThird, fourth, qFourth)
        }

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
        LegacyTableView.insertLegacyTitle("H1(m)", "Q(m3/s)", "H1(m)", "Q(m3/s)", "H1(m)", "Q(m3/s)", "H1(m)", "Q(m3/s)")

        var first = "1.01"
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

            val qFirst: String = String.format("%.3f", baseData.k!!.toFloat() * first.toFloat().pow(baseData.n!!.toFloat()))
            val qSecond: String = String.format("%.3f", baseData.k!!.toFloat() * second.toFloat().pow(baseData.n!!.toFloat()))
            val qThird: String = String.format("%.3f", baseData.k!!.toFloat() * third.toFloat().pow(baseData.n!!.toFloat()))
            val qFourth: String = String.format("%.3f", baseData.k!!.toFloat() * fourth.toFloat().pow(baseData.n!!.toFloat()))

            arrayFirst.add(arrayOf(first, qFirst))
            arraySecond.add(arrayOf(second, qSecond))
            arrayThird.add(arrayOf(third, qThird))
            arrayFourth.add(arrayOf(fourth, qFourth))

            LegacyTableView.insertLegacyContent(first, qFirst, second, qSecond, third, qThird, fourth, qFourth)
        }

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

        loading.dialog.dismiss()
    }

    private fun setViewModel() {
        baseDataViewModel.getBaseDataById(idBaseData.toInt())
        baseDataViewModel.baseDataById.observe(this, {
            baseData = it
            setView()
        })
    }
}