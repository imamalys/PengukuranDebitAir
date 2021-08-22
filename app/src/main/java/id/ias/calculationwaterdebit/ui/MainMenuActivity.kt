package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.adapter.BannerTipeBangunanAdapter
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.database.viewmodel.AmbangTipisSegitigaSudutViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityMainMenuBinding
import id.ias.calculationwaterdebit.helper.CirclePagerIndicatorDecoration
import id.ias.calculationwaterdebit.util.DBAmbangTipisSegitigaSudutUtil
import id.ias.calculationwaterdebit.util.DBInjectorUtil
import id.ias.calculationwaterdebit.util.DBKoefisiensiAmbangTipisSegitigaUtil
import id.ias.calculationwaterdebit.util.LoadingDialogUtil

class MainMenuActivity : AppCompatActivity() {
    val loading = LoadingDialogUtil()
    lateinit var mBinding: ActivityMainMenuBinding

    private val koefisiensiAliranSempurnaViewModel: KoefisiensiAliranSempurnaViewModel by viewModels {
        KoefisiensiAliranSempurnaViewModelFactory((application as Application).koefisiensiAliranSempurnaRepository)
    }
    
    private val ambangTipisSegitigaSudutViewModel: AmbangTipisSegitigaSudutViewModel by viewModels {
        AmbangTipisSegitigaSudutViewModelFactory((application as Application).ambangTipisSegitigaSudutRepository)
    }
    
    private val koefisiensiAmbangTipisSegitigaViewModel: KoefisiensiAmbangTipisSegitigaViewModel by viewModels {
        KoefisiensiAmbangTipisSegitigaViewModelFactory((application as Application).koefisiensiAmbangTipisSegitigaRepository)
    }

    private lateinit var SCROLLING_RUNNABLE: Runnable
    private lateinit var mHandler: Handler
    var count = 0
    var isLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setAction()

        Glide.with(this)
                .load(R.drawable.logo_app)
                .circleCrop()
                .into(mBinding.ivTopLeft)

        mHandler = Handler(Looper.getMainLooper())
        val promotionBannerAdapter = BannerTipeBangunanAdapter(this)
        SCROLLING_RUNNABLE = object : Runnable {
            override fun run() {
                if (count != promotionBannerAdapter.itemCount - 1) {
                    count++
                }
                //                mBinding.listBanner.smoothScrollBy(pixelsToMove, 0);
                mBinding.rvTipeBangunan.smoothScrollToPosition(count)
                mHandler.postDelayed(this, 5000)
            }
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.rvTipeBangunan.layoutManager = layoutManager
        mBinding.rvTipeBangunan.adapter = promotionBannerAdapter
        mBinding.rvTipeBangunan.addItemDecoration(CirclePagerIndicatorDecoration(this))
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

    override fun onStop() {
        super.onStop()
        mHandler.removeCallbacks(SCROLLING_RUNNABLE)
    }

    private fun setAction() {
        mBinding.ivIcon.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, DataInformasiActivity::class.java)
            startActivity(intent)
        }

        mBinding.ivIcon2.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, ReportActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setViewModel() {
        val koefisiensiAliranSempurna = koefisiensiAliranSempurnaViewModel.getKoefisiensiAliranSempurnaById((0.150).toFloat())
        if (koefisiensiAliranSempurna == null) {
            loadingShow()
            injectAliranSempurna(count)
        }

        val koefisiensiAmbangTipisSegitiga = koefisiensiAmbangTipisSegitigaViewModel.getAmbangTipisSegitigaCdViewModelById((1.0).toFloat(), (0.13).toFloat())
        if (koefisiensiAmbangTipisSegitiga == null) {
            loadingShow()

            DBKoefisiensiAmbangTipisSegitigaUtil.insertKoefisiensiAmbangTipisSegitiga(koefisiensiAmbangTipisSegitigaViewModel, object: DBKoefisiensiAmbangTipisSegitigaUtil.InsertSuccess {
                override fun koefisiensiAmbangTipisSegitiga(count: Int) {
                    loadingDismiss()
                }
            })
        }

        val ambangTipisSegitigaModelSudut = ambangTipisSegitigaSudutViewModel.getAmbangTipisSegitigaSudutById(21)
        if (ambangTipisSegitigaModelSudut == null) {
            loadingShow()

            DBAmbangTipisSegitigaSudutUtil.insertSudutAmbangTipisSegitiga(ambangTipisSegitigaSudutViewModel, object: DBAmbangTipisSegitigaSudutUtil.InsertSuccess{
                override fun ambangTipisSegitiga(count: Int) {
                    loadingDismiss()
                }
            })
        }
    }

    private fun injectAliranSempurna(count: Int) {
        when (count) {
            1 -> {
                DBInjectorUtil.insertKoefisiensiAliranSempurna2(koefisiensiAliranSempurnaViewModel, object: DBInjectorUtil.Companion.InsertSuccess{
                    override fun aliranSempurna(count: Int) {
                        injectAliranSempurna(count)
                    }
                })
            }
            2 -> {
                DBInjectorUtil.insertKoefisiensiAliranSempurna3(koefisiensiAliranSempurnaViewModel, object: DBInjectorUtil.Companion.InsertSuccess{
                    override fun aliranSempurna(count: Int) {
                        injectAliranSempurna(count)
                    }
                })
            }
            3 -> {
                loadingDismiss()
            }
            else -> {
                DBInjectorUtil.insertKoefisiensiAliranSempurna(koefisiensiAliranSempurnaViewModel, object: DBInjectorUtil.Companion.InsertSuccess{
                    override fun aliranSempurna(count: Int) {
                        injectAliranSempurna(count)
                    }
                })
            }
        }
    }

    fun loadingShow() {
        if (!isLoading) {
            isLoading = true
            loading.show(this)
        }
    }

    fun loadingDismiss() {
        if (isLoading) {
            isLoading = false
            loading.dialog.dismiss()
        }
    }
}