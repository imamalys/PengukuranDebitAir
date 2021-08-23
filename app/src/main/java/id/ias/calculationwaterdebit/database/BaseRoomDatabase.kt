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
    KoefisiensiCutThroatedFlumeModel::class],
    version = 2)
abstract class BaseRoomDatabase: RoomDatabase() {

    abstract fun baseDataDao(): BaseDataDao
    abstract fun alpsDao(): AmbangLebarPengontrolSegiempatDao
    abstract fun alptDao(): AmbangLebarPengontrolTrapesiumDao
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
                koefisiensi = KoefisiensiAmbangLebarModel((0.26).toFloat(), (1.013).toFloat(), (1.016).toFloat(), (1.018).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.27).toFloat(), (1.014).toFloat(), (1.017).toFloat(), (1.020).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.28).toFloat(), (1.015).toFloat(), (1.018).toFloat(), (1.021).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.29).toFloat(), (1.016).toFloat(), (1.019).toFloat(), (1.023).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.30).toFloat(), (1.017).toFloat(), (1.020).toFloat(), (1.024).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.31).toFloat(), (1.019).toFloat(), (1.021).toFloat(), (1.025).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.32).toFloat(), (1.020).toFloat(), (1.023).toFloat(), (1.026).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.33).toFloat(), (1.022).toFloat(), (1.024).toFloat(), (1.028).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.34).toFloat(), (1.023).toFloat(), (1.026).toFloat(), (1.029).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.35).toFloat(), (1.025).toFloat(), (1.028).toFloat(), (1.031).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.36).toFloat(), (1.026).toFloat(), (1.029).toFloat(), (1.033).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.37).toFloat(), (1.028).toFloat(), (1.032).toFloat(), (1.035).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.38).toFloat(), (1.030).toFloat(), (1.033).toFloat(), (1.037).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.39).toFloat(), (1.031).toFloat(), (1.035).toFloat(), (1.039).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.40).toFloat(), (1.033).toFloat(), (1.037).toFloat(), (1.040).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.41).toFloat(), (1.036).toFloat(), (1.040).toFloat(), (1.044).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.42).toFloat(), (1.038).toFloat(), (1.042).toFloat(), (1.046).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.43).toFloat(), (1.040).toFloat(), (1.045).toFloat(), (1.048).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.44).toFloat(), (1.042).toFloat(), (1.046).toFloat(), (1.051).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.45).toFloat(), (1.044).toFloat(), (1.049).toFloat(), (1.054).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.46).toFloat(), (1.046).toFloat(), (1.051).toFloat(), (1.056).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.47).toFloat(), (1.049).toFloat(), (1.054).toFloat(), (1.058).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.48).toFloat(), (1.051).toFloat(), (1.056).toFloat(), (1.061).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.49).toFloat(), (1.054).toFloat(), (1.059).toFloat(), (1.064).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.50).toFloat(), (1.057).toFloat(), (1.062).toFloat(), (1.067).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.51).toFloat(), (1.060).toFloat(), (1.065).toFloat(), (1.070).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.52).toFloat(), (1.063).toFloat(), (1.068).toFloat(), (1.072).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.53).toFloat(), (1.066).toFloat(), (1.071).toFloat(), (1.076).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.54).toFloat(), (1.069).toFloat(), (1.074).toFloat(), (1.079).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.55).toFloat(), (1.072).toFloat(), (1.076).toFloat(), (1.082).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.56).toFloat(), (1.075).toFloat(), (1.080).toFloat(), (1.085).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.57).toFloat(), (1.078).toFloat(), (1.083).toFloat(), (1.089).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.58).toFloat(), (1.082).toFloat(), (1.087).toFloat(), (1.093).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.59).toFloat(), (1.085).toFloat(), (1.090).toFloat(), (1.097).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.60).toFloat(), (1.088).toFloat(), (1.095).toFloat(), (1.101).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.61).toFloat(), (1.094).toFloat(), (1.099).toFloat(), (1.105).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.62).toFloat(), (1.098).toFloat(), (1.103).toFloat(), (1.109).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.63).toFloat(), (1.101).toFloat(), (1.106).toFloat(), (1.113).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.64).toFloat(), (1.105).toFloat(), (1.111).toFloat(), (1.117).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.65).toFloat(), (1.110).toFloat(), (1.115).toFloat(), (1.122).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.66).toFloat(), (1.115).toFloat(), (1.119).toFloat(), (1.126).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.67).toFloat(), (1.118).toFloat(), (1.124).toFloat(), (1.131).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.68).toFloat(), (1.122).toFloat(), (1.128).toFloat(), (1.136).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.69).toFloat(), (1.127).toFloat(), (1.133).toFloat(), (1.141).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.70).toFloat(), (1.131).toFloat(), (1.138).toFloat(), (1.150).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.71).toFloat(), (1.139).toFloat(), (1.146).toFloat(), (1.155).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.72).toFloat(), (1.145).toFloat(), (1.152).toFloat(), (1.161).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.73).toFloat(), (1.150).toFloat(), (1.157).toFloat(), (1.166).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.74).toFloat(), (1.155).toFloat(), (1.162).toFloat(), (1.171).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.75).toFloat(), (1.161).toFloat(), (1.168).toFloat(), (1.177).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.76).toFloat(), (1.166).toFloat(), (1.173).toFloat(), (1.182).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.77).toFloat(), (1.172).toFloat(), (1.180).toFloat(), (1.189).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.78).toFloat(), (1.177).toFloat(), (1.187).toFloat(), (1.196).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.79).toFloat(), (1.183).toFloat(), (1.192).toFloat(), (1.201).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
                koefisiensi = KoefisiensiAmbangLebarModel((0.80).toFloat(), (1.190).toFloat(), (1.199).toFloat(), (1.208).toFloat())
                koefisiensiAmbangLebarDao.insert(koefisiensi)
            }
        }
    }
}