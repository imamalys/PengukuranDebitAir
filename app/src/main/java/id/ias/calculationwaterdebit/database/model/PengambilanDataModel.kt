package id.ias.calculationwaterdebit.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pengambilan_data")
data class PengambilanDataModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "id_base_data")
    var idBaseData: Int,
    var h1: Float,
    var hb: Float,
    @ColumnInfo(name = "jumlah_saluran_basah")
    var jumlahSaluranBasah: Float,
    @ColumnInfo(name = "variasi_ketinggian_air")
    var variasaiKetinggianAir: Float,
    @ColumnInfo(name = "jumlah_rata_rata")
    var jumlahRataRata: Float?,
    @ColumnInfo(name = "q_pengukuran")
    var qPengukuran: Float?,
    @ColumnInfo(name = "q_bangunan")
    var qBangunan: Float?
)