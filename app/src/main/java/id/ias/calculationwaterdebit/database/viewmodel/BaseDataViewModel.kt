package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import id.ias.calculationwaterdebit.database.repository.BaseDataRepository
import kotlinx.coroutines.launch

class BaseDataViewModel(private val repository: BaseDataRepository): ViewModel() {
    val allBaseDatas: LiveData<List<BaseDataModel>> = repository.allBaseDatas.asLiveData()
    var baseDataById: MutableLiveData<BaseDataModel> = MutableLiveData()
    var insertId: MutableLiveData<Long> = MutableLiveData(0)
    var baseDataUpdate: MutableLiveData<Int> = MutableLiveData(0)

    fun getBaseDataById(id: Int) = viewModelScope.launch {
        baseDataById.value = repository.getBaseDataById(id)
    }

    fun insert(baseData: BaseDataModel) = viewModelScope.launch {
        insertId.value = repository.insert(baseData)
    }

    fun update(id: Int, variablePertama: String, n: String) = viewModelScope.launch {
        baseDataUpdate.value = repository.update(id = id, variablePertama = variablePertama, n = n)
    }

    fun update(id: Int, minH1: String, maxH1: String, minDebitSaluran: String, maxDebitSaluran: String) = viewModelScope.launch {
        baseDataUpdate.value = repository.update(id, minH1, maxH1, minDebitSaluran, maxDebitSaluran)
    }

    fun updateAnalisis(id: Int, k: String, c: String, mape: String) = viewModelScope.launch {
        baseDataUpdate.value = repository.updateAnalisis(id, k, c, mape)
    }

    fun update(id: Int, keterangan: String, nilaiKeterangan: Int) = viewModelScope.launch {
        baseDataUpdate.value = repository.update(id = id, keterangan = keterangan, nilaiKeterangan = nilaiKeterangan)
    }

    fun update(id: Int, tipeBangunan: String) = viewModelScope.launch {
        baseDataUpdate.value = repository.update(id = id, tipeBangunan = tipeBangunan)
    }
}

class BaseDataViewModelFactory(private val repository: BaseDataRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BaseDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BaseDataViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}