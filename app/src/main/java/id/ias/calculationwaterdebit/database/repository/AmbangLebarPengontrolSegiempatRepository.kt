package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.AmbangLebarPengontrolSegiempatDao
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolSegiempatModel
import kotlinx.coroutines.flow.Flow

class AmbangLebarPengontrolSegiempatRepository(private val alpsDao: AmbangLebarPengontrolSegiempatDao) {
    val allAlpsDatas: Flow<List<AmbangLebarPengontrolSegiempatModel>> = alpsDao.getAllalps()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(alpsModel: AmbangLebarPengontrolSegiempatModel): Long {
        return alpsDao.insert(alpsModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(idAlps: Int, data: FloatArray) {
        alpsDao.update(idAlps, data[0].toInt(), data[1].toInt(), data[2].toInt(), data[3].toInt(),
            data[4].toInt(), data[5].toInt(), data[6].toInt())
    }
}