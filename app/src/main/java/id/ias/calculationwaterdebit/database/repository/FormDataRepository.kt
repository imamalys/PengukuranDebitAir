package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import id.ias.calculationwaterdebit.database.dao.FormDataDao
import id.ias.calculationwaterdebit.database.model.FormDataModel
import kotlinx.coroutines.flow.Flow

class FormDataRepository(private val formDataDao: FormDataDao) {

    fun formDataByFormData(id: Int): Flow<List<FormDataModel>> {
        return formDataDao.getFormDataByPengambilanData(id)
    }

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(formDataModel: FormDataModel): Long {
        return formDataDao.insert(formDataModel)
    }
}