package id.ias.calculationwaterdebit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.KoefisiensiAliranSempurnaModel
import id.ias.calculationwaterdebit.database.model.OrificeModel
import id.ias.calculationwaterdebit.database.model.ParshallFlumeModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.databinding.ActivityParshallFlumeBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import id.ias.calculationwaterdebit.util.MessageDialogUtil
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class ParshallFlumeActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    val back = MessageDialogUtil()
    private lateinit var mBinding: ActivityParshallFlumeBinding

    private val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory((application as Application).pengambilanDataRepository)
    }

    private val parshallFlumeViewModel: ParshallFlumeViewModel by viewModels {
        ParshallFlumeViewModelFactory((application as Application).parshallFlumeRepository)
    }

    private val koefisiensiAliranSempurnaViewModel: KoefisiensiAliranSempurnaViewModel by viewModels {
        KoefisiensiAliranSempurnaViewModelFactory((application as Application).koefisiensiAliranSempurnaRepository)
    }

    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0
    var detailBangunan: String = ""
    var isInit = true
    var pengambilanDataModel: List<PengambilanDataModel> = ArrayList()
    var parshallFlumeModel: ParshallFlumeModel = ParshallFlumeModel(null, 0, "0".toFloat())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                detailBangunan = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
                idBaseData = it.getLongExtra("id_base_data", 0)
            }
        }

        mBinding = ActivityParshallFlumeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        loading.show(this)
        setViewModel()
        setAction()

        parshallFlumeViewModel.getParshallFlumeDataById(idTipeBangunan.toInt())
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            val alps = Intent(this@ParshallFlumeActivity, AnalisisActivity::class.java)
            alps.putExtra("id_tipe_bangunan", idTipeBangunan)
            alps.putExtra("tipe_bangunan", detailBangunan)
            alps.putExtra("id_base_data", idBaseData)
            alps.putExtra("b", String.format(Locale.ENGLISH,"%.3f", parshallFlumeModel.lebarTenggorokan))
            startActivity(alps)
            finish()
        }
    }

    private fun setVariable() {
        //set table title labels
        LegacyTableView.insertLegacyTitle("b")
        LegacyTableView.insertLegacyContent(
                String.format(Locale.ENGLISH,"%.3f", parshallFlumeModel.lebarTenggorokan))

        val readTitle = LegacyTableView.readLegacyTitle()
        val readBody = LegacyTableView.readLegacyContent()
        mBinding.parshallFlumeTable.setTitle(readTitle)
        mBinding.parshallFlumeTable.setContent(readBody)

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.parshallFlumeTable.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.parshallFlumeTable.setZoomEnabled(true)
        mBinding.parshallFlumeTable.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.parshallFlumeTable.build()

        checkAliranSempurna()
    }

    private fun checkAliranSempurna() {
        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            if (isInit) {
                isInit = false
                pengambilanDataModel = it
                LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-", "Ha", "Hb", "d", "dc", "Tipe Aliran")
                var koefisiensiList: ArrayList<KoefisiensiAliranSempurnaModel> = ArrayList()
                var setData: ArrayList<Array<String>> = ArrayList()
                for (i in it.indices) {
                    val ha: String = String.format(Locale.ENGLISH,"%.2f", it[i].h1)
                    val hb: String = String.format(Locale.ENGLISH,"%.2f", it[i].hb)
                    val d: String = String.format(Locale.ENGLISH,"%.2f", it[i].h1 - it[i].hb)
                    var koefisiensi = koefisiensiAliranSempurnaViewModel.getKoefisiensiAliranSempurnaById(parshallFlumeModel.lebarTenggorokan)
                    var dc:String = String.format(Locale.ENGLISH,"%.3f",
                            if (koefisiensi != null) koefisiensi.dc else "0.0")
                    var aliran = if (d > dc) "Aliran Sempurna" else "Aliran Tidak Sempurna"

                    setData.add(arrayOf(ha, d, aliran))
                    koefisiensiList.add(koefisiensi)
                    LegacyTableView.insertLegacyContent((i + 1).toString(), ha, hb, d, dc, aliran)
                }

                val readTitle = LegacyTableView.readLegacyTitle()
                val readBody = LegacyTableView.readLegacyContent()
                mBinding.parshallFlumeTable2.setTitle(readTitle)
                mBinding.parshallFlumeTable2.setContent(readBody)

                //depending on the phone screen size default table scale is 100
                //you can change it using this method
                //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

                //if you want a smaller table, change the padding setting
                mBinding.parshallFlumeTable2.setTablePadding(7);

                //to enable users to zoom in and out:
                mBinding.parshallFlumeTable2.setZoomEnabled(true)
                mBinding.parshallFlumeTable2.setShowZoomControls(true)

                //remember to build your table as the last step
                mBinding.parshallFlumeTable2.build()

                setDebit(setData, koefisiensiList)
            }
        })
    }

    private fun setDebit(data: ArrayList<Array<String>>, koefisiensiList: ArrayList<KoefisiensiAliranSempurnaModel>) {
        LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","b", "C", "n", "Q")
        for (i in koefisiensiList.indices) {
            val b: String = String.format(Locale.ENGLISH,"%.3f", parshallFlumeModel.lebarTenggorokan)
            val c: String = String.format(Locale.ENGLISH,"%.3f", koefisiensiList[i].c)
            val n: String = String.format(Locale.ENGLISH,"%.3f", koefisiensiList[i].n)
            val q: String =
                    if (data[i][2] == "Aliran Sempurna") String.format(Locale.ENGLISH,"%.3f",
                            (b.toFloat() * c.toFloat() * data[i][0].toFloat().pow(koefisiensiList[i].n)))
                    else String.format(Locale.ENGLISH, "%.3f", (b.toFloat() * c.toFloat() * data[i][0].toFloat().pow(koefisiensiList[i].n)) -
                            (0.07.toFloat() * ((data[i][0].toFloat() / abs((1.8.toFloat() /
                                    data[i][1].toFloat()).pow(8 - 2.46.toFloat())) *
                                    0.305.toFloat()).pow(4.57.toFloat() -
                                    (3.14.toFloat() * data[i][0].toFloat())) +
                                    data[i][1].toFloat()) * b.toFloat().pow(0.815.toFloat())))

            pengambilanDataViewModel.update(pengambilanDataModel[i].id!!, q.toFloat())
            LegacyTableView.insertLegacyContent((i + 1).toString(), b, c, n, q)

            val readTitle = LegacyTableView.readLegacyTitle()
            val readBody = LegacyTableView.readLegacyContent()
            mBinding.parshallFlumeDebit.setTitle(readTitle)
            mBinding.parshallFlumeDebit.setContent(readBody)

            //depending on the phone screen size default table scale is 100
            //you can change it using this method
            //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

            //if you want a smaller table, change the padding setting
            mBinding.parshallFlumeDebit.setTablePadding(7);

            //to enable users to zoom in and out:
            mBinding.parshallFlumeDebit.setZoomEnabled(true)
            mBinding.parshallFlumeDebit.setShowZoomControls(true)

            //remember to build your table as the last step
            mBinding.parshallFlumeDebit.build()
            loading.dialog.dismiss()
        }
    }

    private fun setViewModel() {
        parshallFlumeViewModel.parshallFlumeById.observe(this, {
            if (it != null) {
                parshallFlumeModel = it
                setVariable()
            }
        })
    }

    override fun onBackPressed() {
        back.show(this, object: MessageDialogUtil.DialogListener {
            override fun onYes(action: Boolean) {
                if (action) {
                    finish()
                }
            }
        })
    }
}