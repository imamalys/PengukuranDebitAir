package id.ias.calculationwaterdebit.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "romijn")
data class RomijnModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "id_base_data")
    var idBaseData: Int,
    @ColumnInfo(name = "lebar_meja")
    val lebarMeja: Float,
    @ColumnInfo(name = "lebar_dasar")
    val lebarDasar: Float,
    @ColumnInfo(name = "panjang_meja")
    val panjangMeja: Float,
    @ColumnInfo(name = "tinggi_meja_dari_dasar")
    val tinggiMejaDariDasar: Float,
    @ColumnInfo(name = "tinggi_diatas_meja")
    val tinggiDiatasMeja: Float
)