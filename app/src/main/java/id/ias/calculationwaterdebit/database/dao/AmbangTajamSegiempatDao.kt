package id.ias.calculationwaterdebit.database.dao

import androidx.room.*
import id.ias.calculationwaterdebit.database.model.AmbangTajamSegiempatModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AmbangTajamSegiempatDao {
    @Query("SELECT * FROM ambang_tajam_segiempat")
    fun getAllAts(): Flow<List<AmbangTajamSegiempatModel>>

    @Query("SELECT * FROM ambang_tajam_segiempat WHERE id = :id")
    suspend fun getAtsById(id: Int): AmbangTajamSegiempatModel

    @Query("SELECT * FROM ambang_tajam_segiempat WHERE id_base_data = :id")
    suspend fun getAtsByIdBaseData(id: Int): AmbangTajamSegiempatModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(atsModel: AmbangTajamSegiempatModel): Long

    @Update
    suspend fun update(atsModel: AmbangTajamSegiempatModel): Int
}