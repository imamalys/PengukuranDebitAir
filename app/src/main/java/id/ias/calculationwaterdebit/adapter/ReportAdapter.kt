package id.ias.calculationwaterdebit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.databinding.ReportItemBinding

class ReportAdapter (private val mContext: Context,
private val dataLists: List<BaseDataModel>,
private val listener: Listener): RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val mBinding: ReportItemBinding) : RecyclerView.ViewHolder(mBinding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(dataLists[position]) {
                mBinding.etNamaDaerah.setText(this.namaDaerahIrigasi)
                mBinding.etWilayahKewenangan.setText(this.wilayahKewenangan)
                mBinding.etTipeBangunan.setText(this.tipeBangunan)

                mBinding.root.setOnClickListener {
                    listener.onClick(this)
                }

                mBinding.etNamaDaerah.setOnClickListener {
                    listener.onClick(this)
                }

                mBinding.etWilayahKewenangan.setOnClickListener {
                    listener.onClick(this)
                }

                mBinding.etTipeBangunan.setOnClickListener {
                    listener.onClick(this)
                }

                mBinding.cvMain.setOnClickListener {
                    listener.onClick(this)
                }

                mBinding.llMain.setOnClickListener {
                    listener.onClick(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataLists.size
    }

    interface Listener {
        fun onClick(baseDataModel: BaseDataModel)
    }
}