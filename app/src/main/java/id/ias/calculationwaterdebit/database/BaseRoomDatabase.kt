package id.ias.calculationwaterdebit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import id.ias.calculationwaterdebit.constant.AppConstant
import id.ias.calculationwaterdebit.database.dao.*
import id.ias.calculationwaterdebit.database.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [
    BaseDataModel::class,
    AmbangLebarPengontrolSegiempatModel::class,
    PengambilanDataModel::class,
    PiasModel::class,
    KoefisiensiAmbangLebarModel::class],
    version = 1)
abstract class BaseRoomDatabase: RoomDatabase() {

    abstract fun baseDataDao(): BaseDataDao
    abstract fun alpsDao(): AmbangLebarPengontrolSegiempatDao
    abstract fun pengambilanDataDao(): PengambilanDataDao
    abstract fun piasDao(): PiasDao
    abstract fun koefisiensiDao(): KoefisiensiAmbangLebarDao

    companion object {
        @Volatile
        private var INSTANCE:   BaseRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
            ): BaseRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BaseRoomDatabase::class.java,
                    "base_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(KoefisiensiDatabaseCallback(scope))
                    .build()
                INSTANCE = instance

                instance
            }
        }

        private class KoefisiensiDatabaseCallback(
            private val scope: CoroutineScope
        ): RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.koefisiensiDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(koefisiensiAmbangLebarDao: KoefisiensiAmbangLebarDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            if (!AppConstant.checkIsInstalled()) {
                AppConstant.setInstalled()
                koefisiensiAmbangLebarDao.deleteAll()

                var koefisiensi = KoefisiensiAmbangLebarModel((0.02).toFloat(), (1).toFloat(), (1).toFloat(), (1).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.03).toFloat(), (1).toFloat(), (1).toFloat(), (1).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.04).toFloat(), (1).toFloat(), (1).toFloat(), (1.001).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.05).toFloat(), (1).toFloat(), (1).toFloat(), (1.001).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.06).toFloat(), (1).toFloat(), (1.001).toFloat(), (1.002).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.07).toFloat(), (1).toFloat(), (1.001).toFloat(), (1.002).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.08).toFloat(), (1).toFloat(), (1.001).toFloat(), (1.002).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.09).toFloat(), (1).toFloat(), (1.002).toFloat(), (1.003).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.10).toFloat(), (1).toFloat(), (1.002).toFloat(), (1.003).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.11).toFloat(), (1).toFloat(), (1.003).toFloat(), (1.004).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.12).toFloat(), (1.001).toFloat(), (1.003).toFloat(), (1.004).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.13).toFloat(), (1.001).toFloat(), (1.004).toFloat(), (1.005).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.14).toFloat(), (1.002).toFloat(), (1.004).toFloat(), (1.005).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.15).toFloat(), (1.002).toFloat(), (1.005).toFloat(), (1.006).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.16).toFloat(), (1.003).toFloat(), (1.005).toFloat(), (1.007).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.17).toFloat(), (1.004).toFloat(), (1.006).toFloat(), (1.008).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.18).toFloat(), (1.004).toFloat(), (1.007).toFloat(), (1.009).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.19).toFloat(), (1.005).toFloat(), (1.008).toFloat(), (1.01).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.20).toFloat(), (1.06).toFloat(), (1.009).toFloat(), (1.011).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.21).toFloat(), (1.007).toFloat(), (1.010).toFloat(), (1.012).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.22).toFloat(), (1.009).toFloat(), (1.011).toFloat(), (1.013).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.23).toFloat(), (1.010).toFloat(), (1.012).toFloat(), (1.015).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.24).toFloat(), (1.011).toFloat(), (1.013).toFloat(), (1.016).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.25).toFloat(), (1.012).toFloat(), (1.014).toFloat(), (1.017).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
            }
        }
    }
}