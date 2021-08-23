package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.CutThroatedFlumeModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CutThroatedFlumeDao {
    @Query("SELECT * FROM cut_throated_flume")
    fun getAllCtf(): Flow<List<CutThroatedFlumeModel>>

    @Query("SELECT * FROM cut_throated_flume WHERE id = :id")
    suspend fun getCtfById(id: Int): CutThroatedFlumeModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ctfModel: CutThroatedFlumeModel): Long
}