package bohdan.sushchak.mywallet.internal

import bohdan.sushchak.mywallet.data.db.entity.Product

fun List<Product>.setOrderId(orderId: Long) {
    forEachIndexed { index, product ->
        this[index].orderId = orderId
    }
}