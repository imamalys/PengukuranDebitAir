package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.ias.calculationwaterdebit.database.model.MercuAmbangModel
import id.ias.calculationwaterdebit.database.repository.AmbangLebarPengontrolTrapesiumRepository
import id.ias.calculationwaterdebit.database.repository.MercuAmbangRepository
import kotlinx.coroutines.launch

class MercuAmbangViewModel(private val repository: MercuAmbangRepository): ViewModel() {
    fun getMercuAmbangById(bPerB: Float): MercuAmbangModel = repository.getMercuAmbangById(bPerB)

    fun insert(mercuAmbangModel: MercuAmbangModel) = viewModelScope.launch {
        repository.insert(mercuAmbangModel)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class MercuAmbangViewModelFactory(private val repository: MercuAmbangRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MercuAmbangViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MercuAmbangViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}