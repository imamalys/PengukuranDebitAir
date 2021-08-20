package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.AmbangTipisSegitigaSudutDao
import id.ias.calculationwaterdebit.database.model.AmbangTipisSegitigaSudutModel

class AmbangTipisSegitigaSudutRepository(private val ambangTipisSegitigaSudutDao: AmbangTipisSegitigaSudutDao) {
    fun getAmbangTipisSegitigaSudutById(sudutDht: Int): AmbangTipisSegitigaSudutModel {
        return ambangTipisSegitigaSudutDao.getAmbangTipisSegitigaSudutById(sudutDht)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(ambangTipisSegitigaSudutModel: AmbangTipisSegitigaSudutModel) {
        ambangTipisSegitigaSudutDao.insert(ambangTipisSegitigaSudutModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        ambangTipisSegitigaSudutDao.deleteAll()
    }
}