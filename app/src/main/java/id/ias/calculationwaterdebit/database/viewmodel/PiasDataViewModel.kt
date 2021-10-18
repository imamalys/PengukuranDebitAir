package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.PiasModel
import id.ias.calculationwaterdebit.database.repository.PiasDataRepository
import kotlinx.coroutines.launch

class PiasDataViewModel(private val repository: PiasDataRepository): ViewModel() {
    val piasDatas: MutableLiveData<List<PiasModel>> = MutableLiveData()
    val insertId: MutableLiveData<Long> = MutableLiveData(0)
    val updateId: MutableLiveData<Int> = MutableLiveData(0)

    fun getPiasDatas(id: Int): LiveData<List<PiasModel>> = repository.piasByFormData(id).asLiveData()

    fun getPiasDataById(id: Int) = viewModelScope.launch {
        piasDatas.value = repository.piasByid(id)
    }
    fun insert(piasModel: PiasModel) = viewModelScope.launch {
        insertId.value = repository.insert(piasModel)
    }

    fun update(piasModel: PiasModel) = viewModelScope.launch {
        updateId.value = repository.update(piasModel)
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