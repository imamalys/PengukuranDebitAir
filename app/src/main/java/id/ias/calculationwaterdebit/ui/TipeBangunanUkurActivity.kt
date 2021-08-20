package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityTipeBangunanUkurBinding
import id.ias.calculationwaterdebit.util.MessageDialogUtil
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import id.ias.calculationwaterdebit.util.PictureDialogUtil
import id.ias.calculationwaterdebit.viewmodel.TipeBangunanUkurViewModel
import id.ias.calculationwaterdebit.viewmodel.TipeBangunanUkurViewModelFactory

class TipeBangunanUkurActivity : AppCompatActivity() {
    val back = MessageDialogUtil()
    val loading = LoadingDialogUtil()
    var picture = PictureDialogUtil()
    var idBaseData: Long = 0
    lateinit var mBinding: ActivityTipeBangunanUkurBinding
    private val tipeBangunanUkurViewModel: TipeBangunanUkurViewModel by viewModels {
        TipeBangunanUkurViewModelFactory()
    }

    private val baseDataViewModel: BaseDataViewModel by viewModels {
        BaseDataViewModelFactory((application as Application).baseDataRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityTipeBangunanUkurBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("id_base_data")) {
                idBaseData = it.getLongExtra("id_base_data", 0)
            }
        }

        setSpinner()
        setViewModel()
        setAction()
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            loading.show(this)
            baseDataViewModel.update(id = idBaseData.toInt(), tipeBangunan = tipeBangunanUkurViewModel.tipeBangunan.value!!)
        }

        mBinding.ivImageBangunan.setOnClickListener {
            picture.show(this, tipeBangunanUkurViewModel.getImage(tipeBangunanUkurViewModel.tipeBangunan.value!!))
        }
    }

    private fun setSpinner() {
        var list = resources.getStringArray(R.array.bangunan_ukur)
        val adapter = ArrayAdapter(applicationContext, R.layout.spinner_item, list)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        mBinding.spTipeBangunan.adapter = adapter
        mBinding.spTipeBangunan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0 && position != 1 && position != 5 && position != 8) {
                    ToastUtils.showShort("Tipe Bangunan belum dapat dipilih")
                    mBinding.spTipeBangunan.setSelection(0)
                } else {
                    tipeBangunanUkurViewModel.tipeBangunan.value = list[position]
                }
            }
        }
    }

    private fun setViewModel() {
        tipeBangunanUkurViewModel.tipeBangunan.observe(this) {
            mBinding.ivImageBangunan.setImageDrawable(theme.getDrawable(tipeBangunanUkurViewModel.getImage(it)))
            mBinding.ivRumus.setImageDrawable(theme.getDrawable(tipeBangunanUkurViewModel.getRumus(it)))
            mBinding.tvKeteranganIsi.text = tipeBangunanUkurViewModel.getKeterangan(it)
        }

        baseDataViewModel.baseDataUpdate.observe(this, {
            if (it != 0) {
                loading.dialog.dismiss()
                val intent = Intent(this@TipeBangunanUkurActivity, DetailBangunanActivity::class.java)
                intent.putExtra("tipe_bangunan", tipeBangunanUkurViewModel.tipeBangunan.value)
                intent.putExtra("id_base_data", idBaseData)
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