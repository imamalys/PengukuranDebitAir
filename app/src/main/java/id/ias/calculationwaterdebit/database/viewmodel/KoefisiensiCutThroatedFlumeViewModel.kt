package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.ias.calculationwaterdebit.database.model.KoefisiensiAmbangTipisSegitigaModel
import id.ias.calculationwaterdebit.database.model.KoefisiensiCutThroatedFlumeModel
import id.ias.calculationwaterdebit.database.repository.KoefisiensiAmbangTipisSegitigaRepository
import id.ias.calculationwaterdebit.database.repository.KoefisiensiCutThroatedFlumeRepository
import kotlinx.coroutines.launch

class KoefisiensiCutThroatedFlumeViewModel(private val repository: KoefisiensiCutThroatedFlumeRepository): ViewModel() {
    fun getKoefisiensiCutThroatedFlumeViewModelById(b: Float): KoefisiensiCutThroatedFlumeModel =
            repository.getKoefisiensiCutThroatedFlumeById(b)

    fun insert(koefisiensiCutThroatedFlumeModel: KoefisiensiCutThroatedFlumeModel) = viewModelScope.launch {
        repository.insert(koefisiensiCutThroatedFlumeModel)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class KoefisiensiCutThroatedFlumeViewModelFactory(private val repositoryKoefiesiensi: KoefisiensiCutThroatedFlumeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KoefisiensiCutThroatedFlumeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KoefisiensiCutThroatedFlumeViewModel(repositoryKoefiesiensi) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}