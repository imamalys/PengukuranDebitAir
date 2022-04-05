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
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import id.ias.calculationwaterdebit.util.MessageDialogUtil
import id.ias.calculationwaterdebit.viewmodel.FormDataActivityViewModel
import id.ias.calculationwaterdebit.viewmodel.FormDataActivityViewModelFactory

class FormDataActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    val back = MessageDialogUtil()
    lateinit var mBinding: ActivityFormDataBinding
    var isBack = false
    val formDataActivityViewModel: FormDataActivityViewModel by viewModels {
        FormDataActivityViewModelFactory()
    }
    var isInitBack = false

    private val piasViewModel: PiasDataViewModel by viewModels {
        PiasDataViewModelFactory((application as Application).piasRepository)
    }

    var idTipeBangunan: Long = 0
    var isLast = false
    var idBaseData: Long = 0
    var saveId: ArrayList<Int> = ArrayList()
    var savePias: List<PiasModel> = ArrayList()
    var currentVariasi = 0
    var isPengambilanDataEdit = false
    var isBackFromPengambilanData = false

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
                formDataActivityViewModel.variasiKetinggianAir = if (it.hasExtra("variasi_Ketinggian_air"))
                    it.getStringExtra("variasi_Ketinggian_air")!! else "0"
                formDataActivityViewModel.h1 = it.getFloatExtra("h1", "0".toFloat()).toString()
                formDataActivityViewModel.hb = it.getFloatExtra("hb", "0".toFloat()).toString()
                isLast = it.getBooleanExtra("is_last", false)
                currentVariasi = it.getIntExtra("current_variasi", 0)
                isBack = it.getBooleanExtra("is_back", false)
                isPengambilanDataEdit= it.getBooleanExtra("is_pengambilan_data_edit", false)
                isBackFromPengambilanData =  it.getBooleanExtra("is_back_from_pengambilan_data", false)
            }
        }

        mBinding.tieH1.setText(formDataActivityViewModel.h1)
        mBinding.tieHb.setText(formDataActivityViewModel.hb)

        setAction()
        setViewModel()
    }

    fun setView(piasModel: List<PiasModel>) {
        loading.dialog.dismiss()
        piasModel.let {
            mBinding.tvPias.text = String.format("Penampang basah ke %s", ((formDataActivityViewModel.currentPiasSize + 1).toString()))
        }

        mBinding.btnNext.isEnabled = true
    }

    fun setAction() {
        mBinding.tieH2.addTextChangedListener(object : TextWatcher {
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
                setSpinner()
                setKecepatanAir()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        mBinding.tieJarak.addTextChangedListener(object : TextWatcher {
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
            if(mBinding.tieH2.text.toString() == "") {
                ToastUtils.showLong("H2 belum diisi")
            } else if(mBinding.tieJarak.text.toString() == "") {
                ToastUtils.showLong("Jarak Antar Pias belum diisi")
            }
            else {
                mBinding.btnNext.isEnabled = false
                loading.show(this)
                val d8 =  mBinding.d8.text.toString()
                val d6 =  mBinding.d6.text.toString()
                val d2 =  mBinding.d2.text.toString()
                val piasModel = PiasModel(
                    if (isBack) savePias[formDataActivityViewModel.currentPiasSize].id else null,
                    formDataActivityViewModel.idPengambilanData,
                    formDataActivityViewModel.h2.toFloat(),
                    formDataActivityViewModel.jarakPias.toFloat(),
                    formDataActivityViewModel.kecepatanAirValues.value!![0], d8.toFloat(),
                    formDataActivityViewModel.kecepatanAirValues.value!![1], d6.toFloat(),
                    formDataActivityViewModel.kecepatanAirValues.value!![2], d2.toFloat(),
                    formDataActivityViewModel.metodePengambilan.value!!
                )
                if (isBack) piasViewModel.update(piasModel) else piasViewModel.insert(piasModel)
            }
        }
    }

    private fun clearView(piasModel: List<PiasModel>) {
        piasModel.let {
            when {
                isBack -> {
                    mBinding.tieH2.setText(piasModel[formDataActivityViewModel.currentPiasSize].h2.toString())
                    mBinding.tieJarak.setText(piasModel[formDataActivityViewModel.currentPiasSize].jarakAntarPias.toString())
                    formDataActivityViewModel.h2 = piasModel[formDataActivityViewModel.currentPiasSize].h2.toString()
                    formDataActivityViewModel.jarakPias = piasModel[formDataActivityViewModel.currentPiasSize].jarakAntarPias.toString()
                    formDataActivityViewModel.kecepatanAirValues.value = FloatArray(3)
                    formDataActivityViewModel.kecepatanAirValues.value!![0] = piasModel[formDataActivityViewModel.currentPiasSize].d8
                    formDataActivityViewModel.kecepatanAirValues.value!![1] = piasModel[formDataActivityViewModel.currentPiasSize].d6
                    formDataActivityViewModel.kecepatanAirValues.value!![2] = piasModel[formDataActivityViewModel.currentPiasSize].d2
                    mBinding.svView.fullScroll(View.FOCUS_UP)
                    mBinding.svView.pageScroll(View.FOCUS_UP)
                    mBinding.svView.smoothScrollTo(0, 0)
                    isInitBack = true
                    setView(piasModel)
                }
                it.size < formDataActivityViewModel.jumlahPias -> {
    //                mBinding.etH1.setText("")
                    mBinding.tieH2.setText("")
                    mBinding.tieJarak.setText("")
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
                    loading.dialog.dismiss()
                    val intent = Intent(this@FormDataActivity, RumusTabelDebitActivity::class.java)
                    intent.putExtra("id_tipe_bangunan", idTipeBangunan)
                    intent.putExtra("tipe_bangunan", formDataActivityViewModel.detailBangunan)
                    intent.putExtra("id_base_data", idBaseData)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    loading.dialog.dismiss()
                    val setIntent = Intent(this@FormDataActivity, PengambilanDataActivity::class.java)
                    setIntent.putExtra("id_tipe_bangunan", idTipeBangunan)
                    setIntent.putExtra("tipe_bangunan", formDataActivityViewModel.detailBangunan)
                    setIntent.putExtra("id_base_data", idBaseData)
                    setIntent.putExtra("variasi_ketinggian_air", formDataActivityViewModel.variasiKetinggianAir)
                    setIntent.putExtra("is_back", intent.getBooleanExtra("is_back", false))
                    setIntent.putExtra("current_variasi", currentVariasi + 1)
                    startActivity(setIntent)
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

        if (isBack) {
            if (savePias[formDataActivityViewModel.currentPiasSize].d2.toInt() != 0 &&
                    savePias[formDataActivityViewModel.currentPiasSize].d6.toInt() != 0 &&
                    savePias[formDataActivityViewModel.currentPiasSize].d8.toInt() != 0) {
                mBinding.spMetodePengambilan.setSelection(1)
            } else {
                mBinding.spMetodePengambilan.setSelection(0)
            }
        } else {
            mBinding.spMetodePengambilan.setSelection(0)
        }
    }

    fun setInputKecepatanAir() {
        if (isInitBack) {
            isInitBack = false
        } else {
            mBinding.d2Value.setText("")
            mBinding.d6Value.setText("")
            mBinding.d8Value.setText("")
            mBinding.d2Value.hint = "0"
            mBinding.d6Value.hint = "0"
            mBinding.d8Value.hint = "0"
            mBinding.d2Value.isEnabled = false
            mBinding.d6Value.isEnabled = false
            mBinding.d8Value.isEnabled = false
        }
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
        var d8 =  mBinding.d8.text.toString()
        d8 = d8.replace(",", ".")
        var d6 =  mBinding.d6.text.toString()
        d6 = d6.replace(",", ".")
        var d2 =  mBinding.d2.text.toString()
        d2 = d2.replace(",", ".")
        mBinding.d2.text = d2
        mBinding.d6.text = d6
        mBinding.d8.text = d8
        if (isBack) {
            if (savePias[formDataActivityViewModel.currentPiasSize].d2.toInt() != 0 &&
                    savePias[formDataActivityViewModel.currentPiasSize].d6.toInt() != 0 &&
                    savePias[formDataActivityViewModel.currentPiasSize].d8.toInt() != 0) {
                mBinding.d2Value.setText(savePias[formDataActivityViewModel.currentPiasSize].d2.toString())
                mBinding.d6Value.setText(savePias[formDataActivityViewModel.currentPiasSize].d6.toString())
                mBinding.d8Value.setText(savePias[formDataActivityViewModel.currentPiasSize].d8.toString())
            } else if (savePias[formDataActivityViewModel.currentPiasSize].d6.toInt() == 0) {
                mBinding.d2Value.setText(savePias[formDataActivityViewModel.currentPiasSize].d2.toString())
                mBinding.d8Value.setText(savePias[formDataActivityViewModel.currentPiasSize].d8.toString())
            } else {
                mBinding.d6Value.setText(savePias[formDataActivityViewModel.currentPiasSize].d6.toString())
            }
        }
    }

    private fun setViewModel() {
        if (isPengambilanDataEdit) {
            loading.show(this)
            piasViewModel.getPiasDataById(formDataActivityViewModel.idPengambilanData)
        }

        piasViewModel.insertId.observe(this, {
            if (it.toInt() != 0) {
                saveId.add(it.toInt())
                piasViewModel.getPiasDataById(formDataActivityViewModel.idPengambilanData)
            }
        })

        piasViewModel.updateId.observe(this, {
            if (it != 0) {
                piasViewModel.getPiasDataById(formDataActivityViewModel.idPengambilanData)
            }
        })

        piasViewModel.piasDatas.observe(this, { piasData ->
            if (piasData.isNotEmpty()) {
                savePias = piasData
                if (formDataActivityViewModel.currentPiasSize != 0 && formDataActivityViewModel.currentPiasSize + 1 != piasData.size) {
                    formDataActivityViewModel.currentPiasSize += 1
                } else if (isPengambilanDataEdit) {
                    formDataActivityViewModel.currentPiasSize = 0
                    isPengambilanDataEdit = false
                } else {
                    formDataActivityViewModel.currentPiasSize += 1
                    isBack = if (formDataActivityViewModel.currentPiasSize <  formDataActivityViewModel.jumlahPias) {
                        try {
                            piasData[formDataActivityViewModel.currentPiasSize].jarakAntarPias != 0.toFloat()
                        } catch (e: Exception) {
                            false
                        }
                    } else {
                        if (formDataActivityViewModel.currentPiasSize !=  piasData.size) {
                            true
                        } else {
                            isBackFromPengambilanData
                        }
                    }
                }
                clearView(piasData)
            } else {
                isBackFromPengambilanData = false
                isBack = false
                isPengambilanDataEdit = false
                loading.dialog.dismiss()
            }
        })
    }

    override fun onBackPressed() {
        if (formDataActivityViewModel.currentPiasSize - 2 != -2) {
            if (formDataActivityViewModel.currentPiasSize != 0) {
                isBack = true
                formDataActivityViewModel.currentPiasSize -= 1
                clearView(savePias)
            }
        } else {
            back.show(this, title = "Apakah anda yakin ingin kembali ke Data Pengambilan variasi air?",
                    yes = "Ya", no = "Tidak", object: MessageDialogUtil.DialogListener {
                override fun onYes(action: Boolean) {
                    if (action) {
                        val intent = Intent(this@FormDataActivity, PengambilanDataActivity::class.java)
                        intent.putExtra("id_tipe_bangunan", idTipeBangunan)
                        intent.putExtra("tipe_bangunan", formDataActivityViewModel.detailBangunan)
                        intent.putExtra("id_base_data", idBaseData)
                        intent.putExtra("variasi_ketinggian_air", formDataActivityViewModel.variasiKetinggianAir)
                        intent.putExtra("is_back", true)
                        intent.putExtra("current_variasi", currentVariasi)
                        startActivity(intent)
                        finish()
                    }
                }
            })
        }
    }
}