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
    var idBaseData: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDataInformasiBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent.hasExtra("id_base_data")) {
            idBaseData = intent.getLongExtra("id_base_data", 0)
            mBinding.btnNext.text = "Save"
        } else {
            calendar = Calendar.getInstance()
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
            mBinding.btnNext.text = "Next"
        }

        setAction()
        setViewModel()
    }

    private fun setAction() {
        mBinding.tieNamaSaluran.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mBinding.tieNamaSaluran.hint = ""
            } else {
                mBinding.tieNamaSaluran.hint = null
            }
        }

        mBinding.tieNamaDaerah.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mBinding.tieNamaDaerah.hint = ""
            } else {
                mBinding.tieNamaDaerah.hint = null
            }
        }

        mBinding.tieInstansiPengelola.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mBinding.tieInstansiPengelola.hint = "BBWS/BWS/Dinas PU SDA Provinsi/Kabupaten/Kota"
            } else {
                mBinding.tieNamaPengukur.hint = null
            }
        }

        mBinding.tieProvinsi.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mBinding.tieProvinsi.hint = ""
            } else {
                mBinding.tieProvinsi.hint = null
            }
        }

        mBinding.tieKabupaten.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mBinding.tieKabupaten.hint = ""
            } else {
                mBinding.tieKabupaten.hint = null
            }
        }

        mBinding.tieNoPengukuran.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mBinding.tieNoPengukuran.hint = "(Nomenklatur Bangun Ukur)"
            } else {
                mBinding.tieNamaPengukur.hint = null
            }
        }

        mBinding.tieNamaPengukur.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mBinding.tieNamaPengukur.hint = "(Nama)"
            } else {
                mBinding.tieNamaPengukur.hint = null
            }
        }

        mBinding.tieTanggal.setOnClickListener {
            try {
                onCreateDialog().show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        mBinding.btnNext.setOnClickListener {
            when("") {
                mBinding.tieNamaSaluran.text.toString() -> {
                    ToastUtils.showLong("Nama Saluran tidak boleh kosong")
                }

                mBinding.tieNamaDaerah.text.toString() -> {
                    ToastUtils.showLong("Nama Daerah Irigasi tidak boleh kosong")
                }
                mBinding.tieInstansiPengelola.text.toString() -> {
                    ToastUtils.showLong("Instansi Pengelola tidak boleh kosong")
                }
                mBinding.tieProvinsi.text.toString() -> {
                    ToastUtils.showLong("Provinsi tidak boleh kosong")
                }
                mBinding.tieKabupaten.text.toString() -> {
                    ToastUtils.showLong("Kabupaten tidak boleh kosong")
                }
                mBinding.tieTanggal.text.toString() -> {
                    ToastUtils.showLong("Tanggal tidak boleh kosong")
                }
                mBinding.tieNoPengukuran.text.toString() -> {
                    ToastUtils.showLong("No. Pengukuran tidak boleh kosong")
                }
                mBinding.tieNamaPengukur.text.toString() -> {
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
        if (idBaseData.toInt() != 0) {
            baseDataViewModel.updateLoad(idBaseData.toInt(), mBinding.tieNamaSaluran.text.toString(),
                    mBinding.tieNamaDaerah.text.toString(), mBinding.tieInstansiPengelola.text.toString(),
                    mBinding.tieProvinsi.text.toString(), mBinding.tieKabupaten.text.toString(),
                    mBinding.tieTanggal.text.toString(), mBinding.tieNoPengukuran.text.toString(),
                    mBinding.tieNamaPengukur.text.toString())
        } else {
            val baseData = BaseDataModel(
                    null,
                    mBinding.tieNamaSaluran.text.toString(),
                    mBinding.tieNamaDaerah.text.toString(),
                    mBinding.tieInstansiPengelola.text.toString(),
                    mBinding.tieProvinsi.text.toString(),
                    mBinding.tieKabupaten.text.toString(),
                    mBinding.tieTanggal.text.toString(),
                    mBinding.tieNoPengukuran.text.toString(),
                    mBinding.tieNamaPengukur.text.toString(),
                    null, null, null, null, null, null,
                    null, null, null, null, null, 0,
                    0, 0, 0, 0 ,0
            )
            baseDataViewModel.insert(baseData)
        }
    }

    private fun onCreateDialog(): Dialog {
        return DatePickerDialog(
            this,
            myDateListener, year, month, day
        )
    }

    private val myDateListener: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { arg0, arg1, arg2, arg3 ->
            mBinding.tieTanggal.setText(DateUtil.getDateSpecific(arg1, arg2, arg3))
        }

    private fun setViewModel() {
        if (idBaseData.toInt() != 0) {
            baseDataViewModel.getBaseDataById(idBaseData.toInt())
            baseDataViewModel.baseDataById.observe(this, {
                mBinding.tieNamaSaluran.setText(it.namaSaluran)
                mBinding.tieNamaDaerah.setText(it.namaDaerahIrigasi)
                mBinding.tieInstansiPengelola.setText(it.wilayahKewenangan)
                mBinding.tieProvinsi.setText(it.provinsi)
                mBinding.tieKabupaten.setText(it.kabupaten)
                mBinding.tieTanggal.setText(it.tanggal)
                mBinding.tieNoPengukuran.setText(it.noPengukuran)
                mBinding.tieNamaPengukur.setText(it.namaPengukur)
            })
        }

        baseDataViewModel.insertId.observe(this, {
            if (it.toInt() != 0) {
                loading.dialog.dismiss()
                idBaseData = it
                val intent = Intent(this@DataInformasiActivity, CheckKondisiActivity::class.java)
                intent.putExtra("id_base_data", it)
                startActivity(intent)
            }
        })

        baseDataViewModel.baseDataUpdate.observe(this, {
            if (it.toInt() != 0) {
                loading.dialog.dismiss()
                val intent = Intent(this@DataInformasiActivity, CheckKondisiActivity::class.java)
                intent.putExtra("id_base_data", idBaseData.toLong())
                startActivity(intent)
            }
        })
    }

    override fun onBackPressed() {
        back.show(this, title = "Apakah anda yakin ingin kembali?", yes = "Ya", no = "Tidak",
                object: MessageDialogUtil.DialogListener {
                    override fun onYes(action: Boolean) {
                        if (action) {
                            finish()
                        }
                    }
                })
    }
}