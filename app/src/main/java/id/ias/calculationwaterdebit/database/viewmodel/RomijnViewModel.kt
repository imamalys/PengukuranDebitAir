package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.RomijnModel
import id.ias.calculationwaterdebit.database.repository.RomijnRepository
import kotlinx.coroutines.launch

class RomijnViewModel(private val repository: RomijnRepository): ViewModel() {
    val allLtfDatas: LiveData<List<RomijnModel>> = repository.allRomijnDatas.asLiveData()
    var idTipeBangunan: MutableLiveData<Long> = MutableLiveData(0)
    val romijnById: MutableLiveData<RomijnModel> = MutableLiveData()

    fun getRomijnDataById(id: Int) = viewModelScope.launch {
        romijnById.value = repository.getRomijnDataById(id)
    }

    fun getRomijnDataByIdBaseData(id: Int) = viewModelScope.launch {
        romijnById.value = repository.getRomijnDataByIdBaseData(id)
    }

    fun insert(romijnModel: RomijnModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.insert(romijnModel)
    }

    fun update(romijnModel: RomijnModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.update(romijnModel).toLong()
    }
}

class RomijnViewModelFactory(private val repository: RomijnRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RomijnViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RomijnViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}