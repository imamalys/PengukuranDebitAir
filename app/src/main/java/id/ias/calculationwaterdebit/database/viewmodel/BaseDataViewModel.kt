package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.database.repository.BaseDataRepository
import kotlinx.coroutines.launch

class BaseDataViewModel(private val repository: BaseDataRepository): ViewModel() {
    val allBaseDatas: LiveData<List<BaseDataModel>> = repository.allBaseDatas.asLiveData()
    var insertId: MutableLiveData<Long> = MutableLiveData(0)

    fun insert(baseData: BaseDataModel) = viewModelScope.launch {
        insertId.value = repository.insert(baseData)
    }
}

class BaseDataViewModelFactory(private val repository: BaseDataRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BaseDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BaseDataViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}