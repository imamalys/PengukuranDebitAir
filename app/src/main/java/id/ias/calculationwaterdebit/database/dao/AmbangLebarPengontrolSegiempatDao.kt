package id.ias.calculationwaterdebit.database.dao

import androidx.room.*
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolSegiempatModel
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolTrapesiumModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AmbangLebarPengontrolSegiempatDao {
    @Query("SELECT * FROM alps")
    fun getAllalps(): Flow<List<AmbangLebarPengontrolSegiempatModel>>

    @Query("SELECT * FROM alps WHERE id = :id")
    suspend fun getAlpsById(id: Int): AmbangLebarPengontrolSegiempatModel

    @Query("SELECT * FROM alps WHERE id_base_data = :id")
    suspend fun getAlpsDataByIdBaseData(id: Int): AmbangLebarPengontrolSegiempatModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alpsModel: AmbangLebarPengontrolSegiempatModel): Long

    @Update
    suspend fun update(alpsModel: AmbangLebarPengontrolSegiempatModel): Int
}