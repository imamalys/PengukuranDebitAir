package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.viewmodel.PengambilanDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.PengambilanDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityPengambilanDataBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import id.ias.calculationwaterdebit.util.MessageDialogUtil
import id.ias.calculationwaterdebit.util.MinMaxFilter
import id.ias.calculationwaterdebit.viewmodel.PengambilanDataActivityViewModel
import id.ias.calculationwaterdebit.viewmodel.PengambilanDataActivityViewModelFactory

class PengambilanDataActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    val back = MessageDialogUtil()
    lateinit var mBinding: ActivityPengambilanDataBinding
    val pengambilanDataActivityViewModel: PengambilanDataActivityViewModel by viewModels {
        PengambilanDataActivityViewModelFactory()
    }
    private val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory((application as Application).pengambilanDataRepository)
    }

    var idTipeBangunan: Long = 0
    var variasiKetinggianAir: String = ""
    var idBaseData: Long = 0
    var isLast = false
    var isBack = false
    var currentVariasi = 0
    var idPengambilanData = 0
    private lateinit var pengambilanDataModel: PengambilanDataModel
    private lateinit var previousPengambilanDataModel: PengambilanDataModel
    private var isMenu = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityPengambilanDataBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                pengambilanDataActivityViewModel.detailBangunan.value = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
                variasiKetinggianAir = if (it.hasExtra("variasi_ketinggian_air"))
                    it.getStringExtra("variasi_ketinggian_air")!! else "0"
                currentVariasi = it.getIntExtra("current_variasi", 0)
                idBaseData = it.getLongExtra("id_base_data", 0)
                isBack = it.getBooleanExtra("is_back", false)
            }
        }

        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt())
        setAction()
        setViewModel()
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            if (pengambilanDataActivityViewModel.checkHaveValue()) {
                if (::pengambilanDataModel.isInitialized) {
                    if (mBinding.tieSaluranBasah.text.toString().toInt() >= pengambilanDataModel.jumlahSaluranBasah) {
                        updateOrCreate()
                    } else {
                        ToastUtils.showLong("Jumlah Penampang Pias harus sama atau lebih besar dari sebelumnya")
                    }
                } else {
                    updateOrCreate()
                }
            } else {
                ToastUtils.showLong("Data masih ada yang kosong, silahkan diisi terlebih dahulu")
            }
        }

        mBinding.tieKetinggianAirBangunan.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != "" && s.toString() != ".") {
                    pengambilanDataActivityViewModel.pengambilValue.value!![0] = s.toString().toFloat()
                } else {
                    pengambilanDataActivityViewModel.pengambilValue.value!![0] = "0".toFloat()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        mBinding.tieKetinggianAirHulu.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != "" && s.toString() != ".") {
                    pengambilanDataActivityViewModel.pengambilValue.value!![1] = s.toString().toFloat()
                } else {
                    pengambilanDataActivityViewModel.pengambilValue.value!![1] = "0".toFloat()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        mBinding.tieSaluranBasah.filters = arrayOf<InputFilter>(MinMaxFilter("1", "100"))
        mBinding.tieSaluranBasah.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != "" && s.toString() != ".") {
                    pengambilanDataActivityViewModel.pengambilValue.value!![2] = s.toString().toFloat()
                    mBinding.tieJumlahPias.setText((s.toString().toInt() - 1).toString())
                } else {
                    pengambilanDataActivityViewModel.pengambilValue.value!![2] = "0".toFloat()
                    mBinding.tieJumlahPias.setText("")
                    mBinding.tieJumlahPias.setHint("0")
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun updateOrCreate() {
        loading.show(this)
        val pengambilanData = PengambilanDataModel(
                if (isBack) idPengambilanData else null,
                idBaseData.toInt(),
                pengambilanDataActivityViewModel.pengambilValue.value!![0],
                pengambilanDataActivityViewModel.pengambilValue.value!![1],
                pengambilanDataActivityViewModel.pengambilValue.value!![2],
                variasiKetinggianAir.toFloat(), null, null, null
        )
        if (isBack) pengambilanDataViewModel.update(pengambilanData) else pengambilanDataViewModel.insert(pengambilanData)
    }

    private fun setViewModel() {
        pengambilanDataViewModel.idUpdateData.observe(this, {
            if (it != 0) {
                loading.dialog.dismiss()
                val intent = Intent(this@PengambilanDataActivity, FormDataActivity::class.java)
                intent.putExtra("id_tipe_bangunan", idTipeBangunan)
                intent.putExtra("tipe_bangunan", pengambilanDataActivityViewModel.detailBangunan.value)
                intent.putExtra("id_pengambilan_data", pengambilanDataModel.id)
                intent.putExtra("id_base_data", idBaseData)
                intent.putExtra("jumlah_pias", mBinding.tieSaluranBasah.text.toString().toInt())
                intent.putExtra("h1", pengambilanDataActivityViewModel.pengambilValue.value!![0])
                intent.putExtra("hb", pengambilanDataActivityViewModel.pengambilValue.value!![1])
                intent.putExtra("variasi_Ketinggian_air", variasiKetinggianAir)
                intent.putExtra("current_variasi", currentVariasi)
                intent.putExtra("is_last", isLast)
                intent.putExtra("is_back", isBack)
                intent.putExtra("is_pengambilan_data_edit", true)
                startActivity(intent)
                finish()
            }
        })
        pengambilanDataViewModel.idPengambilanData.observe(this, {
            if (it.toInt() != 0) {
                loading.dialog.dismiss()
                val intent = Intent(this@PengambilanDataActivity, FormDataActivity::class.java)
                intent.putExtra("id_tipe_bangunan", idTipeBangunan)
                intent.putExtra("tipe_bangunan", pengambilanDataActivityViewModel.detailBangunan.value)
                intent.putExtra("id_pengambilan_data", it.toInt())
                intent.putExtra("id_base_data", idBaseData)
                intent.putExtra("jumlah_pias", mBinding.tieSaluranBasah.text.toString().toInt())
                intent.putExtra("h1", pengambilanDataActivityViewModel.pengambilValue.value!![0])
                intent.putExtra("hb", pengambilanDataActivityViewModel.pengambilValue.value!![1])
                intent.putExtra("variasi_Ketinggian_air", variasiKetinggianAir)
                intent.putExtra("current_variasi", currentVariasi)
                intent.putExtra("is_last", isLast)
                intent.putExtra("is_back", isBack)
                intent.putExtra("is_pengambilan_data_edit", false)
                startActivity(intent)
                finish()
            }
        })

        pengambilanDataViewModel.pengambilanDataById.observe(this, {
            if (variasiKetinggianAir.toInt() == 0) {
                variasiKetinggianAir = it[0].variasaiKetinggianAir.toInt().toString()
                isBack = true
            }
            if (variasiKetinggianAir.toInt() - currentVariasi == 1) {
                isLast = true
            }
            else if (variasiKetinggianAir.toInt() - it.size == 0 && currentVariasi < it.size) {
                isLast = true
            }
            if (isBack) {
                if (it.isNotEmpty()) {
                    if (currentVariasi < it.size ) {
                        pengambilanDataModel = it[currentVariasi]
                        idPengambilanData = it[currentVariasi].id!!
                        mBinding.tieKetinggianAirBangunan.setText(it[currentVariasi].h1.toString())
                        mBinding.tieKetinggianAirHulu.setText(it[currentVariasi].hb.toString())
                        mBinding.tieSaluranBasah.setText(it[currentVariasi].jumlahSaluranBasah.toInt().toString())
                        mBinding.tieSaluranBasah.setText(it[currentVariasi].jumlahSaluranBasah.toInt().toString())
                    } else {
                        isBack = false
                    }
                }
                mBinding.tvTitle.text = String.format("Data Pengambilan Variasi Air Ke-%s", (currentVariasi + 1).toString())
            } else {
                mBinding.tvTitle.text = String.format("Data Pengambilan Variasi Air Ke-%s", (it.size + 1).toString())
            }
            if (it.isNotEmpty()) {
                previousPengambilanDataModel = it[if (currentVariasi != 0) currentVariasi - 1 else 0]
            }
        })
    }

    override fun onBackPressed() {
        if (currentVariasi == 0) {
            if (!isMenu) {
                ToastUtils.showLong("Form Pengambilan data pertama, tidak dapat mengubah kembali data sebelumnya, tekan lagi untuk kembali ke menu utama")
                isMenu = true
                Handler().postDelayed({
                    isMenu = false
                }, 2000)
            } else {
                finish()
            }
        } else {
            val intent = Intent(this@PengambilanDataActivity, FormDataActivity::class.java)
            intent.putExtra("id_tipe_bangunan", idTipeBangunan)
            intent.putExtra("tipe_bangunan", pengambilanDataActivityViewModel.detailBangunan.value)
            intent.putExtra("id_base_data", idBaseData)
            intent.putExtra("id_pengambilan_data", previousPengambilanDataModel.id)
            intent.putExtra("jumlah_pias", previousPengambilanDataModel.jumlahSaluranBasah)
            intent.putExtra("h1", previousPengambilanDataModel.h1)
            intent.putExtra("hb", previousPengambilanDataModel.hb)
            intent.putExtra("variasi_Ketinggian_air", previousPengambilanDataModel.variasaiKetinggianAir.toInt().toString())
            intent.putExtra("current_variasi", currentVariasi - 1)
            intent.putExtra("is_last", false)
            intent.putExtra("is_back", true)
            intent.putExtra("is_pengambilan_data_edit", true)
            intent.putExtra("is_back_from_pengambilan_data", true)
            startActivity(intent)
            finish()
        }
    }
}