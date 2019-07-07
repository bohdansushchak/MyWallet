package bohdan.sushchak.productsdetector.utils

import bohdan.sushchak.productsdetector.model.AddedProduct


fun List<AddedProduct>.indexOfNameProduct(product: AddedProduct): Int {
    var i = 0
    while (i < this.size) {
        if (this[i].product == product.product) {
            return i
        }
        i++
    }
    return -1
}