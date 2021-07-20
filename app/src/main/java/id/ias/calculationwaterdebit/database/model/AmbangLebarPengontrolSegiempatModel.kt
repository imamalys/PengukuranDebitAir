package id.ias.calculationwaterdebit.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alps")
data class AmbangLebarPengontrolSegiempatModel (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "id_base_data")
    var idBaseData: Int,
    @ColumnInfo(name = "lebar_ambang")
    var lebarAmbang: Int,
    @ColumnInfo(name = "lebar_dasar")
    var lebarDasar: Int,
    @ColumnInfo(name = "panjang_ambang")
    var panjangAmbang: Int,
    @ColumnInfo(name = "tinggi_ambang")
    var tinggiAmbang: Int,
    @ColumnInfo(name = "tinggi_di_atas_ambang")
    var tinggiDiatasAmbang: Int,
    @ColumnInfo(name = "tinggi_di_bawah_ambang")
    var tinggiDibawahAmbang: Int,
    @ColumnInfo(name = "lebar_atas")
    var lebarAtas: Int,
    )