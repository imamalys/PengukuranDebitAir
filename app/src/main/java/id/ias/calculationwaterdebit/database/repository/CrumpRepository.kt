package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.CrumpDao
import id.ias.calculationwaterdebit.database.model.CrumpModel
import id.ias.calculationwaterdebit.database.model.RomijnModel
import kotlinx.coroutines.flow.Flow

class CrumpRepository(private val dao: CrumpDao) {
    val allCrumpDatas: Flow<List<CrumpModel>> = dao.getAllCrump()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getCrumpDataById(id: Int): CrumpModel {
        return dao.getCrumpById(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getCrumpDataByIdBaseData(id: Int): CrumpModel {
        return dao.getCrumpByIdBaseData(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(crumpModel: CrumpModel): Long {
        return dao.insert(crumpModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(crumpModel: CrumpModel): Int {
        return dao.update(crumpModel)
    }
}