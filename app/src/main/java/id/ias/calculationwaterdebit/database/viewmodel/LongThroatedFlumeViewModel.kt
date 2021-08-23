package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.LongThrotedFlumeModel
import id.ias.calculationwaterdebit.database.repository.AmbangLebarPengontrolSegiempatRepository
import id.ias.calculationwaterdebit.database.repository.LongThroatedFlumeRepository
import kotlinx.coroutines.launch

class LongThroatedFlumeViewModel(private val repository: LongThroatedFlumeRepository): ViewModel() {
    val allLtfDatas: LiveData<List<LongThrotedFlumeModel>> = repository.allLtfDatas.asLiveData()
    var idTipeBangunan: MutableLiveData<Long> = MutableLiveData(0)
    val ltfById: MutableLiveData<LongThrotedFlumeModel> = MutableLiveData()

    fun getLtfDataById(id: Int) = viewModelScope.launch {
        ltfById.value = repository.getLtfDataById(id)
    }

    fun insert(ltfData: LongThrotedFlumeModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.insert(ltfData)
    }
}

class LongThroatedFlumeViewModelFactory(private val repository: LongThroatedFlumeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LongThroatedFlumeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LongThroatedFlumeViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}