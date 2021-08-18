package id.ias.calculationwaterdebit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.model.DetailBangunanModel

class DetailBangunanViewModel: ViewModel() {
    var detailBangunan: MutableLiveData<String> = MutableLiveData("Ambang Lebar Pengontrol Segiempat")
    var detailBangunanValue: MutableLiveData<FloatArray> = MutableLiveData(FloatArray(0))

    fun getImage(item: String): Int {
        when(item) {
            "Ambang Lebar Pengontrol Segiempat" -> {
                return R.drawable.ambang_lebar_segiempat_image_detail
            }
            "Ambang Lebar Pengontrol Trapesium" -> {
                return R.drawable.ambang_lebar_trapesium_image
            }
            "Ambang Tajam Segiempat" -> {
                return R.drawable.ambang_tajam_segiempat_image
            }
            "Ambang Tajam Segitiga" -> {
                return R.drawable.ambang_tajam_segitiga_image
            }
            "Cipoletti" -> {
                return R.drawable.cipoletti_image
            }
            "Parshall Flume" -> {
                return R.drawable.parshall_flume_image
            }
            "Long Throated Flume" -> {
                return R.drawable.long_throated_flume_image
            }
            "Cut Throated Flume" -> {
                return R.drawable.cut_throated_flume_image
            }
            "Orifice" -> {
                return R.drawable.orifice_image_detail
            }
            "Romijn" -> {
                return R.drawable.romijn_image
            }
            "Crump- De Gyuter" -> {
                return R.drawable.crump_de_guyter_image
            }
            else -> {
                return R.drawable.ambang_lebar_segiempat_image
            }
        }
    }

    fun checkHaveValue(): Boolean {
        for (detail in detailBangunanValue.value!!) {
            if (detail.toString() == "0.0" || detail.toString() == "") {
                return false
            }
        }

        return true
    }

    fun getDetailBangunan(item: String): ArrayList<DetailBangunanModel> {
        var detailBangunans = ArrayList<DetailBangunanModel>()
        detailBangunans.add(DetailBangunanModel("", "", ""))
        when(item) {
            "Ambang Lebar Pengontrol Segiempat" -> {
                detailBangunans.add(DetailBangunanModel("Bc", "0", "Lebar Ambang"))
                detailBangunans.add(DetailBangunanModel("B1", "0", "Lebar Dasar"))
                detailBangunans.add(DetailBangunanModel("L", "0", "Panjang Ambang"))
                detailBangunans.add(DetailBangunanModel("P", "0", "Tinggi Ambang"))
                detailBangunans.add(DetailBangunanModel("m", "0", "Tinggi Di Atas Ambang"))
                detailBangunans.add(DetailBangunanModel("w", "0", "Tinggi Di Bawah Ambang"))
                detailBangunans.add(DetailBangunanModel("b1", "0", "Lebar Atas"))
            }
            "Parshall Flume" -> {
                detailBangunans.add(DetailBangunanModel("b", "0", "Lebar Tenggorokan"))
                detailBangunans.add(DetailBangunanModel("hb", "0", ""))
            }
            "Orifice" -> {
                detailBangunans.add(DetailBangunanModel("Bc", "0", "Lebar Lubang"))
                detailBangunans.add(DetailBangunanModel("W", "0", "Tinggi Lubang"))
            }
        }

        detailBangunanValue.postValue(FloatArray(detailBangunans.size - 1))

        return detailBangunans
    }
}

class DetailBangunanUkurViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailBangunanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailBangunanViewModel() as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}