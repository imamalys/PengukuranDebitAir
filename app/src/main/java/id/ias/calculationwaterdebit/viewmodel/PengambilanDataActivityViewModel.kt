package id.ias.calculationwaterdebit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PengambilanDataActivityViewModel: ViewModel() {
    var pengambilValue: MutableLiveData<FloatArray> = MutableLiveData(FloatArray(3))
    var detailBangunan: MutableLiveData<String> = MutableLiveData("Ambang Lebar Pengontrol Segiempat")

    fun checkHaveValue(): Boolean {
        for (detail in pengambilValue.value!!) {
            if (detail.toInt().toString() == "0" || detail.toString() == "") {
                return false
            }
        }

        return true
    }
}

class PengambilanDataActivityViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PengambilanDataActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PengambilanDataActivityViewModel() as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}