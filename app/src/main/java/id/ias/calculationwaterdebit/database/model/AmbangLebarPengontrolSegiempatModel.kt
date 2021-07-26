package id.ias.calculationwaterdebit.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alps")
data class AmbangLebarPengontrolSegiempatModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "id_base_data")
    var idBaseData: Int,
    @ColumnInfo(name = "id_pengambilan_data")
    var idPengambilanData: Int?,
    @ColumnInfo(name = "lebar_ambang")
    var lebarAmbang: Float,
    @ColumnInfo(name = "lebar_dasar")
    var lebarDasar: Float,
    @ColumnInfo(name = "panjang_ambang")
    var panjangAmbang: Float,
    @ColumnInfo(name = "tinggi_ambang")
    var tinggiAmbang: Float,
    @ColumnInfo(name = "tinggi_di_atas_ambang")
    var tinggiDiatasAmbang: Float,
    @ColumnInfo(name = "tinggi_di_bawah_ambang")
    var tinggiDibawahAmbang: Float,
    @ColumnInfo(name = "lebar_atas")
    var lebarAtas: Float,
    )