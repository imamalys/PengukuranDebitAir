package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.CutThroatedFlumeDao
import id.ias.calculationwaterdebit.database.model.CutThroatedFlumeModel
import kotlinx.coroutines.flow.Flow

class CutThroatedFlumeRepository(private val ctfDao: CutThroatedFlumeDao) {
    val allCtfDatas: Flow<List<CutThroatedFlumeModel>> = ctfDao.getAllCtf()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getCtfDataById(id: Int): CutThroatedFlumeModel {
        return ctfDao.getCtfById(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(ctfData: CutThroatedFlumeModel): Long {
        return ctfDao.insert(ctfData)
    }
}