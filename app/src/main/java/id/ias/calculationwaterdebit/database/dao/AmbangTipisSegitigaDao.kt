package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.AmbangTipisSegitigaModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AmbangTipisSegitigaDao {
    @Query("SELECT * FROM ats")
    fun getAllats(): Flow<List<AmbangTipisSegitigaModel>>

    @Query("SELECT * FROM ats WHERE id = :id")
    suspend fun getAtsById(id: Int): AmbangTipisSegitigaModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(atsModel: AmbangTipisSegitigaModel): Long
}