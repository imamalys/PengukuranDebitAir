package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PengambilanDataDao {
    @Query("SELECT * FROM pengambilan_data")
    fun getPengambilanDatas(): Flow<List<PengambilanDataModel>>

    @Query("SELECT * FROM pengambilan_data WHERE id_base_data = :id")
    suspend fun getPengambilanDataById(id: Int): List<PengambilanDataModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pengambilanDataModel: PengambilanDataModel): Long

    @Query("UPDATE pengambilan_data SET jumlah_rata_rata = :jumlahRataRata, q_pengukuran = :debitSaluran WHERE id = :id")
    suspend fun update(id: Int, jumlahRataRata: Float, debitSaluran: Float): Int

    @Query("UPDATE pengambilan_data SET q_bangunan = :qBangunan WHERE id = :id")
    suspend fun update(id: Int, qBangunan: Float): Int
}