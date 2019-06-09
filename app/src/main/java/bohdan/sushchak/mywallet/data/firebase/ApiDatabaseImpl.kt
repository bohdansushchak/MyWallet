package bohdan.sushchak.mywallet.data.firebase

import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.MetaDataEntity
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.internal.NonAuthorizedExeption
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class ApiDatabaseImpl : ApiDatabase {

    private var db = FirebaseFirestore.getInstance()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val userCollectionRef: CollectionReference
        get() = db.collection("users")

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings
    }

    override suspend fun addCategory(categoryEntity: CategoryEntity): DocumentReference {
        val categoryDocument = categoryEntity.toDocument()
        return Tasks.await(getCategoriesCollectionRef().add(categoryDocument))
    }

    override suspend fun addOrder(orderEntity: OrderEntity, productsEntity: List<ProductEntity>): DocumentReference {
        val orderDocument = orderEntity.toDocument()
        val products = productsEntity.map { it.toDocument() }

        orderDocument["products"] = products

        return Tasks.await(getOrdersCollectionRef().add(orderDocument))
    }

    override suspend fun removeCategory(categoryEntity: CategoryEntity): Void? {
        val queryCategories = Tasks.await(getCategoriesCollectionRef().whereEqualTo("id", categoryEntity.categoryId).get())

        if (queryCategories.isEmpty) return null
        return removeDocumentByPath(queryCategories.documents[0].reference.path)
    }

    override suspend fun removeOrder(orderEntity: OrderEntity): Void? {
        val queryOrders = Tasks.await(getOrdersCollectionRef().whereEqualTo("id", orderEntity.orderId).get())

        if (queryOrders.isEmpty) return null
        return removeDocumentByPath(queryOrders.documents[0].reference.path)
    }

    override suspend fun getVersionOfDatabase(): Long {
        var userDoc = Tasks.await(getUserDocumentRef().get())

        if (!userDoc.contains("databaseVersion")) {
            userDoc = Tasks.await(addNewUser().get())
        }

        return userDoc["databaseVersion"].toString().toLong()
    }

    override suspend fun setMetaData(metaDataEntity: MetaDataEntity): Void? {
        val metadataDoc = metaDataEntity.toDocument()
        return Tasks.await(getUserDocumentRef().set(metadataDoc, SetOptions.merge()))
    }

    private fun removeDocumentByPath(path: String) = Tasks.await(db.document(path).delete())

    private suspend fun getOrdersCollectionRef(): CollectionReference {
        return db.collection("${getUserDocumentRef().path}/orders")
    }

    private suspend fun getCategoriesCollectionRef(): CollectionReference {
        return db.collection("${getUserDocumentRef().path}/categories")
    }

    private fun addNewUser(): DocumentReference {
        if (mAuth.currentUser == null) throw NonAuthorizedExeption()

        val doc = hashMapOf<String, Any?>(
            "databaseVersion" to 1,
            "userId" to mAuth.currentUser!!.uid
        )
        return Tasks.await(userCollectionRef.add(doc))
    }

    private suspend fun getUserDocumentRef(): DocumentReference {
        if (mAuth.currentUser == null) throw NonAuthorizedExeption()

        val queryResult = Tasks.await(userCollectionRef.whereEqualTo("userId", mAuth.currentUser!!.uid).get())

        if (queryResult.isEmpty) {
            return addNewUser()
        }

        return queryResult.documents[0].reference
    }
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

fun MetaDataEntity.toDocument(): HashMap<String, Any?> {
    return hashMapOf(
        "databaseVersion" to this.databaseVersion,
        "userId" to this.userFirebaseId
    )
}