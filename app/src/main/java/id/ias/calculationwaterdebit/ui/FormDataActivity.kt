package id.ias.calculationwaterdebit.ui

import android.content.Intent
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
import id.ias.calculationwaterdebit.database.model.PiasModel
import id.ias.calculationwaterdebit.database.viewmodel.PiasDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.PiasDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityFormDataBinding
import id.ias.calculationwaterdebit.viewmodel.FormDataActivityViewModel
import id.ias.calculationwaterdebit.viewmodel.FormDataActivityViewModelFactory

class FormDataActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityFormDataBinding
    val formDataActivityViewModel: FormDataActivityViewModel by viewModels {
        FormDataActivityViewModelFactory()
    }

    private val piasViewModel: PiasDataViewModel by viewModels {
        PiasDataViewModelFactory((application as Application).piasRepository)
    }

    var idTipeBangunan: Long = 0
    var isLast = false
    var idBaseData: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFormDataBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                formDataActivityViewModel.detailBangunan = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
                formDataActivityViewModel.idPengambilanData = it.getIntExtra("id_pengambilan_data", 0)
                idBaseData = it.getLongExtra("id_base_data", 0)
                formDataActivityViewModel.jumlahPias = it.getIntExtra("jumlah_pias", 0)
                formDataActivityViewModel.variasiKetinggianAir = it.getIntExtra("variasi_Ketinggian_air", 0)
                formDataActivityViewModel.h1 = it.getFloatExtra("h1", "0".toFloat()).toString()
                formDataActivityViewModel.hb = it.getFloatExtra("hb", "0".toFloat()).toString()
                isLast = it.getBooleanExtra("is_last", false)
            }
        }

        mBinding.etH1.setText(formDataActivityViewModel.h1)
        mBinding.etHb.setText(formDataActivityViewModel.hb)

        setAction()
        setSpinner()
        setViewModel()
    }

    fun setView(piasModel: List<PiasModel>) {
        piasModel.let {
            if (piasModel.size < formDataActivityViewModel.jumlahPias) {
                mBinding.tvPias.text = String.format("Pias ke %s", ((piasModel.size + 1).toString()))
            } else {
                mBinding.tvPias.text = "Pias ke 1"
            }
        }

        mBinding.btnNext.isEnabled = true

        setSpinner()
    }

    fun setAction() {
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
                formDataActivityViewModel.h2 = if (s.toString() != ""  && s.toString() != ".") s.toString() else "0"
                setKecepatanAir()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        mBinding.etJarakPias.addTextChangedListener(object : TextWatcher {
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
                formDataActivityViewModel.jarakPias = if (s.toString() != ""  && s.toString() != ".") s.toString() else "0"
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
                formDataActivityViewModel.kecepatanAirValues.value!![0] = if (s.toString() != ""  && s.toString() != ".") s.toString().toFloat() else "0".toFloat()
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
                formDataActivityViewModel.kecepatanAirValues.value!![1] = if (s.toString() != ""  && s.toString() != ".") s.toString().toFloat() else "0".toFloat()
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
                formDataActivityViewModel.kecepatanAirValues.value!![2] = if (s.toString() != ""  && s.toString() != ".") s.toString().toFloat() else "0".toFloat()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        mBinding.btnNext.setOnClickListener {
            if(mBinding.etH2.toString() == "") {
                ToastUtils.showLong("H2 belum diisi")
            } else if(mBinding.etJarakPias.toString() == "") {
                ToastUtils.showLong("Jarak Antar Pias belum diisi")
            }
            else if(!formDataActivityViewModel.checkHaveValue()) {
                ToastUtils.showLong("Kecepatan air data belum diisi")
            } else {
                mBinding.btnNext.isEnabled = false

                val piasModel = PiasModel(
                    null,
                    formDataActivityViewModel.idPengambilanData,
                    formDataActivityViewModel.h2.toFloat(),
                    formDataActivityViewModel.jarakPias.toFloat(),
                    formDataActivityViewModel.kecepatanAirValues.value!![0],
                    mBinding.d8.text.toString().toFloat(),
                    formDataActivityViewModel.kecepatanAirValues.value!![1],
                    mBinding.d6.text.toString().toFloat(),
                    formDataActivityViewModel.kecepatanAirValues.value!![2],
                    mBinding.d2.text.toString().toFloat(),
                    formDataActivityViewModel.metodePengambilan.value!!
                )
                piasViewModel.insert(piasModel)
            }
        }
    }

    private fun clearView(piasModel: List<PiasModel>) {
        piasModel.let {
            when {
                it.size < formDataActivityViewModel.jumlahPias -> {
    //                mBinding.etH1.setText("")
                    mBinding.etH2.setText("")
                    mBinding.etH2.hint = "0"
                    mBinding.etJarakPias.setText("")
                    mBinding.etJarakPias.hint = "0"
                    formDataActivityViewModel.h2 = "0"
                    formDataActivityViewModel.jarakPias = "0"
                    formDataActivityViewModel.kecepatanAirValues.value = FloatArray(3)
                    formDataActivityViewModel.kecepatanAirs = ArrayList()
                    mBinding.svView.fullScroll(View.FOCUS_UP)
                    mBinding.svView.pageScroll(View.FOCUS_UP)
                    mBinding.svView.smoothScrollTo(0, 0)
                    setView(piasModel)
                }
                isLast -> {
                    val intent = Intent(this@FormDataActivity, VariasiOutputActivity::class.java)
                    intent.putExtra("id_tipe_bangunan", idTipeBangunan)
                    intent.putExtra("tipe_bangunan", formDataActivityViewModel.detailBangunan)
                    intent.putExtra("id_base_data", idBaseData)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    val intent = Intent(this@FormDataActivity, PengambilanDataActivity::class.java)
                    intent.putExtra("id_tipe_bangunan", idTipeBangunan)
                    intent.putExtra("tipe_bangunan", formDataActivityViewModel.detailBangunan)
                    intent.putExtra("id_base_data", idBaseData)
                    intent.putExtra("variasi_ketinggian_air", formDataActivityViewModel.variasiKetinggianAir.toString())
                    startActivity(intent)
                    finish()
                }
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
        mBinding.d2Value.setText("")
        mBinding.d6Value.setText("")
        mBinding.d8Value.setText("")
        mBinding.d2Value.hint = "0"
        mBinding.d6Value.hint = "0"
        mBinding.d8Value.hint = "0"
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
        piasViewModel.insertId.observe(this, {
            piasViewModel.getPiasDataById(formDataActivityViewModel.idPengambilanData)
        })

        piasViewModel.piasDatas.observe(this, { piasData ->
            if (piasData.isNotEmpty()) {
                formDataActivityViewModel.currentPiasSize = piasData.size
                clearView(piasData)
            }
        })
    }
}