package id.ias.calculationwaterdebit.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pengambilan_data")
data class PengambilanDataModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "lebar_saluran_pengukuran")
    var lebarSaluranPengukuran: Float,
    @ColumnInfo(name = "jumlah_saluran_basah")
    var jumlahSaluranBasah: Float,
    @ColumnInfo(name = "variasi_ketinggian_air")
    var variasaiKetinggianAir: Float
)