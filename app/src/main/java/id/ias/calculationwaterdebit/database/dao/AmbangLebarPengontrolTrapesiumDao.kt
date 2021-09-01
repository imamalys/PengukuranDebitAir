package id.ias.calculationwaterdebit.database.dao

import androidx.room.*
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolTrapesiumModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AmbangLebarPengontrolTrapesiumDao {
    @Query("SELECT * FROM alpt")
    fun getAllalps(): Flow<List<AmbangLebarPengontrolTrapesiumModel>>

    @Query("SELECT * FROM alpt WHERE id = :id")
    suspend fun getAlpsById(id: Int): AmbangLebarPengontrolTrapesiumModel

    @Query("SELECT * FROM alpt WHERE id_base_data = :id")
    suspend fun getalpsDataByIdBaseData(id: Int): AmbangLebarPengontrolTrapesiumModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alptModel: AmbangLebarPengontrolTrapesiumModel): Long

    @Update
    suspend fun update(alptModel: AmbangLebarPengontrolTrapesiumModel): Int
}