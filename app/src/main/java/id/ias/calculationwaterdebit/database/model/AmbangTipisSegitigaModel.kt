package id.ias.calculationwaterdebit.database.model

import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ats")
data class AmbangTipisSegitigaModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "id_base_data")
    var idBaseData: Int,
    var b: Float,
    var o: Float,
    var p: Float,
)