package id.ias.calculationwaterdebit.database.dao

import androidx.room.*
import id.ias.calculationwaterdebit.database.model.CrumpModel
import id.ias.calculationwaterdebit.database.model.RomijnModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CrumpDao {
    @Query("SELECT * FROM crump")
    fun getAllCrump(): Flow<List<CrumpModel>>

    @Query("SELECT * FROM crump WHERE id = :id")
    suspend fun getCrumpById(id: Int): CrumpModel

    @Query("SELECT * FROM crump WHERE id_base_data = :id")
    suspend fun getCrumpByIdBaseData(id: Int): CrumpModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crumpModel: CrumpModel): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(crumpModel: CrumpModel): Int
}