package id.ias.calculationwaterdebit.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ias.calculationwaterdebit.databinding.KecepatanAirItemBinding
import id.ias.calculationwaterdebit.model.KecepatanAirModel

class KecepatanAirAdapter(
        private val mContext: Context,
        private val dataLists: ArrayList<KecepatanAirModel>,
        private val metodePengambilan: Int,
        private val listener: KecepatanAirAdapter.Listener): RecyclerView.Adapter<KecepatanAirAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val binding = KecepatanAirItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val mBinding: KecepatanAirItemBinding) : RecyclerView.ViewHolder(mBinding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(dataLists[position]) {
                when(position) {
                    0 -> {
                        mBinding.d8.setText("0.8D")
                        mBinding.d8.isFocusable = false
                        mBinding.d6.setText("0.6D")
                        mBinding.d6.isFocusable = false
                        mBinding.d2.setText("0.2D")
                        mBinding.d2.isFocusable = false
                    }
                    1 -> {
                        mBinding.d8.setText(this.d8)
                        mBinding.d8.isFocusable = false
                        mBinding.d6.setText(this.d6)
                        mBinding.d6.isFocusable = false
                        mBinding.d2.setText(this.d2)
                        mBinding.d2.isFocusable = false
                    }
                    2 -> {
                        mBinding.d8.hint = this.d8
                        mBinding.d8.isFocusable = false
                        mBinding.d6.hint = this.d6
                        mBinding.d6.isFocusable = false
                        mBinding.d2.hint = this.d2
                        mBinding.d2.isFocusable = false
                    }
                }

                if (position == 2) {
                    when(metodePengambilan) {
                        0 -> {
                            mBinding.d6.isFocusable = true
                            mBinding.d8.isFocusable = false
                            mBinding.d2.isFocusable = false
                        }
                        1 -> {
                            mBinding.d2.isFocusable = true
                            mBinding.d8.isFocusable = true
                            mBinding.d6.isFocusable = false
                        }
                        2 -> {
                            mBinding.d6.isFocusable = true
                            mBinding.d2.isFocusable = true
                            mBinding.d8.isFocusable = true
                        }
                    }

                    mBinding.d2.addTextChangedListener(object : TextWatcher {
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
                            val value = FloatArray(3)
                            value[0] = mBinding.d8.text.toString().toFloat()
                            value[1] = mBinding.d6.text.toString().toFloat()
                            value[2] = if (s.toString() == "")  "0".toFloat() else s.toString().toFloat()
                            listener.onChanged(value)
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
        fun onChanged(value: FloatArray)
    }
}