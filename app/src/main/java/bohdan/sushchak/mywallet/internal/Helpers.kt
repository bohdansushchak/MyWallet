package bohdan.sushchak.mywallet.internal

import android.content.Context
import androidx.preference.PreferenceManager
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.model.OrdersByDateGroup
import bohdan.sushchak.mywallet.internal.Constants.CURRENCY_KEY_PREF
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

fun parseDate(date: String, patter: String): Date {

    val format = SimpleDateFormat(patter)
    return format.parse(date)
}

fun formatDate(date: Date?, patter: String): String {

    val sdf = SimpleDateFormat(patter)
    return sdf.format(date)
}

fun formatDate(date: Long, patter: String): String {
/*
    val dateClass = Date()
    dateClass.time = date

    */
    val sdf = SimpleDateFormat(patter)
    return sdf.format(date)
}

fun convertOrdersByDate(orders: List<OrderEntity>): List<OrdersByDateGroup> {

    val ordersByDateList = mutableListOf<OrdersByDateGroup>()

    orders.forEach { order ->
        if (!ordersByDateList.containDate(order.date)) {
            val ordersByDate = OrdersByDateGroup(order.date, mutableListOf(order))
            ordersByDateList.add(ordersByDate)
        } else {
            val inx = ordersByDateList.indexByDate(order.date)
            ordersByDateList[inx].orders.add(order)
        }
    }

    return ordersByDateList.toList()
}

fun formatDigitToString(value: Double): String {
    var str: String
    val num = multipleOfTen(value)
    var endIndicator = ""
    var numForRem = 1.0

    when (num) {
        in (3..5) -> {
            endIndicator = "K"
            numForRem = 1000.0
        }
        in (6..8) -> {
            endIndicator = "M"
            numForRem = 1000000.0
        }
        in (9..12) -> {
            endIndicator = "B"
            numForRem = 1000000000.0
        }
    }

    str = if (value.rem(numForRem) == 0.0)
        "${value.div(numForRem).roundToInt()}"
    else value.div(numForRem).format(1)

    if (str.contains(".0", true)) {
        val indEnd = str.indexOf('.')
        str = str.substring(0, indEnd)
    }

    str = "$str$endIndicator"

    return str
}

fun multipleOfTen(value: Double): Int {
    val valueAbs = value.absoluteValue
    return if (valueAbs.div(10) >= 1)
        multipleOfTen(valueAbs / 10) + 1
    else 0
}

fun getSavedCurrency(context: Context, default: String = "") =
    PreferenceManager.getDefaultSharedPreferences(context).getString(CURRENCY_KEY_PREF, default)!!

