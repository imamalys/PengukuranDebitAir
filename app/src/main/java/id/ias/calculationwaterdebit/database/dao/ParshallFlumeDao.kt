package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.ParshallFlumeModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ParshallFlumeDao {
    @Query("SELECT * FROM orifice")
    fun getAllParshallFlume(): Flow<List<ParshallFlumeModel>>

    @Query("SELECT * FROM orifice WHERE id = :id")
    suspend fun getParshallFlumeById(id: Int): ParshallFlumeModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(parshallFlumeModel: ParshallFlumeModel): Long
}