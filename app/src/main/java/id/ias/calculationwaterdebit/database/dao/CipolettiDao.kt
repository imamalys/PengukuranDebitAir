package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.CipolettiModel
import id.ias.calculationwaterdebit.database.model.OrificeModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CipolettiDao {
    @Query("SELECT * FROM cipoletti")
    fun getAllCipoletti(): Flow<List<CipolettiModel>>

    @Query("SELECT * FROM cipoletti WHERE id = :id")
    suspend fun getCipolettiById(id: Int): CipolettiModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cipolettiModel: CipolettiModel): Long
}