package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.KoefisiensiAmbangTipisSegitigaDao
import id.ias.calculationwaterdebit.database.model.KoefisiensiAmbangTipisSegitigaModel

class KoefisiensiAmbangTipisSegitigaRepository(private val koefiesiensiAmbangTipisSegitigaDao: KoefisiensiAmbangTipisSegitigaDao) {
    fun getAmbangTipisSegitigaCdById(nilai: Float, hp: Float): KoefisiensiAmbangTipisSegitigaModel {
        return koefiesiensiAmbangTipisSegitigaDao.getAmbangTipisSegitigaCdById(nilai, hp)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(koefiesiensiAmbangTipisSegitigaModel: KoefisiensiAmbangTipisSegitigaModel) {
        koefiesiensiAmbangTipisSegitigaDao.insert(koefiesiensiAmbangTipisSegitigaModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        koefiesiensiAmbangTipisSegitigaDao.deleteAll()
    }
}