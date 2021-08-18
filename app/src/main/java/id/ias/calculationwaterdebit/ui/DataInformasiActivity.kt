package id.ias.calculationwaterdebit.ui

import android.app.DatePickerDialog
import android.app.Dialog
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
import id.ias.calculationwaterdebit.util.MessageDialogUtil
import id.ias.calculationwaterdebit.util.DateUtil
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import java.util.*

class DataInformasiActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    val back = MessageDialogUtil()
    lateinit var mBinding: ActivityDataInformasiBinding
    private val baseDataViewModel: BaseDataViewModel by viewModels {
        BaseDataViewModelFactory((application as Application).baseDataRepository)
    }
    private lateinit var calendar: Calendar
    private var year = 0
    private  var month:Int = 0
    private  var day:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDataInformasiBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        setAction()
        setViewModel()
    }

    private fun setAction() {
        mBinding.etTanggal.setOnClickListener {
            try {
                onCreateDialog().show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

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
            null, null, null, null, null, 0
        )
        baseDataViewModel.insert(baseData)
    }

    private fun onCreateDialog(): Dialog {
        return DatePickerDialog(
            this,
            myDateListener, year, month, day
        )
    }

    private val myDateListener: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { arg0, arg1, arg2, arg3 ->
            mBinding.etTanggal.setText(DateUtil.getDateSpecific(arg1, arg2, arg3))
        }

    private fun setViewModel() {
        baseDataViewModel.insertId.observe(this, {
            if (it.toInt() != 0) {
                loading.dialog.dismiss()
                val intent = Intent(this@DataInformasiActivity, CheckKondisiActivity::class.java)
                intent.putExtra("id_base_data", it)
                startActivity(intent)
                finish()
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