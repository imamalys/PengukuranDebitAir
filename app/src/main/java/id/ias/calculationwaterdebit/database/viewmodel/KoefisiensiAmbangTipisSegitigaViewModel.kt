package id.ias.calculationwaterdebit.database.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.ias.calculationwaterdebit.database.model.KoefisiensiAmbangTipisSegitigaModel
import id.ias.calculationwaterdebit.database.repository.KoefisiensiAmbangTipisSegitigaRepository
import kotlinx.coroutines.launch

class KoefisiensiAmbangTipisSegitigaViewModel(private val repositoryKoefiesiensi: KoefisiensiAmbangTipisSegitigaRepository): ViewModel() {
    fun getAmbangTipisSegitigaCdViewModelById(nilai: Float, hp: Float): KoefisiensiAmbangTipisSegitigaModel =
            repositoryKoefiesiensi.getAmbangTipisSegitigaCdById(nilai, hp)

    fun insert(koefiesiensiAmbangTipisSegitigaModel: KoefisiensiAmbangTipisSegitigaModel) = viewModelScope.launch {
        repositoryKoefiesiensi.insert(koefiesiensiAmbangTipisSegitigaModel)
    }

    fun deleteAll() = viewModelScope.launch {
        repositoryKoefiesiensi.deleteAll()
    }
}

class KoefisiensiAmbangTipisSegitigaViewModelFactory(private val repositoryKoefiesiensi: KoefisiensiAmbangTipisSegitigaRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KoefisiensiAmbangTipisSegitigaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KoefisiensiAmbangTipisSegitigaViewModel(repositoryKoefiesiensi) as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}