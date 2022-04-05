package id.ias.calculationwaterdebit.ui

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.aceinteract.android.stepper.StepperNavListener
import com.aceinteract.android.stepper.StepperNavigationView
import com.blankj.utilcode.util.ToastUtils
import id.ias.calculationwaterdebit.Application
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.base.BaseActivity
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModel
import id.ias.calculationwaterdebit.database.viewmodel.BaseDataViewModelFactory
import id.ias.calculationwaterdebit.databinding.ActivityCheckKondisiBinding
import id.ias.calculationwaterdebit.model.KondisiModel
import id.ias.calculationwaterdebit.ui.fragment.ChecklistFragment
import id.ias.calculationwaterdebit.util.MessageDialogUtil

class CheckKondisiActivity : BaseActivity(), StepperNavListener, ChecklistFragment.ChecklistFragmentListener {
    val back = MessageDialogUtil()

    private lateinit var mBinding: ActivityCheckKondisiBinding

    private val baseDataViewModel: BaseDataViewModel by viewModels {
        BaseDataViewModelFactory((application as Application).baseDataRepository)
    }

    var idBaseData: Long = 0
    var pertamaInt: Int = 0
    var keduaInt: Int = 0
    var ketigaInt: Int = 0
    var keempatInt: Int = 0
    var kelimaInt: Int = 0
    var tipeBangunan = ""
    var kondisiArray: ArrayList<KondisiModel> = ArrayList()
    private lateinit var kondisiList: IntArray

    override fun initViews() {
        intent.let {
            if (it.hasExtra("id_base_data")) {
                idBaseData = it.getLongExtra("id_base_data", 0)
            }
        }

        mBinding.stepper.initializeStepper()

        setViewModel()
        setAction()
    }

    override fun createViewBinding(): View {
        mBinding = ActivityCheckKondisiBinding.inflate(layoutInflater)
        return mBinding.root
    }

    private fun checkKondisiFill(): Boolean {
        when (mBinding.stepper.currentStep) {
            0 -> return kondisiArray.size > 0
            1 -> return  kondisiArray.size > 1
            2 -> return  kondisiArray.size > 2
            3 -> return  kondisiArray.size > 3
            4 -> return  kondisiArray.size > 4
        }

        return false
    }

    private fun StepperNavigationView.initializeStepper() {
        stepperNavListener = this@CheckKondisiActivity
        setupWithNavController(findNavController(R.id.fragment_steper))
    }

    private fun setAction() {
        mBinding.btnNext.setOnClickListener {
            if (checkKondisiFill()) {
                if (mBinding.stepper.currentStep != 4) {
                    mBinding.stepper.goToNextStep()
                } else {
                    loadingShow()
                    val result = "Keterangan:" +
                            "\n1.${kondisiArray[0].detailKondisi}" +
                            "\n2.${kondisiArray[1].detailKondisi}" +
                            "\n3.${kondisiArray[2].detailKondisi}" +
                            "\n4.${kondisiArray[3].detailKondisi}" +
                            "\n5.${kondisiArray[4].detailKondisi}"
                    val nilai = kondisiArray[0].nilaiKondisi +
                            kondisiArray[1].nilaiKondisi +
                            kondisiArray[2].nilaiKondisi +
                            kondisiArray[3].nilaiKondisi +
                            kondisiArray[4].nilaiKondisi

                    if (nilai >= 5) {
                        back.show(this, "Kondisi Kurang Baik\nTidak dianjurkan melakukan kalibrasi",
                            "Lanjutkan", "Tidak", object: MessageDialogUtil.DialogListener {
                                override fun onYes(action: Boolean) {
                                    if (action) {
                                        baseDataViewModel.updateCheckKondisi(idBaseData.toInt(), result, nilai,
                                            kondisiArray[0].kondisiId, kondisiArray[1].kondisiId,
                                            kondisiArray[2].kondisiId,  kondisiArray[3].kondisiId,
                                            kondisiArray[4].kondisiId)
                                    } else {
                                        finish()
                                    }
                                }
                            })
                    } else {
                        baseDataViewModel.updateCheckKondisi(idBaseData.toInt(), result, nilai,
                            kondisiArray[0].kondisiId, kondisiArray[1].kondisiId,
                            kondisiArray[2].kondisiId,  kondisiArray[3].kondisiId,
                            kondisiArray[4].kondisiId)
                    }
                }
            } else {
                ToastUtils.showLong("Silahkan pilih kondisi terlebih dahulu")
            }
        }

        mBinding.btnBack.setOnClickListener {
            mBinding.stepper.goToPreviousStep()
        }
    }

