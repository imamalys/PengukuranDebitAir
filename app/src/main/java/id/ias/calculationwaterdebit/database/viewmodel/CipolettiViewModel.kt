package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.CipolettiModel
import id.ias.calculationwaterdebit.database.repository.CipolettiRepository
import kotlinx.coroutines.launch

class CipolettiViewModel(private val repository: CipolettiRepository): ViewModel() {
    val allCipolettiDatas: LiveData<List<CipolettiModel>> = repository.allCipolettiDatas.asLiveData()
    var idTipeBangunan: MutableLiveData<Long> = MutableLiveData(0)
    val cipolettiById: MutableLiveData<CipolettiModel> = MutableLiveData()

    fun getCipolettiDataById(id: Int) = viewModelScope.launch {
        cipolettiById.value = repository.getCipolettiDataById(id)
    }

    fun getCipolettiDataByIdBaseData(id: Int) = viewModelScope.launch {
        cipolettiById.value = repository.getCipolettiDataByIdBaseData(id)
    }

    fun insert(cipolettiModel: CipolettiModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.insert(cipolettiModel)
    }

    fun update(cipolettiModel: CipolettiModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.update(cipolettiModel).toLong()
    }
}

class CipolettiViewModelFactory(private val repository: CipolettiRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CipolettiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CipolettiViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}