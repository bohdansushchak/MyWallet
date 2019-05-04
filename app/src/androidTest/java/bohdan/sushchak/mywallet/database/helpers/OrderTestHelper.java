package bohdan.sushchak.mywallet.database.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bohdan.sushchak.mywallet.data.db.entity.OrderEntity;

public final class OrderTestHelper {

    public static List<OrderEntity> createListOfOrders(int amount) {
        List<OrderEntity> orderEntityList = new ArrayList<OrderEntity>();

        for (int i = 0; i < amount; i++) {
            long someDate = new Random().nextLong();
            double somePrice = new Random().nextDouble();
            long id = Long.valueOf(i);

            OrderEntity obj = new OrderEntity(id, "categoryTitle " + i, someDate, somePrice);
            orderEntityList.add(obj);
        }

        return orderEntityList;
    }

    public static boolean ordersAreIdentical(OrderEntity orderEntity1, OrderEntity orderEntity2) throws NullPointerException {
        return orderEntity1.getOrderId().equals(orderEntity2.getOrderId())
                && orderEntity1.getDate() == orderEntity2.getDate()
                && orderEntity1.getPrice() == orderEntity2.getPrice()
                && orderEntity1.getTitle().equals(orderEntity2.getTitle());

    }
}
