package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.os.Bundle
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
                                null,
                                idBaseData.toInt(),
                                detailBangunanViewModel.detailBangunanValue.value!![0],
                                detailBangunanViewModel.detailBangunanValue.value!![1],
                                detailBangunanViewModel.detailBangunanValue.value!![2],
                                detailBangunanViewModel.detailBangunanValue.value!![3],
                                detailBangunanViewModel.detailBangunanValue.value!![4],
                                detailBangunanViewModel.detailBangunanValue.value!![5],
                                detailBangunanViewModel.detailBangunanValue.value!![6])
                        alpsViewModel.insert(alpsData)
                    }
                    "Ambang Lebar Pengontrol Trapesium" -> {
                        val alptData = AmbangLebarPengontrolTrapesiumModel(
                                null,
                                idBaseData.toInt(),
                                detailBangunanViewModel.detailBangunanValue.value!![0],
                                detailBangunanViewModel.detailBangunanValue.value!![1],
                                detailBangunanViewModel.detailBangunanValue.value!![2],
                                detailBangunanViewModel.detailBangunanValue.value!![3],
                                detailBangunanViewModel.detailBangunanValue.value!![4],
                                detailBangunanViewModel.detailBangunanValue.value!![5],
                                detailBangunanViewModel.detailBangunanValue.value!![6],
                                detailBangunanViewModel.detailBangunanValue.value!![7])
                        alptViewModel.insert(alptData)
                    }
                    "Ambang Tajam Segitiga" -> {
                        val atsData = AmbangTipisSegitigaModel(
                            null,
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue.value!![0],
                            detailBangunanViewModel.detailBangunanValue.value!![1],
                            detailBangunanViewModel.detailBangunanValue.value!![2],
                        )
                        atsViewModel.insert(atsData)
                    }
                    "Cipoletti" -> {
                        val cipolettiData = CipolettiModel(
                            null,
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue.value!![0],
                            detailBangunanViewModel.detailBangunanValue.value!![1],
                            detailBangunanViewModel.detailBangunanValue.value!![2],
                            detailBangunanViewModel.detailBangunanValue.value!![3],
                            detailBangunanViewModel.detailBangunanValue.value!![4],
                        )
                        cipolettiViewModel.insert(cipolettiData)
                    }
                    "Parshall Flume" -> {
                        val parshallData = ParshallFlumeModel(
                            null,
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue.value!![0],
                        )
                        parshallFluViewModel.insert(parshallData)
                    }
                    "Long Throated Flume" -> {
                        val ltfData = LongThrotedFlumeModel(
                                null,
                                idBaseData.toInt(),
                                detailBangunanViewModel.detailBangunanValue.value!![0],
                                detailBangunanViewModel.detailBangunanValue.value!![1],
                                detailBangunanViewModel.detailBangunanValue.value!![2],
                                detailBangunanViewModel.detailBangunanValue.value!![3],
                                detailBangunanViewModel.detailBangunanValue.value!![4],
                                detailBangunanViewModel.detailBangunanValue.value!![5],
                                detailBangunanViewModel.detailBangunanValue.value!![6])
                        longThroatedFlumeViewModel.insert(ltfData)
                    }
                    "Cut Throated Flume" -> {
                        val ctfData = CutThroatedFlumeModel(
                            null,
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue.value!![0],
                            detailBangunanViewModel.detailBangunanValue.value!![1]
                        )
                        cutThroatedFlumeViewModel.insert(ctfData)
                    }
                    "Orifice" -> {
                        val orificeData = OrificeModel(
                            null,
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue.value!![0],
                            detailBangunanViewModel.detailBangunanValue.value!![1],
                        )
                        orificeViewModel.insert(orificeData)
                    }
                    "Romijn" -> {
                        val romijnData = RomijnModel(
                            null,
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue.value!![0],
                            detailBangunanViewModel.detailBangunanValue.value!![1],
                            detailBangunanViewModel.detailBangunanValue.value!![2],
                            detailBangunanViewModel.detailBangunanValue.value!![3],
                            detailBangunanViewModel.detailBangunanValue.value!![4],
                        )
                        romijnViewModel.insert(romijnData)
                    }
                    "Crump- De Gyuter" -> {
                        val crumpData = CrumpModel(
                            null,
                            idBaseData.toInt(),
                            detailBangunanViewModel.detailBangunanValue.value!![0],
                            detailBangunanViewModel.detailBangunanValue.value!![1],
                        )
                        crumpViewModel.insert(crumpData)
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
                            detailBangunanViewModel.detailBangunanValue.value!![position - 1] = value.toFloat()
                        } else {
                            detailBangunanViewModel.detailBangunanValue.value!![position - 1] = "0".toFloat()
                        }
                    }
                }
            )
            mBinding.rvDetailBangunan.adapter = adapter
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

    private fun goToVariasiKetinggianAirActivity(it: Long) {
        loading.dialog.dismiss()
        val intent = Intent(this@DetailBangunanActivity, VariasiKetinggianAirActivity::class.java)
        intent.putExtra("id_tipe_bangunan", it)
        intent.putExtra("tipe_bangunan", detailBangunanViewModel.detailBangunan.value)
        intent.putExtra("id_base_data", idBaseData)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        back.show(this, object: MessageDialogUtil.DialogListener {
            override fun onYes(action: Boolean) {
                if (action) {
                    finish()
                }
            }
        })
    }
}