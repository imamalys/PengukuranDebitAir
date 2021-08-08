package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.database.model.KoefisiensiAmbangLebarModel
import kotlinx.coroutines.flow.Flow

@Dao
interface KoefisiensiAmbangLebarDao {
    @Query("SELECT * FROM koefiensi_ambang_lebar WHERE nilai = :nilai")
    fun getKoefiensiAmbangLebarById(nilai: Float): KoefisiensiAmbangLebarModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(koefisiensiAmbangLebarModel: KoefisiensiAmbangLebarModel)

    @Query("DELETE FROM koefiensi_ambang_lebar")
    suspend fun deleteAll()
}