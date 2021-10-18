package id.ias.calculationwaterdebit.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.databinding.DetailBangunanItemBinding
import id.ias.calculationwaterdebit.model.DetailBangunanModel

class DetailBangunanAdapter(
    private val mContext: Context,
    private val dataLists: ArrayList<DetailBangunanModel>,
    private val listener: Listener): RecyclerView.Adapter<DetailBangunanAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = DetailBangunanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val mBinding: DetailBangunanItemBinding) : RecyclerView.ViewHolder(mBinding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(dataLists[position]) {
                if (position == 0) {
                    mBinding.tvNotasi.text = "Notasi"
                    mBinding.tvInput.setText("")
                    mBinding.tvInput.isFocusable = false
                    mBinding.tvSatuan.text = "Satuan"
                    mBinding.tvKeterangan.text = "Keterangan"
                    mBinding.tvInput.setBackgroundResource(0)
                } else {
                    mBinding.tvInput.setBackgroundResource(R.drawable.edit_text_border)
                    mBinding.tvNotasi.text = this.notasi
                    if (this.input != "0") {
                        mBinding.tvInput.setText(this.input)
                    } else {
                        mBinding.tvInput.hint = this.input
                    }
                    mBinding.tvInput.isFocusable = true
                    mBinding.tvSatuan.text = "m"
                    mBinding.tvKeterangan.text = this.keterangan

                    mBinding.tvInput.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            listener.onChanged(s.toString(), position)
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }

                    })
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataLists.size
    }

    interface Listener {
        fun onChanged(value: String, position: Int)
    }
}