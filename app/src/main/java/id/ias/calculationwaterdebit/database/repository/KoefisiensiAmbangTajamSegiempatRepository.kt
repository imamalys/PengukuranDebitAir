package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.KoefisiensiAmbangTajamSegiempatDao
import id.ias.calculationwaterdebit.database.model.KoefisiensiAmbangTajamSegiempatModel

class KoefisiensiAmbangTajamSegiempatRepository(private val dao: KoefisiensiAmbangTajamSegiempatDao) {
    fun getKoefisiensiAmbangTajamSegiempatById(nilai: Float, hPerP: Float): KoefisiensiAmbangTajamSegiempatModel {
        return dao.getKoefisiensiAmbangTajamSegitigaById(nilai, hPerP)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(koefisiensiAmbangTajamSegiempatModel: KoefisiensiAmbangTajamSegiempatModel) {
        dao.insert(koefisiensiAmbangTajamSegiempatModel)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        dao.deleteAll()
    }
}