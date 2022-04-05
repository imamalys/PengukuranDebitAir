package id.ias.calculationwaterdebit.ui.fragment

import android.view.View
import com.bumptech.glide.Glide
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.base.BaseFragment
import id.ias.calculationwaterdebit.databinding.FragmentAboutBinding

class AboutFragment : BaseFragment() {
    private lateinit var mBinding: FragmentAboutBinding
    override fun initViews() {
        Glide.with(this)
            .load(R.drawable.logo_app)
            .circleCrop()
            .into(mBinding.ivTopLeft)
    }

    override fun setArguments() {

    }

    override fun createViewBinding(): View {
        mBinding = FragmentAboutBinding.inflate(layoutInflater)
        return mBinding.root
    }
}