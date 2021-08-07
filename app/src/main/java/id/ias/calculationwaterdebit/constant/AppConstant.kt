package id.ias.calculationwaterdebit.constant

import com.blankj.utilcode.util.SPUtils

class AppConstant {
    companion object {
        private const val IS_INSTALLED = "is_installed"

        fun setInstalled() {
            SPUtils.getInstance().put(IS_INSTALLED, true)
        }

        fun checkIsInstalled(): Boolean {
            return SPUtils.getInstance().getBoolean(IS_INSTALLED)
        }
    }
}