package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.repository.PengambilanDataRepository
import kotlinx.coroutines.launch

class PengambilanDataViewModel(private val repository: PengambilanDataRepository): ViewModel() {
    val allPengambilanDatas: LiveData<List<PengambilanDataModel>> = repository.allPengambilanDatas.asLiveData()

    fun insert(pengambilanData: PengambilanDataModel) = viewModelScope.launch {
        repository.insert(pengambilanData)
    }
}

class PengambilanDataViewModelFactory(private val repository: PengambilanDataRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PengambilanDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PengambilanDataViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}