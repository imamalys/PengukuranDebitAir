package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.PiasModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PiasDao {
    @Query("SELECT * FROM pias WHERE id_pengambilan_data = :id")
    fun getPiasByFormData(id: Int): Flow<List<PiasModel>>

    @Query("SELECT * FROM pias WHERE id_pengambilan_data = :id")
    suspend fun getPiasById(id: Int): List<PiasModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(piasModel: PiasModel): Long
}