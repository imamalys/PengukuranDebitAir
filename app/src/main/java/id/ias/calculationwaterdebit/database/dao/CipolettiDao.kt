package id.ias.calculationwaterdebit.database.dao

import androidx.room.*
import id.ias.calculationwaterdebit.database.model.CipolettiModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CipolettiDao {
    @Query("SELECT * FROM cipoletti")
    fun getAllCipoletti(): Flow<List<CipolettiModel>>

    @Query("SELECT * FROM cipoletti WHERE id = :id")
    suspend fun getCipolettiById(id: Int): CipolettiModel

    @Query("SELECT * FROM cipoletti WHERE id_base_data = :id")
    suspend fun getCipolettiByIdBaseData(id: Int): CipolettiModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cipolettiModel: CipolettiModel): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(cipolettiModel: CipolettiModel): Int
}