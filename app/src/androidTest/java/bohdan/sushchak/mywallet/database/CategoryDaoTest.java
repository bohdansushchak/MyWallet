package bohdan.sushchak.mywallet.database;

import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;
import bohdan.sushchak.mywallet.database.helpers.CategoryTestHelper;
import bohdan.sushchak.mywallet.data.db.dao.CategoryDao;
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity;

@RunWith(AndroidJUnit4.class)
public class CategoryDaoTest extends DatabaseBaseTest {

    private CategoryDao categoryDao;

    @Before
    public void initCategoryDao(){
        categoryDao = db.categoryDao();
    }

    @Test
    public void whenInsertCategoryThenReadTheSameOne() throws Exception{
        List<CategoryEntity> categories = CategoryTestHelper.Companion.createListOfCategory(1);
        categoryDao.insert(categories.get(0));

        List<CategoryEntity> dbCategories = categoryDao.getAllCategoriesNonLive();
        Assert.assertEquals(1, dbCategories.size());
        Assert.assertTrue(CategoryTestHelper.Companion.categoriesAreIdentical(categories.get(0), dbCategories.get(0)));
    }

}
