package id.ias.calculationwaterdebit.database.dao

import androidx.room.*
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BaseDataDao {
    @Query("SELECT * FROM base_data ORDER BY id DESC")
    fun getAllBaseData(): Flow<List<BaseDataModel>>

    @Query("SELECT * FROM base_data WHERE id = :id")
    fun getBaseDataById(id: Int): BaseDataModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(baseDataModel: BaseDataModel): Long
    @Delete
    suspend fun delete(baseDataModel: BaseDataModel):Int

    @Query("UPDATE base_data SET nama_saluran = :namaSaluran, nama_daerah_irigasi = :namaDaerahIrigasi, wilayah_kewenangan = :wilayahKewenangan, provinsi = :provinsi, kabupaten = :kabupaten, tanggal = :tanggal, no_pengukuran = :noPengukuran, nama_pengukur = :namaPengukur  WHERE id = :id")
    suspend fun updateLoad(id: Int, namaSaluran: String, namaDaerahIrigasi: String, wilayahKewenangan: String,
                           provinsi: String, kabupaten: String, tanggal: String, noPengukuran: String,
                           namaPengukur: String): Int

    @Query("UPDATE base_data SET variable_pertama = :variablePertama, n = :n WHERE id = :id")
    suspend fun update(id: Int, variablePertama: String, n: String): Int

    @Query("UPDATE base_data SET min_h1 = :minH1, max_h1 = :maxH1, min_debit_saluran = :minDebitSaluran, max_debit_saluran = :maxDebitSaluran WHERE id = :id")
    suspend fun update(id: Int, minH1: String, maxH1: String, minDebitSaluran: String, maxDebitSaluran: String): Int

    @Query("UPDATE base_data SET k = :k, c = :c, mape = :mape WHERE id = :id")
    suspend fun updateAnalisis(id: Int, k: String, c: String, mape: String): Int

    @Query("UPDATE base_data SET keterangan = :keterangan, nilai_keterangan = :nilaiKeterangan, pertama = :pertama, kedua = :kedua, ketiga = :ketiga, keempat = :keempat, kelima = :kelima WHERE id = :id")
    suspend fun updateCheckKondisi(id: Int, keterangan: String, nilaiKeterangan: Int, pertama: Int, kedua: Int,
                                   ketiga: Int, keempat: Int, kelima: Int): Int
    @Query("UPDATE base_data SET tipe_bangunan = :tipeBangunan WHERE id = :id")
    suspend fun update(id: Int, tipeBangunan: String): Int

}