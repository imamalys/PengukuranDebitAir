package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.KoefisiensiAmbangTipisSegitigaModel

@Dao
interface KoefisiensiAmbangTipisSegitigaDao {
    @Query("SELECT * FROM ambang_tipis_segitiga_cd WHERE nilai = :nilai AND hp = :hp")
    fun getAmbangTipisSegitigaCdById(nilai: Float, hp: Float): KoefisiensiAmbangTipisSegitigaModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(koefiesiensiAmbangTipisSegitigaModel: KoefisiensiAmbangTipisSegitigaModel)

    @Query("DELETE FROM sudut_ambang_tipis_segitiga")
    suspend fun deleteAll()
}