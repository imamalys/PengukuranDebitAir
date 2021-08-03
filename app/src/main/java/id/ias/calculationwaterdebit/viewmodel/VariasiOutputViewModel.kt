package id.ias.calculationwaterdebit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ias.calculationwaterdebit.database.model.FormDataModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel

class VariasiOutputViewModel: ViewModel() {
    var detailBangunan: String = ""
    var idPengambilanData: Int = 0
    var formDatas: List<FormDataModel> = ArrayList()
    lateinit var pengambilanDataById: PengambilanDataModel
}

class VariasiOutputViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VariasiOutputViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VariasiOutputViewModel() as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}