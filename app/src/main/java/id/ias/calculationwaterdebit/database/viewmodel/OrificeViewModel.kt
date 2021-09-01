package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.OrificeModel
import id.ias.calculationwaterdebit.database.repository.OrificeRepository
import kotlinx.coroutines.launch

class OrificeViewModel(private val repository: OrificeRepository): ViewModel() {
    val allOrificeDatas: LiveData<List<OrificeModel>> = repository.allOrificeDatas.asLiveData()
    var idTipeBangunan: MutableLiveData<Long> = MutableLiveData(0)
    val orificeById: MutableLiveData<OrificeModel> = MutableLiveData()

    fun getOrificeDataById(id: Int) = viewModelScope.launch {
        orificeById.value = repository.getOrificeDataById(id)
    }

    fun getOrificeDataByIdBaseData(id: Int) = viewModelScope.launch {
        orificeById.value = repository.getOrificeDataByIdBaseData(id)
    }

    fun insert(orificeModel: OrificeModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.insert(orificeModel)
    }
    fun update(orificeModel: OrificeModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.update(orificeModel).toLong()
    }

}

class OrificeViewModelFactory(private val repository: OrificeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrificeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrificeViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}