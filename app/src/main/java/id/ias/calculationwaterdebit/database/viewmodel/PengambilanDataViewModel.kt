package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.repository.PengambilanDataRepository
import kotlinx.coroutines.launch

class PengambilanDataViewModel(private val repository: PengambilanDataRepository): ViewModel() {
    val allPengambilanDatas: LiveData<List<PengambilanDataModel>> = repository.allPengambilanDatas.asLiveData()
    var idPengambilanData: MutableLiveData<Long> = MutableLiveData(0)
    var pengambilanDataUpdate: MutableLiveData<Int> = MutableLiveData()
    fun getPengambilanDataById(id: Int): LiveData<List<PengambilanDataModel>> = repository.getPengambilanDataById(id).asLiveData()

    fun insert(pengambilanData: PengambilanDataModel) = viewModelScope.launch {
        idPengambilanData.value = repository.insert(pengambilanData)
    }

    fun update(id: Int, jumlahRataRata: Float) = viewModelScope.launch {
        pengambilanDataUpdate.value = repository.update(id, jumlahRataRata)
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