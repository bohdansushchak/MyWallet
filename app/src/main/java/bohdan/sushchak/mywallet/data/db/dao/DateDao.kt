package bohdan.sushchak.mywallet.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import bohdan.sushchak.mywallet.data.db.entity.Date
import bohdan.sushchak.mywallet.data.db.model.OrdersByDate
import com.github.sundeepk.compactcalendarview.domain.Event

@Dao
abstract class DateDao : BaseDao<Date> {

    @Query("select * from date_table order by date asc")
    abstract fun getOrdersByDate(): LiveData<List<OrdersByDate>>

    @Query("select * from date_table where id = :id")
    abstract fun getDateById(id: Long?): Date?

    @Query("select id from date_table where date = :dateLong")
    abstract fun getIdByDate(dateLong: Long): List<Long?>

    @Query("delete from date_table where id = :id")
    abstract fun removeById(id: Long?)

    @Query("select * from date_table order by date asc")
    abstract fun getDatesOrderBy(): LiveData<List<Date>>

    @Query("select date from date_table")
    abstract fun getAllDates(): LiveData<List<Event>>

}