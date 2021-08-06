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

class AmbangLebarPengontrolSegiempatActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityAmbangLebarPengontrolSegiempatBinding
    private val alpsActivityViewModel: AlpsActivityViewModel by viewModels {
        AlpsActivityViewModelFactory()
    }

    private val ambangLebarPengontrolSegiempatViewModel: AmbangLebarPengontrolSegiempatViewModel by viewModels {
        AmbangLebarPengontrolSegiempatViewModelFactory((application as Application).alpsRepository)
    }

    var idTipeBangunan: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAmbangLebarPengontrolSegiempatBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                alpsActivityViewModel.detailBangunan = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
                alpsActivityViewModel.idPengambilanData = it.getIntExtra("id_pengambilan_data", 0)
            }
        }

        setViewModel()

        ambangLebarPengontrolSegiempatViewModel.getalpsDataById(idTipeBangunan.toInt())
    }

    private fun setCd() {
        if (alpsActivityViewModel.alpsData.id != null) {
            //set table title labels
            LegacyTableView.insertLegacyTitle("Bc", "B1", "L", "P", "m", "w", "b1")
            LegacyTableView.insertLegacyContent(String.format("%.3f", alpsActivityViewModel.alpsData.lebarAmbang),
                String.format("%.3f", alpsActivityViewModel.alpsData.lebarDasar),
                String.format("%.3f", alpsActivityViewModel.alpsData.panjangAmbang),
                String.format("%.3f", alpsActivityViewModel.alpsData.tinggiAmbang),
                String.format("%.3f", alpsActivityViewModel.alpsData.tinggiDiatasAmbang),
                String.format("%.3f", alpsActivityViewModel.alpsData.tinggiDibawahAmbang),
                String.format("%.3f", alpsActivityViewModel.alpsData.lebarAtas))

            mBinding.alpsTable.setTitle(LegacyTableView.readLegacyTitle())
            mBinding.alpsTable.setContent(LegacyTableView.readLegacyContent())

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
        }

    }

    private fun setViewModel() {
        ambangLebarPengontrolSegiempatViewModel.alpsById.observe(this, {
            if (it.id != null) {
                alpsActivityViewModel.alpsData = it
                setCd()
            }
        })
    }
}