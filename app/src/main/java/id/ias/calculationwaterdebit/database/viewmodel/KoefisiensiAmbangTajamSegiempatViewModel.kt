package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.ias.calculationwaterdebit.database.model.KoefisiensiAmbangTajamSegiempatModel
import id.ias.calculationwaterdebit.database.repository.KoefisiensiAmbangTajamSegiempatRepository
import kotlinx.coroutines.launch

class KoefisiensiAmbangTajamSegiempatViewModel(private val repository: KoefisiensiAmbangTajamSegiempatRepository): ViewModel() {
    fun getKoefisiensiAmbangTajamSegiempatViewModelById(nilai: Float, hPerP: Float): KoefisiensiAmbangTajamSegiempatModel =
        repository.getKoefisiensiAmbangTajamSegiempatById(nilai, hPerP)

    fun insert(koefisiensiAmbangTajamSegiempatModel: KoefisiensiAmbangTajamSegiempatModel) = viewModelScope.launch {
        repository.insert(koefisiensiAmbangTajamSegiempatModel)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class KoefisiensiAmbangTajamSegiempatViewModelFactory(private val repositoryKoefiesiensi: KoefisiensiAmbangTajamSegiempatRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KoefisiensiAmbangTajamSegiempatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KoefisiensiAmbangTajamSegiempatViewModel(repositoryKoefiesiensi) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}
