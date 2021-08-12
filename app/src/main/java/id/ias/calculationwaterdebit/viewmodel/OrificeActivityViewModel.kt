package id.ias.calculationwaterdebit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ias.calculationwaterdebit.database.model.OrificeModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel

class OrificeActivityViewModel: ViewModel() {
    var orificeData: OrificeModel = OrificeModel(null, 0, "0".toFloat(), "0".toFloat())
    var pengambilanData: List<PengambilanDataModel> = ArrayList()
}

class OrificeActivityViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrificeActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrificeActivityViewModel() as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}