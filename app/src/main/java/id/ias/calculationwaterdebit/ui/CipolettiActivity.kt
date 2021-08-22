package id.ias.calculationwaterdebit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.CipolettiModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.databinding.ActivityCipolettiBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.sqrt

class CipolettiActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    private lateinit var mBinding: ActivityCipolettiBinding

    private val cipolettiViewModel: CipolettiViewModel by viewModels {
        CipolettiViewModelFactory((application as Application).cipolettiRepository)
    }

    private val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory((application as Application).pengambilanDataRepository)
    }

    private val koefisiensiAmbangLebarViewModel: KoefisiensiAmbangLebarViewModel by viewModels {
        KoefisiensiAmbangLebarViewModelFactory((application as Application).koefisiensiAmbangLebarRepository)
    }

    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0
    var detailBangunan: String = ""
    var cipolettiModel: CipolettiModel = CipolettiModel(null, 0, (0.0).toFloat(),
        (0.0).toFloat(), (0.0).toFloat(), (0.0).toFloat(), (0.0).toFloat())
    var cd: ArrayList<String> = ArrayList()
    var cv: ArrayList<String> = ArrayList()
    var pengambilanData: List<PengambilanDataModel> = ArrayList()
    var isInit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityCipolettiBinding.inflate(layoutInflater)
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
        setAction()

        cipolettiViewModel.getOrificeDataById(idTipeBangunan.toInt())
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            val alps = Intent(this@CipolettiActivity, AnalisisActivity::class.java)
            alps.putExtra("id_tipe_bangunan", idTipeBangunan)
            alps.putExtra("tipe_bangunan", detailBangunan)
            alps.putExtra("id_base_data", idBaseData)
            alps.putExtra("b", String.format(Locale.ENGLISH, "%.3f", cipolettiModel.lebarPengukur))
            startActivity(alps)
            finish()
        }
    }

    private fun setVariable() {
        if (cipolettiModel.id != null) {
            //set table title labels
            LegacyTableView.insertLegacyTitle("b", "B1", "p", "w", "b2")
            LegacyTableView.insertLegacyContent(
                String.format(Locale.ENGLISH, "%.3f", cipolettiModel.lebarPengukur),
                String.format(Locale.ENGLISH,"%.3f", cipolettiModel.lebarDasar),
                String.format(Locale.ENGLISH,"%.3f", cipolettiModel.tinggiMercuDiatasAmbang),
                String.format(Locale.ENGLISH,"%.3f", cipolettiModel.tinggiMercu),
                String.format(Locale.ENGLISH,"%.3f", cipolettiModel.lebarAtas))

            val readTitle = LegacyTableView.readLegacyTitle()
            val readBody = LegacyTableView.readLegacyContent()
            mBinding.cipolettiTable.setTitle(readTitle)
            mBinding.cipolettiTable.setContent(readBody)

            //depending on the phone screen size default table scale is 100
            //you can change it using this method
            //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

            //if you want a smaller table, change the padding setting
            mBinding.cipolettiTable.setTablePadding(7)

            //to enable users to zoom in and out:
            mBinding.cipolettiTable.setZoomEnabled(true)
            mBinding.cipolettiTable.setShowZoomControls(true)

            //remember to build your table as the last step
            mBinding.cipolettiTable.build()

            setCd()
        }
    }

    private fun setCd() {
        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            pengambilanData = it
            LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-", "Cd")
            isInit = false
            cd.removeAll(cd)

            for (i in it.indices) {
                LegacyTableView.insertLegacyContent((i + 1).toString(), "0.63")
                cd.add("0.63")
            }

            val readTitle = LegacyTableView.readLegacyTitle()
            val readBody = LegacyTableView.readLegacyContent()
            mBinding.cipolettiCd.setTitle(readTitle)
            mBinding.cipolettiCd.setContent(readBody)

            //depending on the phone screen size default table scale is 100
            //you can change it using this method
            //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

            //if you want a smaller table, change the padding setting
            mBinding.cipolettiCd.setTablePadding(7);

            //to enable users to zoom in and out:
            mBinding.cipolettiCd.setZoomEnabled(true)
            mBinding.cipolettiCd.setShowZoomControls(true)

            //remember to build your table as the last step
            mBinding.cipolettiCd.build()

            setCv()
        })
    }

    private fun setCv() {
        cv.removeAll(cv)
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-", "b", "w", "A*", "B1", "p", "A", "nilai", "Cv")

        for (i in pengambilanData.indices) {
            val b = cipolettiModel.lebarPengukur
            val w = cipolettiModel.tinggiMercu
            val aAsterisk: String = String.format(Locale.ENGLISH, "%.3f", (0.5.toFloat() * (b + cipolettiModel.lebarDasar) * w))
            val b1 = cipolettiModel.lebarDasar
            val p = cipolettiModel.tinggiMercuDiatasAmbang
            val aBig = String.format(Locale.ENGLISH,"%.3f", b1 * (p + w))
            val nilai = String.format(Locale.ENGLISH,"%.2f", (0.63).toFloat() * aAsterisk.toFloat() / aBig.toFloat())
            val koefisiensi = koefisiensiAmbangLebarViewModel.getKoefiensiAmbangLebarById(nilai = nilai.toFloat())
            var cvValue = "0.0"
            if (koefisiensi != null) {
                cvValue = String.format(Locale.ENGLISH,"%.3f", koefisiensi.segitiga)
            }
            cv.add(cvValue)
            LegacyTableView.insertLegacyContent((i + 1).toString(),
                String.format(Locale.ENGLISH,"%.3f", b),
                String.format(Locale.ENGLISH,"%.3f", w), aAsterisk,
                String.format(Locale.ENGLISH,"%.3f", b1),
                String.format(Locale.ENGLISH,"%.3f", p), aBig, nilai, cvValue)
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.cipolettiCv.setTitle(readTitle)
        mBinding.cipolettiCv.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.cipolettiCv.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.cipolettiCv.setZoomEnabled(true)
        mBinding.cipolettiCv.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.cipolettiCv.build()

        setDebit()
    }

    private fun setDebit() {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","Cd", "Cv", "g", "Bc", "h1", "Q")

        val g: String = String.format(Locale.ENGLISH, "%.1f", (9.8).toFloat())
        val bC: String = String.format(Locale.ENGLISH, "%.1f", cipolettiModel.lebarPengukur)
        for (i in cd.indices) {
            val h1: String = String.format(Locale.ENGLISH, "%.2f", pengambilanData[i].h1)
            val q:Float = (cd[i].toFloat() * cv[i].toFloat() * ((2.toFloat() / 3.toFloat())) * (sqrt(((2.toFloat() / 3.toFloat()) * g.toFloat())) * bC.toFloat() * (h1.toFloat().pow(x = (1.5).toFloat()))))
            pengambilanDataViewModel.update(pengambilanData[i].id!!, q)
            LegacyTableView.insertLegacyContent((i + 1).toString(), cd[i], cv[i], g, bC, h1, String.format(Locale.ENGLISH, "%.3f", q))
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.cipolettiDebit.setTitle(readTitle)
        mBinding.cipolettiDebit.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.cipolettiDebit.setTablePadding(7)

        //to enable users to zoom in and out:
        mBinding.cipolettiDebit.setZoomEnabled(true)
        mBinding.cipolettiDebit.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.cipolettiDebit.build()
        loading.dialog.dismiss()
    }

    private fun setViewModel() {
        cipolettiViewModel.cipolettiById.observe(this, {
            if (it.id != null) {
                cipolettiModel = it
                setVariable()
            }
        })
    }
}