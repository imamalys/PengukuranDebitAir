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
    AmbangLebarPengontrolTrapesiumModel::class,
    AmbangTajamSegiempatModel::class,
    AmbangTipisSegitigaModel::class,
    CipolettiModel::class,
    OrificeModel::class,
    ParshallFlumeModel::class,
    LongThrotedFlumeModel::class,
    CutThroatedFlumeModel::class,
    RomijnModel::class,
    CrumpModel::class,
    PengambilanDataModel::class,
    PiasModel::class,
    KoefisiensiAmbangLebarModel::class,
    KoefisiensiAliranSempurnaModel::class,
    AmbangTipisSegitigaSudutModel::class,
    KoefisiensiAmbangTipisSegitigaModel::class,
    KoefisiensiCutThroatedFlumeModel::class,
    KoefisiensiAmbangTajamSegiempatModel::class,
    MercuAmbangModel::class],
    version = 3)
abstract class BaseRoomDatabase: RoomDatabase() {

    abstract fun baseDataDao(): BaseDataDao
    abstract fun alpsDao(): AmbangLebarPengontrolSegiempatDao
    abstract fun alptDao(): AmbangLebarPengontrolTrapesiumDao
    abstract fun ambangTajamSegiempatDao(): AmbangTajamSegiempatDao
    abstract fun atsDao(): AmbangTipisSegitigaDao
    abstract fun cipolettiDao(): CipolettiDao
    abstract fun orificeDao(): OrificeDao
    abstract fun parshallFlumeDao(): ParshallFlumeDao
    abstract fun longThroatedFlumeDao(): LongThroatedFlumeDao
    abstract fun cutThroatedFlumeDao(): CutThroatedFlumeDao
    abstract fun romijnDao(): RomijnDao
    abstract fun crumpDao(): CrumpDao
    abstract fun pengambilanDataDao(): PengambilanDataDao
    abstract fun piasDao(): PiasDao
    abstract fun koefisiensiDao(): KoefisiensiAmbangLebarDao
    abstract fun koefisiensiAliranSempurnaDao(): KoefisiensiAliranSempurnaDao
    abstract fun ambangTipisSegitigaSudutDao(): AmbangTipisSegitigaSudutDao
    abstract fun koefisiensiAmbangTipisSegitiga(): KoefisiensiAmbangTipisSegitigaDao
    abstract fun koefisiensiCutThroatedFlume(): KoefisiensiCutThroatedFlumeDao
    abstract fun koefisiensiAmbangTajamSegiempatDao(): KoefisiensiAmbangTajamSegiempatDao
    abstract fun mercuAmbang(): MercuAmbangDao

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
                    .allowMainThreadQueries()
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

                }
            }
        }
    }
}