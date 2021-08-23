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
    val alptRepository by lazy { AmbangLebarPengontrolTrapesiumRepository(database.alptDao()) }
    val atsRepository by lazy { AmbangTipisSegitigaRepository(database.atsDao()) }
    val cipolettiRepository by lazy { CipolettiRepository(database.cipolettiDao()) }
    val orificeRepository by lazy { OrificeRepository(database.orificeDao()) }
    val parshallFlumeRepository by lazy { ParshallFlumeRepository(database.parshallFlumeDao()) }
    val longThroatedFlumeRepository by lazy { LongThroatedFlumeRepository(database.longThroatedFlumeDao()) }
    val cutThroatedFlumeRepository by lazy { CutThroatedFlumeRepository(database.cutThroatedFlumeDao()) }
    val romijnRepository by lazy { RomijnRepository(database.romijnDao()) }
    val crumpRepository by lazy { CrumpRepository(database.crumpDao()) }
    val pengambilanDataRepository by lazy { PengambilanDataRepository(database.pengambilanDataDao()) }
    val piasRepository by lazy { PiasDataRepository(database.piasDao()) }
    val koefisiensiAmbangLebarRepository by lazy { KoefiensiAmbangLebarRepository(database.koefisiensiDao()) }
    val koefisiensiAliranSempurnaRepository by lazy { KoefisiensiAliranSempurnaRepository(database.koefisiensiAliranSempurnaDao()) }
    val ambangTipisSegitigaSudutRepository by lazy { AmbangTipisSegitigaSudutRepository(database.ambangTipisSegitigaSudutDao()) }
    val koefisiensiAmbangTipisSegitigaRepository by lazy { KoefisiensiAmbangTipisSegitigaRepository(database.koefisiensiAmbangTipisSegitiga()) }
    val koefisiensiCutThroatedFlumeRepository by lazy { KoefisiensiCutThroatedFlumeRepository(database.koefisiensiCutThroatedFlume()) }
}