package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.AmbangTipisSegitigaDao
import id.ias.calculationwaterdebit.database.model.AmbangTipisSegitigaModel
import kotlinx.coroutines.flow.Flow

class AmbangTipisSegitigaRepository(private val atsDao: AmbangTipisSegitigaDao) {
    val allAtsDatas: Flow<List<AmbangTipisSegitigaModel>> = atsDao.getAllats()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getAtsDataById(id: Int): AmbangTipisSegitigaModel {
        return atsDao.getAtsById(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(atsModel: AmbangTipisSegitigaModel): Long {
        return atsDao.insert(atsModel)
    }
}