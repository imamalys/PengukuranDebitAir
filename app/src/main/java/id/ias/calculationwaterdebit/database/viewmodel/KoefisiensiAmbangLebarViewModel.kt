package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.ias.calculationwaterdebit.database.model.KoefisiensiAmbangLebarModel
import id.ias.calculationwaterdebit.database.repository.KoefiensiAmbangLebarRepository
import kotlinx.coroutines.launch

class KoefisiensiAmbangLebarViewModel(private val repository: KoefiensiAmbangLebarRepository): ViewModel() {
    fun getKoefiensiAmbangLebarById(nilai: Float): LiveData<KoefisiensiAmbangLebarModel> = repository.getKoefiensiAmbangLebarById(nilai).asLiveData()

    fun insert(koefisiensiAmbangLebarModel: KoefisiensiAmbangLebarModel) = viewModelScope.launch {
        repository.insert(koefisiensiAmbangLebarModel)
    }
}