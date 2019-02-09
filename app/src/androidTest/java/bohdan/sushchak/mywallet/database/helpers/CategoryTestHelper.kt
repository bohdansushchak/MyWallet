package bohdan.sushchak.mywallet.database.helpers

import android.graphics.Color
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import kotlin.random.Random

class CategoryTestHelper {

    companion object {
        fun createListOfCategory(amount: Int): List<CategoryEntity> {
            val categories =  mutableListOf<CategoryEntity>()

            for (i in 0..amount){
                val rand = Random(1)
                val categoryEntity = CategoryEntity(i.toLong(),
                        "title $i",
                        Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))
                )

                categories.add(categoryEntity)
            }
            return categories.toList()
        }

        fun categoriesAreIdentical(cat1: CategoryEntity, cat2: CategoryEntity) = cat1.title == cat2.title
                && cat1.id == cat1.id && cat1.color == cat2.color
    }
}