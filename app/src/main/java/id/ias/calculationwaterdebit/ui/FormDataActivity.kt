package id.ias.calculationwaterdebit.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.database.model.FormDataModel
import id.ias.calculationwaterdebit.database.viewmodel.FormDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.FormDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityFormDataBinding
import id.ias.calculationwaterdebit.viewmodel.FormDataActivityViewModel
import id.ias.calculationwaterdebit.viewmodel.FormDataActivityViewModelFactory

class FormDataActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityFormDataBinding
    val formDataActivityViewModel: FormDataActivityViewModel by viewModels {
        FormDataActivityViewModelFactory()
    }
    private val formDataViewModel: FormDataViewModel by viewModels {
        FormDataViewModelFactory((application as Application).database.formDataDao(),
                formDataActivityViewModel.idPengambilanData)
    }

    var isSetView = true

    var idTipeBangunan: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFormDataBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                formDataActivityViewModel.detailBangunan = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
                formDataActivityViewModel.idPengambilanData = it.getLongExtra("id_pengambilan_data", 0).toInt()
                formDataActivityViewModel.jumlahPias = it.getLongExtra("jumlah_pias", 0).toInt()
                formDataActivityViewModel.variasiKetinggianAir = it.getLongExtra("variasi_Ketinggian_air", 0).toInt()
            }
        }

        setAction()
        setViewModel()
    }

    fun setView(formData: List<FormDataModel>) {
        formData.let {
            if (mBinding.tvPias.text.toString() != "Pias ke 1") {
                if (formDataActivityViewModel.currentPias < formDataActivityViewModel.jumlahPias) {
                    formDataActivityViewModel.currentPias += 1
                    mBinding.tvPias.text = String.format("Pias ke %s", (formDataActivityViewModel.currentPias).toString())
                }
            }

        }
        if (formDataActivityViewModel.hb != "0") {
            mBinding.etHb.setText(formDataActivityViewModel.hb)
            mBinding.etHb.isFocusable = false
        } else {
            mBinding.etHb.isFocusable = true
        }

        setSpinner()
    }

    fun setAction() {
        mBinding.etH1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
            ) {

            }

            override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
            ) {
                formDataActivityViewModel.h1 = if (s.toString() != "") s.toString() else "0"
                setSpinner()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        mBinding.etH2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
            ) {

            }

            override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
            ) {
                formDataActivityViewModel.h2 = if (s.toString() != "") s.toString() else "0"
                setKecepatanAir()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        mBinding.d8Value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
            ) {

            }

            override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
            ) {
                formDataActivityViewModel.kecepatanAirValues.value!![0] = if (s.toString() != "") s.toString().toFloat() else "0".toFloat()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        mBinding.d6Value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
            ) {

            }

            override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
            ) {
                formDataActivityViewModel.kecepatanAirValues.value!![1] = if (s.toString() != "") s.toString().toFloat() else "0".toFloat()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        mBinding.d2Value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
            ) {

            }

            override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
            ) {
                formDataActivityViewModel.kecepatanAirValues.value!![2] = if (s.toString() != "") s.toString().toFloat() else "0".toFloat()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        mBinding.btnNext.setOnClickListener {
            if (mBinding.etH1.text.toString() == "") {
                ToastUtils.showLong("H1 belum diisi")
            } else if(mBinding.etH2.toString() == "") {
                ToastUtils.showLong("H2 belum diisi")
            } else if(mBinding.etHb.toString() == "") {
                ToastUtils.showLong("Hb belum diisi")
            } else if(!formDataActivityViewModel.checkHaveValue()) {
                ToastUtils.showLong("Kecepatan air data belum diisi")
            } else {
                val formData = FormDataModel(
                    null,
                        formDataActivityViewModel.idPengambilanData,
                        mBinding.etH2.text.toString().toFloat(),
                        mBinding.etH1.text.toString().toFloat(),
                        formDataActivityViewModel.kecepatanAirValues.value!![0],
                        mBinding.d8.text.toString().toFloat(),
                        formDataActivityViewModel.kecepatanAirValues.value!![1],
                        mBinding.d6.text.toString().toFloat(),
                        formDataActivityViewModel.kecepatanAirValues.value!![2],
                        mBinding.d2.text.toString().toFloat(),
                        mBinding.etHb.text.toString().toFloat(),
                        formDataActivityViewModel.metodePengambilan.value!!
                )
                isSetView = false
                formDataViewModel.insert(formData)
            }
        }
    }

    private fun clearView(formData: List<FormDataModel>) {
        isSetView = true
        formData.let {
            if (it.size < formDataActivityViewModel.variasiKetinggianAir) {
                mBinding.etH1.setText("")
                mBinding.etH2.setText("")
                formDataActivityViewModel.kecepatanAirValues.value = FloatArray(3)
                formDataActivityViewModel.kecepatanAirs = ArrayList()
                mBinding.svView.fullScroll(View.FOCUS_UP)
                setView(formData)
            } else {
                ToastUtils.showLong("Finish")
            }
        }
    }

    fun setSpinner() {
        val list = resources.getStringArray(formDataActivityViewModel.metodePengambilanSpinner())
        val adapter = ArrayAdapter(applicationContext, R.layout.spinner_item, list)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        mBinding.spMetodePengambilan.adapter = adapter
        mBinding.spMetodePengambilan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                formDataActivityViewModel.metodePengambilan.value = list[position]
                setInputKecepatanAir()
            }
        }

        mBinding.spMetodePengambilan.setSelection(0)
    }

    fun setInputKecepatanAir() {
        mBinding.d2Value.isEnabled = false
        mBinding.d6Value.isEnabled = false
        mBinding.d8Value.isEnabled = false
        when(formDataActivityViewModel.metodePengambilanInt()) {
            1 -> {
                mBinding.d2Value.isEnabled = true
                mBinding.d8Value.isEnabled = true
            }
            2 -> {
                mBinding.d2Value.isEnabled = true
                mBinding.d8Value.isEnabled = true
                mBinding.d6Value.isEnabled = true
            }
            else -> {
                mBinding.d6Value.isEnabled = true
            }
        }
    }

    fun setKecepatanAir() {
        mBinding.d2.text = String.format("%.2f", formDataActivityViewModel.h2.toFloat() * 0.8)
        mBinding.d6.text = String.format("%.2f", formDataActivityViewModel.h2.toFloat() * 0.4)
        mBinding.d8.text = String.format("%.2f", formDataActivityViewModel.h2.toFloat() * 0.2)
    }

    private fun setViewModel() {
        formDataViewModel.formDatas.observe(this, {
            if (it.isNotEmpty()) {
                if (isSetView) {
                    setView(it)
                } else {
                    clearView(it)
                }
            }
        })
    }
}