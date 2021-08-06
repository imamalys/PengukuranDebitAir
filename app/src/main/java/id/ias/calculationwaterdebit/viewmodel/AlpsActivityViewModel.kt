package id.ias.calculationwaterdebit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolSegiempatModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel

class AlpsActivityViewModel: ViewModel() {
    var detailBangunan: String = ""
    var idPengambilanData: Int = 0
    var pengambilanDataByBangunan: List<PengambilanDataModel> = ArrayList()
    var alpsData: AmbangLebarPengontrolSegiempatModel = AmbangLebarPengontrolSegiempatModel(null, 0, 0, "0".toFloat() ,"0".toFloat() ,"0".toFloat(),"0".toFloat(),"0".toFloat(), "0".toFloat(), "0".toFloat())
}

class AlpsActivityViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlpsActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlpsActivityViewModel() as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}