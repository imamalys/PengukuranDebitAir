package id.ias.calculationwaterdebit.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "koefiensi_ambang_lebar")
data class KoefisiensiAmbangLebarModel (
    @PrimaryKey
    var nilai: Float,
    var segiempat: Float,
    var trapesium: Float,
    var segitiga: Float,
    )