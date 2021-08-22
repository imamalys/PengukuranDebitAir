package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.ias.calculationwaterdebit.database.model.AmbangTipisSegitigaSudutModel
import id.ias.calculationwaterdebit.database.repository.AmbangTipisSegitigaSudutRepository
import kotlinx.coroutines.launch

class AmbangTipisSegitigaSudutViewModel(private val repository: AmbangTipisSegitigaSudutRepository): ViewModel() {
    fun getAmbangTipisSegitigaSudutById(sudutDht: Int): AmbangTipisSegitigaSudutModel = repository.getAmbangTipisSegitigaSudutById(sudutDht)

    fun insert(ambangTipisSegitigaSudutModel: AmbangTipisSegitigaSudutModel) = viewModelScope.launch {
        repository.insert(ambangTipisSegitigaSudutModel)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class AmbangTipisSegitigaSudutViewModelFactory(private val repository: AmbangTipisSegitigaSudutRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AmbangTipisSegitigaSudutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AmbangTipisSegitigaSudutViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}