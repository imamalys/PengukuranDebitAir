package id.ias.calculationwaterdebit.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orifice")
data class OrificeModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "id_base_data")
    var idBaseData: Int,
    @ColumnInfo(name = "lebar_lubang")
    var lebarLubang: Float,
    @ColumnInfo(name = "tinggi_lubang")
    var tinggiLubang: Float
)