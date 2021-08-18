package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolTrapesiumModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AmbangLebarPengontrolTrapesiumDao {
    @Query("SELECT * FROM alpt")
    fun getAllalps(): Flow<List<AmbangLebarPengontrolTrapesiumModel>>

    @Query("SELECT * FROM alpt WHERE id = :id")
    suspend fun getAlpsById(id: Int): AmbangLebarPengontrolTrapesiumModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alptModel: AmbangLebarPengontrolTrapesiumModel): Long
}