package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.BaseDataDao
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import kotlinx.coroutines.flow.Flow

class BaseDataRepository(private val baseDataDao: BaseDataDao) {
    val allBaseDatas: Flow<List<BaseDataModel>> = baseDataDao.getAllBaseData()

    suspend fun getBaseDataById(id: Int): BaseDataModel {
        return baseDataDao.getBaseDataById(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(baseDataModel: BaseDataModel): Long {
        return baseDataDao.insert(baseDataModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun delete(baseDataModel: BaseDataModel): Int {
        return baseDataDao.delete(baseDataModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun updateLoad(id: Int, namaSaluran: String, namaDaerahIrigasi: String, wilayahKewenangan: String,
                           provinsi: String, kabupaten: String, tanggal: String, noPengukuran: String,
                           namaPengukur: String): Int {
        return baseDataDao.updateLoad(id, namaSaluran, namaDaerahIrigasi, wilayahKewenangan, provinsi, kabupaten,
                tanggal, noPengukuran, namaPengukur)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(id: Int, variablePertama: String, n: String): Int {
        return baseDataDao.update(id = id, variablePertama = variablePertama, n = n)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(id: Int, minH1: String, maxH1: String, minDebitSaluran: String, maxDebitSaluran: String): Int {
        return baseDataDao.update(id, minH1, maxH1, minDebitSaluran, maxDebitSaluran)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun updateAnalisis(id: Int, k: String, c: String, mape: String): Int {
        return baseDataDao.updateAnalisis(id, k, c, mape)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun updateCheckKondisi(id: Int, keterangan: String, nilaiKeterangan: Int, pertama: Int, kedua: Int,
                                   ketiga: Int, keempat: Int, kelima: Int): Int {
        return baseDataDao.updateCheckKondisi(id, keterangan, nilaiKeterangan, pertama, kedua, ketiga, keempat, kelima)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(id: Int, tipeBangunan: String): Int {
        return baseDataDao.update(id = id, tipeBangunan = tipeBangunan)
    }
}