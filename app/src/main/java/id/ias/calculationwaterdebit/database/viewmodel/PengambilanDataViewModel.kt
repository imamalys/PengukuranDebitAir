package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.repository.PengambilanDataRepository
import kotlinx.coroutines.launch

class PengambilanDataViewModel(private val repository: PengambilanDataRepository): ViewModel() {
    val allPengambilanDatas: LiveData<List<PengambilanDataModel>> = repository.allPengambilanDatas.asLiveData()
    var idPengambilanData: MutableLiveData<Long> = MutableLiveData(0)
    var idUpdateData: MutableLiveData<Int> = MutableLiveData(0)
    var pengambilanDataUpdate: MutableLiveData<Int> = MutableLiveData()
    var pengambilanDataById: MutableLiveData<List<PengambilanDataModel>> = MutableLiveData()
    fun getPengambilanDataById(id: Int) = viewModelScope.launch {
        pengambilanDataById.value = repository.getPengambilanDataById(id)
    }

    fun insert(pengambilanData: PengambilanDataModel) = viewModelScope.launch {
        idPengambilanData.value = repository.insert(pengambilanData)
    }

    fun update(pengambilanData: PengambilanDataModel) = viewModelScope.launch {
        idUpdateData.value = repository.update(pengambilanData)
    }

    fun update(id: Int, jumlahRataRata: Float, debitSaluran: Float) = viewModelScope.launch {
        pengambilanDataUpdate.value = repository.update(id, jumlahRataRata, debitSaluran)
    }

    fun update(id: Int, qBangunan: Float) = viewModelScope.launch {
        pengambilanDataUpdate.value = repository.update(id, qBangunan)
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