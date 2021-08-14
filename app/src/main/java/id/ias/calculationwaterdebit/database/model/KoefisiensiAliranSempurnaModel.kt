package id.ias.calculationwaterdebit.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "koefiesiensi_aliran_sempurna")
data class KoefisiensiAliranSempurnaModel(
    @PrimaryKey
    var b: Float,
    var c: Float,
    var n: Float,
    var dc: Float
)