package id.ias.calculationwaterdebit.database.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ias.calculationwaterdebit.database.dao.BaseDataDao
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import kotlinx.coroutines.flow.Flow

class BaseDataRepository(private val baseDataDao: BaseDataDao) {
    val allBaseDatas: Flow<List<BaseDataModel>> = baseDataDao.getAllBaseData()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(baseDataModel: BaseDataModel) {
        baseDataDao.insert(baseDataModel)
    }
}