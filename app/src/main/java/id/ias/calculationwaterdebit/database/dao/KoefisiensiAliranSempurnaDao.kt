package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.KoefisiensiAliranSempurnaModel
import kotlinx.coroutines.flow.Flow

@Dao
interface KoefisiensiAliranSempurnaDao {
    @Query("SELECT * FROM koefiesiensi_aliran_sempurna WHERE b = :b")
    fun getKoefisiensiAliranSempurnaById(b: Float): KoefisiensiAliranSempurnaModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(koefisiensiAliranSempurnaModel: KoefisiensiAliranSempurnaModel)

    @Query("DELETE FROM koefiesiensi_aliran_sempurna")
    suspend fun deleteAll()
}