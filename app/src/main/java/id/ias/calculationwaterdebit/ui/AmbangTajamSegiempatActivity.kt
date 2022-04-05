package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.AmbangTajamSegiempatModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.databinding.ActivityAmbangTajamSegiempatBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.sqrt

class AmbangTajamSegiempatActivity : AppCompatActivity() {

    private val atsViewModel: AmbangTajamSegiempatViewModel by viewModels {
        AmbangTajamSegiempatViewModelFactory((application as Application).ambangTajamSegiempatRepository)
    }

    private val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory((application as Application).pengambilanDataRepository)
    }

    private val koefisiensiAmTajSegiempatViewModel: KoefisiensiAmbangTajamSegiempatViewModel by viewModels {
        KoefisiensiAmbangTajamSegiempatViewModelFactory((application as Application).koefisiensiAmbangTajamSegiempatRepository)
    }

    private val mercuAmbangViewModel: MercuAmbangViewModel by viewModels {
        MercuAmbangViewModelFactory((application as Application).mercuAmbangRepository)
    }

    val loading = LoadingDialogUtil()
    private lateinit var mBinding: ActivityAmbangTajamSegiempatBinding
    var detailBangunan = ""
    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0
    var hef: ArrayList<String> = ArrayList()
    var ba: ArrayList<String> = ArrayList()
    var cd: ArrayList<String> = ArrayList()
    var atsData: AmbangTajamSegiempatModel = AmbangTajamSegiempatModel(null, 0, 0.toFloat(),
        0.toFloat(), 0.toFloat())
    var pengambilanDataById: List<PengambilanDataModel> = ArrayList()
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

        mBinding = ActivityAmbangTajamSegiempatBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        loading.show(this)
        setViewModel()
        setAction()

        atsViewModel.getAtsDataById(idTipeBangunan.toInt())
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            val alps = Intent(this@AmbangTajamSegiempatActivity, AnalisisActivity::class.java)
            alps.putExtra("id_tipe_bangunan", idTipeBangunan)
            alps.putExtra("tipe_bangunan", detailBangunan)
            alps.putExtra("id_base_data", idBaseData)
            alps.putExtra("b", String.format(Locale.ENGLISH, "%.3f", atsData.lebarSaluran))
            startActivity(alps)
            finish()
        }
    }

    private fun setVariable() {
        if (atsData.id != null) {
            //set table title labels
            LegacyTableView.insertLegacyTitle("B", "b", "p")
            LegacyTableView.insertLegacyContent(
                String.format(Locale.ENGLISH, "%.3f", atsData.lebarSaluran),
                String.format(Locale.ENGLISH,"%.3f", atsData.lebarMercu),
                String.format(Locale.ENGLISH,"%.3f", atsData.tinggiMercuDiatasAmbang))

            val readTitle = LegacyTableView.readLegacyTitle()
            val readBody = LegacyTableView.readLegacyContent()

            mBinding.atsTable.setTheme(LegacyTableView.CUSTOM)
            mBinding.atsTable.setBackgroundColor(Color.TRANSPARENT)
            mBinding.atsTable.setHeaderBackgroundLinearGradientTOP("#3E8E7E")
            mBinding.atsTable.setHeaderBackgroundLinearGradientBOTTOM("#3E8E7E")
            mBinding.atsTable.setTitle(readTitle)
            mBinding.atsTable.setContent(readBody)

            //depending on the phone screen size default table scale is 100
            //you can change it using this method
            //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

            //if you want a smaller table, change the padding setting
            mBinding.atsTable.setTablePadding(7)

            //to enable users to zoom in and out:
            mBinding.atsTable.setZoomEnabled(true)
            mBinding.atsTable.setShowZoomControls(true)

            //remember to build your table as the last step
            mBinding.atsTable.build()

            setReaktif()
        }
    }

    private fun setReaktif() {
        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            if (isInit) {
                pengambilanDataById = it
                isInit = false
                LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-", "h", "ϑh", "hef")

                for (i in it .indices) {
                    val h1 = String.format(Locale.ENGLISH,"%.3f",pengambilanDataById[i].h1)
                    val dh = "0.001"
                    val hefValue = String.format(Locale.ENGLISH, "%.3f", h1.toFloat() + dh.toFloat())

                    hef.add(hefValue)
                    LegacyTableView.insertLegacyContent((i + 1).toString(), h1, dh, hefValue)
                }

                val readTitle = LegacyTableView.readLegacyTitle()
                val readBody = LegacyTableView.readLegacyContent()

                mBinding.atsAirReaktif.setTheme(LegacyTableView.CUSTOM)
                mBinding.atsAirReaktif.setBackgroundColor(Color.TRANSPARENT)
                mBinding.atsAirReaktif.setHeaderBackgroundLinearGradientTOP("#3E8E7E")
                mBinding.atsAirReaktif.setHeaderBackgroundLinearGradientBOTTOM("#3E8E7E")
                mBinding.atsAirReaktif.setTitle(readTitle)
                mBinding.atsAirReaktif.setContent(readBody)

                //depending on the phone screen size default table scale is 100
                //you can change it using this method
                //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

                //if you want a smaller table, change the padding setting
                mBinding.atsAirReaktif.setTablePadding(7);

                //to enable users to zoom in and out:
                mBinding.atsAirReaktif.setZoomEnabled(true)
                mBinding.atsAirReaktif.setShowZoomControls(true)

                //remember to build your table as the last step
                mBinding.atsAirReaktif.build()

                setBentangMercu()
            }
        })
    }

    private fun setBentangMercu() {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-", "b", "B", "Nilai", "ϑb", "ba")

        val b = String.format(Locale.ENGLISH, "%.3f", atsData.lebarMercu)
        val bBig = String.format(Locale.ENGLISH, "%.3f", atsData.lebarSaluran)
        val nilai = String.format(Locale.ENGLISH, "%.2f", atsData.lebarSaluran / atsData.lebarMercu)
        val mercuAmbang = mercuAmbangViewModel.getMercuAmbangById(nilai.toFloat())
        var db = "0.0"
        if (mercuAmbang == null) {
            db = mercuAmbang.bef.toString()
        }
        val baValue = String.format(Locale.ENGLISH, "%.4f", atsData.lebarMercu + mercuAmbang.bef)
        for (i in pengambilanDataById.indices) {
            ba.add(baValue)
            LegacyTableView.insertLegacyContent((i + 1).toString(), b, bBig, nilai, db, baValue)
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()

        mBinding.atsBentangMercu.setTheme(LegacyTableView.CUSTOM)
        mBinding.atsBentangMercu.setBackgroundColor(Color.TRANSPARENT)
        mBinding.atsBentangMercu.setHeaderBackgroundLinearGradientTOP("#3E8E7E")
        mBinding.atsBentangMercu.setHeaderBackgroundLinearGradientBOTTOM("#3E8E7E")
        mBinding.atsBentangMercu.setTitle(readTitle)
        mBinding.atsBentangMercu.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.atsBentangMercu.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.atsBentangMercu.setZoomEnabled(true)
        mBinding.atsBentangMercu.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.atsBentangMercu.build()

        setCd()
    }

    private fun setCd() {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-", "b/B", "h/p", "Cd")
        val bPerB = String.format(Locale.ENGLISH, "%.1f", atsData.lebarMercu / atsData.lebarSaluran)
        for (i in pengambilanDataById.indices) {
            val hPerP = String.format(Locale.ENGLISH, "%.2f", pengambilanDataById[i].h1 / atsData.tinggiMercuDiatasAmbang)
            var cdValue = "0.0"
            val koefisiensi =
                    koefisiensiAmTajSegiempatViewModel.getKoefisiensiAmbangTajamSegiempatViewModelById(bPerB.toFloat(), hPerP.toFloat())
            if (koefisiensi != null) {
                cdValue = String.format(Locale.ENGLISH, "%5f", koefisiensi.cd)
            }
            cd.add(cdValue)
            LegacyTableView.insertLegacyContent((i + 1).toString(), bPerB, hPerP, cdValue)
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()

        mBinding.atsCd.setTheme(LegacyTableView.CUSTOM)
        mBinding.atsCd.setBackgroundColor(Color.TRANSPARENT)
        mBinding.atsCd.setHeaderBackgroundLinearGradientTOP("#3E8E7E")
        mBinding.atsCd.setHeaderBackgroundLinearGradientBOTTOM("#3E8E7E")
        mBinding.atsCd.setTitle(readTitle)
        mBinding.atsCd.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.atsCd.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.atsCd.setZoomEnabled(true)
        mBinding.atsCd.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.atsCd.build()

        setDebit()
    }

    private fun setDebit() {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","Cd", "g", "Ba", "hef", "Q")

        val g: String = String.format(Locale.ENGLISH, "%.1f", (9.8).toFloat())
        for (i in cd.indices) {
            val q:Float = ((2 / 3) * (sqrt(2 * g.toFloat()) * cd[i].toFloat() *
                    ba[i].toFloat() * hef[i].toFloat().pow(3/2)))

            pengambilanDataViewModel.update(pengambilanDataById[i].id!!, q)

            LegacyTableView.insertLegacyContent((i + 1).toString(), cd[i], g, ba[i], hef[i], String.format(Locale.ENGLISH, "%.3f", q))
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()

        mBinding.atsDebit.setTheme(LegacyTableView.CUSTOM)
        mBinding.atsDebit.setBackgroundColor(Color.TRANSPARENT)
        mBinding.atsDebit.setHeaderBackgroundLinearGradientTOP("#3E8E7E")
        mBinding.atsDebit.setHeaderBackgroundLinearGradientBOTTOM("#3E8E7E")
        mBinding.atsDebit.setTitle(readTitle)
        mBinding.atsDebit.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.atsDebit.setTablePadding(7)

        //to enable users to zoom in and out:
        mBinding.atsDebit.setZoomEnabled(true)
        mBinding.atsDebit.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.atsDebit.build()
        loading.dialog.dismiss()
    }

    private fun setViewModel() {
        atsViewModel.atsById.observe(this, {
            if (it.id != null) {
                atsData = it
                setVariable()
            }
        })
    }
}