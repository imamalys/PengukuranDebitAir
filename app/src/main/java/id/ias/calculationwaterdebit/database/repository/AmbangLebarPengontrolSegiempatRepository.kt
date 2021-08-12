package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.AmbangLebarPengontrolSegiempatDao
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolSegiempatModel
import kotlinx.coroutines.flow.Flow

class AmbangLebarPengontrolSegiempatRepository(private val alpsDao: AmbangLebarPengontrolSegiempatDao) {
    val allAlpsDatas: Flow<List<AmbangLebarPengontrolSegiempatModel>> = alpsDao.getAllalps()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getAlpsDataById(id: Int): AmbangLebarPengontrolSegiempatModel {
        return alpsDao.getAlpsById(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(alpsModel: AmbangLebarPengontrolSegiempatModel): Long {
        return alpsDao.insert(alpsModel)
    }
}