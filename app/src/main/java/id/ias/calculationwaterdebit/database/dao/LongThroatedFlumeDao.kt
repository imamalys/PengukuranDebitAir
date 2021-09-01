package id.ias.calculationwaterdebit.database.dao

import androidx.room.*
import id.ias.calculationwaterdebit.database.model.LongThrotedFlumeModel
import id.ias.calculationwaterdebit.database.model.ParshallFlumeModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LongThroatedFlumeDao {
    @Query("SELECT * FROM ltf")
    fun getAllLtf(): Flow<List<LongThrotedFlumeModel>>

    @Query("SELECT * FROM ltf WHERE id = :id")
    suspend fun getLtfById(id: Int): LongThrotedFlumeModel

    @Query("SELECT * FROM ltf WHERE id_base_data = :id")
    suspend fun getLtfByIdBaseData(id: Int): LongThrotedFlumeModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(longThrotedFlumeModel: LongThrotedFlumeModel): Long

    @Update
    suspend fun update(longThrotedFlumeModel: LongThrotedFlumeModel): Int
}