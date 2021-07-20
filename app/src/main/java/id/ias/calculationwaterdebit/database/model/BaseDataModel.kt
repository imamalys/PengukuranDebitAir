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
    val namaDaerahIrigasi: String,
    @ColumnInfo(name = "wilayah_kewenangan")
    val wilayahKewenangan: String,
    val provinsi: String,
    val kabupaten: String,
    val tanggal: String,
    @ColumnInfo(name = "no_pengukuran")
    val noPengukuran: String,
    @ColumnInfo(name = "nama_pengukur")
    val namaPengukur: String
    )