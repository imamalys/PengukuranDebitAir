package id.ias.calculationwaterdebit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ias.calculationwaterdebit.database.model.BaseDataModel
import id.ias.calculationwaterdebit.databinding.RiwayatDataItemBinding

class RiwayatDataAdapter(
    private val dataLists: List<BaseDataModel>): RecyclerView.Adapter<RiwayatDataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RiwayatDataItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val mBinding: RiwayatDataItemBinding) : RecyclerView.ViewHolder(mBinding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(dataLists[position]) {
                mBinding.tvDate.text = this.tanggal
                mBinding.tvNamaSaluran.text = this.namaSaluran
                mBinding.tvDaerahIrigasi.text = this.namaDaerahIrigasi
                mBinding.tvInstansiPengelola.text = this.wilayahKewenangan
                mBinding.tvBangunanUkur.text = this.noPengukuran
                mBinding.tvTipeBangunan.text = this.tipeBangunan
            }
        }
    }

    override fun getItemCount(): Int {
        return dataLists.size
    }
}