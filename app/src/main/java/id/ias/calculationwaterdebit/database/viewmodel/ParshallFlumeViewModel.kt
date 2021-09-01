package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.ParshallFlumeModel
import id.ias.calculationwaterdebit.database.repository.ParshallFlumeRepository
import kotlinx.coroutines.launch

class ParshallFlumeViewModel(private val repository: ParshallFlumeRepository): ViewModel() {
    val allParshallFlumeDatas: LiveData<List<ParshallFlumeModel>> = repository.allParshallFlumeDatas.asLiveData()
    var idTipeBangunan: MutableLiveData<Long> = MutableLiveData(0)
    val parshallFlumeById: MutableLiveData<ParshallFlumeModel> = MutableLiveData()

    fun getParshallFlumeDataById(id: Int) = viewModelScope.launch {
        parshallFlumeById.value = repository.getParshallFlumeDataById(id)
    }

    fun getParshallFlumeDataByIdBaseData(id: Int) = viewModelScope.launch {
        parshallFlumeById.value = repository.getParshallFlumeDataByIdBaseData(id)
    }

    fun insert(parshallFlumeModel: ParshallFlumeModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.insert(parshallFlumeModel)
    }

    fun update(parshallFlumeModel: ParshallFlumeModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.update(parshallFlumeModel).toLong()
    }
}

class ParshallFlumeViewModelFactory(private val repository: ParshallFlumeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParshallFlumeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ParshallFlumeViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}