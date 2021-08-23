package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.CrumpModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CrumpDao {
    @Query("SELECT * FROM crump")
    fun getAllCrump(): Flow<List<CrumpModel>>

    @Query("SELECT * FROM crump WHERE id = :id")
    suspend fun getCrumpById(id: Int): CrumpModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crumpModel: CrumpModel): Long
}