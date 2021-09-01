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
import id.ias.calculationwaterdebit.util.*

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

    private val koefisiensiCutThroatedFlumeViewModel: KoefisiensiCutThroatedFlumeViewModel by viewModels {
        KoefisiensiCutThroatedFlumeViewModelFactory((application as Application).koefisiensiCutThroatedFlumeRepository)
    }

    private val koefisiensiAmbangTajamSegiempatViewModel: KoefisiensiAmbangTajamSegiempatViewModel by viewModels {
        KoefisiensiAmbangTajamSegiempatViewModelFactory((application as Application).koefisiensiAmbangTajamSegiempatRepository)
    }

    private val mercuAmbangViewModel: MercuAmbangViewModel by viewModels {
        MercuAmbangViewModelFactory((application as Application).mercuAmbangRepository)
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

        mBinding.ivIcon1.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, ReportActivity::class.java)
            intent.putExtra("type", 0)
            startActivity(intent)
        }

        mBinding.ivIcon2.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, ReportActivity::class.java)
            intent.putExtra("type", 1)
            startActivity(intent)
        }

        mBinding.ivIcon3.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setViewModel() {
        val koefisiensiAliranSempurna = koefisiensiAliranSempurnaViewModel.getKoefisiensiAliranSempurnaById((0.150).toFloat())
        if (koefisiensiAliranSempurna == null) {
            loadingShow()
            injectAliranSempurna(0)
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

        val koefisiensiCutThroatedFlumeModel = koefisiensiCutThroatedFlumeViewModel.getKoefisiensiCutThroatedFlumeViewModelById((0.50).toFloat())
        if (koefisiensiCutThroatedFlumeModel == null) {
            loadingShow()

            DBKoefisiensiCutThroatedFlumeUtil.insertKoefisiensiCutThroatedFlume(koefisiensiCutThroatedFlumeViewModel,
                    object: DBKoefisiensiCutThroatedFlumeUtil.InsertSuccess {
                        override fun koefisiensiCutThroatedFlume(count: Int) {
                            loadingDismiss()
                        }
                    })
        }

        val mercuAmbangModel = mercuAmbangViewModel.getMercuAmbangById((0.11).toFloat())
        if (mercuAmbangModel == null) {
            loadingShow()

            DBMercuAmbangUtil.insertMercuAmbang(mercuAmbangViewModel, object: DBMercuAmbangUtil.InsertSuccess {
                override fun mercuAmbang(count: Int) {
                    loadingDismiss()
                }

            })
        }

        val koefisiensiAmbangTajamSegitigaModel =
                koefisiensiAmbangTajamSegiempatViewModel.getKoefisiensiAmbangTajamSegiempatViewModelById((1.0).toFloat(),(0.01).toFloat())
        if (koefisiensiAmbangTajamSegitigaModel == null) {
            loadingShow()

            injectAmbangTajamSegitiga(0)
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

    private fun injectAmbangTajamSegitiga(count: Int) {
        when (count) {
            1 -> {
                DBKoefisiensiAmbangTajamSegiempatUtil.insertAmbangTajamSegitiga2(
                    koefisiensiAmbangTajamSegiempatViewModel,
                    object: DBKoefisiensiAmbangTajamSegiempatUtil.InsertSuccess {
                        override fun koefisiensiAmbangTajamSegitiga(count: Int) {
                            injectAmbangTajamSegitiga(count)
                        }
                    })
            }
            2 -> {
                DBKoefisiensiAmbangTajamSegiempatUtil.insertAmbangTajamSegitiga3(
                        koefisiensiAmbangTajamSegiempatViewModel,
                        object: DBKoefisiensiAmbangTajamSegiempatUtil.InsertSuccess {
                            override fun koefisiensiAmbangTajamSegitiga(count: Int) {
                                injectAmbangTajamSegitiga(count)
                            }
                        })
            }
            3 -> {
                DBKoefisiensiAmbangTajamSegiempatUtil.insertAmbangTajamSegitiga4(
                        koefisiensiAmbangTajamSegiempatViewModel,
                        object: DBKoefisiensiAmbangTajamSegiempatUtil.InsertSuccess {
                            override fun koefisiensiAmbangTajamSegitiga(count: Int) {
                                injectAmbangTajamSegitiga(count)
                            }
                        })
            }
            4 -> {
                DBKoefisiensiAmbangTajamSegiempatUtil.insertAmbangTajamSegitiga5(
                        koefisiensiAmbangTajamSegiempatViewModel,
                        object: DBKoefisiensiAmbangTajamSegiempatUtil.InsertSuccess {
                            override fun koefisiensiAmbangTajamSegitiga(count: Int) {
                                injectAmbangTajamSegitiga(count)
                            }
                        })
            }
            5 -> {
                loadingDismiss()
            }
            else -> {
                DBKoefisiensiAmbangTajamSegiempatUtil.insertAmbangTajamSegitiga(
                        koefisiensiAmbangTajamSegiempatViewModel,
                        object: DBKoefisiensiAmbangTajamSegiempatUtil.InsertSuccess {
                            override fun koefisiensiAmbangTajamSegitiga(count: Int) {
                                injectAmbangTajamSegitiga(count)
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