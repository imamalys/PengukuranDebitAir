package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityDataInformasiBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil

class DataInformasiActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    lateinit var mBinding: ActivityDataInformasiBinding
    private val baseDataViewModel: BaseDataViewModel by viewModels {
        BaseDataViewModelFactory((application as Application).baseDataRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDataInformasiBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setAction()
        setViewModel()
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            when("") {
                mBinding.etNamaDaerah.text.toString() -> {
                    ToastUtils.showLong("Nama Daerah Irigasi tidak boleh kosong")
                }
                mBinding.etWilayahKewenangan.text.toString() -> {
                    ToastUtils.showLong("Wilayah Kewenangan tidak boleh kosong")
                }
                mBinding.etProvinsi.text.toString() -> {
                    ToastUtils.showLong("Provinsi tidak boleh kosong")
                }
                mBinding.etKabupaten.text.toString() -> {
                    ToastUtils.showLong("Kabupaten tidak boleh kosong")
                }
                mBinding.etTanggal.text.toString() -> {
                    ToastUtils.showLong("Tanggal tidak boleh kosong")
                }
                mBinding.etNoPengukuran.text.toString() -> {
                    ToastUtils.showLong("No. Pengukuran tidak boleh kosong")
                }
                mBinding.etNamaPengukur.text.toString() -> {
                    ToastUtils.showLong("Nama Pengukur tidak boleh kosong")
                }
                else -> {
                    next()
                }
            }
        }
    }

    private fun next() {
        loading.show(this)
        val baseData = BaseDataModel(
            null,
            mBinding.etNamaDaerah.text.toString(),
            mBinding.etWilayahKewenangan.text.toString(),
            mBinding.etProvinsi.text.toString(),
            mBinding.etKabupaten.text.toString(),
            mBinding.etTanggal.text.toString(),
            mBinding.etNoPengukuran.text.toString(),
            mBinding.etNamaPengukur.text.toString(),
            null, null, null, null, null, null,
            null, null, null
        )
        baseDataViewModel.insert(baseData)
    }

    private fun setViewModel() {
        baseDataViewModel.insertId.observe(this, {
            if (it.toInt() != 0) {
                loading.dialog.dismiss()
                val intent = Intent(this@DataInformasiActivity, TipeBangunanUkurActivity::class.java)
                intent.putExtra("id_base_data", it)
                startActivity(intent)
                finish()
            }
        })
    }
}