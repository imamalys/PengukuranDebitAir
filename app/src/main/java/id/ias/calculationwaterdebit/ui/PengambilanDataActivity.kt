package id.ias.calculationwaterdebit.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.databinding.ActivityPengambilanDataBinding
import id.ias.calculationwaterdebit.viewmodel.PengambilanDataViewModel
import id.ias.calculationwaterdebit.viewmodel.PengambilanDataViewModelFactory

class PengambilanDataActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityPengambilanDataBinding
    val pengambilanDataViewModel: PengambilanDataViewModel by viewModels {
        PengambilanDataViewModelFactory()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityPengambilanDataBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setAction()
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            if (pengambilanDataViewModel.checkHaveValue()) {
                ToastUtils.showLong("Have Value")
            } else {
                ToastUtils.showLong("No Value")
            }
        }

        mBinding.etLebarSaluran.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pengambilanDataViewModel.pengambilValue.value!![0] = s.toString().toFloat()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        mBinding.etRangeAntarPias.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pengambilanDataViewModel.pengambilValue.value!![1] = s.toString().toFloat()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        mBinding.etJmlhSaluranBasah.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pengambilanDataViewModel.pengambilValue.value!![2] = s.toString().toFloat()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        mBinding.etJumlahPias.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pengambilanDataViewModel.pengambilValue.value!![3] = s.toString().toFloat()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        mBinding.etVariasiKetinggianAir.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pengambilanDataViewModel.pengambilValue.value!![4] = s.toString().toFloat()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}