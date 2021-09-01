package id.ias.calculationwaterdebit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityCheckKondisiBinding
import id.ias.calculationwaterdebit.util.MessageDialogUtil
import id.ias.calculationwaterdebit.util.LoadingDialogUtil

class CheckKondisiActivity : AppCompatActivity() {
    val back = MessageDialogUtil()
    val loading = LoadingDialogUtil()

    private lateinit var mBinding: ActivityCheckKondisiBinding

    private val baseDataViewModel: BaseDataViewModel by viewModels {
        BaseDataViewModelFactory((application as Application).baseDataRepository)
    }

    var idBaseData: Long = 0
    var pertamaInt: Int = 0
    var keduaInt: Int = 0
    var ketigaInt: Int = 0
    var keempatInt: Int = 0
    var kelimaInt: Int = 0
    var tipeBangunan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityCheckKondisiBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("id_base_data")) {
                idBaseData = it.getLongExtra("id_base_data", 0)
            }
        }

        setViewModel()
        setAction()
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            if (mBinding.rg1.checkedRadioButtonId == -1 || mBinding.rg2.checkedRadioButtonId == -1 ||
                    mBinding.rg3.checkedRadioButtonId == -1 || mBinding.rg4.checkedRadioButtonId == -1 ||
                    mBinding.rg5.checkedRadioButtonId == -1) {
                ToastUtils.showLong("Checklist Kondisi Bangunan masih kosong")
            } else {
                loading.show(this)
                var pertama = ""
                if (mBinding.rg1.checkedRadioButtonId == R.id.rb_yes_rg1) {
                    pertama = "Adanya"
                    pertamaInt = R.id.rb_yes_rg1
                } else {
                    pertama = "Tidak adanya"
                    pertamaInt = R.id.rb_no_rg1
                }
                pertama += "keretakan pada sayap banguanan ukur debit yang akan dikalibrasi"
                val nilaiPertama = if (mBinding.rg1.checkedRadioButtonId == R.id.rb_yes_rg1) 1 else 0

                var kedua = ""
                if (mBinding.rg2.checkedRadioButtonId == R.id.rb_yes_rg2) {
                    kedua = "Adanya"
                    keduaInt = R.id.rb_yes_rg2
                } else {
                    kedua = "Tidak adanya"
                    keduaInt = R.id.rb_no_rg2
                }
                kedua += "keretakan besar pada ambang/pengontrol/pengukur pada bangunan ukur debit yang akan dikalbrasi"
                val nilaiKedua = if (mBinding.rg2.checkedRadioButtonId == R.id.rb_yes_rg2) 3 else 0

                var ketiga = ""

               when (mBinding.rg3.checkedRadioButtonId) {
                    R.id.rb_yes_rg3 -> {
                        ketiga = "Kondisi aliran debit kecil/debit besar berfungsi dengan baik"
                        ketigaInt = R.id.rb_yes_rg3
                    }
                    R.id.rb_no1_rg3 -> {
                        ketiga = "Kondisi aliran kecil tidak berfungsi dengan baik"
                        ketigaInt = R.id.rb_yes_rg3
                    }
                    R.id.rb_no2_rg3 -> {
                        ketiga = "Kondisi aliran besar tidak berfungsi dengan baik"
                        ketigaInt = R.id.rb_no2_rg3
                    }
                    else -> {
                        ketiga = "kedua kondisi tidak berfungsi dengan baik"
                        ketigaInt = R.id.rb_no3_rg3
                    }
                }
                val nilaiKetiga = when (mBinding.rg3.checkedRadioButtonId) {
                    R.id.rb_yes_rg3 ->
                        0
                    R.id.rb_no1_rg3 ->
                        0
                    R.id.rb_no2_rg3 ->
                        2
                    else ->
                        3
                }

                var keempat = ""
                if (mBinding.rg4.checkedRadioButtonId == R.id.rb_yes_rg4) {
                    keempat = "Kondisi pielschall masih dapat terlihat"
                    keempatInt = R.id.rb_yes_rg4
                } else {
                    keempat = "Kondisi pielschall tidak dapat terlihat"
                    keempatInt = R.id.rb_no_rg4
                }
                val nilaiKeempat = if (mBinding.rg4.checkedRadioButtonId == R.id.rb_yes_rg4)
                    1 else 0

                var kelima = ""
                when(mBinding.rg5.checkedRadioButtonId) {
                    R.id.rb_no1_rg5 -> {
                        kelima = "Kondisi sedimen di saluran tidak ada"
                        kelimaInt = R.id.rb_no1_rg5
                    }
                    R.id.rb_no2_rg5 -> {
                        kelima = "Kondisi sedimen di saluran dengan sedikit batuan/pasir, tidak menganggu aliran"
                        kelimaInt = R.id.rb_no2_rg5
                    }
                    R.id.rb_no3_rg5 -> {
                        kelima = "Kondisi sedimen di saluran dengan sedikit tanah, tidak menganggu aliran"
                        kelimaInt = R.id.rb_no3_rg5
                    }
                    R.id.rb_no4_rg5 -> {
                        kelima = "Kondisi sedimen di saluran dengan tanah/pasir/tanah, berbentuk bongkohan"
                        kelimaInt = R.id.rb_no4_rg5
                    }
                    else -> {
                        kelima = "Kondisi sedimen di saluran dengan banyak tanah/pasir/batuan yang menutupi sebagian saluran"
                        kelimaInt = R.id.rb_no5_rg5
                    }
                }
                val nilaiKelima = when(mBinding.rg5.checkedRadioButtonId) {
                    R.id.rb_no1_rg5 ->
                        0
                    R.id.rb_no2_rg5 ->
                        0
                    R.id.rb_no3_rg5 ->
                        0
                    R.id.rb_no4_rg5 ->
                        3
                    else ->
                        5
                }

                val result = "Keterangan:\n1.$pertama\n2.$kedua\n3.$ketiga\n4.$keempat\n5.$kelima"
                val nilai = nilaiPertama + nilaiKedua + nilaiKetiga + nilaiKeempat + nilaiKelima

                if (nilai >= 5) {
                    back.show(this, "Kondisi Kurang Baik\nTidak dianjurkan melakukan kalibrasi",
                        "Lanjutkan", "Tidak", object: MessageDialogUtil.DialogListener {
                        override fun onYes(action: Boolean) {
                            if (action) {
                                baseDataViewModel.updateCheckKondisi(idBaseData.toInt(), result, nilai,
                                        pertamaInt, keduaInt, ketigaInt, keempatInt, kelimaInt)
                            } else {
                                finish()
                            }
                        }
                    })
                } else {
                    baseDataViewModel.updateCheckKondisi(idBaseData.toInt(), result, nilai, pertamaInt,
                            keduaInt, ketigaInt, keempatInt, kelimaInt)
                }
            }
        }
    }

    private fun setViewModel() {
        baseDataViewModel.getBaseDataById(idBaseData.toInt())
        baseDataViewModel.baseDataById.observe(this, {
            if (it.pertama != 0) {
                if (it.tipeBangunan != null) {
                    tipeBangunan = it.tipeBangunan!!
                }
                mBinding.rg1.check(it.pertama!!)
                mBinding.rg2.check(it.kedua!!)
                mBinding.rg3.check(it.ketiga!!)
                mBinding.rg4.check(it.keempat!!)
                mBinding.rg5.check(it.kelima!!)
            }
        })
        baseDataViewModel.baseDataUpdate.observe(this, {
            if (it != 0) {
                if (tipeBangunan != "") {
                    loading.dialog.dismiss()
                    val intent = Intent(this@CheckKondisiActivity, DetailBangunanActivity::class.java)
                    intent.putExtra("id_base_data", idBaseData)
                    intent.putExtra("tipe_bangunan", tipeBangunan)
                    startActivity(intent)
                    finish()
                } else {
                    loading.dialog.dismiss()
                    val intent = Intent(this@CheckKondisiActivity, TipeBangunanUkurActivity::class.java)
                    intent.putExtra("id_base_data", idBaseData)
                    startActivity(intent)
                    finish()
                }
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