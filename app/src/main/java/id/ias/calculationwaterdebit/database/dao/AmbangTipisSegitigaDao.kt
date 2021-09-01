package id.ias.calculationwaterdebit.database.dao

import androidx.room.*
import id.ias.calculationwaterdebit.database.model.AmbangTajamSegiempatModel
import id.ias.calculationwaterdebit.database.model.AmbangTipisSegitigaModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AmbangTipisSegitigaDao {
    @Query("SELECT * FROM ambang_tajam_segitiga")
    fun getAllAts(): Flow<List<AmbangTipisSegitigaModel>>

    @Query("SELECT * FROM ambang_tajam_segitiga WHERE id = :id")
    suspend fun getAtsById(id: Int): AmbangTipisSegitigaModel

    @Query("SELECT * FROM ambang_tajam_segitiga WHERE id_base_data = :id")
    suspend fun getAtsByIdBaseData(id: Int): AmbangTipisSegitigaModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(atsModel: AmbangTipisSegitigaModel): Long

    @Update
    suspend fun update(atsModel: AmbangTipisSegitigaModel): Int
}