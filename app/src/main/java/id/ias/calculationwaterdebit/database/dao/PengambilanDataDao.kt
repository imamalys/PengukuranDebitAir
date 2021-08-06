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

    @Query("SELECT * FROM pengambilan_data WHERE id = :id")
    fun getPengambilanDataById(id: Int): Flow<PengambilanDataModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pengambilanDataModel: PengambilanDataModel): Long
}