package id.ias.calculationwaterdebit.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "koefisiensi_cut_throated_flume")
data class KoefisiensiCutThroatedFlumeModel(
    @PrimaryKey
    var b: Float,
    var k: Float,
    var n: Float,
    var sT: Float
)