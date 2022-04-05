package id.ias.calculationwaterdebit.base

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.util.LoadingDialogUtil

abstract class BaseActivity: AppCompatActivity() {
    val loading = LoadingDialogUtil()
    lateinit var viewBinding: View

    val koefisiensiAliranSempurnaViewModel: KoefisiensiAliranSempurnaViewModel by viewModels {
        KoefisiensiAliranSempurnaViewModelFactory((application as Application).koefisiensiAliranSempurnaRepository)
    }

    val ambangTipisSegitigaSudutViewModel: AmbangTipisSegitigaSudutViewModel by viewModels {
        AmbangTipisSegitigaSudutViewModelFactory((application as Application).ambangTipisSegitigaSudutRepository)
    }

    val koefisiensiAmbangTipisSegitigaViewModel: KoefisiensiAmbangTipisSegitigaViewModel by viewModels {
        KoefisiensiAmbangTipisSegitigaViewModelFactory((application as Application).koefisiensiAmbangTipisSegitigaRepository)
    }

    val koefisiensiCutThroatedFlumeViewModel: KoefisiensiCutThroatedFlumeViewModel by viewModels {
        KoefisiensiCutThroatedFlumeViewModelFactory((application as Application).koefisiensiCutThroatedFlumeRepository)
    }

    val koefisiensiAmbangTajamSegiempatViewModel: KoefisiensiAmbangTajamSegiempatViewModel by viewModels {
        KoefisiensiAmbangTajamSegiempatViewModelFactory((application as Application).koefisiensiAmbangTajamSegiempatRepository)
    }

    val mercuAmbangViewModel: MercuAmbangViewModel by viewModels {
        MercuAmbangViewModelFactory((application as Application).mercuAmbangRepository)
    }

    val koefisiensiAmbangLebarViewModel: KoefisiensiAmbangLebarViewModel by viewModels {
        KoefisiensiAmbangLebarViewModelFactory((application as Application).koefisiensiAmbangLebarRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = createViewBinding()
        setContentView(viewBinding)

        initViews()
    }

    protected abstract fun initViews()

    protected abstract fun createViewBinding(): View

    protected fun loadingShow() {
        if (!loading.isShow()) {
            loading.show(this)
        }
    }

    protected fun loadingDismiss() {
        if (loading.isShow()) {
            loading.dialog.dismiss()
        }
    }
}