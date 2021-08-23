package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.KoefisiensiCutThroatedFlumeDao
import id.ias.calculationwaterdebit.database.model.KoefisiensiCutThroatedFlumeModel

class KoefisiensiCutThroatedFlumeRepository(private val dao: KoefisiensiCutThroatedFlumeDao) {
    fun getKoefisiensiCutThroatedFlumeById(b: Float): KoefisiensiCutThroatedFlumeModel {
        return dao.getKoefisiensiCutThroatedFlumeById(b)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(koefisiensiCutThroatedFlumeModel: KoefisiensiCutThroatedFlumeModel) {
        dao.insert(koefisiensiCutThroatedFlumeModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        dao.deleteAll()
    }
}