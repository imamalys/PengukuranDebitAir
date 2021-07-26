package id.ias.calculationwaterdebit.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "base_data")
data class BaseDataModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "nama_daerah_irigasi")
    var namaDaerahIrigasi: String,
    @ColumnInfo(name = "wilayah_kewenangan")
    var wilayahKewenangan: String,
    var provinsi: String,
    var kabupaten: String,
    var tanggal: String,
    @ColumnInfo(name = "no_pengukuran")
    var noPengukuran: String,
    @ColumnInfo(name = "nama_pengukur")
    var namaPengukur: String
    )