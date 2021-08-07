package id.ias.calculationwaterdebit

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import id.ias.calculationwaterdebit.database.BaseRoomDatabase
import id.ias.calculationwaterdebit.database.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class Application: Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { BaseRoomDatabase.getDatabase(this, applicationScope) }
    val baseDataRepository by lazy { BaseDataRepository(database.baseDataDao()) }
    val alpsRepository by lazy { AmbangLebarPengontrolSegiempatRepository(database.alpsDao()) }
    val pengambilanDataRepository by lazy { PengambilanDataRepository(database.pengambilanDataDao()) }
    val piasRepository by lazy { PiasDataRepository(database.piasDao()) }
}