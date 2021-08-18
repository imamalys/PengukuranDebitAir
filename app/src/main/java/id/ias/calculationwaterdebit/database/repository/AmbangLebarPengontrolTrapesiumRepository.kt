package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.AmbangLebarPengontrolTrapesiumDao
import id.ias.calculationwaterdebit.database.model.AmbangLebarPengontrolTrapesiumModel
import kotlinx.coroutines.flow.Flow

class AmbangLebarPengontrolTrapesiumRepository(private val alpt: AmbangLebarPengontrolTrapesiumDao) {
    val allAlptDatas: Flow<List<AmbangLebarPengontrolTrapesiumModel>> = alpt.getAllalps()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun getAlptDataById(id: Int): AmbangLebarPengontrolTrapesiumModel {
        return alpt.getAlpsById(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(alptModel: AmbangLebarPengontrolTrapesiumModel): Long {
        return alpt.insert(alptModel)
    }
}