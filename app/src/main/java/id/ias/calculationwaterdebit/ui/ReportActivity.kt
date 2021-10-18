package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.adapter.ReportAdapter
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityReport2Binding
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import id.ias.calculationwaterdebit.util.MessageDialogUtil

class ReportActivity : AppCompatActivity() {
    val dialog = MessageDialogUtil()
    val loading = LoadingDialogUtil()
    private lateinit var mBinding: ActivityReport2Binding

    private val baseDataViewModel: BaseDataViewModel by viewModels {
        BaseDataViewModelFactory((application as Application).baseDataRepository)
    }

    private var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityReport2Binding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent.hasExtra("type")) {
            type = intent.getIntExtra("type", 0)
        }
        loading.show(this)
        setViewModel()
    }

    private fun setViewModel() {
        baseDataViewModel.allBaseDatas.observe(this, {
            if (it.isNotEmpty()) {
                mBinding.rvReport.layoutManager = LinearLayoutManager(this)
                val adapter = ReportAdapter(
                    this,
                    it, object : ReportAdapter.Listener {
                        override fun onClick(baseDataModel: BaseDataModel) {
                            if (baseDataModel.k == null) {
                                ToastUtils.showLong("Data tidak lengkap")
                            } else {
                                if (type == 0) {
                                    val intent = Intent(this@ReportActivity, DataInformasiActivity::class.java)
                                    intent.putExtra("id_base_data", baseDataModel.id!!.toLong())
                                    startActivity(intent)
                                } else {
                                    val intent = Intent(this@ReportActivity, ReportDetailActivity::class.java)
                                    intent.putExtra("id_base_data", baseDataModel.id!!.toLong())
                                    intent.putExtra("is_report", true)
                                    startActivity(intent)
                                }
                            }
                        }

                        override fun onDelete(baseDataModel: BaseDataModel) {
                            dialog.show(this@ReportActivity, "Apakah anda yakin menghapus data?",
                                    "Iya", "Tidak", object: MessageDialogUtil.DialogListener {
                                override fun onYes(action: Boolean) {
                                    if (action) {
                                        loading.show(this@ReportActivity)
                                        baseDataViewModel.delete(baseDataModel)
                                    } else {
                                        finish()
                                    }
                                }
                            })
                        }
                    }
                )
                mBinding.rvReport.adapter = adapter
                loading.dialog.dismiss()
            } else {
                mBinding.rvReport.visibility = View.GONE
                loading.dialog.dismiss()
            }
        })
    }
}