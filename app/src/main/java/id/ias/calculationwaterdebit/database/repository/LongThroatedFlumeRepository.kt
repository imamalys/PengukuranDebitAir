package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.LongThroatedFlumeDao
import id.ias.calculationwaterdebit.database.model.LongThrotedFlumeModel
import kotlinx.coroutines.flow.Flow

class LongThroatedFlumeRepository(private val ltfDao: LongThroatedFlumeDao) {
    val allLtfDatas: Flow<List<LongThrotedFlumeModel>> = ltfDao.getAllLtf()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getLtfDataById(id: Int): LongThrotedFlumeModel {
        return ltfDao.getLtfById(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(ltfModel: LongThrotedFlumeModel): Long {
        return ltfDao.insert(ltfModel)
    }
}