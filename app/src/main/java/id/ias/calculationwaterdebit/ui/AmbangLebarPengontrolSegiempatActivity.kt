package id.ias.calculationwaterdebit.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.databinding.ActivityAmbangLebarPengontrolSegiempatBinding
import id.ias.calculationwaterdebit.viewmodel.AlpsActivityViewModel
import id.ias.calculationwaterdebit.viewmodel.AlpsActivityViewModelFactory
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

    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0
    var cd: ArrayList<Float> = ArrayList()

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

        ambangLebarPengontrolSegiempatViewModel.getalpsDataById(idTipeBangunan.toInt())
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
        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt()).observe(this, {
            LegacyTableView.insertLegacyTitle("h1", "v1", "H1", "L", "H1/L", "Cd")

            for (i in it.indices) {
                val h1 = it[i].h1
                val v1 = (it[i].jumlahRataRata!! / it[i].variasaiKetinggianAir)
                val capitalH1 = (h1 + (v1 * v1) / (2 * 9.8)).toFloat()
                val l = (1.2).toFloat()
                val h1perL = capitalH1 / l
                val cdValue = if (0.1 < h1perL && h1perL > 1) (0.93 + 0.1 * h1perL).toFloat() else (0.9).toFloat()

                cd.add(cdValue)
                LegacyTableView.insertLegacyContent(String.format("%.3f", h1), String.format("%.3f", v1),
                    String.format("%.3f", capitalH1), String.format("%.1f", l), String.format("%.3f", h1perL),
                    String.format("%.3f", cdValue))
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
        })
    }

    private fun setCv() {
        LegacyTableView.insertLegacyTitle("Bc", "p", "A*", "B1", "P", "w", "b1", "mc", "A", "Nilai", "Cv")

        val bentukA =
            if (alpsActivityViewModel.alpsData.lebarAmbang == alpsActivityViewModel.alpsData.lebarAtas)
                "Bentuk Peralihan Persegiempat"
            else if (alpsActivityViewModel.alpsData.lebarAmbang > alpsActivityViewModel.alpsData.lebarAtas ||
                alpsActivityViewModel.alpsData.lebarAmbang < alpsActivityViewModel.alpsData.lebarAtas)
                    "Bentuk Peralihan Trapesium"
            else "Bentuk Peralihan Segitiga"
        for (i in cd.indices) {
            val bc = alpsActivityViewModel.alpsData.lebarDasar
            val pM = alpsActivityViewModel.alpsData.tinggiDiatasAmbang
            val aQuote = bc * pM
            val capitalB1 = alpsActivityViewModel.alpsData.panjangAmbang
            val m = alpsActivityViewModel.alpsData.tinggiDiatasAmbang
            val p = alpsActivityViewModel.alpsData.tinggiAmbang
            val w = alpsActivityViewModel.alpsData.tinggiDibawahAmbang
            val b1 = alpsActivityViewModel.alpsData.lebarAtas
            val mc1: Float = (b1 - capitalB1) / 2
            val mc2: Float = m + p + w
            val mc: Float = if (bentukA == "Bentuk Peralihan Trapesium") sqrt(mc1 * mc1 + mc2 * mc2) else
                mc2 * b1
            val a: Float = if (bentukA == "Bentuk Peralihan Trapesium") sqrt((0.5).toFloat() * (b1 + capitalB1) * (mc2)) else
                mc2 * b1
            val nilai: Float = (capitalB1 * aQuote) / a

        }
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