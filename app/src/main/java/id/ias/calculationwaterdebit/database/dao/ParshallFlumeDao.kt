package id.ias.calculationwaterdebit.database.dao

import androidx.room.*
import id.ias.calculationwaterdebit.database.model.CipolettiModel
import id.ias.calculationwaterdebit.database.model.ParshallFlumeModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ParshallFlumeDao {
    @Query("SELECT * FROM parshall_flume")
    fun getAllParshallFlume(): Flow<List<ParshallFlumeModel>>

    @Query("SELECT * FROM parshall_flume WHERE id = :id")
    suspend fun getParshallFlumeById(id: Int): ParshallFlumeModel

    @Query("SELECT * FROM parshall_flume WHERE id_base_data = :id")
    suspend fun getParshallFlumeByIdBaseData(id: Int): ParshallFlumeModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(parshallFlumeModel: ParshallFlumeModel): Long

    @Update
    suspend fun update(parshallFlumeModel: ParshallFlumeModel): Int
}