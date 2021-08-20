package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.ias.calculationwaterdebit.database.model.KoefiesiensiAmbangTajamSegitigaModel
import id.ias.calculationwaterdebit.database.repository.KoefiesiensiAmbangTajamSegitigaRepository
import kotlinx.coroutines.launch

class AmbangTipisSegitigaCdViewModel(private val repositoryKoefiesiensi: KoefiesiensiAmbangTajamSegitigaRepository): ViewModel() {
    fun getAmbangTipisSegitigaCdViewModelById(nilai: Float = 1.0.toFloat(), hp: Float = 0.13.toFloat()): KoefiesiensiAmbangTajamSegitigaModel =
            repositoryKoefiesiensi.getAmbangTipisSegitigaCdById(nilai, hp)

    fun insert(koefiesiensiAmbangTajamSegitigaModel: KoefiesiensiAmbangTajamSegitigaModel) = viewModelScope.launch {
        repositoryKoefiesiensi.insert(koefiesiensiAmbangTajamSegitigaModel)
    }

    fun deleteAll() = viewModelScope.launch {
        repositoryKoefiesiensi.deleteAll()
    }
}

class AmbangTipisSegitigaCdViewModelFactory(private val repositoryKoefiesiensi: KoefiesiensiAmbangTajamSegitigaRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AmbangTipisSegitigaCdViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AmbangTipisSegitigaCdViewModel(repositoryKoefiesiensi) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}