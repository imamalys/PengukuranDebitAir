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
    fun getPengambilanDataById(id: Int): Flow<List<PengambilanDataModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pengambilanDataModel: PengambilanDataModel): Long

    @Query("UPDATE pengambilan_data SET id = :id WHERE jumlah_rata_rata = :jumlahRataRata")
    suspend fun update(id: Int, jumlahRataRata: Float): Int
}