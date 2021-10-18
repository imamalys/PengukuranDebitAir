package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.PengambilanDataDao
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import kotlinx.coroutines.flow.Flow

class PengambilanDataRepository(private val pengambilanDataDao: PengambilanDataDao) {
    val allPengambilanDatas: Flow<List<PengambilanDataModel>> = pengambilanDataDao.getPengambilanDatas()

    suspend fun getPengambilanDataById(id: Int): List<PengambilanDataModel> {
        return pengambilanDataDao.getPengambilanDataById(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(pengambilanDataModel: PengambilanDataModel): Long {
        return pengambilanDataDao.insert(pengambilanDataModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(pengambilanDataModel: PengambilanDataModel): Int {
        return pengambilanDataDao.update(pengambilanDataModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(id: Int, jumlahRataRata: Float, debitSaluran: Float): Int {
        return pengambilanDataDao.update(id, jumlahRataRata, debitSaluran)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(id: Int, qBangunan: Float): Int {
        return pengambilanDataDao.update(id, qBangunan)
    }
}