package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.ParshallFlumeDao
import id.ias.calculationwaterdebit.database.model.ParshallFlumeModel
import kotlinx.coroutines.flow.Flow

class ParshallFlumeRepository(private val parshallFlumeDao: ParshallFlumeDao) {
    val allParshallFlumeDatas: Flow<List<ParshallFlumeModel>> = parshallFlumeDao.getAllParshallFlume()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getParshallFlumeDataById(id: Int): ParshallFlumeModel {
        return parshallFlumeDao.getParshallFlumeById(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(parshallFlumeModel: ParshallFlumeModel): Long {
        return parshallFlumeDao.insert(parshallFlumeModel)
    }
}