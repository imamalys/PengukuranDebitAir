package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.databinding.ActivityVariasiKetinggianAirBinding
import id.ias.calculationwaterdebit.util.BackDialogUtil
import id.ias.calculationwaterdebit.util.MinMaxFilter


class VariasiKetinggianAirActivity : AppCompatActivity() {
    val back = BackDialogUtil()
    private lateinit var mBinding: ActivityVariasiKetinggianAirBinding
    var tipeBangunan: String = ""
    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityVariasiKetinggianAirBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                tipeBangunan = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
                idBaseData = it.getLongExtra("id_base_data", 0)
            }
        }

        setAction()
    }

    private fun setAction() {
        mBinding.etVariasiKetinggianAir.filters = arrayOf<InputFilter>(MinMaxFilter("1", "30"))

        mBinding.btnNext.setOnClickListener {
            if (mBinding.etVariasiKetinggianAir.text.toString() != "") {
                val intent = Intent(
                    this@VariasiKetinggianAirActivity,
                    PengambilanDataActivity::class.java
                )
                intent.putExtra("id_tipe_bangunan", idTipeBangunan)
                intent.putExtra("tipe_bangunan", tipeBangunan)
                intent.putExtra("id_base_data", idBaseData)
                intent.putExtra(
                    "variasi_ketinggian_air",
                    mBinding.etVariasiKetinggianAir.text.toString()
                )
                startActivity(intent)
                finish()
            } else {
                ToastUtils.showLong("Anda belum mengisi variasi ketinggian air")
            }
        }
    }

    override fun onBackPressed() {
        back.show(this, object: BackDialogUtil.DialogListener {
            override fun onYes(action: Boolean) {
                if (action) {
                    finish()
                }
            }
        })
    }
}