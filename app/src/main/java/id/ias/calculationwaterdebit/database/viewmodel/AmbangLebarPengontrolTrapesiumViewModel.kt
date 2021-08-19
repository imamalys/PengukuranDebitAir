package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolSegiempatModel
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolTrapesiumModel
import id.ias.calculationwaterdebit.database.repository.AmbangLebarPengontrolSegiempatRepository
import id.ias.calculationwaterdebit.database.repository.AmbangLebarPengontrolTrapesiumRepository
import kotlinx.coroutines.launch

class AmbangLebarPengontrolTrapesiumViewModel(private val repository: AmbangLebarPengontrolTrapesiumRepository): ViewModel() {
    val allAlptDatas: LiveData<List<AmbangLebarPengontrolTrapesiumModel>> = repository.allAlptDatas.asLiveData()
    var idTipeBangunan: MutableLiveData<Long> = MutableLiveData(0)
    val alptById: MutableLiveData<AmbangLebarPengontrolTrapesiumModel> = MutableLiveData()

    fun getalpsDataById(id: Int) = viewModelScope.launch {
        alptById.value = repository.getAlptDataById(id)
    }

    fun insert(alptData: AmbangLebarPengontrolTrapesiumModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.insert(alptData)
    }
}

class AmbangLebarPengontrolTrapesiumViewModelFactory(private val repository: AmbangLebarPengontrolTrapesiumRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AmbangLebarPengontrolTrapesiumViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AmbangLebarPengontrolTrapesiumViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}