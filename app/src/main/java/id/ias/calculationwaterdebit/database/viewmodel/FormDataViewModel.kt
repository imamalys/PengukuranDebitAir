package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.ias.calculationwaterdebit.database.model.FormDataModel
import id.ias.calculationwaterdebit.database.repository.FormDataRepository
import kotlinx.coroutines.launch

class FormDataViewModel(private val repository: FormDataRepository): ViewModel() {
    val formDatas: MutableLiveData<List<FormDataModel>> = MutableLiveData()
    var idFormData: MutableLiveData<Long> = MutableLiveData(0)

    fun getformDatas(id: Int) = viewModelScope.launch {
        formDatas.value = repository.formDataByFormData(id)
    }

    fun insert(formDataModel: FormDataModel) = viewModelScope.launch {
        idFormData.value = repository.insert(formDataModel)
    }
}

class FormDataViewModelFactory(private val repository: FormDataRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FormDataViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}