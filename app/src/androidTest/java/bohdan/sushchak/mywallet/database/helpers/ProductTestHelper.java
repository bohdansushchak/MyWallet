package bohdan.sushchak.mywallet.database.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bohdan.sushchak.mywallet.data.db.entity.ProductEntity;

public final class ProductTestHelper {

    public static List<ProductEntity> createListOfProductss(int amount, long categoryId, long orderId) {
        List<ProductEntity> productEntityList = new ArrayList<ProductEntity>();

        for (int i = 0; i < amount; i++) {
            double somePrice = new Random().nextDouble();
            long id = Long.valueOf(i);

            ProductEntity obj = new ProductEntity(id, "title " + i, somePrice, categoryId, orderId);
            productEntityList.add(obj);
        }

        return productEntityList;
    }

    public static boolean productsAreIdentical(ProductEntity productEntity1, ProductEntity productEntity2) throws NullPointerException{
        return productEntity1.getId().equals(productEntity2.getId())
                && productEntity1.getOrderId().equals(productEntity2.getOrderId())
                && productEntity1.getPrice() == productEntity2.getPrice()
                && productEntity1.getTitle().equals(productEntity2.getTitle())
                && productEntity1.getCategoryId().equals(productEntity2.getCategoryId());

    }
}
