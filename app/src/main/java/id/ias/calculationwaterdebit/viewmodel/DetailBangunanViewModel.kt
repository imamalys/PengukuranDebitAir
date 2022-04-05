package id.ias.calculationwaterdebit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.model.DetailBangunanModel

class DetailBangunanViewModel: ViewModel() {
    var detailBangunan: MutableLiveData<String> = MutableLiveData("Ambang Lebar Pengontrol Segiempat")
    var detailBangunanValue: FloatArray = (FloatArray(0))

    fun getImage(item: String): Int {
        when(item) {
            "Ambang Lebar Pengontrol Segiempat" -> {
                return R.drawable.ambang_lebar_segiempat_image_detail
            }
            "Ambang Lebar Pengontrol Trapesium" -> {
                return R.drawable.ambang_lebar_trapesium_detail
            }
            "Ambang Tajam Segiempat" -> {
                return R.drawable.ambang_tajam_segiempat_image_detail
            }
            "Ambang Tajam Segitiga" -> {
                return R.drawable.ambang_tajam_segitiga_image_detail
            }
            "Cipoletti" -> {
                return R.drawable.cipoletti_image_detail
            }
            "Parshall Flume" -> {
                return R.drawable.parshall_flume_image_detail
            }
            "Long Throated Flume" -> {
                return R.drawable.long_throated_flume_image
            }
            "Cut Throated Flume" -> {
                return R.drawable.cut_throated_flume_image_detail
            }
            "Orifice" -> {
                return R.drawable.orifice_image_detail
            }
            "Romijn" -> {
                return R.drawable.romijn_image_detail
            }
            "Crump- De Gyuter" -> {
                return R.drawable.crump_de_guyter_image_detail
            }
            else -> {
                return R.drawable.ambang_lebar_segiempat_image_detail
            }
        }
    }

    fun checkHaveValue(): Boolean {
        for (detail in detailBangunanValue) {
            if (detail.toString() == "0.0" || detail.toString() == "") {
                return false
            }
        }

        return true
    }

