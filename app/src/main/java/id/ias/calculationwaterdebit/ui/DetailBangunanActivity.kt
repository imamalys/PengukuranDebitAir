package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.adapter.DetailBangunanAdapter
import id.ias.calculationwaterdebit.database.model.*
import id.ias.calculationwaterdebit.database.viewmodel.*
import id.ias.calculationwaterdebit.databinding.ActivityDetailBangunanBinding
import id.ias.calculationwaterdebit.util.MessageDialogUtil
import id.ias.calculationwaterdebit.util.LoadingDialogUtil
import id.ias.calculationwaterdebit.util.PictureDialogUtil
import id.ias.calculationwaterdebit.viewmodel.DetailBangunanUkurViewModelFactory
import id.ias.calculationwaterdebit.viewmodel.DetailBangunanViewModel

class DetailBangunanActivity : AppCompatActivity() {
    var picture = PictureDialogUtil()
    val loading = LoadingDialogUtil()
    val back = MessageDialogUtil()
    lateinit var mBinding: ActivityDetailBangunanBinding
    private val detailBangunanViewModel: DetailBangunanViewModel by viewModels {
        DetailBangunanUkurViewModelFactory()
    }
    private val alpsViewModel: AmbangLebarPengontrolSegiempatViewModel by viewModels {
        AmbangLebarPengontrolSegiempatViewModelFactory((application as Application).alpsRepository)
    }

    private val alptViewModel: AmbangLebarPengontrolTrapesiumViewModel by viewModels {
        AmbangLebarPengontrolTrapesiumViewModelFactory((application as Application).alptRepository)
    }

    private val ambangTajamSegiempatModel: AmbangTajamSegiempatViewModel by viewModels {
        AmbangTajamSegiempatViewModelFactory((application as Application).ambangTajamSegiempatRepository)
    }

    private val atsViewModel: AmbangTipisSegitigaViewModel by viewModels {
        AmbangTipisSegitigaViewModelFactory((application as Application).atsRepository)
    }

    private val cipolettiViewModel: CipolettiViewModel by viewModels {
        CipolettiViewModelFactory((application as Application).cipolettiRepository)
    }

    private val parshallFluViewModel: ParshallFlumeViewModel by viewModels {
        ParshallFlumeViewModelFactory((application as Application).parshallFlumeRepository)
    }

    private val longThroatedFlumeViewModel: LongThroatedFlumeViewModel by viewModels {
        LongThroatedFlumeViewModelFactory((application as Application).longThroatedFlumeRepository)
    }

    private val cutThroatedFlumeViewModel: CutThroaedFlumeViewModel by viewModels {
        CutThroaedFlumeViewModelFactory((application as Application).cutThroatedFlumeRepository)
    }

    private val orificeViewModel: OrificeViewModel by viewModels {
        OrificeViewModelFactory((application as Application).orificeRepository)
    }

    private val romijnViewModel: RomijnViewModel by viewModels {
        RomijnViewModelFactory((application as Application).romijnRepository)
    }

    private val crumpViewModel: CrumpViewModel by viewModels {
        CrumpViewModelFactory((application as Application).crumpRepository)
    }

    var idTipeBangunan: Long = 0
    var idBaseData: Long = 0
    var isLoad = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDetailBangunanBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent.let {
            if (it.hasExtra("tipe_bangunan")) {
                detailBangunanViewModel.detailBangunan.value = it.getStringExtra("tipe_bangunan")!!
                idBaseData = it.getLongExtra("id_base_data", 0)
            }
        }

