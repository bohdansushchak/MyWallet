package bohdan.sushchak.mywallet.database;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import bohdan.sushchak.mywallet.data.db.dao.CategoryDao;
import bohdan.sushchak.mywallet.data.db.dao.OrderDao;
import bohdan.sushchak.mywallet.data.db.dao.ProductDao;
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity;
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity;
import bohdan.sushchak.mywallet.data.db.entity.ProductEntity;
import bohdan.sushchak.mywallet.database.helpers.CategoryTestHelper;
import bohdan.sushchak.mywallet.database.helpers.OrderTestHelper;
import bohdan.sushchak.mywallet.database.helpers.ProductTestHelper;

public class ProductDaoTest extends DatabaseBaseTest {

    private ProductDao productDao;
    private OrderDao orderDao;
    private CategoryDao categoryDao;

    @Before
    public void initProductDao(){
        productDao = db.productDao();
        orderDao = db.orderDao();
        categoryDao = db.categoryDao();
    }

    @Test
    public void whenInsertProductThenReadTheSameOne() throws NullPointerException{

        long orderId = getSomeOrderId();
        long categoryId = getSomeCategoryId();

        List<ProductEntity> productEntities = ProductTestHelper.createListOfProductss(1, categoryId, orderId);
        productDao.insert(productEntities.get(0));

        List<ProductEntity> dbProductEntities = productDao.getProductsNonLive();
        Assert.assertEquals(1, dbProductEntities.size());

        Assert.assertTrue(ProductTestHelper.productsAreIdentical(productEntities.get(0), dbProductEntities.get(0)));
    }

    public long getSomeOrderId() throws NullPointerException {
        List<OrderEntity> orderEntities = OrderTestHelper.createListOfOrders(1);
        orderDao.insert(orderEntities.get(0));
        long orderId = orderDao.getOrdersNonLive().get(0).getId();

        return orderId;
    }

    public long getSomeCategoryId() throws NullPointerException {
        List<CategoryEntity> categories = CategoryTestHelper.Companion.createListOfCategory(1);
        categoryDao.insert(categories.get(0));
        long categoryId = categoryDao.getAllCategoriesNonLive().get(0).getId();

        return categoryId;
    }
}
