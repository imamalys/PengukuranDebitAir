package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.PiasModel
import id.ias.calculationwaterdebit.database.repository.PiasDataRepository
import kotlinx.coroutines.launch

class PiasDataViewModel(private val repository: PiasDataRepository): ViewModel() {
    val piasDatas: MutableLiveData<List<PiasModel>> = MutableLiveData()
    val insertId: MutableLiveData<Long> = MutableLiveData(0)

    fun getPiasDatas(id: Int): LiveData<List<PiasModel>> = repository.piasByFormData(id).asLiveData()
    
    fun insert(piasModel: PiasModel) = viewModelScope.launch {
        insertId.value = repository.insert(piasModel)
    }
}

class PiasDataViewModelFactory(private val repository: PiasDataRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PiasDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PiasDataViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}