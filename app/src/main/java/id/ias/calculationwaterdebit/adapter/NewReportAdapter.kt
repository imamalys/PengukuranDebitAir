package id.ias.calculationwaterdebit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.databinding.NewReportItemBinding

class NewReportAdapter(
    private val dataLists: List<BaseDataModel>,
    private val listener: Listener
): RecyclerView.Adapter<NewReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = NewReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val mBinding: NewReportItemBinding) : RecyclerView.ViewHolder(mBinding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(dataLists[position]) {
                mBinding.tvDate.text = this.tanggal
                mBinding.tvNamaSaluran.text = this.namaSaluran
                mBinding.tvDaerahIrigasi.text = this.namaDaerahIrigasi
                mBinding.tvInstansiPengelola.text = this.wilayahKewenangan
                mBinding.tvBangunanUkur.text = this.noPengukuran
                mBinding.tvTipeBangunan.text = this.tipeBangunan

                mBinding.llShow.setOnClickListener {
                    listener.onClick(this)
                }

                mBinding.llDelete.setOnClickListener {
                    listener.onDelete(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataLists.size
    }

    interface Listener {
        fun onClick(baseDataModel: BaseDataModel)

        fun onDelete(baseDataModel: BaseDataModel)
    }
}