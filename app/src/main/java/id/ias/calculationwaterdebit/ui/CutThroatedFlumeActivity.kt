package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.CutThroatedFlumeModel
import id.ias.calculationwaterdebit.database.model.KoefisiensiCutThroatedFlumeModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.databinding.ActivityCutThroatedFlumeBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

class CutThroatedFlumeActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    private lateinit var mBinding: ActivityCutThroatedFlumeBinding

    private val ctfViewModel: CutThroaedFlumeViewModel by viewModels {
        CutThroaedFlumeViewModelFactory((application as Application).cutThroatedFlumeRepository)
    }

    private val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory((application as Application).pengambilanDataRepository)
    }

    private val koefisiensiCutThroaedFlumeViewModel: KoefisiensiCutThroatedFlumeViewModel by viewModels {
        KoefisiensiCutThroatedFlumeViewModelFactory((application as Application).koefisiensiCutThroatedFlumeRepository)
    }

    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0
    var detailBangunan: String = ""
    var pengambilanData: List<PengambilanDataModel> = ArrayList()
    var ctfData: CutThroatedFlumeModel = CutThroatedFlumeModel(null, 0, (0.0).toFloat(), (0.0).toFloat())
    var c: ArrayList<String> = ArrayList()
    var isInit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                detailBangunan = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
                idBaseData = it.getLongExtra("id_base_data", 0)
            }
        }

        mBinding = ActivityCutThroatedFlumeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        loading.show(this)
        setViewModel()
        setAction()

        ctfViewModel.getCtfDataById(idTipeBangunan.toInt())
    }

    private fun setVariable() {
        if (ctfData.id != null) {
            //set table title labels
            LegacyTableView.insertLegacyTitle("W", "L")
            LegacyTableView.insertLegacyContent(
                String.format(Locale.ENGLISH, "%.3f", ctfData.w),
                String.format(Locale.ENGLISH,"%.3f", ctfData.l))

            val readTitle = LegacyTableView.readLegacyTitle()
            val readBody = LegacyTableView.readLegacyContent()
            mBinding.ctfTable.setTitle(readTitle)
            mBinding.ctfTable.setContent(readBody)

            //depending on the phone screen size default table scale is 100
            //you can change it using this method
            //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

            //if you want a smaller table, change the padding setting
            mBinding.ctfTable.setTablePadding(7);

            //to enable users to zoom in and out:
            mBinding.ctfTable.setZoomEnabled(true)
            mBinding.ctfTable.setShowZoomControls(true)

            //remember to build your table as the last step
            mBinding.ctfTable.build()

            setTentukan()
        }
    }

    private fun setTentukan() {
        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            if (isInit) {
                isInit = false
                pengambilanData = it
                LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-", "L", "K", "W", "C")

                val koefisiensi = koefisiensiCutThroaedFlumeViewModel.getKoefisiensiCutThroatedFlumeViewModelById(ctfData.l)

                val l = String.format(Locale.ENGLISH, "%.3f", ctfData.l)
                var kValue = "0.0"
                var nValue = "0.0"
                var sTValue = "0.0"
                if (koefisiensi != null) {
                    kValue = koefisiensi.k.toString()
                    nValue = koefisiensi.n.toString()
                    sTValue = koefisiensi.sT.toString()
                }
                for (i in it.indices) {
                    LegacyTableView.insertLegacyContent((i + 1).toString(), l, kValue, nValue, sTValue)
                }

                val readTitle = LegacyTableView.readLegacyTitle()
                val readBody = LegacyTableView.readLegacyContent()
                mBinding.ctfTentukan.setTitle(readTitle)
                mBinding.ctfTentukan.setContent(readBody)

                //depending on the phone screen size default table scale is 100
                //you can change it using this method
                //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

                //if you want a smaller table, change the padding setting
                mBinding.ctfTentukan.setTablePadding(7);

                //to enable users to zoom in and out:
                mBinding.ctfTentukan.setZoomEnabled(true)
                mBinding.ctfTentukan.setShowZoomControls(true)

                //remember to build your table as the last step
                mBinding.ctfTentukan.build()

                setC(koefisiensi)
            }
        })
    }

    private fun setC(koefisiensi: KoefisiensiCutThroatedFlumeModel) {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-", "K", "W", "C")
        val k = if (koefisiensi == null) 0.toFloat() else koefisiensi.k
        val w = ctfData.w
        for (i in pengambilanData.indices) {
            val cValue = k * w.pow(1.205.toFloat())
            c.add(String.format(Locale.ENGLISH, "%.5f", cValue))
            LegacyTableView.insertLegacyContent((i + 1).toString(),
                String.format(Locale.ENGLISH, "%.3f", k), String.format(Locale.ENGLISH, "%.3f", w),
                String.format(Locale.ENGLISH, "%.5f", cValue))
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.ctfC.setTitle(readTitle)
        mBinding.ctfC.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.ctfC.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.ctfC.setZoomEnabled(true)
        mBinding.ctfC.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.ctfC.build()

        setDebit(koefisiensi)
    }

    private fun setDebit(koefisiensi: KoefisiensiCutThroatedFlumeModel) {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","C", "Ha", "n", "Q")

        val n = if (koefisiensi == null) 0.toFloat() else koefisiensi.n

        for (i in pengambilanData.indices) {
            val cValue = c[i]
            val h1 = String.format(Locale.ENGLISH, "%.3f",pengambilanData[i].h1)
            val q = cValue.toFloat() * h1.toFloat().pow(n)

            pengambilanDataViewModel.update(pengambilanData[i].id!!, q)

            LegacyTableView.insertLegacyContent((i + 1).toString(), cValue, h1, n.toString(),
                String.format(Locale.ENGLISH, "%.3f", q))
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.ctfDebit.setTitle(readTitle)
        mBinding.ctfDebit.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.ctfDebit.setTablePadding(7)

        //to enable users to zoom in and out:
        mBinding.ctfDebit.setZoomEnabled(true)
        mBinding.ctfDebit.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.ctfDebit.build()
        loading.dialog.dismiss()
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            val alps = Intent(this@CutThroatedFlumeActivity, AnalisisActivity::class.java)
            alps.putExtra("id_tipe_bangunan", idTipeBangunan)
            alps.putExtra("tipe_bangunan", detailBangunan)
            alps.putExtra("id_base_data", idBaseData)
            alps.putExtra("b", String.format(Locale.ENGLISH, "%.3f", ctfData.w))
            startActivity(alps)
            finish()
        }
    }

    private fun setViewModel() {
        ctfViewModel.ctfById.observe(this, {
            if (it.id != null) {
                ctfData = it
                setVariable()
            }
        })
    }
}