package id.ias.calculationwaterdebit.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mercu_ambang")
data class MercuAmbangModel(
    @PrimaryKey
    val bPerB: Float,
    val bef: Float
)