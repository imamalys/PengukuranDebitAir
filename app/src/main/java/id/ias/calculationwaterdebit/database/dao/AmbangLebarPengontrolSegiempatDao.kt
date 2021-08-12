package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolSegiempatModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AmbangLebarPengontrolSegiempatDao {
    @Query("SELECT * FROM alps")
    fun getAllalps(): Flow<List<AmbangLebarPengontrolSegiempatModel>>

    @Query("SELECT * FROM alps WHERE id = :id")
    suspend fun getAlpsById(id: Int): AmbangLebarPengontrolSegiempatModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alpsModel: AmbangLebarPengontrolSegiempatModel): Long
}