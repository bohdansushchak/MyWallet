package bohdan.sushchak.mywallet.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import bohdan.sushchak.mywallet.data.db.dao.CategoryDao
import bohdan.sushchak.mywallet.data.db.dao.MetaDataDao
import bohdan.sushchak.mywallet.data.db.dao.OrderDao
import bohdan.sushchak.mywallet.data.db.dao.ProductDao
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.MetaDataEntity
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity
import bohdan.sushchak.mywallet.internal.Converters

/**
 * Main class to create a database.
 * @author Bohdan
 * @version 3
 */
@Database(
    entities = [ProductEntity::class, OrderEntity::class, CategoryEntity::class, MetaDataEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MyWalletDatabase : RoomDatabase() {

    /**
     * Method to get a instance of {@code CategoryDao} object
     * @return A {@code CategoryDao} witch you can use to get access to table of categories
     */
    abstract fun categoryDao(): CategoryDao

    /**
     * Method to get a instance of {@code OrderDao} object
     * @return A {@code OrderDao} witch you can use to get access to table of orders
     */
    abstract fun orderDao(): OrderDao

    /**
     * Method to get a instance of {@code ProductDao} object
     * @return A {@code ProductDao} witch you can use to get access to table of products
     */
    abstract fun productDao(): ProductDao

    /**
     * Method to get instance of {@code MetaDataDao} object
     *
     * @return
     */
    abstract fun metaDataDao(): MetaDataDao



    companion object {
        @Volatile
        private var instance: MyWalletDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        /**
         * Method to build MyWalletDatabase
         *
         * @param context Context
         */
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MyWalletDatabase::class.java, "my_wallet.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}