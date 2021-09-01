package id.ias.calculationwaterdebit.database.model

import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ambang_tajam_segitiga")
data class AmbangTipisSegitigaModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "id_base_data")
    var idBaseData: Int,
    @ColumnInfo(name = "lebar_saluran")
    var lebarSaluran: Float,
    @ColumnInfo(name = "sudut_celah_mercu")
    var sudutCelahMercu: Float,
    @ColumnInfo(name = "tinggi_mercu_diatas_ambang")
    var tinggiMercuDiatasAmbang: Float,
)