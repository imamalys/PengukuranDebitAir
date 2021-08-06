package id.ias.calculationwaterdebit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolSegiempatModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AmbangLebarPengontrolSegiempatDao {
    @Query("SELECT * FROM alps")
    fun getAllalps(): Flow<List<AmbangLebarPengontrolSegiempatModel>>

    @Query("SELECT * FROM alps WHERE id = :id")
    suspend fun getAlpsById(id: Int): AmbangLebarPengontrolSegiempatModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alpsModel: AmbangLebarPengontrolSegiempatModel): Long

    @Query("UPDATE alps SET lebar_ambang = :lebarAmbang, lebar_ambang = :lebarDasar, panjang_ambang = :panjangAmbang, tinggi_ambang = :tinggiAmbang, tinggi_di_atas_ambang = :tinggiDiatasAmbang, tinggi_di_bawah_ambang = :tinggiDibawahAmbang, lebar_atas = :lebarAtas WHERE id = :id")
    suspend fun update(id: Int, lebarAmbang: Int, lebarDasar: Int, panjangAmbang: Int, tinggiAmbang: Int,
                       tinggiDiatasAmbang: Int, tinggiDibawahAmbang: Int, lebarAtas: Int)

    @Query("UPDATE alps SET id_pengambilan_data = :id WHERE id = :idAlps")
    suspend fun updateIdPengambilanData(idAlps: Int, id: Int)
}