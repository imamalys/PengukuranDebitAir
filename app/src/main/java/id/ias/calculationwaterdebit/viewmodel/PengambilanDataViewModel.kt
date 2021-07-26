package id.ias.calculationwaterdebit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PengambilanDataViewModel: ViewModel() {
    var pengambilValue: MutableLiveData<FloatArray> = MutableLiveData(FloatArray(5))

    fun checkHaveValue(): Boolean {
        for (detail in pengambilValue.value!!) {
            if (detail.toString() == "0") {
                return false
            }
        }

        return true
    }
}

class PengambilanDataViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PengambilanDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PengambilanDataViewModel() as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}