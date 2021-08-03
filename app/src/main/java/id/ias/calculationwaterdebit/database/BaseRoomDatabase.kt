package id.ias.calculationwaterdebit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.ias.calculationwaterdebit.database.dao.*
import id.ias.calculationwaterdebit.database.model.*
import kotlinx.coroutines.CoroutineScope

@Database(entities = [
    BaseDataModel::class,
    AmbangLebarPengontrolSegiempatModel::class,
    PengambilanDataModel::class,
    FormDataModel::class,
    PiasModel::class],
    version = 1)
abstract class BaseRoomDatabase: RoomDatabase() {

    abstract fun baseDataDao(): BaseDataDao
    abstract fun alpsDao(): AmbangLebarPengontrolSegiempatDao
    abstract fun pengambilanDataDao(): PengambilanDataDao
    abstract fun formDataDao(): FormDataDao
    abstract fun piasDao(): PiasDao

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
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}