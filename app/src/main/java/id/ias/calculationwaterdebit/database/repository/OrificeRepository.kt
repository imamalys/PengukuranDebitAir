package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.OrificeDao
import id.ias.calculationwaterdebit.database.model.OrificeModel
import kotlinx.coroutines.flow.Flow

class OrificeRepository(private val orificeDao: OrificeDao) {
    val allOrificeDatas: Flow<List<OrificeModel>> = orificeDao.getAllOrifice()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getOrificeDataById(id: Int): OrificeModel {
        return orificeDao.getOrificeById(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getOrificeDataByIdBaseData(id: Int): OrificeModel {
        return orificeDao.getOrificeByIdBaseData(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(orificeModel: OrificeModel): Long {
        return orificeDao.insert(orificeModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(orificeModel: OrificeModel): Int {
        return orificeDao.update(orificeModel)
    }
}