package id.ias.calculationwaterdebit.database.repository

import id.ias.calculationwaterdebit.database.dao.FormDataDao
import id.ias.calculationwaterdebit.database.model.FormDataModel
import kotlinx.coroutines.flow.Flow

class FormDataRepository(private val formDataDao: FormDataDao, private val id: Int) {
    val formDataByPengambilanData: Flow<List<FormDataModel>> by lazy {
        formDataDao.getFormDataByPengambilanData(id)
    }

    suspend fun insert(formDataModel: FormDataModel) {
        formDataDao.insert(formDataModel)
    }
}