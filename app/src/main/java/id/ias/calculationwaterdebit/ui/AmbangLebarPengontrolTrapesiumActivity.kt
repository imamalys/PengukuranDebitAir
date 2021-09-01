package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolTrapesiumModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.databinding.ActivityAmbangLebarPengontrolTrapesiumBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

class AmbangLebarPengontrolTrapesiumActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    private lateinit var mBinding: ActivityAmbangLebarPengontrolTrapesiumBinding

    private val ambangLebarPengontrolTrapesiumViewModel: AmbangLebarPengontrolTrapesiumViewModel by viewModels {
        AmbangLebarPengontrolTrapesiumViewModelFactory((application as Application).alptRepository)
    }
    private val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory((application as Application).pengambilanDataRepository)
    }

    private val koefisiensiAmbangLebarViewModel: KoefisiensiAmbangLebarViewModel by viewModels {
        KoefisiensiAmbangLebarViewModelFactory((application as Application).koefisiensiAmbangLebarRepository)
    }

    var detailBangunan = ""
    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0
    var cd: ArrayList<String> = ArrayList()
    var cv: ArrayList<String> = ArrayList()
    var alptData: AmbangLebarPengontrolTrapesiumModel = AmbangLebarPengontrolTrapesiumModel(null, 0, 0.toFloat(),
            0.toFloat(), 0.toFloat(), 0.toFloat(), 0.toFloat(), 0.toFloat(), 0.toFloat(), 0.toFloat())
    var pengambilanDataById: List<PengambilanDataModel> = ArrayList()
    var isInit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAmbangLebarPengontrolTrapesiumBinding.inflate(layoutInflater)
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

        ambangLebarPengontrolTrapesiumViewModel.getAlptDataById(idTipeBangunan.toInt())
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            val alps = Intent(this@AmbangLebarPengontrolTrapesiumActivity, AnalisisActivity::class.java)
            alps.putExtra("id_tipe_bangunan", idTipeBangunan)
            alps.putExtra("tipe_bangunan", detailBangunan)
            alps.putExtra("id_base_data", idBaseData)
            alps.putExtra("b", String.format(Locale.ENGLISH, "%.3f", alptData.lebarAmbang))
            startActivity(alps)
            finish()
        }
    }

    private fun setVariable() {
        if (alptData.id != null) {
            //set table title labels
            LegacyTableView.insertLegacyTitle("Bc", "B1", "L", "P", "m", "w", "b1", "Mc")
            LegacyTableView.insertLegacyContent(
                    String.format(Locale.ENGLISH, "%.3f", alptData.lebarAmbang),
                    String.format(Locale.ENGLISH,"%.3f", alptData.lebarDasar),
                    String.format(Locale.ENGLISH,"%.3f", alptData.panjangAmbang),
                    String.format(Locale.ENGLISH,"%.3f", alptData.tinggiAmbang),
                    String.format(Locale.ENGLISH,"%.3f", alptData.tinggiDiatasAmbang),
                    String.format(Locale.ENGLISH,"%.3f", alptData.tinggiDibawahAmbang),
                    String.format(Locale.ENGLISH,"%.3f", alptData.lebarAtas),
                    String.format(Locale.ENGLISH,"%.3f", alptData.kemiringanPengontrol))

            val readTitle = LegacyTableView.readLegacyTitle()
            val readBody = LegacyTableView.readLegacyContent()
            mBinding.alptTable.setTitle(readTitle)
            mBinding.alptTable.setContent(readBody)

            //depending on the phone screen size default table scale is 100
            //you can change it using this method
            //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

            //if you want a smaller table, change the padding setting
            mBinding.alptTable.setTablePadding(7);

            //to enable users to zoom in and out:
            mBinding.alptTable.setZoomEnabled(true)
            mBinding.alptTable.setShowZoomControls(true)

            //remember to build your table as the last step
            mBinding.alptTable.build()

            setCd()
        }
    }

    private fun setCd() {
        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            if (isInit) {
                isInit = false
                cd.removeAll(cd)
                pengambilanDataById = it
                LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-", "h1", "v1", "H1", "L", "H1/L", "Cd")

                for (i in it.indices) {
                    val h1: String = String.format(Locale.ENGLISH, "%.3f", it[i].h1)
                    val v1: String = String.format(Locale.ENGLISH, "%.3f", (it[i].jumlahRataRata!! / it[i].variasaiKetinggianAir))
                    val capitalH1: String = String.format(Locale.ENGLISH, "%.3f", (h1.toFloat() + (v1.toFloat() * v1.toFloat()) / (2 * 9.8)).toFloat())
                    val l: String = String.format(Locale.ENGLISH, "%.3f", alptData.panjangAmbang)
                    val h1perL: String = String.format(Locale.ENGLISH, "%.3f", capitalH1.toFloat() / l.toFloat())
                    val cdValue: String = if (0.1 < h1perL.toFloat() && h1perL.toFloat() < 1)
                        String.format(Locale.ENGLISH, "%.3f", (0.93 + 0.1 * h1perL.toFloat()).toFloat())  else String.format(Locale.ENGLISH, "%.1f", (0.9).toFloat())

                    cd.add(cdValue)
                    LegacyTableView.insertLegacyContent((i + 1).toString(), h1, v1, capitalH1, l, h1perL, cdValue)
                }

                val readTitle = LegacyTableView.readLegacyTitle()
                val readBody = LegacyTableView.readLegacyContent()
                mBinding.alptCd.setTitle(readTitle)
                mBinding.alptCd.setContent(readBody)

                //depending on the phone screen size default table scale is 100
                //you can change it using this method
                //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

                //if you want a smaller table, change the padding setting
                mBinding.alptCd.setTablePadding(7);

                //to enable users to zoom in and out:
                mBinding.alptCd.setZoomEnabled(true)
                mBinding.alptCd.setShowZoomControls(true)

                //remember to build your table as the last step
                mBinding.alptCd.build()

                setCv()
            }
        })
    }

    private fun setCv() {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","Bc", "p", "b1", "A*", "A", "Nilai", "Cv")

        cv.removeAll(cv)
        val bentukA =
                if (alptData.lebarDasar ==
                        (alptData.lebarAtas - alptData.lebarAmbang) + (alptData.lebarDasar - alptData.lebarAtas) + alptData.lebarAmbang)
                    "Peralihan Bentuk Persegiempat"
                else
                    "Peralihan Bentuk Trapesium"

        mBinding.tvCvBentuk.text = String.format(Locale.ENGLISH, "Bentuk = %s", bentukA)

        for (i in cd.indices) {
            val bc: String = String.format(Locale.ENGLISH, "%.3f", alptData.lebarAmbang)
            val pM: String = String.format(Locale.ENGLISH, "%.3f", alptData.tinggiDiatasAmbang)
            val b1: String = String.format(Locale.ENGLISH, "%.3f", alptData.lebarAtas)
            val aQuote: String = String.format(Locale.ENGLISH, "%.3f",
                    0.5.toFloat() * (b1.toFloat() + bc.toFloat()) * pM.toFloat())
            val a: String = if (bentukA == "Peralihan Bentuk Persegiempat")
                String.format(Locale.ENGLISH, "%.3f",
                        b1.toFloat() * (alptData.tinggiDiatasAmbang + alptData.tinggiAmbang + alptData.tinggiDibawahAmbang)) else
                String.format(Locale.ENGLISH, "%.3f",
                        0.5.toFloat() * (bc.toFloat() / 5) + b1.toFloat() * (alptData.tinggiDiatasAmbang + alptData.tinggiAmbang + alptData.tinggiDibawahAmbang))
            val nilai: String = String.format(Locale.ENGLISH, "%.2f", (cd[i].toFloat() * aQuote.toFloat()) / a.toFloat())
            var cvValue: String
            var koefisiensi = koefisiensiAmbangLebarViewModel.getKoefiensiAmbangLebarById(nilai.toFloat())

            cvValue = if (koefisiensi == null) {
                "0.0"
            } else {
                when(bentukA) {
                    "Bentuk Peralihan Persegiempat" -> {
                        String.format(Locale.ENGLISH, "%.3f", koefisiensi.segiempat)
                    }
                    else -> {
                        String.format(Locale.ENGLISH, "%.3f", koefisiensi.trapesium)
                    }
                }
            }

            cv.add(cvValue)

            LegacyTableView.insertLegacyContent(
                    (i + 1).toString(), bc, pM, b1, aQuote, a, nilai, cvValue)
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.alptCv.setTitle(readTitle)
        mBinding.alptCv.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.alptCv.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.alptCv.setZoomEnabled(true)
        mBinding.alptCv.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.alptCv.build()

        setDebit()
    }

    private fun setDebit() {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","Cd", "Cv", "g", "Bc", "yc", "h1", "mc", "Q")

        val g: String = String.format(Locale.ENGLISH, "%.1f", (9.8).toFloat())
        val bC: String = String.format(Locale.ENGLISH, "%.1f", alptData.lebarAmbang)
        for (i in cd.indices) {
            val yc: String = String.format(Locale.ENGLISH, "%.2f", pengambilanDataById[i].h1)
            val h1: String = String.format(Locale.ENGLISH, "%.3f", pengambilanDataById[i].h1)
            val mc: String = String.format(Locale.ENGLISH, "%.2f", alptData.kemiringanPengontrol)
            val q:Float = ((cd[i].toFloat() * cv[i].toFloat()) *
                    (bC.toFloat() * yc.toFloat() + mc.toFloat().pow(2)) *
                    ((2.toFloat() * g.toFloat() * (h1.toFloat() - yc.toFloat())).pow(0.5.toFloat())))
            pengambilanDataViewModel.update(pengambilanDataById[i].id!!, q)

            LegacyTableView.insertLegacyContent((i + 1).toString(), cd[i], cv[i], g, bC, yc, h1, mc, String.format(Locale.ENGLISH, "%.3f", q))
        }
        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.alptDebit.setTitle(readTitle)
        mBinding.alptDebit.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.alptDebit.setTablePadding(7)

        //to enable users to zoom in and out:
        mBinding.alptDebit.setZoomEnabled(true)
        mBinding.alptDebit.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.alptDebit.build()
        loading.dialog.dismiss()
    }

    private fun setViewModel() {
        ambangLebarPengontrolTrapesiumViewModel.alptById.observe(this, {
            if (it.id != null) {
               alptData = it
                setVariable()
            }
        })
    }
}