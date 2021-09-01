package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.CipolettiDao
import id.ias.calculationwaterdebit.database.model.CipolettiModel
import kotlinx.coroutines.flow.Flow

class CipolettiRepository(private val cipolettiDao: CipolettiDao) {
    val allCipolettiDatas: Flow<List<CipolettiModel>> = cipolettiDao.getAllCipoletti()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getCipolettiDataById(id: Int): CipolettiModel {
        return cipolettiDao.getCipolettiById(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getCipolettiDataByIdBaseData(id: Int): CipolettiModel {
        return cipolettiDao.getCipolettiByIdBaseData(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(cipolettiModel: CipolettiModel): Long {
        return cipolettiDao.insert(cipolettiModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(cipolettiModel: CipolettiModel): Int {
        return cipolettiDao.update(cipolettiModel)
    }
}