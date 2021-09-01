package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.AmbangTipisSegitigaModel
import id.ias.calculationwaterdebit.database.repository.AmbangTipisSegitigaRepository
import kotlinx.coroutines.launch

class AmbangTipisSegitigaViewModel(private val repository: AmbangTipisSegitigaRepository): ViewModel() {
    val allAtsDatas: LiveData<List<AmbangTipisSegitigaModel>> = repository.allAtsDatas.asLiveData()
    var idTipeBangunan: MutableLiveData<Long> = MutableLiveData(0)
    val atsById: MutableLiveData<AmbangTipisSegitigaModel> = MutableLiveData()

    fun getatsDataById(id: Int) = viewModelScope.launch {
        atsById.value = repository.getAtsDataById(id)
    }

    fun getAtsDataByIdBaseData(id: Int) = viewModelScope.launch {
        atsById.value = repository.getAtsDataByIdBaseData(id)
    }

    fun insert(atsModel: AmbangTipisSegitigaModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.insert(atsModel)
    }

    fun update(atsModel: AmbangTipisSegitigaModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.update(atsModel).toLong()
    }
}

class AmbangTipisSegitigaViewModelFactory(private val repository: AmbangTipisSegitigaRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AmbangTipisSegitigaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AmbangTipisSegitigaViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}