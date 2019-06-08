package bohdan.sushchak.mywallet.data.firebase

import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.internal.NonAuthorizedExeption
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class ApiDatabaseImpl : ApiDatabase {

    private var db = FirebaseFirestore.getInstance()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val categoriesRef: CollectionReference
        get() = if (mAuth.currentUser != null) db.collection("users/${mAuth.currentUser!!.uid}/categories")
        else throw NonAuthorizedExeption()

    private val ordersRef: CollectionReference
        get() = if (mAuth.currentUser != null) db.collection("users/${mAuth.currentUser!!.uid}/orders")
        else throw NonAuthorizedExeption()

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings
    }

    override suspend fun addCategory(categoryEntity: CategoryEntity): DocumentReference {
        val categoryDocument = categoryEntity.toDocument()
        return Tasks.await(categoriesRef.add(categoryDocument))
    }

    override suspend fun addOrder(orderEntity: OrderEntity, productsEntity: List<ProductEntity>): DocumentReference {
        val orderDocument = orderEntity.toDocument()
        val products = productsEntity.map { it.toDocument() }

        orderDocument["products"] = products

        return Tasks.await(ordersRef.add(orderDocument))
    }

    override suspend fun removeCategory(categoryEntity: CategoryEntity): Void? {
        val queryCategories = Tasks.await(categoriesRef.whereEqualTo("id", categoryEntity.categoryId).get())

        if (queryCategories.isEmpty) return null
        return removeDocumentByPath(queryCategories.documents[0].reference.path)
    }

    override suspend fun removeOrder(orderEntity: OrderEntity): Void? {
        val queryOrders = Tasks.await(ordersRef.whereEqualTo("id", orderEntity.orderId).get())

        if (queryOrders.isEmpty) return null
        return removeDocumentByPath(queryOrders.documents[0].reference.path)
    }

    private fun removeDocumentByPath(path: String) = Tasks.await(db.document(path).delete())
}

fun OrderEntity.toDocument(): HashMap<String, Any?> {
    return hashMapOf(
        "id" to this.orderId,
        "title" to this.title,
        "data" to this.date,
        "price" to this.price
    )
}

fun ProductEntity.toDocument(): HashMap<String, Any?> {
    return hashMapOf(
        "id" to this.productId,
        "categoryId" to this.categoryId,
        "productId" to this.productId,
        "title" to this.title,
        "price" to this.price
    )
}

fun CategoryEntity.toDocument(): HashMap<String, Any?> {
    return hashMapOf(
        "id" to this.categoryId,
        "title" to this.categoryTitle,
        "color" to this.color
    )
}