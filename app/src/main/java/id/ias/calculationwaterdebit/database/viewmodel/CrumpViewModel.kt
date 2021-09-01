package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.CrumpModel
import id.ias.calculationwaterdebit.database.repository.CrumpRepository
import kotlinx.coroutines.launch

class CrumpViewModel(private val repository: CrumpRepository): ViewModel() {
    val allCrumpDatas: LiveData<List<CrumpModel>> = repository.allCrumpDatas.asLiveData()
    var idTipeBangunan: MutableLiveData<Long> = MutableLiveData(0)
    val crumpById: MutableLiveData<CrumpModel> = MutableLiveData()

    fun getCrumpDataById(id: Int) = viewModelScope.launch {
        crumpById.value = repository.getCrumpDataById(id)
    }

    fun getCrumpDataByIdBaseData(id: Int) = viewModelScope.launch {
        crumpById.value = repository.getCrumpDataByIdBaseData(id)
    }

    fun insert(crumpModel: CrumpModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.insert(crumpModel)
    }

    fun update(crumpModel: CrumpModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.update(crumpModel).toLong()
    }
}

class CrumpViewModelFactory(private val repository: CrumpRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrumpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CrumpViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}