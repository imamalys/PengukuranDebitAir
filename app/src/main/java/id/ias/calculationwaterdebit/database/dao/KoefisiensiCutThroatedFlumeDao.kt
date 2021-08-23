package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.KoefisiensiCutThroatedFlumeModel

@Dao
interface KoefisiensiCutThroatedFlumeDao {
    @Query("SELECT * FROM koefisiensi_cut_throated_flume WHERE b = :b")
    fun getKoefisiensiCutThroatedFlumeById(b: Float): KoefisiensiCutThroatedFlumeModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(koefisiensiCutThroatedFlumeModel: KoefisiensiCutThroatedFlumeModel)

    @Query("DELETE FROM koefisiensi_cut_throated_flume")
    suspend fun deleteAll()
}