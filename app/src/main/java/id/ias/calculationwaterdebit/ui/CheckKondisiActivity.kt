package id.ias.calculationwaterdebit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityCheckKondisiBinding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil

class CheckKondisiActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    private lateinit var mBinding: ActivityCheckKondisiBinding

    private val baseDataViewModel: BaseDataViewModel by viewModels {
        BaseDataViewModelFactory((application as Application).baseDataRepository)
    }

    var idBaseData: Long = 0

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
                var pertama = if (mBinding.rg1.checkedRadioButtonId == R.id.rb_yes_rg1) "Adanya" else
                    "Tidak adanya"
                pertama += "keretakan pada sayap banguanan ukur debit yang akan dikalibrasi"
                val nilaiPertama = if (mBinding.rg1.checkedRadioButtonId == R.id.rb_yes_rg1) 1 else 0
                var kedua = if (mBinding.rg2.checkedRadioButtonId == R.id.rb_yes_rg2) "Adanya" else
                    "Tidak adanya"
                kedua += "keretakan besar pada ambang/pengontrol/pengukur pada bangununan ukur debit yang akan dikalbrasi"
                val nilaiKedua = if (mBinding.rg2.checkedRadioButtonId == R.id.rb_yes_rg2) 3 else 0
                val ketiga = when (mBinding.rg3.checkedRadioButtonId) {
                    R.id.rb_yes_rg3 ->
                        "Kondisi aliran (debit kecil/debit besarberfungsi dengan baik"
                    R.id.rb_no1_rg3 ->
                        "Kondisi aliran kecil tidak berfungsi dengan baik"
                    R.id.rb_no2_rg3 ->
                        "Kondisi aliran besar tidak berfungsi dengan baik"
                    else ->
                        "kedua kondisi tidak berfungsi dengan baik"
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
                val keempat = if (mBinding.rg4.checkedRadioButtonId == R.id.rb_yes_rg4)
                    "Kondisi pielschall masih dapat terlihat" else "Kondisi pielschall tidak dapat terlihat"
                val nilaiKeempat = if (mBinding.rg4.checkedRadioButtonId == R.id.rb_yes_rg4)
                    1 else 0
                val kelima = when(mBinding.rg5.checkedRadioButtonId) {
                    R.id.rb_no1_rg5 ->
                        "Kondisi sedimen di saluran tidak ada"
                    R.id.rb_no2_rg5 ->
                        "Kondisi sedimen di saluran dengan sedikit batuan/pasir, tidak menganggu aliran"
                    R.id.rb_no3_rg5 ->
                        "Kondisi sedimen di saluran dengan sedikit tanah, tidak menganggu aliran"
                    R.id.rb_no4_rg5 ->
                        "Kondisi sedimen di saluran dengan tanah/pasir/tanah, berbentuk bongkohan"
                    else ->
                        "Kondisi sedimen di saluran dengan banyak tanah/pasir/batuan yang menutupi sebagian saluran"
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
                baseDataViewModel.update(id = idBaseData.toInt(), keterangan =  result, nilaiKeterangan =  nilai)
            }
        }
    }

    private fun setViewModel() {
        baseDataViewModel.baseDataUpdate.observe(this, {
            if (it != 0) {
                loading.dialog.dismiss()
                val intent = Intent(this@CheckKondisiActivity, TipeBangunanUkurActivity::class.java)
                intent.putExtra("id_base_data", idBaseData)
                startActivity(intent)
                finish()
            }
        })
    }
}