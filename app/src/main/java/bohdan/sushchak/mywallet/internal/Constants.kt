package bohdan.sushchak.mywallet.internal

import android.graphics.Color

object Constants {
    /*
    "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
    "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
    "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
    "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
    "yyMMddHHmmssZ"-------------------- 010704120856-0700
    "K:mm a, z" ----------------------- 0:08 PM, PDT
    "h:mm a" -------------------------- 12:08 PM
    "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01
     */
    const val DATE_FORMAT = "EEE, d MMM yyyy"

    const val COLOR_EVENT = Color.BLACK

    const val MONTH_FORMAT = "MMMM yyyy"
}