package id.ias.calculationwaterdebit.ui.fragment

import android.content.Context
import android.view.View
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.base.BaseFragment
import id.ias.calculationwaterdebit.databinding.FragmentChecklistBinding
import id.ias.calculationwaterdebit.model.KondisiModel


private const val TYPE_VIEW = "view"

class ChecklistFragment : BaseFragment() {
    lateinit var mBinding: FragmentChecklistBinding
    private lateinit var listener: ChecklistFragmentListener
    private var view: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as ChecklistFragmentListener
    }

    fun setKondisiFill(position: Int, kondisi: Int) {
        when (position) {
            0 -> {
                if (kondisi == 0) {
                    mBinding.rbYesRg1.isChecked = true
                } else {
                    mBinding.rbNoRg1.isChecked = true
                }
            }
            1 -> {
                if (kondisi == 0) {
                    mBinding.rbYesRg2.isChecked = true
                } else {
                    mBinding.rbNoRg2.isChecked = true
                }
            }
            2 -> {
                when (kondisi) {
                    0 -> mBinding.rbYesRg3.isChecked = true
                    1 -> mBinding.rbNo1Rg3.isChecked = true
                    2 -> mBinding.rbNo2Rg3.isChecked = true
                    3 -> mBinding.rbNo3Rg3.isChecked = true
                }
            }
            3 -> {
                if (kondisi == 0) {
                    mBinding.rbYesRg4.isChecked = true
                } else {
                    mBinding.rbNoRg4.isChecked = true
                }
            }
            4 -> {
                when (kondisi) {
                    0 -> mBinding.rbNo1Rg5.isChecked = true
                    1 -> mBinding.rbNo2Rg5.isChecked = true
                    2 -> mBinding.rbNo3Rg5.isChecked = true
                    3 -> mBinding.rbNo4Rg5.isChecked = true
                    4 -> mBinding.rbNo5Rg5.isChecked = true
                }
            }
        }
    }

    fun setKondisiFill(position: Int, kondisi: KondisiModel) {
        when (position) {
            0 -> {
                if (kondisi.kondisiId == 0) {
                    mBinding.rbYesRg1.isChecked = true
                } else {
                    mBinding.rbNoRg1.isChecked = true
                }
            }
            1 -> {
                if (kondisi.kondisiId == 0) {
                    mBinding.rbYesRg2.isChecked = true
                } else {
                    mBinding.rbNoRg2.isChecked = true
                }
            }
            2 -> {
                when (kondisi.kondisiId) {
                    0 -> mBinding.rbYesRg3.isChecked = true
                    1 -> mBinding.rbNo1Rg3.isChecked = true
                    2 -> mBinding.rbNo2Rg3.isChecked = true
                    3 -> mBinding.rbNo3Rg3.isChecked = true
                }
            }
            3 -> {
                if (kondisi.kondisiId == 0) {
                    mBinding.rbYesRg4.isChecked = true
                } else {
                    mBinding.rbNoRg4.isChecked = true
                }
            }
            4 -> {
                when (kondisi.kondisiId) {
                    0 -> mBinding.rbNo1Rg5.isChecked = true
                    1 -> mBinding.rbNo2Rg5.isChecked = true
                    2 -> mBinding.rbNo3Rg5.isChecked = true
                    3 -> mBinding.rbNo4Rg5.isChecked = true
                    4 -> mBinding.rbNo5Rg5.isChecked = true
                }
            }
        }
    }

    override fun initViews() {
        when (view) {
            0 -> {
                mBinding.cvRg1.visibility = View.VISIBLE
                mBinding.cvRg2.visibility = View.GONE
                mBinding.cvRg3.visibility = View.GONE
                mBinding.cvRg4.visibility = View.GONE
                mBinding.cvRg5.visibility = View.GONE

                mBinding.rg1.setOnCheckedChangeListener { _, checkedId ->
                    val kondisi = KondisiModel()
                    if (checkedId == R.id.rb_yes_rg1) {
                        kondisi.detailKondisi = "Adanya keretakan pada sayap bangunan ukur debit yang akan dikalibrasi"
                        kondisi.kondisiId = 0
                        kondisi.nilaiKondisi = 1
                    } else {
                        kondisi.detailKondisi = "Tidak Adanya keretakan pada sayap bangunan ukur debit yang akan dikalibrasi"
                        kondisi.kondisiId = 1
                        kondisi.nilaiKondisi = 0
                    }
                    listener.onChecklist(0, kondisi)
                }
            }
            1 -> {
                mBinding.cvRg1.visibility = View.GONE
                mBinding.cvRg2.visibility = View.VISIBLE
                mBinding.cvRg3.visibility = View.GONE
                mBinding.cvRg4.visibility = View.GONE
                mBinding.cvRg5.visibility = View.GONE

                mBinding.rg2.setOnCheckedChangeListener { _, checkedId ->
                    val kondisi = KondisiModel()
                    if (checkedId == R.id.rb_yes_rg2) {
                        kondisi.detailKondisi = "Adanya keretakan besar pada ambang/pengontrol/pengukur pada bangunan ukur debit yang akan dikalbrasi"
                        kondisi.kondisiId = 0
                        kondisi.nilaiKondisi = 3
                    } else {
                        kondisi.detailKondisi = "Tidak keretakan besar pada ambang/pengontrol/pengukur pada bangunan ukur debit yang akan dikalbrasi"
                        kondisi.kondisiId = 1
                        kondisi.nilaiKondisi = 0
                    }
                    listener.onChecklist(1, kondisi)
                }
            }
            2 -> {
                mBinding.cvRg1.visibility = View.GONE
                mBinding.cvRg2.visibility = View.GONE
                mBinding.cvRg3.visibility = View.VISIBLE
                mBinding.cvRg4.visibility = View.GONE
                mBinding.cvRg5.visibility = View.GONE

                mBinding.rg3.setOnCheckedChangeListener { _, checkedId ->
                    val kondisi = KondisiModel()
                    when (checkedId) {
                        R.id.rb_yes_rg3 -> {
                            kondisi.detailKondisi = "Kondisi aliran debit kecil/debit besar berfungsi dengan baik"
                            kondisi.kondisiId = 0
                            kondisi.nilaiKondisi = 0
                        }
                        R.id.rb_no1_rg3 -> {
                            kondisi.detailKondisi = "Kondisi aliran kecil tidak berfungsi dengan baik"
                            kondisi.kondisiId = 1
                            kondisi.nilaiKondisi = 0
                        }
                        R.id.rb_no2_rg3 -> {
                            kondisi.detailKondisi = "Kondisi aliran besar tidak berfungsi dengan baik"
                            kondisi.kondisiId = 2
                            kondisi.nilaiKondisi = 2
                        }
                        else -> {
                            kondisi.detailKondisi = "kedua kondisi tidak berfungsi dengan baik"
                            kondisi.kondisiId = 3
                            kondisi.nilaiKondisi = 3
                        }
                    }
                    listener.onChecklist(2, kondisi)
                }
            }
            3 -> {
                mBinding.cvRg1.visibility = View.GONE
                mBinding.cvRg2.visibility = View.GONE
                mBinding.cvRg3.visibility = View.GONE
                mBinding.cvRg4.visibility = View.VISIBLE
                mBinding.cvRg5.visibility = View.GONE

                mBinding.rg4.setOnCheckedChangeListener { _, checkedId ->
                    val kondisi = KondisiModel()
                    if (checkedId == R.id.rb_yes_rg4) {
                        kondisi.detailKondisi = "Kondisi pielschall masih dapat terlihat"
                        kondisi.kondisiId = 0
                        kondisi.nilaiKondisi = 1
                    } else {
                        kondisi.detailKondisi = "Kondisi pielschall tidak dapat terlihat"
                        kondisi.kondisiId = 1
                        kondisi.nilaiKondisi = 0
                    }
                    listener.onChecklist(3, kondisi)
                }
            }
            4 -> {
                mBinding.cvRg1.visibility = View.GONE
                mBinding.cvRg2.visibility = View.GONE
                mBinding.cvRg3.visibility = View.GONE
                mBinding.cvRg4.visibility = View.GONE
                mBinding.cvRg5.visibility = View.VISIBLE

                mBinding.rg5.setOnCheckedChangeListener { _, checkedId ->
                    val kondisi = KondisiModel()
                    when(checkedId) {
                        R.id.rb_no1_rg5 -> {
                            kondisi.detailKondisi = "Kondisi sedimen di saluran tidak ada"
                            kondisi.kondisiId = 0
                            kondisi.nilaiKondisi = 0
                        }
                        R.id.rb_no2_rg5 -> {
                            kondisi.detailKondisi = "Kondisi sedimen di saluran dengan sedikit batuan/pasir, tidak menganggu aliran"
                            kondisi.kondisiId = 1
                            kondisi.nilaiKondisi = 0
                        }
                        R.id.rb_no3_rg5 -> {
                            kondisi.detailKondisi = "Kondisi sedimen di saluran dengan sedikit tanah, tidak menganggu aliran"
                            kondisi.kondisiId = 2
                            kondisi.nilaiKondisi = 0
                        }
                        R.id.rb_no4_rg5 -> {
                            kondisi.detailKondisi = "Kondisi sedimen di saluran dengan tanah/pasir/tanah, berbentuk bongkohan"
                            kondisi.kondisiId = 3
                            kondisi.nilaiKondisi = 3
                        }
                        else -> {
                            kondisi.detailKondisi = "Kondisi sedimen di saluran dengan banyak tanah/pasir/batuan yang menutupi sebagian saluran"
                            kondisi.kondisiId = 4
                            kondisi.nilaiKondisi = 5
                        }
                    }
                    listener.onChecklist(4, kondisi)
                }
            }
        }

        listener.onViewCreated()
    }

    override fun setArguments() {
        arguments?.let {
            view = it.getInt(TYPE_VIEW)
        }
    }

    override fun createViewBinding(): View {
        mBinding = FragmentChecklistBinding.inflate(layoutInflater)
        return mBinding.root
    }

    interface ChecklistFragmentListener {
        fun onChecklist(kondisiPosition: Int, kondisiModel: KondisiModel)
        fun onViewCreated()
    }
}