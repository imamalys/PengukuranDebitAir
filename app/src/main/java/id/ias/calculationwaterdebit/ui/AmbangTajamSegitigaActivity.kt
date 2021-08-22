package id.ias.calculationwaterdebit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.AmbangTipisSegitigaModel
import id.ias.calculationwaterdebit.database.model.AmbangTipisSegitigaSudutModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.database.viewmodel.AmbangTipisSegitigaSudutViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityAmbangTajamSegitigaBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.tan

class AmbangTajamSegitigaActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    private lateinit var mBinding: ActivityAmbangTajamSegitigaBinding

    private val atsViewModel: AmbangTipisSegitigaViewModel by viewModels {
        AmbangTipisSegitigaViewModelFactory((application as Application).atsRepository)
    }
    private val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory((application as Application).pengambilanDataRepository)
    }

    private val ambangTipisSegitigaSudutViewModel: AmbangTipisSegitigaSudutViewModel by viewModels {
        AmbangTipisSegitigaSudutViewModelFactory((application as Application).ambangTipisSegitigaSudutRepository)
    }

    private val koefisiensiAmbangTipisSegitigaViewModel: KoefisiensiAmbangTipisSegitigaViewModel by viewModels {
        KoefisiensiAmbangTipisSegitigaViewModelFactory((application as Application).koefisiensiAmbangTipisSegitigaRepository)
    }

    var detailBangunan = ""
    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0
    var cd: ArrayList<String> = ArrayList()
    var cv: ArrayList<String> = ArrayList()
    var atsData: AmbangTipisSegitigaModel = AmbangTipisSegitigaModel(null, 0, 0.toFloat(), 0.toFloat(), 0.toFloat())
    var pengambilanDataById: List<PengambilanDataModel> = ArrayList()
    var isInit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAmbangTajamSegitigaBinding.inflate(layoutInflater)
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

        atsViewModel.getatsDataById(idTipeBangunan.toInt())
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            val alps = Intent(this@AmbangTajamSegitigaActivity, AnalisisActivity::class.java)
            alps.putExtra("id_tipe_bangunan", idTipeBangunan)
            alps.putExtra("tipe_bangunan", detailBangunan)
            alps.putExtra("id_base_data", idBaseData)
            alps.putExtra("b", String.format(Locale.ENGLISH, "%.3f", atsData.b))
            startActivity(alps)
            finish()
        }
    }

    private fun setVariable() {
        if (atsData.id != null) {
            //set table title labels
            LegacyTableView.insertLegacyTitle("B", "θ", "p")
            LegacyTableView.insertLegacyContent(
                String.format(Locale.ENGLISH, "%.3f", atsData.b),
                String.format(Locale.ENGLISH,"%.3f", atsData.o),
                String.format(Locale.ENGLISH,"%.3f", atsData.p))

            val readTitle = LegacyTableView.readLegacyTitle()
            val readBody = LegacyTableView.readLegacyContent()
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
                LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-", "θ", "ϑHt", "hef")

                val o = atsData.o.toInt().toString()
                val sudut = ambangTipisSegitigaSudutViewModel.getAmbangTipisSegitigaSudutById(atsData.o.toInt())
                var dht: Float = 0.toFloat()
                if (sudut != null) {
                    dht = sudut.dht
                }
                val hef: Float = o.toFloat() + dht
                for (i in it .indices) {
                    LegacyTableView.insertLegacyContent((i + 1).toString(), o,
                        String.format(Locale.ENGLISH,"%.3f", dht), String.format(Locale.ENGLISH,"%.3f", hef))
                }

                val readTitle = LegacyTableView.readLegacyTitle()
                val readBody = LegacyTableView.readLegacyContent()
                mBinding.atsReaktif.setTitle(readTitle)
                mBinding.atsReaktif.setContent(readBody)

                //depending on the phone screen size default table scale is 100
                //you can change it using this method
                //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

                //if you want a smaller table, change the padding setting
                mBinding.atsReaktif.setTablePadding(7);

                //to enable users to zoom in and out:
                mBinding.atsReaktif.setZoomEnabled(true)
                mBinding.atsReaktif.setShowZoomControls(true)

                //remember to build your table as the last step
                mBinding.atsReaktif.build()

                setCd(sudut)
            }
        })
    }

    private fun setCd(sudut: AmbangTipisSegitigaSudutModel) {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-", "θ", "Cd")
        val o = atsData.o.toInt().toString()
        var cd = 0.toFloat()
        if (sudut != null) {
            cd = sudut.dht
        }
        for (i in pengambilanDataById.indices) {
            LegacyTableView.insertLegacyContent((i + 1).toString(), o,
                String.format(Locale.ENGLISH,"%.3f", cd))
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
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

        setCdPenuh()
    }

    private fun setCdPenuh() {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-", "p", "b", "p/b", "h", "h/p", "Cd")
        for (i in pengambilanDataById.indices) {
            val p: String = if (atsData.o.toInt() == 90) String.format(Locale.ENGLISH,"%.2f", atsData.p) else "Bukan 90"
            val b = String.format(Locale.ENGLISH,"%.2f", atsData.b)
            var pDivideB: String = if (p == "Bukan 90") "0.0" else String.format(Locale.ENGLISH,"%.1f",  p.toFloat() / b.toFloat())
            if (pDivideB.contains("infinity")) {
                pDivideB = "0.0"
            }
            val h1 = String.format(Locale.ENGLISH,"%.2f", pengambilanDataById[i].h1)
            var hDivideP: String = if (p == "Bukan 90") "0.0" else String.format(Locale.ENGLISH,"%.2f", h1.toFloat() / p.toFloat())
            if (hDivideP.contains("infinity")) {
                hDivideP = "0.0"
            }
            val koefisiensi = koefisiensiAmbangTipisSegitigaViewModel.getAmbangTipisSegitigaCdViewModelById(
                nilai = pDivideB.toFloat(), hp = hDivideP.toFloat())
            var cdValue = "0.0"
            if (koefisiensi != null) {
                cdValue = String.format(Locale.ENGLISH,"%.3f", koefisiensi.nilaiCd)
            }

            cd.add(cdValue)
            LegacyTableView.insertLegacyContent((i + 1).toString(), p, b, pDivideB, h1, hDivideP, cdValue)
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.atsCdPenuh.setTitle(readTitle)
        mBinding.atsCdPenuh.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.atsCdPenuh.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.atsCdPenuh.setZoomEnabled(true)
        mBinding.atsCdPenuh.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.atsCdPenuh.build()

        setDebit()
    }

    private fun setDebit() {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","Cd", "g", "θ", "H", "Q")

        val g: String = String.format(Locale.ENGLISH, "%.1f", (9.8).toFloat())
        val o = atsData.o.toInt().toString()
        for (i in cd.indices) {
            val h1: String = String.format(Locale.ENGLISH, "%.2f", pengambilanDataById[i].h1)
            val q:Float = ((8/15).toFloat() * cd[i].toFloat() *
                    sqrt((2 * g.toFloat())) * tan(Math.toRadians((o.toFloat() / 2).toDouble())).toFloat() *
                    h1.toFloat().pow(2.5.toFloat()))


            LegacyTableView.insertLegacyContent((i + 1).toString(), cd[i], g, o, h1, String.format(Locale.ENGLISH, "%.3f", q))
        }

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
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