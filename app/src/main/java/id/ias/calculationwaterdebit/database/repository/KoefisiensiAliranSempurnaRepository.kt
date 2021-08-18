package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.KoefisiensiAliranSempurnaDao
import id.ias.calculationwaterdebit.database.model.KoefisiensiAliranSempurnaModel
import id.ias.calculationwaterdebit.database.model.KoefisiensiAmbangLebarModel
import kotlinx.coroutines.flow.Flow

class KoefisiensiAliranSempurnaRepository(private val koefisiensiAliranSempurnaDao: KoefisiensiAliranSempurnaDao) {
    fun getKoefisiensiAliranSempurnById(b: Float): KoefisiensiAliranSempurnaModel {
        return koefisiensiAliranSempurnaDao.getKoefisiensiAliranSempurnaById(b)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(koefisiensiAmbangLebarModel: KoefisiensiAliranSempurnaModel) {
        koefisiensiAliranSempurnaDao.insert(koefisiensiAmbangLebarModel)
    }
}