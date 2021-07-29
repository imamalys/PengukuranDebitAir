package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.FormDataModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FormDataDao {
    @Query("SELECT * FROM form_data WHERE id_pengambilan_data = :id")
    fun getFormDataByPengambilanData(id: Int): Flow<List<FormDataModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(formDataModel: FormDataModel)
}