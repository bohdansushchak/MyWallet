package bohdan.sushchak.mywallet.internal

import android.graphics.Color
import android.os.Build
import androidx.room.TypeConverter

object Converters {

    @TypeConverter
    @JvmStatic
    fun fromColorToInt(value: Color): Int{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            value.toArgb()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromIntToColor(value: Int): Color {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Color.valueOf(value)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }
}