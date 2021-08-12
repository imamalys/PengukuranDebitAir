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
    suspend fun update(id: Int, variablePertama: String, n: String): Int {
        return baseDataDao.update(id = id, variablePertama = variablePertama, n = n)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(id: Int, minH2: String, maxH2: String, minDebitSaluran: String, maxDebitSaluran: String): Int {
        return baseDataDao.update(id, minH2, maxH2, minDebitSaluran, maxDebitSaluran)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun updateAnalisis(id: Int, k: String, c: String, mape: String): Int {
        return baseDataDao.updateAnalisis(id, k, c, mape)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(id: Int, keterangan: String, nilaiKeterangan: Int): Int {
        return baseDataDao.update(id = id, keterangan = keterangan, nilaiKeterangan = nilaiKeterangan)
    }
}