package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.dao.FormDataDao
import id.ias.calculationwaterdebit.database.model.FormDataModel
import id.ias.calculationwaterdebit.database.repository.FormDataRepository
import kotlinx.coroutines.launch

class FormDataViewModel(private val repository: FormDataRepository): ViewModel() {
    val formDatas: LiveData<List<FormDataModel>> = repository.formDataByPengambilanData.asLiveData()

    fun insert(formDataModel: FormDataModel) = viewModelScope.launch {
        repository.insert(formDataModel)
    }
}

class FormDataViewModelFactory(private val database: FormDataDao, private val id: Int): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FormDataViewModel(FormDataRepository(database, id)) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}