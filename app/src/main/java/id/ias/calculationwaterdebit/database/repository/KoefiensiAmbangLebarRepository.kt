package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.KoefisiensiAmbangLebarDao
import id.ias.calculationwaterdebit.database.dao.PengambilanDataDao
import id.ias.calculationwaterdebit.database.model.KoefisiensiAmbangLebarModel
import id.ias.calculationwaterdebit.database.model.PengambilanDataModel
import kotlinx.coroutines.flow.Flow

class KoefiensiAmbangLebarRepository(private val koefisiensiAmbangLebarDao: KoefisiensiAmbangLebarDao) {
    fun getKoefiensiAmbangLebarById(nilai: Float): Flow<KoefisiensiAmbangLebarModel> {
        return koefisiensiAmbangLebarDao.getKoefiensiAmbangLebarById(nilai)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(koefisiensiAmbangLebarModel: KoefisiensiAmbangLebarModel) {
        koefisiensiAmbangLebarDao.insert(koefisiensiAmbangLebarModel)
    }
}