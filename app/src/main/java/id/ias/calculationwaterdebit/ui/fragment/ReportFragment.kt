package id.ias.calculationwaterdebit.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.adapter.NewReportAdapter
import id.ias.calculationwaterdebit.base.BaseFragment
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.FragmentReportBinding
import id.ias.calculationwaterdebit.ui.DataInformasiActivity
import id.ias.calculationwaterdebit.ui.ReportDetailActivity
import id.ias.calculationwaterdebit.util.MessageDialogUtil

private const val REPORT_TYPE = "report_type"

class ReportFragment : BaseFragment() {
    val dialog = MessageDialogUtil()
    private lateinit var mBinding: FragmentReportBinding

    private val baseDataViewModel: BaseDataViewModel by viewModels {
        BaseDataViewModelFactory((activity?.application as Application).baseDataRepository)
    }

    private var type: Int = 0

    override fun initViews() {
        Glide.with(this)
            .load(R.drawable.logo_app)
            .circleCrop()
            .into(mBinding.ivTopLeft)

        setViewModel()
    }

    private fun setViewModel() {
        loadingShow()
        baseDataViewModel.allBaseDatas.observe(this, {
            if (it.isNotEmpty()) {
                mBinding.rvReport.layoutManager = LinearLayoutManager(activity)
                val adapter = NewReportAdapter(it, object : NewReportAdapter.Listener {
                    override fun onClick(baseDataModel: BaseDataModel) {
                        if (baseDataModel.k == null) {
                            ToastUtils.showLong("Data tidak lengkap")
                        } else {
                            if (type == 0) {
                                val intent =
                                    Intent(activity, DataInformasiActivity::class.java)
                                intent.putExtra("id_base_data", baseDataModel.id!!.toLong())
                                startActivity(intent)
                            } else {
                                val intent =
                                    Intent(activity, ReportDetailActivity::class.java)
                                intent.putExtra("id_base_data", baseDataModel.id!!.toLong())
                                intent.putExtra("is_report", true)
                                startActivity(intent)
                            }
                        }
                    }

                    override fun onDelete(baseDataModel: BaseDataModel) {
                        context?.let { it1 ->
                            dialog.show(
                                it1, "Apakah anda yakin menghapus data?",
                                "Iya", "Tidak", object : MessageDialogUtil.DialogListener {
                                    override fun onYes(action: Boolean) {
                                        if (action) {
                                            loadingShow()
                                            baseDataViewModel.delete(baseDataModel)
                                        }
                                    }
                                })
                        }
                    }
                })
                mBinding.rvReport.adapter = adapter
                loading.dialog.dismiss()
            } else {
                mBinding.rvReport.visibility = View.GONE
                loading.dialog.dismiss()
            }
        })
    }

    override fun setArguments() {
        arguments?.let {
            type = it.getInt(REPORT_TYPE)
        }
    }

    override fun createViewBinding(): View {
        mBinding = FragmentReportBinding.inflate(layoutInflater)
        return mBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(type: Int) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putInt(REPORT_TYPE, type)
                }
            }
    }
}