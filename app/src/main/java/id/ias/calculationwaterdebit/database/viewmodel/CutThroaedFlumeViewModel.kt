package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.CutThroatedFlumeModel
import id.ias.calculationwaterdebit.database.repository.AmbangLebarPengontrolSegiempatRepository
import id.ias.calculationwaterdebit.database.repository.CutThroatedFlumeRepository
import kotlinx.coroutines.launch

class CutThroaedFlumeViewModel(private val repository: CutThroatedFlumeRepository): ViewModel() {
    val allCtfDatas: LiveData<List<CutThroatedFlumeModel>> = repository.allCtfDatas.asLiveData()
    var idTipeBangunan: MutableLiveData<Long> = MutableLiveData(0)
    val ctfById: MutableLiveData<CutThroatedFlumeModel> = MutableLiveData()

    fun getCtfDataById(id: Int) = viewModelScope.launch {
        ctfById.value = repository.getCtfDataById(id)
    }

    fun insert(ctfData: CutThroatedFlumeModel) = viewModelScope.launch {
        idTipeBangunan.value = repository.insert(ctfData)
    }
}

class CutThroaedFlumeViewModelFactory(private val repository: CutThroatedFlumeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CutThroaedFlumeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CutThroaedFlumeViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}