package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.KoefiesiensiAmbangTajamSegitigaDao
import id.ias.calculationwaterdebit.database.model.KoefiesiensiAmbangTajamSegitigaModel

class KoefiesiensiAmbangTajamSegitigaRepository(private val koefiesiensiAmbangTajamSegitigaDao: KoefiesiensiAmbangTajamSegitigaDao) {
    fun getAmbangTipisSegitigaCdById(nilai: Float, hp: Float): KoefiesiensiAmbangTajamSegitigaModel {
        return koefiesiensiAmbangTajamSegitigaDao.getAmbangTipisSegitigaCdById(nilai, hp)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(koefiesiensiAmbangTajamSegitigaModel: KoefiesiensiAmbangTajamSegitigaModel) {
        koefiesiensiAmbangTajamSegitigaDao.insert(koefiesiensiAmbangTajamSegitigaModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        koefiesiensiAmbangTajamSegitigaDao.deleteAll()
    }
}