    fun getDetailBangunan(item: String): ArrayList<DetailBangunanModel> {
        val detailBangunans = ArrayList<DetailBangunanModel>()
        detailBangunans.add(DetailBangunanModel("", "", ""))
        when(item) {
            "Ambang Lebar Pengontrol Segiempat" -> {
                detailBangunans.add(DetailBangunanModel("Bc", "0", "*Bc: Lebar Ambang"))
                detailBangunans.add(DetailBangunanModel("B1", "0", "*B1: Lebar Dasar"))
                detailBangunans.add(DetailBangunanModel("L", "0", "*L: Panjang Ambang"))
                detailBangunans.add(DetailBangunanModel("P", "0", "*P: Tinggi Ambang"))
                detailBangunans.add(DetailBangunanModel("m", "0", "*m: Tinggi Di Atas Ambang"))
                detailBangunans.add(DetailBangunanModel("w", "0", "*w: Tinggi Di Bawah Ambang"))
                detailBangunans.add(DetailBangunanModel("b1", "0", "*b1: Lebar Atas"))
            }
            "Ambang Lebar Pengontrol Trapesium" -> {
                detailBangunans.add(DetailBangunanModel("Bc", "0", "*Bc: Lebar Ambang"))
                detailBangunans.add(DetailBangunanModel("B1", "0", "*B1: Lebar Dasar"))
                detailBangunans.add(DetailBangunanModel("L", "0", "*L: Panjang Ambang"))
                detailBangunans.add(DetailBangunanModel("P", "0", "*P: Tinggi Ambang"))
                detailBangunans.add(DetailBangunanModel("m", "0", "*m: Tinggi Di Atas Ambang"))
                detailBangunans.add(DetailBangunanModel("w", "0", "*w: Tinggi Di Bawah Ambang"))
                detailBangunans.add(DetailBangunanModel("b1", "0", "*b1: Lebar Atas"))
                detailBangunans.add(DetailBangunanModel("Mc", "0", "*Mc: Kemiringan Pengontrol"))
            }
            "Ambang Tajam Segiempat" -> {
                detailBangunans.add(DetailBangunanModel("B", "0", "*B: Lebar Saluran"))
                detailBangunans.add(DetailBangunanModel("b", "0", "*b: Lebar Mercu"))
                detailBangunans.add(DetailBangunanModel("p", "0", "*p: Tinggi Mercu di atas Ambang"))
            }
            "Ambang Tajam Segitiga" -> {
                detailBangunans.add(DetailBangunanModel("B", "0", "*B: Lebar Saluran"))
                detailBangunans.add(DetailBangunanModel("θ", "0", "*θ: Sudut celah Mercu"))
                detailBangunans.add(DetailBangunanModel("p", "0", "*p: Tinggi mercu diatas Ambang"))
            }
            "Cipoletti" -> {
                detailBangunans.add(DetailBangunanModel("b", "0", "*b: Lebar Pengukur"))
                detailBangunans.add(DetailBangunanModel("B1", "0", "*B1: Lebar Dasar"))
                detailBangunans.add(DetailBangunanModel("p", "0", "*p: Tinggi mercu diatas Ambang"))
                detailBangunans.add(DetailBangunanModel("w", "0", "*w: Tinggi Mercu"))
                detailBangunans.add(DetailBangunanModel("B2", "0", "*B2: Lebar Atas"))
            }
            "Parshall Flume" -> {
                detailBangunans.add(DetailBangunanModel("b", "0", "*b: Lebar Tenggorokan"))
            }
            "Long Throated Flume" -> {
                detailBangunans.add(DetailBangunanModel("Bc", "0", "*Bc: Lebar Ambang"))
                detailBangunans.add(DetailBangunanModel("B1", "0", "*B1: Lebar Dasar"))
                detailBangunans.add(DetailBangunanModel("L", "0", "*L: Panjang Ambang"))
                detailBangunans.add(DetailBangunanModel("P", "0", "*P: Tinggi Ambang"))
                detailBangunans.add(DetailBangunanModel("m", "0", "*m: Tinggi Di Atas Ambang"))
                detailBangunans.add(DetailBangunanModel("w", "0", "*w: Tinggi Di Bawah Ambang"))
                detailBangunans.add(DetailBangunanModel("b1", "0", "*b1: Lebar Atas"))
            }
            "Cut Throated Flume" -> {
                detailBangunans.add(DetailBangunanModel("W", "0", "*W: Lebar Tenggorokan"))
                detailBangunans.add(DetailBangunanModel("L", "0", "*L: Panjang Flume"))
            }
            "Orifice" -> {
                detailBangunans.add(DetailBangunanModel("Bc", "0", "*Bc: Lebar Lubang"))
                detailBangunans.add(DetailBangunanModel("W", "0", "*W: Tinggi Lubang"))
            }
            "Romijn" -> {
                detailBangunans.add(DetailBangunanModel("Bc", "0", "*Bc: Lebar Meja"))
                detailBangunans.add(DetailBangunanModel("B1", "0", "*B1: Tinggi Dasar"))
                detailBangunans.add(DetailBangunanModel("L", "0", "*L: Panjang Meja"))
                detailBangunans.add(DetailBangunanModel("P", "0", "*P: Tinggi Meja dari Dasar"))
                detailBangunans.add(DetailBangunanModel("m", "0", "*m: Tinggi Meja di Atas Meja"))
            }
            "Crump- De Gyuter" -> {
                detailBangunans.add(DetailBangunanModel("Bc", "0", "*Bc: Lebar Bukaan"))
                detailBangunans.add(DetailBangunanModel("W", "0", "*W: Tinggi Bukaan"))
            }
        }

        detailBangunanValue = FloatArray(detailBangunans.size - 1)

        return detailBangunans
    }

