package id.ias.calculationwaterdebit.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cipoletti")
data class CipolettiModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "id_base_data")
    var idBaseData: Int,
    @ColumnInfo(name = "lebar_pengukur")
    val lebarPengukur: Float,
    @ColumnInfo(name = "lebar_dasar")
    val lebarDasar: Float,
    @ColumnInfo(name = "tinggi_mercu_diatas_ambang")
    val tinggiMercuDiatasAmbang: Float,
    @ColumnInfo(name = "tinggi_mercu")
    val tinggiMercu: Float,
    @ColumnInfo(name = "lebar_atas")
    val lebarAtas: Float
)