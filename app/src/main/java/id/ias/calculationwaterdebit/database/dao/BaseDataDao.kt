package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BaseDataDao {
    @Query("SELECT * FROM base_data")
    fun getAllBaseData(): Flow<List<BaseDataModel>>

    @Query("SELECT * FROM base_data WHERE id = :id")
    fun getBaseDataById(id: Int): Flow<BaseDataModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(baseDataModel: BaseDataModel)
}