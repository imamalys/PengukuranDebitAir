package id.ias.calculationwaterdebit.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "sudut_ambang_tipis_segitiga")
data class AmbangTipisSegitigaSudutModel(
    @ColumnInfo(name = "sudut_cd")
    var sudutCd: Int,
    var cd: Float,
    @ColumnInfo(name = "sudut_dht")
    var sudutDht: Int,
    var dht: Float
)