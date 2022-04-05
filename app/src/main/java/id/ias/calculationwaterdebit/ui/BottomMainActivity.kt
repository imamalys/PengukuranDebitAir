package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentTransaction
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.base.BaseActivity
import id.ias.calculationwaterdebit.databinding.ActivityBottomMainBinding
import id.ias.calculationwaterdebit.ui.fragment.AboutFragment
import id.ias.calculationwaterdebit.ui.fragment.HomeFragment
import id.ias.calculationwaterdebit.ui.fragment.ReportFragment
import id.ias.calculationwaterdebit.util.*


class BottomMainActivity : BaseActivity() {

    lateinit var mBinding: ActivityBottomMainBinding

    override fun initViews() {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(mBinding.frameLayout.id, HomeFragment())
        ft.commit()

        mBinding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.replace(mBinding.frameLayout.id, HomeFragment(), HomeFragment.TAG_HOME)
                    ft.commit()
                    true
                }
                R.id.load -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.replace(mBinding.frameLayout.id, ReportFragment.newInstance(0))
                    ft.commit()
                    true
                }
                R.id.report -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.replace(mBinding.frameLayout.id, ReportFragment.newInstance(1))
                    ft.commit()
                    true
                }
                R.id.about -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.replace(mBinding.frameLayout.id, AboutFragment())
                    ft.commit()
                    true
                }
                else -> false
            }
        }

        mBinding.fab.setOnClickListener {
            val intent = Intent(this, DataInformasiActivity::class.java)
            startActivity(intent)
        }

        setViewModel()
    }

    override fun createViewBinding(): View {
        mBinding = ActivityBottomMainBinding.inflate(layoutInflater)
        return mBinding.root
    }

    private fun setViewModel() {
        val koefisiensiAliranSempurna = koefisiensiAliranSempurnaViewModel.getKoefisiensiAliranSempurnaById((0.150).toFloat())
        if (koefisiensiAliranSempurna == null) {
            loadingShow()
            injectAliranSempurna(0)
        }

        val koefisiensiAmbangLebarSegiempat = koefisiensiAmbangLebarViewModel.getKoefiensiAmbangLebarById((0.02).toFloat())
        if (koefisiensiAmbangLebarSegiempat == null) {
            loadingShow()

            DBKoefisiensiAmbangLebarSegiempat.insertKoefisiensiAmbangLebarSegiempat(koefisiensiAmbangLebarViewModel, object: DBKoefisiensiAmbangLebarSegiempat.Companion.InsertSuccess {
                override fun ambangLebarSegiempat(count: Int) {
                    loadingDismiss()
                }
            })
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
}