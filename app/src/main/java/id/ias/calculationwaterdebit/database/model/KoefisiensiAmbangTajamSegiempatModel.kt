package id.ias.calculationwaterdebit.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "koefisiensi_ambang_tajam_segiempat")
data class KoefisiensiAmbangTajamSegiempatModel(
    @PrimaryKey
    var nilai: Float,
    var hPerP: Float,
    var cd: Float
)