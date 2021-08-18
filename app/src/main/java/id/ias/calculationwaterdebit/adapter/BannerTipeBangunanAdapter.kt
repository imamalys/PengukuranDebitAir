package id.ias.calculationwaterdebit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.databinding.BannerTipeBangunanItemBinding

class BannerTipeBangunanAdapter(
    private val mContext: Context) : RecyclerView.Adapter<BannerTipeBangunanAdapter.ViewHolder>() {

    val images: Array<Int> = arrayOf(R.drawable.slide_1, R.drawable.slide_2, R.drawable.slide_3, R.drawable.slide_4, R.drawable.slide_5)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = BannerTipeBangunanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val mBinding: BannerTipeBangunanItemBinding) : RecyclerView.ViewHolder(mBinding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(images[position]) {
                Glide.with(mContext)
                    .load(this)
                    .into(mBinding.ivImage)
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}