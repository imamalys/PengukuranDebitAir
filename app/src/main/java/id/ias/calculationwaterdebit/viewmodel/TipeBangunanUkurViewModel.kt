package id.ias.calculationwaterdebit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ias.calculationwaterdebit.R

class TipeBangunanUkurViewModel: ViewModel() {
    var tipeBangunan: MutableLiveData<String> = MutableLiveData("Ambang Lebar Pengontrol Segiempat")

    fun getImage(item: String): Int {
        when(item) {
            "Ambang Lebar Pengontrol Segiempat" -> {
                return R.drawable.ambang_lebar_segiempat_image
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
                return R.drawable.orifice_image
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

    fun getRumus(item: String): Int {
        when(item) {
            "Ambang Lebar Pengontrol Segiempat" -> {
                return R.drawable.ambang_lebar_segiempat
            }
            "Ambang Lebar Pengontrol Trapesium" -> {
                return R.drawable.ambang_lebar_trapesium
            }
            "Ambang Tajam Segiempat" -> {
                return R.drawable.ambang_tajam_segiempat
            }
            "Ambang Tajam Segitiga" -> {
                return R.drawable.ambang_tajam_segitiga
            }
            "Cipoletti" -> {
                return R.drawable.cipoletti
            }
            "Parshall Flume" -> {
                return R.drawable.parshall_flume
            }
            "Long Throated Flume" -> {
                return R.drawable.long_throated_flume
            }
            "Cut Throated Flume" -> {
                return R.drawable.cut_throated_flume
            }
            "Orifice" -> {
                return R.drawable.orifice
            }
            "Romijn" -> {
                return R.drawable.romijn
            }
            "Crump- De Gyuter" -> {
                return R.drawable.crump_de_guyter
            }
            else -> {
                return R.drawable.ambang_lebar_segiempat
            }
        }
    }

    fun getKeterangan(item: String): String {
        when(item) {
            "Ambang Lebar Pengontrol Segiempat" -> {
                return "Cd : Koefisien Debit (-)\n" +
                        "Cv : Koefisien Aliran (-)\n" +
                        "g   : Gravitasi (m/s2)\n" +
                        "Bc : Lebar Ambang (m)\n" +
                        "h1 : Kedalaman air di Hulu trhdp Ambang (m)"
            }
            "Ambang Lebar Pengontrol Trapesium" -> {
                return "Cd : Koefisien Debit\n" +
                        "bc   : Lebar Ambang (m)\n" +
                        "yc   : Kedalaman air di pada Ambang (m)\n" +
                        "mc : Kemiringan Ambang (m)\n" +
                        "g     : Gravitasi (m/s2)\n" +
                        "H1  : Tinggi Energi di Hulu (m)"
            }
            "Ambang Tajam Segiempat" -> {
                return "Cd : Koefisien Debit\n" +
                        "b   : Lebar Mercu (m)\n" +
                        "g   : Grafitasi (m/s2)\n" +
                        "H  : Tinggi Muka Air (m)"
            }
            "Ambang Tajam Segitiga" -> {
                return "Cd : Koefisien Debit\n" +
                        "g    : Gravitasi (m/s2)\n" +
                        "∅   : Sudut Celah Mercu (°)\n" +
                        "H   : Tinggi Muka Air (m)"
            }
            "Cipoletti" -> {
                return "Cd : Koefisien Debit\n" +
                        "Cv : Koefisien Aliran\n" +
                        "g   : Gravitasi \n" +
                        "B  : Lebar Mercu\n" +
                        "h1 : Kedalaman air di Hulu trhdp Mercu"
            }
            "Parshall Flume" -> {
                return "Berdasarkan Tabel"
            }
            "Long Throated Flume" -> {
                return "Cd : Koefisien Debit\n" +
                        "Cv : Koefisien Aliran\n" +
                        "g   : Gravitasi (m/s2)\n" +
                        "Bc : Lebar Ambang (m)\n" +
                        "h1 : Kedalaman air di Hulu trhdp Ambang (m)"
            }
            "Cut Throated Flume" -> {
                return "K    : Koefisien aliran bebas\n" +
                        "W   : Lebar tenggorokan (m)\n" +
                        "Ha : Kedalaman Air di Hulu (m)\n" +
                        "n     : Flow Exponent "
            }
            "Orifice" -> {
                return "C : Koefisien Aliran bebas (0.7)\n" +
                        "A : Luas Lubang (m2)\n" +
                        "Z : Perbedaan muka air (m)"
            }
            "Romijn" -> {
                return "Cd : Koefisien Debit\n" +
                        "Cv : Koefisien Aliran\n" +
                        "g    : Gravitasi (9,8 m2/s)\n" +
                        "Bc : Lebar Meja (m)\n" +
                        "H1 : Tinggi Energi di Hulu, (m)"
            }
            "Crump- De Gyuter" -> {
                return "Cd : Koefisien Debit\n" +
                        "b    : Lebar bukaan (m)\n" +
                        "w   : bukaan pintu (m)\n" +
                        "H1 : Tinggi Energi di Hulu, (m)"
            }
            else -> {
                return ""
            }
        }
    }
}

class TipeBangunanUkurViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TipeBangunanUkurViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TipeBangunanUkurViewModel() as T
        }
        throw IllegalAccessException("unknown ViewModel class")
    }
}