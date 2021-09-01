package id.ias.calculationwaterdebit.database.dao

import androidx.room.*
import id.ias.calculationwaterdebit.database.model.RomijnModel
import kotlinx.coroutines.flow.Flow

@Dao
interface RomijnDao {
    @Query("SELECT * FROM romijn")
    fun getAllRomijn(): Flow<List<RomijnModel>>

    @Query("SELECT * FROM romijn WHERE id = :id")
    suspend fun getRomijnById(id: Int): RomijnModel

    @Query("SELECT * FROM romijn WHERE id_base_data = :id")
    suspend fun getRomijnByIdBaseData(id: Int): RomijnModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(romijnModel: RomijnModel): Long

    @Update
    suspend fun update(romijnModel: RomijnModel): Int
}