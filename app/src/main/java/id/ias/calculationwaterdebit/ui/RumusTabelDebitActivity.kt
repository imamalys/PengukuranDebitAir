package id.ias.calculationwaterdebit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityRumusTabelDebitBinding

class RumusTabelDebitActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRumusTabelDebitBinding

    private val baseDataViewModel: BaseDataViewModel by viewModels {
        BaseDataViewModelFactory((application as Application).baseDataRepository)
    }

    var detailBangunan: String = ""
    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityRumusTabelDebitBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                detailBangunan = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
                idBaseData = it.getLongExtra("id_base_data", 0)
            }
        }

        setAction()
        setViewModel()
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            if (mBinding.etVariablePertama.text.toString() == "" && mBinding.etVariablePertama.text.toString() == ","
                && mBinding.etN.text.toString() == "" && mBinding.etN.text.toString() == ",") {
                ToastUtils.showLong("Data masih belum terisi")
            } else {
                baseDataViewModel.update(idBaseData.toInt(),
                    mBinding.etVariablePertama.text.toString(), mBinding.etN.text.toString())
            }
        }

        mBinding.etVariablePertama.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != "" && s.toString() != ".") {
                    mBinding.tvVariablePertama.text = String.format("Q = %s x B x H1", s.toString())
                } else {
                    mBinding.tvVariablePertama.text = "Q = (V) x B x H1"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        mBinding.etN.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != "" && s.toString() != ".") {
                    mBinding.tvN.text = s.toString()
                } else {
                    mBinding.tvN.text = "N"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun setViewModel() {
        baseDataViewModel.baseDataUpdate.observe(this, {
            if (it == 0) {
                val intent = Intent(this@RumusTabelDebitActivity, VariasiOutputActivity::class.java)
                intent.putExtra("id_tipe_bangunan", idTipeBangunan)
                intent.putExtra("tipe_bangunan", detailBangunan)
                intent.putExtra("id_base_data", idBaseData)
                startActivity(intent)
                finish()
            }
        })
    }
}