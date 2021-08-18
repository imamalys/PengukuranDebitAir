package id.ias.calculationwaterdebit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.viewmodel.OrificeViewModel
import id.ias.calculationwaterdebit.database.viewmodel.OrificeViewModelFactory
import id.ias.calculationwaterdebit.database.viewmodel.PengambilanDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.PengambilanDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityOrificeBinding
import id.ias.calculationwaterdebit.util.MessageDialogUtil
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import id.ias.calculationwaterdebit.viewmodel.OrificeActivityViewModel
import id.ias.calculationwaterdebit.viewmodel.OrificeActivityViewModelFactory
import java.util.*
import kotlin.math.sqrt

class OrificeActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    val back = MessageDialogUtil()
    private lateinit var mBinding: ActivityOrificeBinding

    private val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory((application as Application).pengambilanDataRepository)
    }

    private val orificeActivityViewModel: OrificeActivityViewModel by viewModels {
        OrificeActivityViewModelFactory()
    }

    private val orificeViewModel: OrificeViewModel by viewModels {
        OrificeViewModelFactory((application as Application).orificeRepository)
    }

    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0
    var detailBangunan: String = ""
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

        mBinding = ActivityOrificeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        loading.show(this)
        setViewModel()
        setAction()

        orificeViewModel.getOrificeDataById(idTipeBangunan.toInt())
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            val alps = Intent(this@OrificeActivity, AnalisisActivity::class.java)
            alps.putExtra("id_tipe_bangunan", idTipeBangunan)
            alps.putExtra("tipe_bangunan", detailBangunan)
            alps.putExtra("id_base_data", idBaseData)
            alps.putExtra("b", String.format(Locale.ENGLISH,"%.3f", orificeActivityViewModel.orificeData.lebarLubang))
            startActivity(alps)
            finish()
        }
    }

    private fun setVariable() {
        if (orificeActivityViewModel.orificeData.id != null) {
            //set table title labels
            LegacyTableView.insertLegacyTitle("Bc", "W")
            LegacyTableView.insertLegacyContent(
                String.format(Locale.ENGLISH,"%.3f", orificeActivityViewModel.orificeData.lebarLubang),
                String.format(Locale.ENGLISH,"%.3f", orificeActivityViewModel.orificeData.tinggiLubang))

            val readTitle = LegacyTableView.readLegacyTitle()
            val readBody = LegacyTableView.readLegacyContent()
            mBinding.orificeTable.setTitle(readTitle)
            mBinding.orificeTable.setContent(readBody)

            //depending on the phone screen size default table scale is 100
            //you can change it using this method
            //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

            //if you want a smaller table, change the padding setting
            mBinding.orificeTable.setTablePadding(7);

            //to enable users to zoom in and out:
            mBinding.orificeTable.setZoomEnabled(true)
            mBinding.orificeTable.setShowZoomControls(true)

            //remember to build your table as the last step
            mBinding.orificeTable.build()

            setDebit()
        }
    }

    private fun setDebit() {
        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            if (isInit) {
                isInit = false
                LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","C", "Bc", "W", "A", "g",
                    "Hb", "H1", "z", "Q")

                val c: String = String.format(Locale.ENGLISH,"%.2f", (0.7).toFloat())
                val bc: String = String.format(Locale.ENGLISH,"%.3f", orificeActivityViewModel.orificeData.lebarLubang)
                val w: String = String.format(Locale.ENGLISH,"%.3f", orificeActivityViewModel.orificeData.tinggiLubang)
                val a: String = String.format(Locale.ENGLISH,"%.2f", (35.60).toFloat())
                val g: String = String.format(Locale.ENGLISH,"%.1f", (9.8).toFloat())

                for (i in it.indices) {
                    val hb: String = String.format(Locale.ENGLISH,"%.2f", it[i].hb)
                    val h1: String = String.format(Locale.ENGLISH,"%.2f", it[i].h1)
                    val z: String = String.format(Locale.ENGLISH,"%.2f", hb.toFloat() - h1.toFloat())
                    var q:String = String.format(Locale.ENGLISH,"%.3f",
                        c.toFloat() * a.toFloat() * sqrt((2 * g.toFloat() * z.toFloat())))

                    q = q.replace(",", ".")
                    pengambilanDataViewModel.update(it[i].id!!, q.toFloat())

                    LegacyTableView.insertLegacyContent((i + 1).toString(), c, bc, w, a, g, hb, h1, z, q)
                }

                val readTitle = LegacyTableView.readLegacyTitle()
                val readBody = LegacyTableView.readLegacyContent()
                mBinding.orificeDebit.setTitle(readTitle)
                mBinding.orificeDebit.setContent(readBody)

                //depending on the phone screen size default table scale is 100
                //you can change it using this method
                //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

                //if you want a smaller table, change the padding setting
                mBinding.orificeDebit.setTablePadding(7)

                //to enable users to zoom in and out:
                mBinding.orificeDebit.setZoomEnabled(true)
                mBinding.orificeDebit.setShowZoomControls(true)

                //remember to build your table as the last step
                mBinding.orificeDebit.build()
                loading.dialog.dismiss()
            }
        })
    }

    private fun setViewModel() {
        orificeViewModel.orificeById.observe(this, {
            if (it.id != null) {
                orificeActivityViewModel.orificeData = it
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