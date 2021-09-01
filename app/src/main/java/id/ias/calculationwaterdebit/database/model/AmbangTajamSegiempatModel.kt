package id.ias.calculationwaterdebit.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ambang_tajam_segiempat")
data class AmbangTajamSegiempatModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "id_base_data")
    var idBaseData: Int,
    @ColumnInfo(name = "lebar_saluran")
    var lebarSaluran: Float,
    @ColumnInfo(name = "lebar_mercu")
    var lebarMercu: Float,
    @ColumnInfo(name = "tinggi_mercu_diatas_ambang")
    var tinggiMercuDiatasAmbang: Float
)