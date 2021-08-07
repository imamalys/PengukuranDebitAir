package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.viewmodel.AmbangLebarPengontrolSegiempatViewModel
import id.ias.calculationwaterdebit.database.viewmodel.AmbangLebarPengontrolSegiempatViewModelFactory
import id.ias.calculationwaterdebit.database.viewmodel.PengambilanDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.PengambilanDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityPengambilanDataBinding
import id.ias.calculationwaterdebit.viewmodel.PengambilanDataActivityViewModel
import id.ias.calculationwaterdebit.viewmodel.PengambilanDataActivityViewModelFactory
import kotlin.math.sign

class PengambilanDataActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityPengambilanDataBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                pengambilanDataActivityViewModel.detailBangunan.value = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
                variasiKetinggianAir = it.getStringExtra("variasi_ketinggian_air")!!
                idBaseData = it.getLongExtra("id_base_data", 0)
            }
        }

        setAction()
        setViewModel()
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            if (pengambilanDataActivityViewModel.checkHaveValue()) {
                val pengambilanData = PengambilanDataModel(
                    null,
                    idBaseData.toInt(),
                    pengambilanDataActivityViewModel.pengambilValue.value!![0],
                    pengambilanDataActivityViewModel.pengambilValue.value!![1],
                    pengambilanDataActivityViewModel.pengambilValue.value!![2],
                    variasiKetinggianAir.toFloat(),
                    null
                )
                pengambilanDataViewModel.insert(pengambilanData)
            } else {
                ToastUtils.showLong("Data masih ada yang kosong, silahkan diisi terlebih dahulu")
            }
        }

        mBinding.etKetinggianBangunanUkur.addTextChangedListener(object: TextWatcher {
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

        mBinding.etKetinggianAirHulu.addTextChangedListener(object: TextWatcher {
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

        mBinding.etJmlhSaluranBasah.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != "" && s.toString() != ".") {
                    pengambilanDataActivityViewModel.pengambilValue.value!![2] = s.toString().toFloat()
                    mBinding.etJumlahPias.setText((s.toString().toInt() - 1).toString())
                } else {
                    pengambilanDataActivityViewModel.pengambilValue.value!![2] = "0".toFloat()
                    mBinding.etJumlahPias.setText("")
                    mBinding.etJumlahPias.setHint("0")
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun setViewModel() {
        pengambilanDataViewModel.idPengambilanData.observe(this, {
            if (it.toInt() != 0) {
                val intent = Intent(this@PengambilanDataActivity, FormDataActivity::class.java)
                intent.putExtra("id_tipe_bangunan", idTipeBangunan)
                intent.putExtra("tipe_bangunan", pengambilanDataActivityViewModel.detailBangunan.value)
                intent.putExtra("id_pengambilan_data", it.toInt())
                intent.putExtra("id_base_data", idBaseData)
                intent.putExtra("jumlah_pias", mBinding.etJumlahPias.text.toString().toInt())
                intent.putExtra("h1", pengambilanDataActivityViewModel.pengambilValue.value!![0])
                intent.putExtra("hb", pengambilanDataActivityViewModel.pengambilValue.value!![1])
                intent.putExtra("variasi_Ketinggian_air", pengambilanDataActivityViewModel.pengambilValue.value!![2].toInt())
                intent.putExtra("is_last", isLast)
                startActivity(intent)
                finish()
            }
        })

        pengambilanDataViewModel.getPengambilanDataById(idBaseData.toInt()).observe(this, {
            if (it.size < variasiKetinggianAir.toInt()) {
                if (variasiKetinggianAir.toInt() - it.size == 1) {
                    isLast = true
                }
                mBinding.tvTitle.text = String.format("Data Pengambilan Variasi Air Ke-%s", (it.size + 1).toString() )
            }
        })
    }
}