package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.LongThrotedFlumeModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LongThroatedFlumeDao {
    @Query("SELECT * FROM ltf")
    fun getAllLtf(): Flow<List<LongThrotedFlumeModel>>

    @Query("SELECT * FROM ltf WHERE id = :id")
    suspend fun getLtfById(id: Int): LongThrotedFlumeModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(longThrotedFlumeModel: LongThrotedFlumeModel): Long
}