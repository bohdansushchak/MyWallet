package bohdan.sushchak.mywallet.database;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import bohdan.sushchak.mywallet.data.db.dao.OrderDao;
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity;
import bohdan.sushchak.mywallet.database.helpers.OrderTestHelper;

public class OrderDaoTest extends DatabaseBaseTest {

    private OrderDao orderDao;

    @Before
    public void initOrderDao() {
        orderDao = db.orderDao();
    }

    @Test
    public void whenInsertOrderThenReadTheSameOne() {
        List<OrderEntity> orderEntities = OrderTestHelper.createListOfOrders(1);
        orderDao.insert(orderEntities.get(0));

        List<OrderEntity> dbOrderEntities = orderDao.getOrdersNonLive();
        Assert.assertEquals(1, dbOrderEntities.size());
        Assert.assertTrue(OrderTestHelper.ordersAreIdentical(orderEntities.get(0), dbOrderEntities.get(0)));
    }
}
