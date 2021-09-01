package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.MercuAmbangDao
import id.ias.calculationwaterdebit.database.model.MercuAmbangModel

class MercuAmbangRepository(private val dao: MercuAmbangDao) {
    fun getMercuAmbangById(bPerB: Float): MercuAmbangModel {
        return dao.getMercuAmbangById(bPerB)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(mercuAmbangModel: MercuAmbangModel) {
        dao.insert(mercuAmbangModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        dao.deleteAll()
    }
}