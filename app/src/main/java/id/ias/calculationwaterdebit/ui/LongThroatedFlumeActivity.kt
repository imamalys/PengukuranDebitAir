package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.LongThrotedFlumeModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.databinding.ActivityLongThroatedFlumeBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.hypot
import kotlin.math.pow
import kotlin.math.sqrt

class LongThroatedFlumeActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    private lateinit var mBinding: ActivityLongThroatedFlumeBinding

    private val ltfViewModel: LongThroatedFlumeViewModel by viewModels {
        LongThroatedFlumeViewModelFactory((application as Application).longThroatedFlumeRepository)
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
    var ltfData: LongThrotedFlumeModel = LongThrotedFlumeModel(null, 0, (0.0).toFloat(),
            (0.0).toFloat(), (0.0).toFloat(), (0.0).toFloat(), (0.0).toFloat(), (0.0).toFloat(), (0.0).toFloat())
    var cd: ArrayList<String> = ArrayList()
    var cv: ArrayList<String> = ArrayList()
    var pengambilanData: List<PengambilanDataModel> = ArrayList()
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

        mBinding = ActivityLongThroatedFlumeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        loading.show(this)
        setViewModel()
        setAction()

        ltfViewModel.getLtfDataById(idTipeBangunan.toInt())
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            val alps = Intent(this@LongThroatedFlumeActivity, AnalisisActivity::class.java)
            alps.putExtra("id_tipe_bangunan", idTipeBangunan)
            alps.putExtra("tipe_bangunan", detailBangunan)
            alps.putExtra("id_base_data", idBaseData)
            alps.putExtra("b", String.format(Locale.ENGLISH, "%.3f", ltfData.lebarAmbang))
            startActivity(alps)
            finish()
        }
    }

    private fun setVariable() {
        if (ltfData.id != null) {
            //set table title labels
            LegacyTableView.insertLegacyTitle("Bc", "B1", "L", "P", "m", "w", "b1")
            LegacyTableView.insertLegacyContent(
                    String.format(Locale.ENGLISH, "%.3f", ltfData.lebarAmbang),
                    String.format(Locale.ENGLISH,"%.3f", ltfData.lebarDasar),
                    String.format(Locale.ENGLISH,"%.3f", ltfData.panjangAmbang),
                    String.format(Locale.ENGLISH,"%.3f", ltfData.tinggiAmbang),
                    String.format(Locale.ENGLISH,"%.3f", ltfData.tinggiDiatasAmbang),
                    String.format(Locale.ENGLISH,"%.3f", ltfData.tinggiDibawahAmbang),
                    String.format(Locale.ENGLISH,"%.3f", ltfData.lebarAtas))

            val readTitle = LegacyTableView.readLegacyTitle()
            val readBody = LegacyTableView.readLegacyContent()
            mBinding.ltfTable.setTitle(readTitle)
            mBinding.ltfTable.setContent(readBody)

            //depending on the phone screen size default table scale is 100
            //you can change it using this method
            //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

            //if you want a smaller table, change the padding setting
            mBinding.ltfTable.setTablePadding(7);

            //to enable users to zoom in and out:
            mBinding.ltfTable.setZoomEnabled(true)
            mBinding.ltfTable.setShowZoomControls(true)

            //remember to build your table as the last step
            mBinding.ltfTable.build()

            setCd()
        }
    }

    private fun setCd() {
        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            if (isInit) {
                isInit = false
                cd.removeAll(cd)
                pengambilanData = it
                LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-", "h1", "v1", "H1", "L", "H1/L", "Cd")

                for (i in it.indices) {
                    val h1: String = String.format(Locale.ENGLISH, "%.3f", it[i].h1)
                    val v1: String = String.format(Locale.ENGLISH, "%.3f", (it[i].jumlahRataRata!! / it[i].variasaiKetinggianAir))
                    val capitalH1: String = String.format(Locale.ENGLISH, "%.3f", (h1.toFloat() + (v1.toFloat() * v1.toFloat()) / (2 * 9.8)).toFloat())
                    val l: String = String.format(Locale.ENGLISH, "%.3f", ltfData.panjangAmbang)
                    val h1perL: String = String.format(Locale.ENGLISH, "%.3f", capitalH1.toFloat() / l.toFloat())
                    val cdValue: String = if (0.1 < h1perL.toFloat() && h1perL.toFloat() < 1)
                        String.format(Locale.ENGLISH, "%.3f", (0.93 + 0.1 * h1perL.toFloat()).toFloat())  else
                            String.format(Locale.ENGLISH, "%.1f", (0.9).toFloat())

                    cd.add(cdValue)
                    LegacyTableView.insertLegacyContent((i + 1).toString(), h1, v1, capitalH1, l, h1perL, cdValue)
                }

                val readTitle = LegacyTableView.readLegacyTitle()
                val readBody = LegacyTableView.readLegacyContent()
                mBinding.ltfCd.setTitle(readTitle)
                mBinding.ltfCd.setContent(readBody)

                //depending on the phone screen size default table scale is 100
                //you can change it using this method
                //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

                //if you want a smaller table, change the padding setting
                mBinding.ltfCd.setTablePadding(7);

                //to enable users to zoom in and out:
                mBinding.ltfCd.setZoomEnabled(true)
                mBinding.ltfCd.setShowZoomControls(true)

                //remember to build your table as the last step
                mBinding.ltfCd.build()

                setCv()
            }
        })
    }

    private fun setCv() {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","Bc", "p", "A*", "B1", "m", "P", "w", "b1", "mc", "A", "Nilai", "Cv")
        cv.removeAll(cv)
        val bentukA =
                if (ltfData.lebarAtas == ltfData.lebarDasar)
                    "Bentuk Peralihan Persegiempat"
                else if (ltfData.lebarAtas > ltfData.lebarDasar || ltfData.lebarAtas < ltfData.lebarDasar)
                    "Bentuk Peralihan Trapesium"
                else "Bentuk Peralihan Segitiga"

        mBinding.tvCvBentuk.text = String.format(Locale.ENGLISH, "Bentuk = %s", bentukA)

        for (i in cd.indices) {
            val bc: String = String.format(Locale.ENGLISH, "%.3f", ltfData.lebarAmbang)
            val pM: String = String.format(Locale.ENGLISH, "%.3f", ltfData.tinggiDiatasAmbang)
            val aQuote: String = String.format(Locale.ENGLISH, "%.3f", bc.toFloat() * pM.toFloat())
            val capitalB1: String = String.format(Locale.ENGLISH, "%.3f", ltfData.lebarDasar)
            val m: String = String.format(Locale.ENGLISH, "%.3f", ltfData.tinggiDiatasAmbang)
            val p: String = String.format(Locale.ENGLISH, "%.3f", ltfData.tinggiAmbang)
            val w: String = String.format(Locale.ENGLISH, "%.3f", ltfData.tinggiDibawahAmbang)
            val b1: String = String.format(Locale.ENGLISH, "%.3f", ltfData.lebarAtas)
            val mc1: String = String.format(Locale.ENGLISH, "%.3f", (b1.toFloat() - capitalB1.toFloat()) / 2)
            val mc2: String = String.format(Locale.ENGLISH, "%.3f", (m.toFloat() + p.toFloat() + w.toFloat()))
            val mc: String = if (bentukA == "Bentuk Peralihan Trapesium")
                String.format(Locale.ENGLISH, "%.2f", hypot(mc1.toFloat(), mc2.toFloat())) else
                String.format(Locale.ENGLISH, "%.2f", mc2.toFloat() * b1.toFloat())
            val a: String = if (bentukA == "Bentuk Peralihan Trapesium")
                String.format(Locale.ENGLISH, "%.3f", (0.5).toFloat() * (b1.toFloat() + capitalB1.toFloat()) * (mc2.toFloat())) else
                String.format(Locale.ENGLISH, "%.3f", mc2.toFloat() * b1.toFloat())
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
                    "Bentuk Peralihan Trapesium" -> {
                        String.format(Locale.ENGLISH, "%.3f", koefisiensi.trapesium)
                    }
                    else -> {
                        String.format(Locale.ENGLISH, "%.3f", koefisiensi.segitiga)
                    }
                }
            }

            cv.add(cvValue)

            LegacyTableView.insertLegacyContent(
                    (i + 1).toString(), bc, pM, aQuote, capitalB1, m, p, w, b1, mc, a, nilai, cvValue)
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.ltfCv.setTitle(readTitle)
        mBinding.ltfCv.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.ltfCv.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.ltfCv.setZoomEnabled(true)
        mBinding.ltfCv.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.ltfCv.build()

        setDebit()
    }

    private fun setDebit() {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","Cd", "Cv", "g", "Bc", "h1", "Q")

        val g: String = String.format(Locale.ENGLISH, "%.1f", (9.8).toFloat())
        val bC: String = String.format(Locale.ENGLISH, "%.1f", ltfData.lebarAmbang)
        for (i in cd.indices) {
            val h1: String = String.format(Locale.ENGLISH, "%.2f", pengambilanData[i].h1)
            val q:Float = (cd[i].toFloat() * cv[i].toFloat() * ((2.toFloat() / 3.toFloat())) * (sqrt(((2.toFloat() / 3.toFloat()) * g.toFloat())) * bC.toFloat() * (h1.toFloat().pow(x = (1.5).toFloat()))))
            pengambilanDataViewModel.update(pengambilanData[i].id!!, q)

            LegacyTableView.insertLegacyContent((i + 1).toString(), cd[i], cv[i], g, bC, h1, String.format(Locale.ENGLISH, "%.3f", q))
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.ltfDebit.setTitle(readTitle)
        mBinding.ltfDebit.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.ltfDebit.setTablePadding(7)

        //to enable users to zoom in and out:
        mBinding.ltfDebit.setZoomEnabled(true)
        mBinding.ltfDebit.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.ltfDebit.build()
        loading.dialog.dismiss()
    }

    private fun setViewModel() {
        ltfViewModel.ltfById.observe(this, {
            if (it.id != null) {
                ltfData = it
                setVariable()
            }
        })
    }
}