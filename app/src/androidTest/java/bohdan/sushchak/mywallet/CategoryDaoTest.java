package bohdan.sushchak.mywallet;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import bohdan.sushchak.mywallet.helpers.CategoryTestHelper;
import bohdan.sushchak.mywallet.data.db.MyWalletDatabase;
import bohdan.sushchak.mywallet.data.db.dao.CategoryDao;
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity;

@RunWith(AndroidJUnit4.class)
public class CategoryDaoTest {

    private MyWalletDatabase db;
    private CategoryDao categoryDao;

    @Before
    public void createDb() throws Exception {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                MyWalletDatabase.class).build();
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

    @After
    public void closeDb() throws Exception {
        db.close();
    }
}
