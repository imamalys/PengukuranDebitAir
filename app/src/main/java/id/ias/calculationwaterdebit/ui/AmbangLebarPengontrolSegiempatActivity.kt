package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.databinding.ActivityAmbangLebarPengontrolSegiempatBinding
import id.ias.calculationwaterdebit.viewmodel.AlpsActivityViewModel
import id.ias.calculationwaterdebit.viewmodel.AlpsActivityViewModelFactory
import kotlin.math.hypot
import kotlin.math.pow
import kotlin.math.sqrt

class AmbangLebarPengontrolSegiempatActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityAmbangLebarPengontrolSegiempatBinding
    private val alpsActivityViewModel: AlpsActivityViewModel by viewModels {
        AlpsActivityViewModelFactory()
    }

    private val ambangLebarPengontrolSegiempatViewModel: AmbangLebarPengontrolSegiempatViewModel by viewModels {
        AmbangLebarPengontrolSegiempatViewModelFactory((application as Application).alpsRepository)
    }

    private val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory((application as Application).pengambilanDataRepository)
    }

    private val koefisiensiAmbangLebarViewModel: KoefisiensiAmbangLebarViewModel by viewModels {
        KoefisiensiAmbangLebarViewModelFactory((application as Application).koefisiensiAmbangLebarRepository)
    }

    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0
    var cd: ArrayList<String> = ArrayList()
    var cv: ArrayList<String> = ArrayList()
    var isInit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAmbangLebarPengontrolSegiempatBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                alpsActivityViewModel.detailBangunan = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
                idBaseData = it.getLongExtra("id_base_data", 0)
            }
        }

        setViewModel()
        setAction()

        ambangLebarPengontrolSegiempatViewModel.getalpsDataById(idTipeBangunan.toInt())
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            val alps = Intent(this@AmbangLebarPengontrolSegiempatActivity, AnalisisActivity::class.java)
            alps.putExtra("id_tipe_bangunan", idTipeBangunan)
            alps.putExtra("tipe_bangunan", alpsActivityViewModel.detailBangunan)
            alps.putExtra("id_base_data", idBaseData)
            startActivity(alps)
            finish()
        }
    }

    private fun setVariable() {
        if (alpsActivityViewModel.alpsData.id != null) {
            //set table title labels
            LegacyTableView.insertLegacyTitle("Bc", "B1", "L", "P", "m", "w", "b1")
            LegacyTableView.insertLegacyContent(
                String.format("%.3f", alpsActivityViewModel.alpsData.lebarAmbang),
                String.format("%.3f", alpsActivityViewModel.alpsData.lebarDasar),
                String.format("%.3f", alpsActivityViewModel.alpsData.panjangAmbang),
                String.format("%.3f", alpsActivityViewModel.alpsData.tinggiAmbang),
                String.format("%.3f", alpsActivityViewModel.alpsData.tinggiDiatasAmbang),
                String.format("%.3f", alpsActivityViewModel.alpsData.tinggiDibawahAmbang),
                String.format("%.3f", alpsActivityViewModel.alpsData.lebarAtas))

            val readTitle = LegacyTableView.readLegacyTitle()
            val readBody = LegacyTableView.readLegacyContent()
            mBinding.alpsTable.setTitle(readTitle)
            mBinding.alpsTable.setContent(readBody)

            //depending on the phone screen size default table scale is 100
            //you can change it using this method
            //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

            //if you want a smaller table, change the padding setting
            mBinding.alpsTable.setTablePadding(7);

            //to enable users to zoom in and out:
            mBinding.alpsTable.setZoomEnabled(true)
            mBinding.alpsTable.setShowZoomControls(true)

            //remember to build your table as the last step
            mBinding.alpsTable.build()

            setCd()
        }
    }

    private fun setCd() {
        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            if (isInit) {
                isInit = false
                cd.removeAll(cd)
                alpsActivityViewModel.pengambilanData = it
                LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-", "h1", "v1", "H1", "L", "H1/L", "Cd")

                for (i in it.indices) {
                    val h1: String = String.format("%.3f", it[i].h1)
                    val v1: String = String.format("%.3f", (it[i].jumlahRataRata!! / it[i].variasaiKetinggianAir))
                    val capitalH1: String = String.format("%.3f", (h1.toFloat() + (v1.toFloat() * v1.toFloat()) / (2 * 9.8)).toFloat())
                    val l: String = String.format("%.3f", alpsActivityViewModel.alpsData.panjangAmbang)
                    val h1perL: String = String.format("%.3f", capitalH1.toFloat() / l.toFloat())
                    val cdValue: String = if (0.1 < h1perL.toFloat() && h1perL.toFloat() < 1)
                        String.format("%.3f", (0.93 + 0.1 * h1perL.toFloat()).toFloat())  else String.format("%.1f", (0.9).toFloat())

                    cd.add(cdValue)
                    LegacyTableView.insertLegacyContent((i + 1).toString(), h1, v1, capitalH1, l, h1perL, cdValue)
                }

                val readTitle = LegacyTableView.readLegacyTitle()
                val readBody = LegacyTableView.readLegacyContent()
                mBinding.alpsCd.setTitle(readTitle)
                mBinding.alpsCd.setContent(readBody)

                //depending on the phone screen size default table scale is 100
                //you can change it using this method
                //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

                //if you want a smaller table, change the padding setting
                mBinding.alpsCd.setTablePadding(7);

                //to enable users to zoom in and out:
                mBinding.alpsCd.setZoomEnabled(true)
                mBinding.alpsCd.setShowZoomControls(true)

                //remember to build your table as the last step
                mBinding.alpsCd.build()

                setCv()
            }
        })
    }

    private fun setCv() {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","Bc", "p", "A*", "B1", "m", "P", "w", "b1", "mc", "A", "Nilai", "Cv")

        cv.removeAll(cv)
        val bentukA =
            if (alpsActivityViewModel.alpsData.lebarAmbang == alpsActivityViewModel.alpsData.lebarAtas)
                "Bentuk Peralihan Persegiempat"
            else if (alpsActivityViewModel.alpsData.lebarAmbang > alpsActivityViewModel.alpsData.lebarAtas ||
                alpsActivityViewModel.alpsData.lebarAmbang < alpsActivityViewModel.alpsData.lebarAtas)
                    "Bentuk Peralihan Trapesium"
            else "Bentuk Peralihan Segitiga"

        mBinding.tvCvBentuk.text = String.format("Bentuk = %s", bentukA)

        for (i in cd.indices) {
            val bc: String = String.format("%.3f", alpsActivityViewModel.alpsData.lebarAmbang)
            val pM: String = String.format("%.3f", alpsActivityViewModel.alpsData.tinggiDiatasAmbang)
            val aQuote: String = String.format("%.3f", bc.toFloat() * pM.toFloat())
            val capitalB1: String = String.format("%.3f", alpsActivityViewModel.alpsData.lebarDasar)
            val m: String = String.format("%.3f", alpsActivityViewModel.alpsData.tinggiDiatasAmbang)
            val p: String = String.format("%.3f", alpsActivityViewModel.alpsData.tinggiAmbang)
            val w: String = String.format("%.3f", alpsActivityViewModel.alpsData.tinggiDibawahAmbang)
            val b1: String = String.format("%.3f", alpsActivityViewModel.alpsData.lebarAtas)
            val mc1: String = String.format("%.3f", (b1.toFloat() - capitalB1.toFloat()) / 2)
            val mc2: String = String.format("%.3f", (m.toFloat() + p.toFloat() + w.toFloat()))
            val mc: String = if (bentukA == "Bentuk Peralihan Trapesium")
                String.format("%.2f", hypot(mc1.toFloat(), mc2.toFloat())) else
                    String.format("%.2f", mc2.toFloat() * b1.toFloat())
            val a: String = if (bentukA == "Bentuk Peralihan Trapesium")
                String.format("%.3f", (0.5).toFloat() * (b1.toFloat() + capitalB1.toFloat()) * (mc2.toFloat())) else
                    String.format("%.3f", mc2.toFloat() * b1.toFloat())
            val nilai: String = String.format("%.2f", (cd[i].toFloat() * aQuote.toFloat()) / a.toFloat())
            var cvValue: String
            var koefisiensi = koefisiensiAmbangLebarViewModel.getKoefiensiAmbangLebarById(nilai.toFloat())

            cvValue = if (koefisiensi == null) {
                "Bukan"
            } else {
                when(bentukA) {
                    "Bentuk Peralihan Persegiempat" -> {
                        String.format("%.3f", koefisiensi.segiempat)
                    }
                    "Bentuk Peralihan Trapesium" -> {
                        String.format("%.3f", koefisiensi.trapesium)
                    }
                    else -> {
                        String.format("%.3f", koefisiensi.segitiga)
                    }
                }
            }

            cv.add(cvValue)

            LegacyTableView.insertLegacyContent(
                    (i + 1).toString(), bc, pM, aQuote, capitalB1, m, p, w, b1, mc, a, nilai, cvValue)
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.alpsCv.setTitle(readTitle)
        mBinding.alpsCv.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.alpsCv.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.alpsCv.setZoomEnabled(true)
        mBinding.alpsCv.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.alpsCv.build()

        setDebit()
    }

    private fun setDebit() {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","Cd", "Cv", "g", "Bc", "h1", "Q")

        val g: String = String.format("%.1f", (9.8).toFloat())
        val bC: String = String.format("%.1f", alpsActivityViewModel.alpsData.lebarAmbang)
        for (i in cd.indices) {
            val h1: String = String.format("%.2f", alpsActivityViewModel.pengambilanData[i].h1)
            val q:String = if (cv[i] == "Bukan") "Bukan" else
                String.format("%.3f", (cd[i].toFloat() * cv[i].toFloat() * ((2.toFloat() / 3.toFloat())) * (sqrt(((2.toFloat() / 3.toFloat()) * g.toFloat())) * bC.toFloat() * (h1.toFloat().pow(x = (1.5).toFloat())))))

            LegacyTableView.insertLegacyContent((i + 1).toString(), cd[i], cv[i], g, bC, h1, q)
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.alpsDebit.setTitle(readTitle)
        mBinding.alpsDebit.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.alpsDebit.setTablePadding(7)

        //to enable users to zoom in and out:
        mBinding.alpsDebit.setZoomEnabled(true)
        mBinding.alpsDebit.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.alpsDebit.build()
    }

    private fun setViewModel() {
        ambangLebarPengontrolSegiempatViewModel.alpsById.observe(this, {
            if (it.id != null) {
                alpsActivityViewModel.alpsData = it
                setVariable()
            }
        })
    }
}