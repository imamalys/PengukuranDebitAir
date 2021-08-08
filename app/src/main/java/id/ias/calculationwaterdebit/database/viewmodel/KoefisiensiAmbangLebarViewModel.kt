package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.*
import id.ias.calculationwaterdebit.database.model.KoefisiensiAmbangLebarModel
import id.ias.calculationwaterdebit.database.repository.KoefiensiAmbangLebarRepository
import kotlinx.coroutines.launch

class KoefisiensiAmbangLebarViewModel(private val repository: KoefiensiAmbangLebarRepository): ViewModel() {
    val koefiensiAmbangLebar: MutableLiveData<KoefisiensiAmbangLebarModel> = MutableLiveData()

    fun getKoefiensiAmbangLebarById(nilai: Float): KoefisiensiAmbangLebarModel = repository.getKoefiensiAmbangLebarById(nilai)

    fun insert(koefisiensiAmbangLebarModel: KoefisiensiAmbangLebarModel) = viewModelScope.launch {
        repository.insert(koefisiensiAmbangLebarModel)
    }
}

class KoefisiensiAmbangLebarViewModelFactory(private val repository: KoefiensiAmbangLebarRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KoefisiensiAmbangLebarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KoefisiensiAmbangLebarViewModel(repository) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}