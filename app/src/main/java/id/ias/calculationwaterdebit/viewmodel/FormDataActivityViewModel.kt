package id.ias.calculationwaterdebit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.model.KecepatanAirModel

class FormDataActivityViewModel: ViewModel() {
    var metodePengambilan: MutableLiveData<String> = MutableLiveData("Satu Titik")
    var kecepatanAirValues: MutableLiveData<FloatArray> = MutableLiveData(FloatArray(3))
    var kecepatanAirs: ArrayList<KecepatanAirModel> = ArrayList()
    var detailBangunan: String = ""
    var idPengambilanData: Int = 0
    var jumlahPias: Int = 0
    var variasiKetinggianAir: String = "0"
    var currentPiasSize: Int = 0

    var h1: String = "0"
    var h2: String = "0"
    var hb: String = "0"
    var jarakPias: String = "0"

    fun checkHaveValue(): Boolean {
        for (i in kecepatanAirValues.value!!.indices) {
            when(metodePengambilan.value) {
                "Dua Titik" -> {
                    if (i == 0 || i == 2) {
                        if (kecepatanAirValues.value!![i].toString() == "0.0" || kecepatanAirValues.value!![i].toString() == "") {
                            return false
                        }
                    }
                }
                "Tiga Titik" -> {
                    if (kecepatanAirValues.value!![i].toString() == "0.0" || kecepatanAirValues.value!![i].toString() == "") {
                        return false
                    }
                } else -> {
                    if (i == 1) {
                        if (kecepatanAirValues.value!![i].toString() == "0.0" || kecepatanAirValues.value!![i].toString() == "") {
                            return false
                        }
                    }
                }
            }
        }

        return true
    }

    fun metodePengambilanSpinner(): Int {
        if (h2.toFloat() >= 0.75) {
            return R.array.metode_pengambilan_3
        } else {
            return R.array.metode_pengambilan_1
        }
    }

    fun metodePengambilanInt(): Int {
        return when(metodePengambilan.value) {
            "Dua Titik" -> {
                1
            }
            "Tiga Titik" -> {
                2
            } else -> {
                0
            }
        }
    }
}

class FormDataActivityViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormDataActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FormDataActivityViewModel() as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}