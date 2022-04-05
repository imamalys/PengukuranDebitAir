package id.ias.calculationwaterdebit.ui.fragment

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.adapter.BannerTipeBangunanAdapter
import id.ias.calculationwaterdebit.adapter.RiwayatDataAdapter
import id.ias.calculationwaterdebit.base.BaseFragment
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.FragmentHomeBinding
import id.ias.calculationwaterdebit.helper.CirclePagerIndicatorDecoration
import id.ias.calculationwaterdebit.helper.SimpleDividerItemDecoration

class HomeFragment : BaseFragment() {

    private val baseDataViewModel: BaseDataViewModel by viewModels {
        BaseDataViewModelFactory((activity?.application as Application).baseDataRepository)
    }

    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var SCROLLING_RUNNABLE: Runnable
    private lateinit var mHandler: Handler
    var count = 0

    override fun initViews() {
        Glide.with(this)
            .load(R.drawable.logo_app)
            .circleCrop()
            .into(mBinding.ivTopLeft)

        mHandler = Handler(Looper.getMainLooper())
        val promotionBannerAdapter = context?.let { BannerTipeBangunanAdapter(it) }
        SCROLLING_RUNNABLE = object : Runnable {
            override fun run() {
                if (promotionBannerAdapter != null) {
                    if (count != promotionBannerAdapter.itemCount - 1) {
                        count++
                    }
                }
                mBinding.rvTipeBangunan.smoothScrollToPosition(count)
                mHandler.postDelayed(this, 5000)
            }
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mBinding.rvTipeBangunan.layoutManager = layoutManager
        mBinding.rvTipeBangunan.adapter = promotionBannerAdapter
        context?.let { CirclePagerIndicatorDecoration(it) }?.let {
            mBinding.rvTipeBangunan.addItemDecoration(
                it
            )
        }
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(mBinding.rvTipeBangunan)
        mBinding.rvTipeBangunan.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastItem: Int = layoutManager.findLastCompletelyVisibleItemPosition()
                if (lastItem == layoutManager.getItemCount() - 1) {
                    mHandler.removeCallbacks(SCROLLING_RUNNABLE)
                    val postHandler = Handler()
                    postHandler.postDelayed({
                        count = 0
                        mBinding.rvTipeBangunan.smoothScrollToPosition(0)
                        mHandler.postDelayed(SCROLLING_RUNNABLE, 5000)
                    }, 5000)
                }
            }
        })

        mHandler.postDelayed(SCROLLING_RUNNABLE, 200)

        setViewModel()
    }

    override fun setArguments() {

    }

    private fun setViewModel() {
        loadingShow()
        baseDataViewModel.allBaseDatas.observe(this, {
            if (it.isNotEmpty()) {
                mBinding.rvRiwayat.layoutManager = LinearLayoutManager(activity)
                context?.let { it1 ->
                    SimpleDividerItemDecoration(
                        it1, R.drawable.line_divider)
                }?.let { it2 -> mBinding.rvRiwayat.addItemDecoration(it2) }
                val adapter = RiwayatDataAdapter(it)
                mBinding.rvRiwayat.adapter = adapter
                mBinding.clRvRiwayat.visibility = View.VISIBLE
                mBinding.clNoRiwayat.visibility = View.GONE
                loadingDismiss()
            } else {
                mBinding.clRvRiwayat.visibility = View.GONE
                mBinding.clNoRiwayat.visibility = View.VISIBLE
                loadingDismiss()
            }
        })
    }

    override fun createViewBinding(): View {
        mBinding = FragmentHomeBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onStop() {
        super.onStop()
        mHandler.removeCallbacks(SCROLLING_RUNNABLE)
    }

    companion object {
        const val TAG_HOME = "TAG_HOME"
    }
}