package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.AmbangTipisSegitigaSudutModel

@Dao
interface AmbangTipisSegitigaSudutDao {
    @Query("SELECT * FROM sudut_ambang_tipis_segitiga WHERE sudut_dht = :sudutDht")
    fun getAmbangTipisSegitigaSudutById(sudutDht: Int): AmbangTipisSegitigaSudutModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ambangTipisSegitigaSudutModel: AmbangTipisSegitigaSudutModel)

    @Query("DELETE FROM sudut_ambang_tipis_segitiga")
    suspend fun deleteAll()
}