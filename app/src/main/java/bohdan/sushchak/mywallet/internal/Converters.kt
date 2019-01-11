package bohdan.sushchak.mywallet.internal

import androidx.room.TypeConverter
import com.github.sundeepk.compactcalendarview.domain.Event

//val localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
class Converters {

    @TypeConverter
    fun dateToEvent(date: Long): Event{
        val event = Event(Constants.COLOR_EVENT, date)
        return event
    }

}