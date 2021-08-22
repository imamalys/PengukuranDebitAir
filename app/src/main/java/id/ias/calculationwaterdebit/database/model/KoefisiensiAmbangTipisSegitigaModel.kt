package id.ias.calculationwaterdebit.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ambang_tipis_segitiga_cd")
data class KoefisiensiAmbangTipisSegitigaModel(
    @PrimaryKey
    var nilai: Float,
    var hp: Float,
    @ColumnInfo(name = "nilai_cd")
    var nilaiCd: Float,
)