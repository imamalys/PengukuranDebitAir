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
    var namaPengukur: String,
    @ColumnInfo(name = "tipe_bangunan")
    var tipeBangunan: String?,
    @ColumnInfo(name = "variable_pertama")
    var variablePertama: String?,
    var n: String?,
    @ColumnInfo(name = "min_h2")
    var minH2: String?,
    @ColumnInfo(name = "max_h2")
    var maxH2: String?,
    @ColumnInfo(name = "min_debit_saluran")
    var minDebitSaluran: String?,
    @ColumnInfo(name = "max_debit_saluran")
    var maxDebitSaluran: String?,
    var k: String?,
    var c: String?,
    var mape: String?,
    var keterangan: String?,
    @ColumnInfo(name = "nilai_keterangan")
    var nilaiKeterangan: Int,
    )