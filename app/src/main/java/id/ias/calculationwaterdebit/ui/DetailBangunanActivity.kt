package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.adapter.DetailBangunanAdapter
import id.ias.calculationwaterdebit.database.viewmodel.AmbangLebarPengontrolSegiempatViewModel
import id.ias.calculationwaterdebit.database.viewmodel.AmbangLebarPengontrolSegiempatViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityDetailBangunanBinding
import id.ias.calculationwaterdebit.viewmodel.DetailBangunanUkurViewModelFactory
import id.ias.calculationwaterdebit.viewmodel.DetailBangunanViewModel

class DetailBangunanActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityDetailBangunanBinding
    private val detailBangunanViewModel: DetailBangunanViewModel by viewModels {
        DetailBangunanUkurViewModelFactory()
    }
    private val alpsViewModel: AmbangLebarPengontrolSegiempatViewModel by viewModels {
        AmbangLebarPengontrolSegiempatViewModelFactory((application as Application).alpsRepository)
    }
    var idTipeBangunan: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDetailBangunanBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan") && it.hasExtra("id_tipe_bangunan")) {
                detailBangunanViewModel.detailBangunan.value = it.getStringExtra("tipe_bangunan")!!
                idTipeBangunan = it.getLongExtra("id_tipe_bangunan", 0)
            }
        }

        setAction()
        setViewModel()
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            if (detailBangunanViewModel.checkHaveValue()) {
                alpsViewModel.update(idTipeBangunan.toInt(), detailBangunanViewModel.detailBangunanValue.value!!)
                val intent = Intent(this@DetailBangunanActivity, PengambilanDataActivity::class.java)
                intent.putExtra("id_tipe_bangunan", idTipeBangunan)
                intent.putExtra("tipe_bangunan", detailBangunanViewModel.detailBangunan.value)
                startActivity(intent)
                finish()
            } else {
                ToastUtils.showLong("Data masih ada yang kosong, silahkan diisi terlebih dahulu")
            }
        }
    }

    private fun setViewModel() {
        detailBangunanViewModel.detailBangunan.observe(this) {
            mBinding.ivImageBangunan.setImageDrawable(theme.getDrawable(detailBangunanViewModel.getImage(it)))
            mBinding.tvTipeBangunan.text = it
            mBinding.rvDetailBangunan.layoutManager = LinearLayoutManager(this)
            val adapter = DetailBangunanAdapter(
                this,
                detailBangunanViewModel.getDetailBangunan(it),
                object: DetailBangunanAdapter.Listener {
                    override fun onChanged(value: String, position: Int) {
                        if (value != "") {
                            detailBangunanViewModel.detailBangunanValue.value!![position - 1] = value.toFloat()
                        } else {
                            detailBangunanViewModel.detailBangunanValue.value!![position - 1] = "0".toFloat()
                        }
                    }
                }
            )
            mBinding.rvDetailBangunan.adapter = adapter
        }
    }
}