    private fun setViewModel() {
        baseDataViewModel.getBaseDataById(idBaseData.toInt())
        baseDataViewModel.baseDataById.observe(this, {
            if (it != null) {
                if (it.tipeBangunan != null) {
                    tipeBangunan = it.tipeBangunan!!
                }
                kondisiList = IntArray(5)
                kondisiList[0] = it.pertama ?: 0
                kondisiList[1] = it.kedua ?: 0
                kondisiList[2] = it.ketiga ?: 0
                kondisiList[3] = it.keempat ?: 0
                kondisiList[4] = it.kelima ?: 0

                onCheckFillData()
            }
        })
        baseDataViewModel.baseDataUpdate.observe(this, {
            if (it != 0) {
                if (tipeBangunan != "") {
                    loadingDismiss()
                    val intent = Intent(this@CheckKondisiActivity, DetailBangunanActivity::class.java)
                    intent.putExtra("id_base_data", idBaseData)
                    intent.putExtra("tipe_bangunan", tipeBangunan)
                    startActivity(intent)
                } else {
                    loadingDismiss()
                    val intent = Intent(this@CheckKondisiActivity, TipeBangunanUkurActivity::class.java)
                    intent.putExtra("id_base_data", idBaseData)
                    startActivity(intent)
                }
            }
        })
    }

    override fun onBackPressed() {
        if (mBinding.stepper.currentStep != 0) {
            findNavController(R.id.fragment_steper).navigateUp()
        } else {
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

    override fun onCompleted() {

    }

    override fun onStepChanged(step: Int) {
        if (step > 2) {
            mBinding.stepper.pageScroll(View.FOCUS_RIGHT)
        } else {
            mBinding.stepper.pageScroll(View.FOCUS_LEFT)
        }
        if (step != 0) {
            mBinding.btnBack.visibility = View.VISIBLE
        } else {
            mBinding.btnBack.visibility = View.GONE
        }
    }

    private fun getFragment(): Fragment {
        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.fragment_steper)
        return (navHostFragment?.childFragmentManager?.fragments?.get(0))!!
    }

    override fun onChecklist(kondisiPosition: Int, kondisiModel: KondisiModel) {
        when (kondisiPosition) {
            0 -> {
                if (kondisiArray.size == 0) {
                    kondisiArray.add(kondisiModel)
                } else {
                    kondisiArray[0] = kondisiModel
                }
            }
            1 -> {
                if (kondisiArray.size == 1) {
                    kondisiArray.add(kondisiModel)
                } else {
                    kondisiArray[1] = kondisiModel
                }
            }
            2 -> {
                if (kondisiArray.size == 2) {
                    kondisiArray.add(kondisiModel)
                } else {
                    kondisiArray[2] = kondisiModel
                }
            }
            3 -> {
                if (kondisiArray.size == 3) {
                    kondisiArray.add(kondisiModel)
                } else {
                    kondisiArray[3] = kondisiModel
                }
            }
            4 -> {
                if (kondisiArray.size == 4) {
                    kondisiArray.add(kondisiModel)
                } else {
                    kondisiArray[4] = kondisiModel
                }
            }
        }
    }

    override fun onViewCreated() {
        onCheckFillData()
    }

    private fun onCheckFillData() {
        if (::mBinding.isInitialized) {
            if (::kondisiList.isInitialized) {
                val fragment = getFragment() as ChecklistFragment
                fragment.setKondisiFill(mBinding.stepper.currentStep, kondisiList[mBinding.stepper.currentStep])
            } else {
                (kondisiArray.size > 0).let {
                    if (mBinding.stepper.currentStep == kondisiArray.size - 1) {
                        val fragment = getFragment() as ChecklistFragment
                        fragment.setKondisiFill(mBinding.stepper.currentStep, kondisiArray[mBinding.stepper.currentStep])
                    }
                }
            }
        }
    }
}