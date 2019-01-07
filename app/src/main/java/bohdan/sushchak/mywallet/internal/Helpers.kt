package bohdan.sushchak.mywallet.internal

import java.text.SimpleDateFormat
import java.util.*


fun parseDate(date: String): Date {

    val format = SimpleDateFormat(Constants.DATE_FORMAT);
    val date = format.parse(date)
    return date
}