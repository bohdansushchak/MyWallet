package bohdan.sushchak.mywallet.internal

import java.text.SimpleDateFormat
import java.util.*

fun parseDate(date: String, patter: String): Date {

    val format = SimpleDateFormat(patter);
    return format.parse(date)
}

fun formatDate(date: Date?, patter: String): String {

    val sdf = SimpleDateFormat(patter)
    return sdf.format(date)
}


