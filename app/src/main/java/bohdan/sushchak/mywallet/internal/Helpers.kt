package bohdan.sushchak.mywallet.internal

import java.text.SimpleDateFormat
import java.util.*
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.model.OrdersByDateGroup

fun parseDate(date: String, patter: String): Date {

    val format = SimpleDateFormat(patter);
    return format.parse(date)
}

fun formatDate(date: Date?, patter: String): String {

    val sdf = SimpleDateFormat(patter)
    return sdf.format(date)
}

fun convertOrdersByDate(orders: List<Order>): List<OrdersByDateGroup> {

    val ordersByDateList = mutableListOf<OrdersByDateGroup>()

    orders.forEach { order ->
        if(!ordersByDateList.containDate(order.date)){
            val date = Date()
            date.time = order.date
            val ordersByDate = OrdersByDateGroup(formatDate(date, Constants.DATE_FORMAT), mutableListOf(order))
            ordersByDateList.add(ordersByDate)
        }else{
            val inx = ordersByDateList.indexBydate(order.date)
            ordersByDateList[inx].orders.add(order)
        }
    }

    return ordersByDateList.toList()
}


