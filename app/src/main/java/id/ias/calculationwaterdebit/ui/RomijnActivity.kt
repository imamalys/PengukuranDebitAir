package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.model.RomijnModel
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.databinding.ActivityRomijnBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.sqrt

class RomijnActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    private lateinit var mBinding: ActivityRomijnBinding

    private val romijnViewModel: RomijnViewModel by viewModels {
        RomijnViewModelFactory((application as Application).romijnRepository)
    }

    private val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory((application as Application).pengambilanDataRepository)
    }

    private val koefisiensiAmbangLebarViewModel: KoefisiensiAmbangLebarViewModel by viewModels {
        KoefisiensiAmbangLebarViewModelFactory((application as Application).koefisiensiAmbangLebarRepository)
    }

    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0
    var detailBangunan = ""
    var romijnData: RomijnModel = RomijnModel(null, 0, (0.0).toFloat(), (0.0).toFloat(),
        (0.0).toFloat(), (0.0).toFloat(), (0.0).toFloat())
    var cd: ArrayList<String> = ArrayList()
    var cv: ArrayList<String> = ArrayList()
    var isInit = true
    var pengambilanData: List<PengambilanDataModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityRomijnBinding.inflate(layoutInflater)
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

        romijnViewModel.getRomijnDataById(idTipeBangunan.toInt())
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            val alps = Intent(this@RomijnActivity, AnalisisActivity::class.java)
            alps.putExtra("id_tipe_bangunan", idTipeBangunan)
            alps.putExtra("tipe_bangunan", detailBangunan)
            alps.putExtra("id_base_data", idBaseData)
            alps.putExtra("b", String.format(Locale.ENGLISH, "%.3f", romijnData.lebarMeja))
            startActivity(alps)
            finish()
        }
    }

    private fun setVariable() {
        if (romijnData.id != null) {
            //set table title labels
            LegacyTableView.insertLegacyTitle("Bc", "B1", "L", "P", "m")
            LegacyTableView.insertLegacyContent(
                String.format(Locale.ENGLISH, "%.3f", romijnData.lebarMeja),
                String.format(Locale.ENGLISH,"%.3f", romijnData.lebarDasar),
                String.format(Locale.ENGLISH,"%.3f", romijnData.panjangMeja),
                String.format(Locale.ENGLISH,"%.3f", romijnData.tinggiMejaDariDasar),
                String.format(Locale.ENGLISH,"%.3f", romijnData.tinggiDiatasMeja))

            val readTitle = LegacyTableView.readLegacyTitle()
            val readBody = LegacyTableView.readLegacyContent()

            mBinding.romijnTable.setTheme(LegacyTableView.CUSTOM)
            mBinding.romijnTable.setBackgroundColor(Color.TRANSPARENT)
            mBinding.romijnTable.setHeaderBackgroundLinearGradientTOP("#3E8E7E")
            mBinding.romijnTable.setHeaderBackgroundLinearGradientBOTTOM("#3E8E7E")
            mBinding.romijnTable.setTitle(readTitle)
            mBinding.romijnTable.setContent(readBody)

            //depending on the phone screen size default table scale is 100
            //you can change it using this method
            //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

            //if you want a smaller table, change the padding setting
            mBinding.romijnTable.setTablePadding(7);

            //to enable users to zoom in and out:
            mBinding.romijnTable.setZoomEnabled(true)
            mBinding.romijnTable.setShowZoomControls(true)

            //remember to build your table as the last step
            mBinding.romijnTable.build()

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
                    val l: String = String.format(Locale.ENGLISH, "%.3f", romijnData.panjangMeja)
                    val h1perL: String = String.format(Locale.ENGLISH, "%.3f", capitalH1.toFloat() / l.toFloat())
                    val cdValue: String = if (0.1 < h1perL.toFloat() && h1perL.toFloat() < 1)
                        String.format(Locale.ENGLISH, "%.3f", (0.93 + 0.1 * h1perL.toFloat()).toFloat())  else String.format(Locale.ENGLISH, "%.1f", (0.9).toFloat())

                    cd.add(cdValue)
                    LegacyTableView.insertLegacyContent((i + 1).toString(), h1, v1, capitalH1, l, h1perL, cdValue)
                }

                val readTitle = LegacyTableView.readLegacyTitle()
                val readBody = LegacyTableView.readLegacyContent()

                mBinding.romijnCd.setTheme(LegacyTableView.CUSTOM)
                mBinding.romijnCd.setBackgroundColor(Color.TRANSPARENT)
                mBinding.romijnCd.setHeaderBackgroundLinearGradientTOP("#3E8E7E")
                mBinding.romijnCd.setHeaderBackgroundLinearGradientBOTTOM("#3E8E7E")
                mBinding.romijnCd.setTitle(readTitle)
                mBinding.romijnCd.setContent(readBody)

                //depending on the phone screen size default table scale is 100
                //you can change it using this method
                //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

                //if you want a smaller table, change the padding setting
                mBinding.romijnCd.setTablePadding(7);

                //to enable users to zoom in and out:
                mBinding.romijnCd.setZoomEnabled(true)
                mBinding.romijnCd.setShowZoomControls(true)

                //remember to build your table as the last step
                mBinding.romijnCd.build()

                setCv()
            }
        })
    }

    private fun setCv() {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","Bc", "p", "A*", "B1", "m", "p", "A", "Nilai", "Cv")

        cv.removeAll(cv)
        for (i in cd.indices) {
            val bc: String = String.format(Locale.ENGLISH, "%.3f", romijnData.lebarMeja)
            val pM: String = String.format(Locale.ENGLISH, "%.3f", romijnData.tinggiDiatasMeja)
            val aQuote: String = String.format(Locale.ENGLISH, "%.3f", bc.toFloat() * pM.toFloat())
            val capitalB1: String = String.format(Locale.ENGLISH, "%.3f", romijnData.lebarDasar)
            val m: String = String.format(Locale.ENGLISH, "%.3f", romijnData.tinggiDiatasMeja)
            val p: String = String.format(Locale.ENGLISH, "%.3f", romijnData.tinggiMejaDariDasar)
            val b1: String = String.format(Locale.ENGLISH, "%.3f", romijnData.lebarDasar)
            val a: String = String.format(Locale.ENGLISH, "%.3f", b1.toFloat() * (m.toFloat() + p.toFloat()))
            val nilai: String = String.format(Locale.ENGLISH, "%.2f", (cd[i].toFloat() * aQuote.toFloat()) / a.toFloat())
            var cvValue: String
            var koefisiensi = koefisiensiAmbangLebarViewModel.getKoefiensiAmbangLebarById(nilai.toFloat())

            cvValue = if (koefisiensi == null) {
                "0.0"
            } else {
                String.format(Locale.ENGLISH, "%.2f",koefisiensi.trapesium)
            }

            cv.add(cvValue)

            LegacyTableView.insertLegacyContent(
                (i + 1).toString(), bc, pM, aQuote, capitalB1, b1, m, p, a, nilai, cvValue)
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()

        mBinding.romijnCv.setTheme(LegacyTableView.CUSTOM)
        mBinding.romijnCv.setBackgroundColor(Color.TRANSPARENT)
        mBinding.romijnCv.setHeaderBackgroundLinearGradientTOP("#3E8E7E")
        mBinding.romijnCv.setHeaderBackgroundLinearGradientBOTTOM("#3E8E7E")
        mBinding.romijnCv.setTitle(readTitle)
        mBinding.romijnCv.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.romijnCv.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.romijnCv.setZoomEnabled(true)
        mBinding.romijnCv.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.romijnCv.build()

        setDebit()
    }

    private fun setDebit() {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","Cd", "Cv", "g", "Bc", "h1", "Q")

        val g: String = String.format(Locale.ENGLISH, "%.1f", (9.8).toFloat())
        val bC: String = String.format(Locale.ENGLISH, "%.1f", romijnData.lebarMeja)
        for (i in cd.indices) {
            val h1: String = String.format(Locale.ENGLISH, "%.2f", pengambilanData[i].h1)
            val q:Float = (cd[i].toFloat() * cv[i].toFloat() * ((2.toFloat() / 3.toFloat())) *
                    (sqrt(((2.toFloat() / 3.toFloat()) * g.toFloat())) * bC.toFloat() *
                            (h1.toFloat().pow(x = (1.5).toFloat()))))
            pengambilanDataViewModel.update(pengambilanData[i].id!!, q)

            LegacyTableView.insertLegacyContent((i + 1).toString(), cd[i], cv[i], g, bC, h1, String.format(Locale.ENGLISH, "%.3f", q))
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()

        mBinding.romijnDebit.setTheme(LegacyTableView.CUSTOM)
        mBinding.romijnDebit.setBackgroundColor(Color.TRANSPARENT)
        mBinding.romijnDebit.setHeaderBackgroundLinearGradientTOP("#3E8E7E")
        mBinding.romijnDebit.setHeaderBackgroundLinearGradientBOTTOM("#3E8E7E")
        mBinding.romijnDebit.setTitle(readTitle)
        mBinding.romijnDebit.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.romijnDebit.setTablePadding(7)

        //to enable users to zoom in and out:
        mBinding.romijnDebit.setZoomEnabled(true)
        mBinding.romijnDebit.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.romijnDebit.build()
        loading.dialog.dismiss()
    }

    private fun setViewModel() {
        romijnViewModel.romijnById.observe(this, {
            if (it.id != null) {
                romijnData = it
                setVariable()
            }
        })
    }
}