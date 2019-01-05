package bohdan.sushchak.mywallet.internal

import bohdan.sushchak.mywallet.data.db.entity.Product

fun List<Product>.setOrderId(orderId: Int) {
    forEachIndexed { index, product ->
        this[index].orderId = orderId
    }
}