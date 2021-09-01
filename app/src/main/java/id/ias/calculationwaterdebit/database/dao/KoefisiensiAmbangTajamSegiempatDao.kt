package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.KoefisiensiAmbangTajamSegiempatModel

@Dao
interface KoefisiensiAmbangTajamSegiempatDao {
    @Query("SELECT * FROM koefisiensi_ambang_tajam_segiempat WHERE nilai = :nilai AND hPerP = :hPerP")
    fun getKoefisiensiAmbangTajamSegitigaById(nilai: Float, hPerP: Float): KoefisiensiAmbangTajamSegiempatModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(koefisiensiAmbangTajamSegiempatModel: KoefisiensiAmbangTajamSegiempatModel)

    @Query("DELETE FROM koefisiensi_ambang_tajam_segiempat")
    suspend fun deleteAll()
}