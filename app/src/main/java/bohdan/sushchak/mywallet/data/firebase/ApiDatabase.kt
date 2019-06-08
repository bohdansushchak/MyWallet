package bohdan.sushchak.mywallet.data.firebase

import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference


interface ApiDatabase {

    suspend fun addCategory(categoryEntity: CategoryEntity): DocumentReference
    suspend fun addOrder(orderEntity: OrderEntity, productsEntity: List<ProductEntity>): DocumentReference

    suspend fun removeCategory(categoryEntity: CategoryEntity): Void?
    suspend fun removeOrder(orderEntity: OrderEntity): Void?

    suspend fun getVersionOfDatabase(): Long

    suspend fun registerNewUser(): Void?

}