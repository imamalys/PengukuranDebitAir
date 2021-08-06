package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolSegiempatModel
import id.ias.calculationwaterdebit.database.model.FormDataModel
import id.ias.calculationwaterdebit.database.repository.AmbangLebarPengontrolSegiempatRepository
import kotlinx.coroutines.launch

class AmbangLebarPengontrolSegiempatViewModel(private val repository: AmbangLebarPengontrolSegiempatRepository): ViewModel() {
    val allAlpsDatas: LiveData<List<AmbangLebarPengontrolSegiempatModel>> = repository.allAlpsDatas.asLiveData()
    var idTipeBangunan: MutableLiveData<Long> = MutableLiveData(0)
    val alpsById: MutableLiveData<AmbangLebarPengontrolSegiempatModel> = MutableLiveData()

    fun getalpsDataById(id: Int) = viewModelScope.launch {
        alpsById.value = repository.getAlpsDataById(id)
    }

    fun insert(alpsData: AmbangLebarPengontrolSegiempatModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.insert(alpsData)
    }

    fun update(idAlps: Int, data: FloatArray) = viewModelScope.launch {
        repository.update(idAlps, data)
    }

    fun updateIdPengambilanData(idAlps: Int, id: Int) = viewModelScope.launch {
        repository.updateIdPengambilanData(idAlps, id)
    }
}

class AmbangLebarPengontrolSegiempatViewModelFactory(private val repository: AmbangLebarPengontrolSegiempatRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AmbangLebarPengontrolSegiempatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AmbangLebarPengontrolSegiempatViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}