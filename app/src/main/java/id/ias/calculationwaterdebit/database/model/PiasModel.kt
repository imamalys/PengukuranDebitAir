package id.ias.calculationwaterdebit.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pias")
data class PiasModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "id_form_data")
    var idFormData: Int,
    var h2: Float,
    var d8: Float,
    var d8Calculation: Float,
    var d6: Float,
    var d6Calculation: Float,
    var d2: Float,
    var d2Calculation: Float,
    var metodePengmbilan: String,
)