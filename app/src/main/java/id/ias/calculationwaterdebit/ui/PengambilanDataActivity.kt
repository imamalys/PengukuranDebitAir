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

class PengambilanDataActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityPengambilanDataBinding
    val pengambilanDataActivityViewModel: PengambilanDataActivityViewModel by viewModels {
        PengambilanDataActivityViewModelFactory()
    }
    private val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory((application as Application).pengambilanDataRepository)
    }
    private val alpsViewModel: AmbangLebarPengontrolSegiempatViewModel by viewModels {
        AmbangLebarPengontrolSegiempatViewModelFactory((application as Application).alpsRepository)
    }

    var idTipeBangunan: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityPengambilanDataBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                pengambilanDataActivityViewModel.detailBangunan.value = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
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
                        pengambilanDataActivityViewModel.pengambilValue.value!![0],
                        pengambilanDataActivityViewModel.pengambilValue.value!![1],
                        pengambilanDataActivityViewModel.pengambilValue.value!![2]
                )
                pengambilanDataViewModel.insert(pengambilanData)
            } else {
                ToastUtils.showLong("Data masih ada yang kosong, silahkan diisi terlebih dahulu")
            }
        }

        mBinding.etLebarSaluran.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != "" && s.toString() != ".") {
                    pengambilanDataActivityViewModel.pengambilValue.value!![0] = s.toString().toFloat()
                    if (mBinding.etJmlhSaluranBasah.text.toString() != "") {
                        mBinding.etRangeAntarPias.setText((s.toString().toFloat() / mBinding.etJmlhSaluranBasah.text.toString().toFloat()).toString())
                    }
                } else {
                    pengambilanDataActivityViewModel.pengambilValue.value!![0] = "0".toFloat()
                    mBinding.etRangeAntarPias.setText("")
                    mBinding.etRangeAntarPias.hint = "0"
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
                    pengambilanDataActivityViewModel.pengambilValue.value!![1] = s.toString().toFloat()
                    mBinding.etJumlahPias.setText((s.toString().toInt() - 1).toString())
                    if (mBinding.etLebarSaluran.text.toString() != "") {
                        mBinding.etRangeAntarPias.setText((s.toString().toFloat() / mBinding.etLebarSaluran.text.toString().toFloat()).toString())
                    }
                } else {
                    pengambilanDataActivityViewModel.pengambilValue.value!![1] = "0".toFloat()
                    mBinding.etRangeAntarPias.setText("")
                    mBinding.etRangeAntarPias.hint = "0"
                    mBinding.etJumlahPias.setText("")
                    mBinding.etJumlahPias.hint = "0"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        mBinding.etVariasiKetinggianAir.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != "" && s.toString() != ".") {
                    pengambilanDataActivityViewModel.pengambilValue.value!![2] = s.toString().toFloat()
                } else {
                    pengambilanDataActivityViewModel.pengambilValue.value!![2] = "0".toFloat()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun setViewModel() {
        pengambilanDataViewModel.idPengambilanData.observe(this, {
            if (it.toInt() != 0) {
                alpsViewModel.updateIdPengambilanData(idTipeBangunan.toInt(), it.toInt())
                val intent = Intent(this@PengambilanDataActivity, FormDataActivity::class.java)
                intent.putExtra("id_tipe_bangunan", idTipeBangunan)
                intent.putExtra("tipe_bangunan", pengambilanDataActivityViewModel.detailBangunan.value)
                intent.putExtra("id_pengambilan_data", it.toInt())
                intent.putExtra("jumlah_pias", mBinding.etJumlahPias.text.toString().toInt())
                intent.putExtra("variasi_Ketinggian_air", pengambilanDataActivityViewModel.pengambilValue.value!![2].toInt())
                startActivity(intent)
                finish()
            }
        })
    }
}