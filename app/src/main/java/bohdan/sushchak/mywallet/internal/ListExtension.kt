package bohdan.sushchak.mywallet.internal

import bohdan.sushchak.mywallet.data.db.entity.Category
import bohdan.sushchak.mywallet.data.db.entity.Product
import bohdan.sushchak.mywallet.data.db.model.CategoryWithProducts
import org.jetbrains.anko.collections.forEachWithIndex

fun List<Product>.setOrderId(orderId: Long) {
    forEachIndexed { index, product ->
        this[index].orderId = orderId
    }
}

fun MutableList<CategoryWithProducts>.containCategory(category: Category): Boolean {
    var isExist = false

    this.forEach { categoryWithProducts ->
        if (categoryWithProducts.category.equals(category))
            isExist = true
    }

    return isExist
}

fun MutableList<CategoryWithProducts>.indexOfCategory(category: Category): Int {
    var index = -1

    this.forEachWithIndex { i, categoryWithProducts ->
        if (categoryWithProducts.category.equals(category))
            index = i
    }

    return index
}

fun MutableList<CategoryWithProducts>.removeProduct(product: Product) {
    var categoryToRemove: CategoryWithProducts? = null

    this.forEach { categoryWithProducts ->
        categoryWithProducts.products.remove(product)
        if(categoryWithProducts.products.size == 0)
            categoryToRemove = categoryWithProducts
    }

    if(categoryToRemove != null)
        this.remove(categoryToRemove!!)
}
