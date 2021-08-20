package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.KoefisiensiAliranSempurnaModel
import id.ias.calculationwaterdebit.database.repository.KoefisiensiAliranSempurnaRepository
import kotlinx.coroutines.launch

class KoefisiensiAliranSempurnaViewModel(private val repository: KoefisiensiAliranSempurnaRepository): ViewModel() {
    fun getKoefisiensiAliranSempurnaById(b: Float = (0.150).toFloat()): KoefisiensiAliranSempurnaModel = repository.getKoefisiensiAliranSempurnById(b)

    fun insert(koefisiensiAliranSempurnaModel: KoefisiensiAliranSempurnaModel) = viewModelScope.launch {
        repository.insert(koefisiensiAliranSempurnaModel)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class KoefisiensiAliranSempurnaViewModelFactory(private val repository: KoefisiensiAliranSempurnaRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KoefisiensiAliranSempurnaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KoefisiensiAliranSempurnaViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}