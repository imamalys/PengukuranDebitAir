package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.levitnudi.legacytableview.LegacyTableView
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.PiasModel
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.databinding.ActivityVariasiOutputBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import id.ias.calculationwaterdebit.viewmodel.VariasiOutputViewModel
import id.ias.calculationwaterdebit.viewmodel.VariasiOutputViewModelFactory

class VariasiOutputActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    private lateinit var mBinding: ActivityVariasiOutputBinding
    private val variasiOutputViewModel: VariasiOutputViewModel by viewModels {
        VariasiOutputViewModelFactory()
    }
    private val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory((application as Application).pengambilanDataRepository)
    }

    private val baseDataViewModel: BaseDataViewModel by viewModels {
        BaseDataViewModelFactory((application as Application).baseDataRepository)
    }

    private val piasDataViewModel: PiasDataViewModel by viewModels {
        PiasDataViewModelFactory((application as Application).piasRepository)
    }
    var idTipeBangunan: Long = 0
    var currentFormData: Int = 0
    var idBaseData: Long = 0
    val h2MinAll: ArrayList<Float> = ArrayList()
    val h2MaxAll: ArrayList<Float> = ArrayList()
    val debitSaluranAll: ArrayList<Float> = ArrayList()
    var isUdated = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityVariasiOutputBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                variasiOutputViewModel.detailBangunan = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
                idBaseData = it.getLongExtra("id_base_data", 0)
            }
        }

        loading.show(this)
        setViewModel()
        setAction()

        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            if (currentFormData < variasiOutputViewModel.pengambilanDataById.size) {
                currentFormData += 1
            }
            getPias()
        }

        mBinding.btnPrevious.setOnClickListener {
            if (currentFormData != 0) {
                currentFormData -= 1
                getPias()
            }
        }

        mBinding.btnNextCalc.setOnClickListener {
            loading.show(this)
            val minH2 = String.format("%.3f", h2MinAll.minOrNull())
            val maxH2 = String.format("%.3f", h2MaxAll.maxOrNull())
            val minDebitSaluran = String.format("%.3f", debitSaluranAll.minOrNull())
            val maxDebitSaluran = String.format("%.3f", debitSaluranAll.maxOrNull())

            baseDataViewModel.update(idBaseData.toInt(), minH2, maxH2, minDebitSaluran, maxDebitSaluran)
        }
    }

    private fun setOutput(piasDatas: List<PiasModel>) {
        //set table title labels
        if (currentFormData + 1 == variasiOutputViewModel.pengambilanDataById.size) {
            mBinding.btnNext.visibility = View.GONE
            mBinding.btnPrevious.visibility = View.GONE
            mBinding.btnNextCalc.visibility = View.VISIBLE
        } else {
            mBinding.btnNext.visibility = View.VISIBLE
            mBinding.btnPrevious.visibility = View.VISIBLE
            mBinding.btnNextCalc.visibility = View.GONE
        }

        LegacyTableView.insertLegacyTitle("Jarak", "H2", "Saluran Basah ke-",
            "Metode Pengambilan", "0,8D", "0,6D", "0,2D", "Rai (m/s)", "Rata-rata (m/s",
            "Luas Basah (m2)", "Debit (m3)")

        LegacyTableView.insertLegacyContent("0", "0", "Tepi", " ", "-", "-", "-", "0", "0", "0", "0")
        mBinding.etH1.setText(String.format("%.3f", variasiOutputViewModel.pengambilanDataById[currentFormData].h1))
        var debitSaluran = "0".toFloat()
        var jumlahRataRata = "0".toFloat()
        val h2Put: ArrayList<Float> = ArrayList()
        for (i in piasDatas.indices) {
            val jarak = piasDatas[i].jarakAntarPias
            var previousRai: Float
            var currentRai: Float
            when (piasDatas[i].metodePengmbilan) {
                "Dua Titik" -> {
                    previousRai = if (i == 0) {
                        "0".toFloat()
                    } else {
                        (piasDatas[i - 1].d8 + piasDatas[i - 1].d2) / 2
                    }
                    currentRai = (piasDatas[i].d8 + piasDatas[i].d2) / 2
                }
                "Tiga Titik" -> {
                    previousRai = if (i == 0) {
                        "0".toFloat()
                    } else {
                        (piasDatas[i - 1].d8 + piasDatas[i - 1].d2) / 2 + piasDatas[i - 1].d6
                    }
                    currentRai = (piasDatas[i].d8 + piasDatas[i].d2) / 2 + piasDatas[i].d6
                }
                else -> {
                    previousRai = if (i == 0) {
                        "0".toFloat()
                    } else {
                        piasDatas[i - 1].d6
                    }
                    currentRai = piasDatas[i].d6
                }
            }
            val rataRata = (previousRai + currentRai) / 2
            val previousH2 = (if (i == 0) "0".toFloat() else piasDatas[i - 1].h2)
            val luasBasah = if (jarak > 0.0001) {
                ("0.5".toFloat() * (previousH2 + piasDatas[i].h2) * jarak)
            } else {
                "0".toFloat()
            }
            val debit = luasBasah * rataRata
            LegacyTableView.insertLegacyContent(String.format("%.3f", jarak), String.format("%.3f", piasDatas[i].h2),
                (i + 1).toString(), piasDatas[i].metodePengmbilan, String.format("%.3f", piasDatas[i].d8),
                String.format("%.3f", piasDatas[i].d6), String.format("%.3f", piasDatas[i].d2),
                String.format("%.3f", currentRai),  String.format("%.3f", rataRata),
                String.format("%.3f", luasBasah), String.format("%.3f", debit))

            debitSaluran += debit
            jumlahRataRata += rataRata
            if (i + 1 == piasDatas.size) {
                mBinding.etDebitSaluran.setText(String.format("%.3f", debitSaluran))
                pengambilanDataViewModel.update(variasiOutputViewModel.pengambilanDataById[currentFormData].id!!, jumlahRataRata, debitSaluran)
            }

            h2Put.add(piasDatas[i].h2)

            if (i + 1 == piasDatas.size) {
                h2MinAll.add(h2Put.minOrNull()!!)
                h2MaxAll.add(h2Put.maxOrNull()!!)
                debitSaluranAll.add(debitSaluran)
            }
        }

        mBinding.legacyTableView.setTitle(LegacyTableView.readLegacyTitle())
        mBinding.legacyTableView.setContent(LegacyTableView.readLegacyContent())

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        mBinding.legacyTableView.setTablePadding(7);

        //to enable users to zoom in and out:
        mBinding.legacyTableView.setZoomEnabled(true)
        mBinding.legacyTableView.setShowZoomControls(true)

        //remember to build your table as the last step
        mBinding.legacyTableView.build()

        loading.dialog.dismiss()
    }

    private fun getPias() {
        mBinding.tvVariasiKetinggianAir.text = "Variasi ketinggian air Ke-${currentFormData + 1}"
        piasDataViewModel.getPiasDataById(variasiOutputViewModel.pengambilanDataById[currentFormData].id!!)
    }

    private fun setViewModel() {
        piasDataViewModel.piasDatas.observe(this, {
            setOutput(it)
        })

        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            variasiOutputViewModel.pengambilanDataById = it
            getPias()
        })

        baseDataViewModel.baseDataUpdate.observe(this, {
            if (it != 0) {
                if (isUdated) {
                    isUdated = false
                    loading.dialog.dismiss()
                    when(variasiOutputViewModel.detailBangunan) {
                        "Ambang Lebar Pengontrol Segiempat" -> {
                            val alps = Intent(this@VariasiOutputActivity, AmbangLebarPengontrolSegiempatActivity::class.java)
                            alps.putExtra("id_tipe_bangunan", idTipeBangunan)
                            alps.putExtra("tipe_bangunan", variasiOutputViewModel.detailBangunan)
                            alps.putExtra("id_base_data", idBaseData)
                            startActivity(alps)
                            finish()
                        }
                    }
                }
            }
        })
    }
}