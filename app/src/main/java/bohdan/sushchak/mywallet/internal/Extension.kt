package bohdan.sushchak.mywallet.internal

import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.data.model.CategoryWithProducts
import bohdan.sushchak.mywallet.data.model.OrdersByDateGroup
import org.jetbrains.anko.collections.forEachWithIndex
import java.util.*

fun List<ProductEntity>.setOrderId(orderId: Long) {
    forEachIndexed { index, _ ->
        this[index].orderId = orderId
    }
}

fun MutableList<CategoryWithProducts>.containCategory(categoryEntity: CategoryEntity): Boolean {
    var isExist = false

    loop@ for (categoryWithProducts in this) {
        if (categoryWithProducts.categoryEntity.equals(categoryEntity)) {
            isExist = true
            break@loop
        }
    }

    return isExist
}

fun MutableList<CategoryWithProducts>.indexOfCategory(categoryEntity: CategoryEntity): Int {
    var index = -1

    this.forEachWithIndex { i, categoryWithProducts ->
        if (categoryWithProducts.categoryEntity.equals(categoryEntity))
            index = i
    }

    return index
}

fun MutableList<CategoryWithProducts>.removeProduct(product: ProductEntity) {
    var categoryToRemove: CategoryWithProducts? = null

    this.forEach { categoryWithProducts ->
        categoryWithProducts.products.remove(product)
        if (categoryWithProducts.products.size == 0)
            categoryToRemove = categoryWithProducts
    }

    if (categoryToRemove != null)
        this.remove(categoryToRemove!!)
}

fun Calendar.onlyDateInMillis(result: (time: Long) -> Unit) {
    val year = this.get(Calendar.YEAR)
    val month = this.get(Calendar.MONTH)
    val dayOfMonth = this.get(Calendar.DAY_OF_MONTH)

    val cal = Calendar.getInstance()
    cal.clear()

    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month)
    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

    result.invoke(cal.timeInMillis)
}

fun List<OrdersByDateGroup>.containDate(date: Long): Boolean {
    var isExist = false

    loop@ for (ordersByDate in this) {
        val time = ordersByDate.date
        if (time == date) {
            isExist = true
            break@loop
        }
    }

    return isExist
}

fun List<OrdersByDateGroup>.indexBydate(date: Long): Int {
    var index = -1

    this.forEachWithIndex { i, ordersByDate ->
        if (ordersByDate.date == date){
            index = i
            return@forEachWithIndex
        }
    }
    return index
}

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

fun Double.myToString(): String {
    var str = this.toString()

    if(str.length > 2 && str.last() == '0')
        str = str.substring(0, str.length -2)

    return str
}

fun Double.correctPlus(num: Double): Double {
    val sum = this + num

    return sum
}


