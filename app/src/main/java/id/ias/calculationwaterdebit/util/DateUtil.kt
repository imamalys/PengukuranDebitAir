package id.ias.calculationwaterdebit.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object {
        fun getDateSpecific(year: Int, month: Int, day: Int): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val date = Calendar.getInstance()
            date.set(Calendar.YEAR, year)
            date.set(Calendar.MONTH, month)
            date.set(Calendar.DATE, day)
            return sdf.format(date.time)
        }
    }
}