package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.AmbangTajamSegiempatModel
import id.ias.calculationwaterdebit.database.model.AmbangTipisSegitigaModel
import id.ias.calculationwaterdebit.database.model.AmbangTipisSegitigaSudutModel
import id.ias.calculationwaterdebit.database.repository.AmbangLebarPengontrolSegiempatRepository
import id.ias.calculationwaterdebit.database.repository.AmbangTajamSegiempatRepository
import kotlinx.coroutines.launch

class AmbangTajamSegiempatViewModel(private val repository: AmbangTajamSegiempatRepository): ViewModel() {
    val allAtsDatas: LiveData<List<AmbangTajamSegiempatModel>> = repository.allAtsDatas.asLiveData()
    var idTipeBangunan: MutableLiveData<Long> = MutableLiveData(0)
    val atsById: MutableLiveData<AmbangTajamSegiempatModel> = MutableLiveData()

    fun getAtsDataById(id: Int) = viewModelScope.launch {
        atsById.value = repository.getAtsDataById(id)
    }

    fun getAtsDataByIdBaseData(id: Int) = viewModelScope.launch {
        atsById.value = repository.getAtsDataByIdBaseData(id)
    }

    fun insert(atsModel: AmbangTajamSegiempatModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.insert(atsModel)
    }

    fun update(atsModel: AmbangTajamSegiempatModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.update(atsModel).toLong()
    }
}

class AmbangTajamSegiempatViewModelFactory(private val repository: AmbangTajamSegiempatRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AmbangTajamSegiempatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AmbangTajamSegiempatViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}