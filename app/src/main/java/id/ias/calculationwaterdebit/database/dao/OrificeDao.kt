package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.OrificeModel
import kotlinx.coroutines.flow.Flow

@Dao
interface OrificeDao {
    @Query("SELECT * FROM orifice")
    fun getAllOrifice(): Flow<List<OrificeModel>>

    @Query("SELECT * FROM orifice WHERE id = :id")
    suspend fun getOrificeById(id: Int): OrificeModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(orificeModel: OrificeModel): Long
}