package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.MercuAmbangModel

@Dao
interface MercuAmbangDao {
    @Query("SELECT * FROM mercu_ambang WHERE bPerB = :bPerB")
    fun getMercuAmbangById(bPerB: Float): MercuAmbangModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(mercuAmbangModel: MercuAmbangModel)

    @Query("DELETE FROM mercu_ambang")
    suspend fun deleteAll()
}