    fun getDetailBangunanLoad(item: String, value: FloatArray): ArrayList<DetailBangunanModel> {
        val detailBangunans = ArrayList<DetailBangunanModel>()
        detailBangunans.add(DetailBangunanModel("", "", ""))
        when(item) {
            "Ambang Lebar Pengontrol Segiempat" -> {
                detailBangunans.add(DetailBangunanModel("Bc", value[0].toString(), "Lebar Ambang"))
                detailBangunans.add(DetailBangunanModel("B1", value[1].toString(), "Lebar Dasar"))
                detailBangunans.add(DetailBangunanModel("L", value[2].toString(), "Panjang Ambang"))
                detailBangunans.add(DetailBangunanModel("P", value[3].toString(), "Tinggi Ambang"))
                detailBangunans.add(DetailBangunanModel("m", value[4].toString(), "Tinggi Di Atas Ambang"))
                detailBangunans.add(DetailBangunanModel("w", value[5].toString(), "Tinggi Di Bawah Ambang"))
                detailBangunans.add(DetailBangunanModel("b1", value[6].toString(), "Lebar Atas"))

                detailBangunanValue = FloatArray(detailBangunans.size - 1)
                detailBangunanValue[0] = value[0]
                detailBangunanValue[1] = value[1]
                detailBangunanValue[2] = value[2]
                detailBangunanValue[3] = value[3]
                detailBangunanValue[4] = value[4]
                detailBangunanValue[5] = value[5]
                detailBangunanValue[6] = value[6]
            }
            "Ambang Lebar Pengontrol Trapesium" -> {
                detailBangunans.add(DetailBangunanModel("Bc", value[0].toString(), "Lebar Ambang"))
                detailBangunans.add(DetailBangunanModel("B1", value[1].toString(), "Lebar Dasar"))
                detailBangunans.add(DetailBangunanModel("L", value[2].toString(), "Panjang Ambang"))
                detailBangunans.add(DetailBangunanModel("P", value[3].toString(), "Tinggi Ambang"))
                detailBangunans.add(DetailBangunanModel("m", value[4].toString(), "Tinggi Di Atas Ambang"))
                detailBangunans.add(DetailBangunanModel("w", value[5].toString(), "Tinggi Di Bawah Ambang"))
                detailBangunans.add(DetailBangunanModel("b1", value[6].toString(), "Lebar Atas"))
                detailBangunans.add(DetailBangunanModel("Mc", value[7].toString(), "Kemiringan Pengontrol"))

                detailBangunanValue = FloatArray(detailBangunans.size - 1)
                detailBangunanValue[0] = value[0]
                detailBangunanValue[1] = value[1]
                detailBangunanValue[2] = value[2]
                detailBangunanValue[3] = value[3]
                detailBangunanValue[4] = value[4]
                detailBangunanValue[5] = value[5]
                detailBangunanValue[6] = value[6]
                detailBangunanValue[7] = value[7]
            }
            "Ambang Tajam Segiempat" -> {
                detailBangunans.add(DetailBangunanModel("B", value[0].toString(), "Lebar Saluran"))
                detailBangunans.add(DetailBangunanModel("b", value[1].toString(), "Lebar Mercu"))
                detailBangunans.add(DetailBangunanModel("p", value[2].toString(), "Tinggi Mercu di atas Ambang"))

                detailBangunanValue = FloatArray(detailBangunans.size - 1)
                detailBangunanValue[0] = value[0]
                detailBangunanValue[1] = value[1]
                detailBangunanValue[2] = value[2]
            }
            "Ambang Tajam Segitiga" -> {
                detailBangunans.add(DetailBangunanModel("B", value[0].toString(), "Lebar Saluran"))
                detailBangunans.add(DetailBangunanModel("θ", value[1].toString(), "Sudut celah Mercu"))
                detailBangunans.add(DetailBangunanModel("p", value[2].toString(), "Tinggi mercu diatas Ambang"))

                detailBangunanValue = FloatArray(detailBangunans.size - 1)
                detailBangunanValue[0] = value[0]
                detailBangunanValue[1] = value[1]
                detailBangunanValue[2] = value[2]
            }
            "Cipoletti" -> {
                detailBangunans.add(DetailBangunanModel("b", value[0].toString(), "Lebar Pengukur"))
                detailBangunans.add(DetailBangunanModel("B1", value[1].toString(), "Lebar Dasar"))
                detailBangunans.add(DetailBangunanModel("p", value[2].toString(), "Tinggi mercu diatas Ambang"))
                detailBangunans.add(DetailBangunanModel("w", value[3].toString(), "Tinggi Mercu"))
                detailBangunans.add(DetailBangunanModel("B2", value[4].toString(), "Lebar Atas"))

                detailBangunanValue = FloatArray(detailBangunans.size - 1)
                detailBangunanValue[0] = value[0]
                detailBangunanValue[1] = value[1]
                detailBangunanValue[2] = value[2]
                detailBangunanValue[3] = value[3]
                detailBangunanValue[4] = value[4]
            }
            "Parshall Flume" -> {
                detailBangunans.add(DetailBangunanModel("b", value[0].toString(), "Lebar Tenggorokan"))

                detailBangunanValue = FloatArray(detailBangunans.size - 1)
                detailBangunanValue[0] = value[0]
            }
            "Long Throated Flume" -> {
                detailBangunans.add(DetailBangunanModel("Bc", value[0].toString(), "Lebar Ambang"))
                detailBangunans.add(DetailBangunanModel("B1", value[1].toString(), "Lebar Dasar"))
                detailBangunans.add(DetailBangunanModel("L", value[2].toString(), "Panjang Ambang"))
                detailBangunans.add(DetailBangunanModel("P", value[3].toString(), "Tinggi Ambang"))
                detailBangunans.add(DetailBangunanModel("m", value[4].toString(), "Tinggi Di Atas Ambang"))
                detailBangunans.add(DetailBangunanModel("w", value[5].toString(), "Tinggi Di Bawah Ambang"))
                detailBangunans.add(DetailBangunanModel("b1", value[6].toString(), "Lebar Atas"))

                detailBangunanValue = FloatArray(detailBangunans.size - 1)
                detailBangunanValue[0] = value[0]
                detailBangunanValue[1] = value[1]
                detailBangunanValue[2] = value[2]
                detailBangunanValue[3] = value[3]
                detailBangunanValue[4] = value[4]
                detailBangunanValue[5] = value[5]
                detailBangunanValue[6] = value[6]
            }
            "Cut Throated Flume" -> {
                detailBangunans.add(DetailBangunanModel("W", value[0].toString(), "Lebar Tenggorokan"))
                detailBangunans.add(DetailBangunanModel("L", value[1].toString(), "Panjang Flume"))

                detailBangunanValue = FloatArray(detailBangunans.size - 1)
                detailBangunanValue[0] = value[0]
                detailBangunanValue[1] = value[1]
            }
            "Orifice" -> {
                detailBangunans.add(DetailBangunanModel("Bc", value[0].toString(), "Lebar Lubang"))
                detailBangunans.add(DetailBangunanModel("W", value[1].toString(), "Tinggi Lubang"))

                detailBangunanValue = FloatArray(detailBangunans.size - 1)
                detailBangunanValue[0] = value[0]
                detailBangunanValue[1] = value[1]
            }
            "Romijn" -> {
                detailBangunans.add(DetailBangunanModel("Bc", value[0].toString(), "Lebar Meja"))
                detailBangunans.add(DetailBangunanModel("B1", value[1].toString(), "Tinggi Dasar"))
                detailBangunans.add(DetailBangunanModel("L", value[2].toString(), "Panjang Meja"))
                detailBangunans.add(DetailBangunanModel("P", value[3].toString(), "Tinggi Meja dari Dasar"))
                detailBangunans.add(DetailBangunanModel("m", value[4].toString(), "Tinggi Meja di Atas Meja"))

                detailBangunanValue = FloatArray(detailBangunans.size - 1)
                detailBangunanValue[0] = value[0]
                detailBangunanValue[1] = value[1]
                detailBangunanValue[2] = value[2]
                detailBangunanValue[3] = value[3]
                detailBangunanValue[4] = value[4]
            }
            "Crump- De Gyuter" -> {
                detailBangunans.add(DetailBangunanModel("Bc", value[0].toString(), "Lebar Bukaan"))
                detailBangunans.add(DetailBangunanModel("W", value[1].toString(), "Tinggi Bukaan"))

                detailBangunanValue = FloatArray(detailBangunans.size - 1)
                detailBangunanValue[0] = value[0]
                detailBangunanValue[1] = value[1]
            }
        }

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