package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.CrumpModel
import id.ias.calculationwaterdebit.database.model.KoefisiensiCutThroatedFlumeModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.viewmodel.CrumpViewModel
import id.ias.calculationwaterdebit.database.viewmodel.CrumpViewModelFactory
import id.ias.calculationwaterdebit.database.viewmodel.PengambilanDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.PengambilanDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityCrumpBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.sqrt

class CrumpActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    private lateinit var mBinding: ActivityCrumpBinding

    private val crumpViewModel: CrumpViewModel by viewModels {
        CrumpViewModelFactory((application as Application).crumpRepository)
    }

    private val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory((application as Application).pengambilanDataRepository)
    }

    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0
    var detailBangunan: String = ""
    var pengambilanData: List<PengambilanDataModel> = ArrayList()
    var crumpData: CrumpModel = CrumpModel(null, 0, 0.toFloat(), 0.toFloat())
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

        mBinding = ActivityCrumpBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        loading.show(this)
        setViewModel()
        setAction()

        crumpViewModel.getCrumpDataById(idTipeBangunan.toInt())
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            val crump = Intent(this@CrumpActivity, AnalisisActivity::class.java)
            crump.putExtra("id_tipe_bangunan", idTipeBangunan)
            crump.putExtra("tipe_bangunan", detailBangunan)
            crump.putExtra("id_base_data", idBaseData)
            crump.putExtra("b", String.format(Locale.ENGLISH, "%.3f", crumpData.bC))
            startActivity(crump)
            finish()
        }
    }

    private fun setVariable() {
        if (crumpData.id != null) {
            //set table title labels
            LegacyTableView.insertLegacyTitle("Bc", "W")
            LegacyTableView.insertLegacyContent(
                String.format(Locale.ENGLISH, "%.3f", crumpData.bC),
                String.format(Locale.ENGLISH,"%.3f", crumpData.w))

            val readTitle = LegacyTableView.readLegacyTitle()
            val readBody = LegacyTableView.readLegacyContent()

            mBinding.crumpTable.setTheme(LegacyTableView.CUSTOM)
            mBinding.crumpTable.setBackgroundColor(Color.TRANSPARENT)
            mBinding.crumpTable.setHeaderBackgroundLinearGradientTOP("#3E8E7E")
            mBinding.crumpTable.setHeaderBackgroundLinearGradientBOTTOM("#3E8E7E")
            mBinding.crumpTable.setTitle(readTitle)
            mBinding.crumpTable.setContent(readBody)

            //depending on the phone screen size default table scale is 100
            //you can change it using this method
            //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

            //if you want a smaller table, change the padding setting
            mBinding.crumpTable.setTablePadding(7);

            //to enable users to zoom in and out:
            mBinding.crumpTable.setZoomEnabled(true)
            mBinding.crumpTable.setShowZoomControls(true)

            //remember to build your table as the last step
            mBinding.crumpTable.build()

            setDebit()
        }
    }

    private fun setDebit() {
        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            if (isInit) {
                isInit = false
                pengambilanData = it

                LegacyTableView.insertLegacyTitle("Banyaknya variasi air Ke-","C", "Bc", "W", "A", "g", "h1", "Q")

                val c = (0.94).toFloat()
                val bC = crumpData.bC
                val wValue = crumpData.w
                val aValue = bC * wValue
                val g = (9.8).toFloat()

                for (i in it.indices) {
                    val h1 = pengambilanData[i].h1
                    val q = c * aValue * (sqrt(2 * g) * (h1 - wValue))

                    pengambilanDataViewModel.update(pengambilanData[i].id!!, q)

                    LegacyTableView.insertLegacyContent((i + 1).toString(),
                        String.format(Locale.ENGLISH,"%.3f", c),
                        String.format(Locale.ENGLISH,"%.3f", bC),
                        String.format(Locale.ENGLISH,"%.3f", wValue),
                        String.format(Locale.ENGLISH,"%.3f", aValue),
                        String.format(Locale.ENGLISH,"%.3f", g),
                        String.format(Locale.ENGLISH,"%.3f", h1),
                        String.format(Locale.ENGLISH,"%.3f", q))
                }

                val readTitle = LegacyTableView.readLegacyTitle()
                val readBody = LegacyTableView.readLegacyContent()

                mBinding.crumpDebit.setTheme(LegacyTableView.CUSTOM)
                mBinding.crumpDebit.setBackgroundColor(Color.TRANSPARENT)
                mBinding.crumpDebit.setHeaderBackgroundLinearGradientTOP("#3E8E7E")
                mBinding.crumpDebit.setHeaderBackgroundLinearGradientBOTTOM("#3E8E7E")
                mBinding.crumpDebit.setTitle(readTitle)
                mBinding.crumpDebit.setContent(readBody)

                //depending on the phone screen size default table scale is 100
                //you can change it using this method
                //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

                //if you want a smaller table, change the padding setting
                mBinding.crumpDebit.setTablePadding(7)

                //to enable users to zoom in and out:
                mBinding.crumpDebit.setZoomEnabled(true)
                mBinding.crumpDebit.setShowZoomControls(true)

                //remember to build your table as the last step
                mBinding.crumpDebit.build()
                loading.dialog.dismiss()
            }
        })
    }

    private fun setViewModel() {
        crumpViewModel.crumpById.observe(this, {
            if (it.id != null) {
                crumpData = it
                setVariable()
            }
        })
    }
}