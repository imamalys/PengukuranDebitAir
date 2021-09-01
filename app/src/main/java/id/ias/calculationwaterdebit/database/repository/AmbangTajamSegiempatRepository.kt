package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.AmbangTajamSegiempatDao
import id.ias.calculationwaterdebit.database.dao.KoefisiensiAmbangTajamSegiempatDao
import id.ias.calculationwaterdebit.database.model.*
import kotlinx.coroutines.flow.Flow

class AmbangTajamSegiempatRepository(private val dao: AmbangTajamSegiempatDao) {
    val allAtsDatas: Flow<List<AmbangTajamSegiempatModel>> = dao.getAllAts()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getAtsDataById(id: Int): AmbangTajamSegiempatModel {
        return dao.getAtsById(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getAtsDataByIdBaseData(id: Int): AmbangTajamSegiempatModel {
        return dao.getAtsByIdBaseData(id)
    }


    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(atsModel: AmbangTajamSegiempatModel): Long {
        return dao.insert(atsModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun update(atsModel: AmbangTajamSegiempatModel): Int {
        return dao.update(atsModel)
    }
}