        setAction()
        setViewModel()
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            if (detailBangunanViewModel.checkHaveValue()) {
                loading.show(this)
                when (detailBangunanViewModel.detailBangunan.value!!) {
                    "Ambang Lebar Pengontrol Segiempat" -> {
                        val alpsData = AmbangLebarPengontrolSegiempatModel(
                                if (idTipeBangunan.toInt() == 0) null else idTipeBangunan.toInt(),
                                idBaseData.toInt(),
                                detailBangunanViewModel.detailBangunanValue[0],
                                detailBangunanViewModel.detailBangunanValue[1],
                                detailBangunanViewModel.detailBangunanValue[2],
                                detailBangunanViewModel.detailBangunanValue[3],
                                detailBangunanViewModel.detailBangunanValue[4],
                                detailBangunanViewModel.detailBangunanValue[5],
                                detailBangunanViewModel.detailBangunanValue[6])
                        if (isLoad) {
                            alpsViewModel.update(alpsData)
                        } else {
                            alpsViewModel.insert(alpsData)
                        }
                    }
                    "Ambang Lebar Pengontrol Trapesium" -> {
                        val alptData = AmbangLebarPengontrolTrapesiumModel(
                                if (idTipeBangunan.toInt() == 0) null else idTipeBangunan.toInt(),
                                idBaseData.toInt(),
                                detailBangunanViewModel.detailBangunanValue[0],
                                detailBangunanViewModel.detailBangunanValue[1],
                                detailBangunanViewModel.detailBangunanValue[2],
                                detailBangunanViewModel.detailBangunanValue[3],
                                detailBangunanViewModel.detailBangunanValue[4],
                                detailBangunanViewModel.detailBangunanValue[5],
                                detailBangunanViewModel.detailBangunanValue[6],
                                detailBangunanViewModel.detailBangunanValue[7])

                        if (isLoad) {
                            alptViewModel.update(alptData)
                        } else {
                            alptViewModel.insert(alptData)
                        }
                    }
                    "Ambang Tajam Segiempat" -> {
                        val atsData = AmbangTajamSegiempatModel(
                                if (idTipeBangunan.toInt() == 0) null else idTipeBangunan.toInt(),
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue[0],
                            detailBangunanViewModel.detailBangunanValue[1],
                            detailBangunanViewModel.detailBangunanValue[2],
                        )

                        if (isLoad) {
                            ambangTajamSegiempatModel.update(atsData)
                        } else {
                            ambangTajamSegiempatModel.insert(atsData)
                        }
                    }
                    "Ambang Tajam Segitiga" -> {
                        val atsData = AmbangTipisSegitigaModel(
                                if (idTipeBangunan.toInt() == 0) null else idTipeBangunan.toInt(),
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue[0],
                            detailBangunanViewModel.detailBangunanValue[1],
                            detailBangunanViewModel.detailBangunanValue[2],
                        )

                        if (isLoad) {
                            atsViewModel.update(atsData)
                        } else {
                            atsViewModel.insert(atsData)
                        }
                    }
                    "Cipoletti" -> {
                        val cipolettiData = CipolettiModel(
                                if (idTipeBangunan.toInt() == 0) null else idTipeBangunan.toInt(),
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue[0],
                            detailBangunanViewModel.detailBangunanValue[1],
                            detailBangunanViewModel.detailBangunanValue[2],
                            detailBangunanViewModel.detailBangunanValue[3],
                            detailBangunanViewModel.detailBangunanValue[4],
                        )

                        if (isLoad) {
                            cipolettiViewModel.update(cipolettiData)
                        } else {
                            cipolettiViewModel.insert(cipolettiData)
                        }
                    }
                    "Parshall Flume" -> {
                        val parshallData = ParshallFlumeModel(
                                if (idTipeBangunan.toInt() == 0) null else idTipeBangunan.toInt(),
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue[0],
                        )

                        if (isLoad) {
                            parshallFluViewModel.update(parshallData)
                        } else {
                            parshallFluViewModel.insert(parshallData)
                        }
                    }
                    "Long Throated Flume" -> {
                        val ltfData = LongThrotedFlumeModel(
                                if (idTipeBangunan.toInt() == 0) null else idTipeBangunan.toInt(),
                                idBaseData.toInt(),
                                detailBangunanViewModel.detailBangunanValue[0],
                                detailBangunanViewModel.detailBangunanValue[1],
                                detailBangunanViewModel.detailBangunanValue[2],
                                detailBangunanViewModel.detailBangunanValue[3],
                                detailBangunanViewModel.detailBangunanValue[4],
                                detailBangunanViewModel.detailBangunanValue[5],
                                detailBangunanViewModel.detailBangunanValue[6])

                        if (isLoad) {
                            longThroatedFlumeViewModel.update(ltfData)
                        } else {
                            longThroatedFlumeViewModel.insert(ltfData)
                        }
                    }
                    "Cut Throated Flume" -> {
                        val ctfData = CutThroatedFlumeModel(
                                if (idTipeBangunan.toInt() == 0) null else idTipeBangunan.toInt(),
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue[0],
                            detailBangunanViewModel.detailBangunanValue[1]
                        )

                        if (isLoad) {
                            cutThroatedFlumeViewModel.update(ctfData)
                        } else {
                            cutThroatedFlumeViewModel.insert(ctfData)
                        }
                    }
                    "Orifice" -> {
                        val orificeData = OrificeModel(
                                if (idTipeBangunan.toInt() == 0) null else idTipeBangunan.toInt(),
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue[0],
                            detailBangunanViewModel.detailBangunanValue[1],
                        )

                        if (isLoad) {
                            orificeViewModel.update(orificeData)
                        } else {
                            orificeViewModel.insert(orificeData)
                        }
                    }
                    "Romijn" -> {
                        val romijnData = RomijnModel(
                                if (idTipeBangunan.toInt() == 0) null else idTipeBangunan.toInt(),
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue[0],
                            detailBangunanViewModel.detailBangunanValue[1],
                            detailBangunanViewModel.detailBangunanValue[2],
                            detailBangunanViewModel.detailBangunanValue[3],
                            detailBangunanViewModel.detailBangunanValue[4],
                        )

                        if (isLoad) {
                            romijnViewModel.update(romijnData)
                        } else {
                            romijnViewModel.insert(romijnData)
                        }
                    }
                    "Crump- De Gyuter" -> {
                        val crumpData = CrumpModel(
                                if (idTipeBangunan.toInt() == 0) null else idTipeBangunan.toInt(),
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue[0],
                            detailBangunanViewModel.detailBangunanValue[1],
                        )

                        if (isLoad) {
                            crumpViewModel.update(crumpData)
                        } else {
                            crumpViewModel.insert(crumpData)
                        }
                    }
                }
            } else {
                ToastUtils.showLong("Data masih ada yang kosong, silahkan diisi terlebih dahulu")
            }
        }

        mBinding.ivImageBangunan.setOnClickListener {
            picture.show(this, detailBangunanViewModel.getImage(detailBangunanViewModel.detailBangunan.value!!))
        }
    }

    private fun setViewModel() {
        detailBangunanViewModel.detailBangunan.observe(this) {
            mBinding.ivImageBangunan.setImageDrawable(theme.getDrawable(detailBangunanViewModel.getImage(it)))
            mBinding.tvTipeBangunan.text = it
            mBinding.rvDetailBangunan.layoutManager = LinearLayoutManager(this)
            val adapter = DetailBangunanAdapter(
                this, detailBangunanViewModel.getDetailBangunan(it),
                object: DetailBangunanAdapter.Listener {
                    override fun onChanged(value: String, position: Int) {
                        if (value != "" && value != ".") {
                            detailBangunanViewModel.detailBangunanValue[position - 1] = value.toFloat()
                        } else {
                            detailBangunanViewModel.detailBangunanValue[position - 1] = "0".toFloat()
                        }
                    }
                }
            )
            mBinding.rvDetailBangunan.adapter = adapter
        }

        when(detailBangunanViewModel.detailBangunan.value) {
            "Ambang Lebar Pengontrol Segiempat" -> {
                alpsViewModel.getalpsDataByIdBaseData(idBaseData.toInt())
                alpsViewModel.alpsById.observe(this, {
                    if (it != null) {
                        idTipeBangunan = it.id!!.toLong()
                        val value = FloatArray(7)
                        value[0] = it.lebarAmbang
                        value[1] = it.lebarDasar
                        value[2] = it.panjangAmbang
                        value[3] = it.tinggiAmbang
                        value[4] = it.tinggiDiatasAmbang
                        value[5] = it.tinggiDibawahAmbang
                        value[6] = it.lebarAtas

                        isLoad = true
                        setDetailBangunanLoad(value)
                    }
                })
            }
            "Ambang Lebar Pengontrol Trapesium" -> {
                alptViewModel.getAlptDataByIdBaseData(idBaseData.toInt())
                alptViewModel.alptById.observe(this, {
                    if (it != null) {
                        idTipeBangunan = it.id!!.toLong()
                        val value = FloatArray(8)
                        value[0] = it.lebarAmbang
                        value[1] = it.lebarDasar
                        value[2] = it.panjangAmbang
                        value[3] = it.tinggiAmbang
                        value[4] = it.tinggiDiatasAmbang
                        value[5] = it.tinggiDibawahAmbang
                        value[6] = it.lebarAtas
                        value[6] = it.kemiringanPengontrol

                        isLoad = true
                        setDetailBangunanLoad(value)
                    }
                })
            }
            "Ambang Tajam Segiempat" -> {
                ambangTajamSegiempatModel.getAtsDataByIdBaseData(idBaseData.toInt())
                ambangTajamSegiempatModel.atsById.observe(this, {
                    if (it != null) {
                        idTipeBangunan = it.id!!.toLong()
                        val value = FloatArray(3)
                        value[0] = it.lebarSaluran
                        value[1] = it.lebarMercu
                        value[2] = it.tinggiMercuDiatasAmbang

                        isLoad = true
                        setDetailBangunanLoad(value)
                    }
                })
            }
            "Ambang Tajam Segitiga" -> {
                atsViewModel.getAtsDataByIdBaseData(idBaseData.toInt())
                atsViewModel.atsById.observe(this, {
                    if (it != null) {
                        idTipeBangunan = it.id!!.toLong()
                        val value = FloatArray(3)
                        value[0] = it.lebarSaluran
                        value[1] = it.sudutCelahMercu
                        value[2] = it.tinggiMercuDiatasAmbang

                        isLoad = true
                        setDetailBangunanLoad(value)
                    }
                })
            }
            "Cipoletti" -> {
                cipolettiViewModel.getCipolettiDataByIdBaseData(idBaseData.toInt())
                cipolettiViewModel.cipolettiById.observe(this, {
                    if (it != null) {
                        idTipeBangunan = it.id!!.toLong()
                        val value = FloatArray(5)
                        value[0] = it.lebarPengukur
                        value[1] = it.lebarDasar
                        value[2] = it.tinggiMercuDiatasAmbang
                        value[3] = it.tinggiMercu
                        value[4] = it.lebarAtas

                        isLoad = true
                        setDetailBangunanLoad(value)
                    }
                })
            }
            "Parshall Flume" -> {
                parshallFluViewModel.getParshallFlumeDataByIdBaseData(idBaseData.toInt())
                parshallFluViewModel.parshallFlumeById.observe(this, {
                    if (it != null) {
                        idTipeBangunan = it.id!!.toLong()
                        val value = FloatArray(1)
                        value[0] = it.lebarTenggorokan

                        isLoad = true
                        setDetailBangunanLoad(value)
                    }
                })
            }
            "Long Throated Flume" -> {
                longThroatedFlumeViewModel.getLtfDataByIdBaseData(idBaseData.toInt())
                longThroatedFlumeViewModel.ltfById.observe(this, {
                    if (it != null) {
                        idTipeBangunan = it.id!!.toLong()
                        val value = FloatArray(7)
                        value[0] = it.lebarAmbang
                        value[1] = it.lebarDasar
                        value[2] = it.panjangAmbang
                        value[3] = it.tinggiAmbang
                        value[4] = it.tinggiDiatasAmbang
                        value[5] = it.tinggiDibawahAmbang
                        value[6] = it.lebarAtas

                        isLoad = true
                        setDetailBangunanLoad(value)
                    }
                })
            }
            "Cut Throated Flume" -> {
                cutThroatedFlumeViewModel.getCtfDataByIdBaseData(idBaseData.toInt())
                cutThroatedFlumeViewModel.ctfById.observe(this, {
                    if (it != null) {
                        idTipeBangunan = it.id!!.toLong()
                        val value = FloatArray(2)
                        value[0] = it.w
                        value[1] = it.l

                        isLoad = true
                        setDetailBangunanLoad(value)
                    }
                })
            }
            "Orifice" -> {
                orificeViewModel.getOrificeDataByIdBaseData(idBaseData.toInt())
                orificeViewModel.orificeById.observe(this, {
                    if (it != null) {
                        idTipeBangunan = it.id!!.toLong()
                        val value = FloatArray(2)
                        value[0] = it.lebarLubang
                        value[1] = it.tinggiLubang

                        isLoad = true
                        setDetailBangunanLoad(value)
                    }
                })
            }
            "Romijn" -> {
                romijnViewModel.getRomijnDataByIdBaseData(idBaseData.toInt())
                romijnViewModel.romijnById.observe(this, {
                    if (it != null) {
                        idTipeBangunan = it.id!!.toLong()
                        val value = FloatArray(5)
                        value[0] = it.lebarMeja
                        value[1] = it.lebarDasar
                        value[2] = it.panjangMeja
                        value[3] = it.tinggiMejaDariDasar
                        value[4] = it.tinggiDiatasMeja

                        isLoad = true
                        setDetailBangunanLoad(value)
                    }
                })
            }
            "Crump- De Gyuter" -> {
                crumpViewModel.getCrumpDataByIdBaseData(idBaseData.toInt())
                crumpViewModel.crumpById.observe(this, {
                    if (it != null) {
                        idTipeBangunan = it.id!!.toLong()
                        val value = FloatArray(3)
                        value[0] = it.bC
                        value[1] = it.w

                        isLoad = true
                        setDetailBangunanLoad(value)
                    }
                })
            }
        }

        alpsViewModel.idTipeBangunan.observe(this, {
            if (it.toInt() != 0) {
                goToVariasiKetinggianAirActivity(it)
            }
        })

        alptViewModel.idTipeBangunan.observe(this, {
            if (it.toInt() != 0) {
                goToVariasiKetinggianAirActivity(it)
            }
        })

        ambangTajamSegiempatModel.idTipeBangunan.observe(this, {
            if (it.toInt() != 0) {
                goToVariasiKetinggianAirActivity(it)
            }
        })

        atsViewModel.idTipeBangunan.observe(this, {
            if (it.toInt() != 0) {
                goToVariasiKetinggianAirActivity(it)
            }
        })

        cipolettiViewModel.idTipeBangunan.observe(this, {
            if (it.toInt() != 0) {
                goToVariasiKetinggianAirActivity(it)
            }
        })

        orificeViewModel.idTipeBangunan.observe(this, {
            if (it.toInt() != 0) {
                goToVariasiKetinggianAirActivity(it)
            }
        })

        parshallFluViewModel.idTipeBangunan.observe(this, {
            if (it.toInt() != 0) {
               goToVariasiKetinggianAirActivity(it)
            }
        })

        longThroatedFlumeViewModel.idTipeBangunan.observe(this, {
            if (it.toInt() != 0) {
                goToVariasiKetinggianAirActivity(it)
            }
        })

        cutThroatedFlumeViewModel.idTipeBangunan.observe(this, {
            if (it.toInt() != 0) {
                goToVariasiKetinggianAirActivity(it)
            }
        })

        romijnViewModel.idTipeBangunan.observe(this, {
            if (it.toInt() != 0) {
                goToVariasiKetinggianAirActivity(it)
            }
        })

        crumpViewModel.idTipeBangunan.observe(this, {
            if (it.toInt() != 0) {
                goToVariasiKetinggianAirActivity(it)
            }
        })
    }

    private fun setDetailBangunanLoad(value: FloatArray) {
        mBinding.rvDetailBangunan.layoutManager = LinearLayoutManager(this)
        val adapter = DetailBangunanAdapter(
                this, detailBangunanViewModel.getDetailBangunanLoad(
                detailBangunanViewModel.detailBangunan.value!!, value),
                object: DetailBangunanAdapter.Listener {
                    override fun onChanged(value: String, position: Int) {
                        if (value != "" && value != ".") {
                            detailBangunanViewModel.detailBangunanValue[position - 1] = value.toFloat()
                        } else {
                            detailBangunanViewModel.detailBangunanValue[position - 1] = "0".toFloat()
                        }
                    }
                }
        )
        mBinding.rvDetailBangunan.adapter = adapter
    }

    private fun goToVariasiKetinggianAirActivity(it: Long) {
        if (isLoad) {
            val intent = Intent(this@DetailBangunanActivity, PengambilanDataActivity::class.java)
            intent.putExtra("id_base_data", idBaseData)
            intent.putExtra("id_tipe_bangunan", idTipeBangunan)
            intent.putExtra("tipe_bangunan", detailBangunanViewModel.detailBangunan.value)
            startActivity(intent)
            finish()
        } else {
            loading.dialog.dismiss()
            val intent = Intent(this@DetailBangunanActivity, VariasiKetinggianAirActivity::class.java)
            intent.putExtra("id_tipe_bangunan", it)
            intent.putExtra("tipe_bangunan", detailBangunanViewModel.detailBangunan.value)
            intent.putExtra("id_base_data", idBaseData)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        back.show(this, title = "Apakah anda yakin ingin kembali ke menu sebelumnya",
                yes = "Ya", no = "Tidak", object: MessageDialogUtil.DialogListener {
            override fun onYes(action: Boolean) {
                if (action) {
                    finish()
                }
            }
        })
    }
}