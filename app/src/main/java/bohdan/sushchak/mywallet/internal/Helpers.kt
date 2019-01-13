package bohdan.sushchak.mywallet.internal

import java.text.SimpleDateFormat
import java.util.*
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.model.OrdersByDate

fun parseDate(date: String, patter: String): Date {

    val format = SimpleDateFormat(patter);
    return format.parse(date)
}

fun formatDate(date: Date?, patter: String): String {

    val sdf = SimpleDateFormat(patter)
    return sdf.format(date)
}

fun convertOrdersByDate(orders: List<Order>): List<OrdersByDate> {

    val ordersByDateList = mutableListOf<OrdersByDate>()

    orders.forEach { order ->
        if(!ordersByDateList.containDate(order.date)){
            val ordersByDate = OrdersByDate(order.date, mutableListOf(order))
            ordersByDateList.add(ordersByDate)
        }else{
            val inx = ordersByDateList.indexBydate(order.date)
            ordersByDateList[inx].orders.add(order)
        }
    }

    return ordersByDateList.toList()
}


