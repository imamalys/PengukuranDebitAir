package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.RomijnDao
import id.ias.calculationwaterdebit.database.model.RomijnModel
import kotlinx.coroutines.flow.Flow

class RomijnRepository(private val romijnDao: RomijnDao) {
    val allRomijnDatas: Flow<List<RomijnModel>> = romijnDao.getAllRomijn()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getRomijnDataById(id: Int): RomijnModel {
        return romijnDao.getRomijnById(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getRomijnDataByIdBaseData(id: Int): RomijnModel {
        return romijnDao.getRomijnByIdBaseData(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(romijnModel: RomijnModel): Long {
        return romijnDao.insert(romijnModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(romijnModel: RomijnModel): Int {
        return romijnDao.update(romijnModel)
    }
}