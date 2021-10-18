package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.PiasDao
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.database.model.PiasModel
import kotlinx.coroutines.flow.Flow

class PiasDataRepository(private val piasDataDao: PiasDao) {
    fun piasByFormData(id: Int): Flow<List<PiasModel>> {
        return piasDataDao.getPiasByFormData(id)
    }

    suspend fun piasByid(id: Int): List<PiasModel> {
        return piasDataDao.getPiasById(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(piasModel: PiasModel): Long {
        return piasDataDao.insert(piasModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(piasModel: PiasModel): Int {
        return piasDataDao.update(piasModel)
    }
}