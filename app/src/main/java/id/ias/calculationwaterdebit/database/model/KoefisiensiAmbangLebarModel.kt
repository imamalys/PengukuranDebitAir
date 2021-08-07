package id.ias.calculationwaterdebit.database.model

import androidx.room.Entity

@Entity(tableName = "koefiensi_ambang_lebar")
data class KoefisiensiAmbangLebarModel (
    var nilai: Float,
    var segiempat: Float,
    var trapesium: Float,
    var segitiga: Float,
    )