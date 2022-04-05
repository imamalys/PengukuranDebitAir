package id.ias.calculationwaterdebit.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.adapter.ReportAdapter
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.ui.DataInformasiActivity
import id.ias.calculationwaterdebit.ui.ReportDetailActivity
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import id.ias.calculationwaterdebit.util.MessageDialogUtil

abstract class BaseFragment: Fragment() {
    val loading = LoadingDialogUtil()
    lateinit var viewBinding: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setArguments()
        viewBinding = createViewBinding()
        return viewBinding.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    protected abstract fun initViews()

    protected abstract fun setArguments()

    protected abstract fun createViewBinding(): View

    protected fun loadingShow() {
        if (!loading.isShow()) {
            context?.let { loading.show(it) }
        }
    }

    protected fun loadingDismiss() {
        if (loading.isShow()) {
            loading.dialog.dismiss()
        }
    }
}