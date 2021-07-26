package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.PengambilanDataDao
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import kotlinx.coroutines.flow.Flow

class PengambilanDataRepository(private val pengambilanDataDao: PengambilanDataDao) {
    val allPengambilanDatas: Flow<List<PengambilanDataModel>> = pengambilanDataDao.getPengambilanDatas()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(pengambilanDataModel: PengambilanDataModel) {
        pengambilanDataDao.insert(pengambilanDataModel)
    }
}