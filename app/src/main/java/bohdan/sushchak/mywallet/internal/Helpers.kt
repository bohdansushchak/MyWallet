package bohdan.sushchak.mywallet.internal

import java.text.SimpleDateFormat
import java.util.*


fun parseDate(date: String): Date {

    val format = SimpleDateFormat(Constants.DATE_FORMAT);
    val date = format.parse(date)
    return date
}

fun formatDate(date: Date): String {

    val sdf = SimpleDateFormat(Constants.DATE_FORMAT)
    val formattedDate = sdf.format(date)

    return formattedDate
}

