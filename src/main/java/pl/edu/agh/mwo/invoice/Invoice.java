package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private Map<Product, Integer> products = new HashMap<>();


    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("The product cannot be null");
        }
        products.put(product,1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <=0) {
            throw new IllegalArgumentException("The product cannot be null and the quantity has to be > 0");
        }
        for (int i = 0; i<quantity; i++) {
            products.put(product,quantity);
        }
    }

    public BigDecimal getSubtotal() {
        BigDecimal subTotal = new BigDecimal("0");
        for (Map.Entry<Product, Integer> product : products.entrySet()) {
            BigDecimal productPrice = product.getKey().getPrice();
            Integer quantity = product.getValue();
         subTotal = subTotal.add(productPrice.multiply((new BigDecimal(quantity))));
        }
        return subTotal;
    }

    public BigDecimal getTax() {
        BigDecimal totalTax = BigDecimal.ZERO;

        for (Map.Entry<Product, Integer> product : products.entrySet()){
            BigDecimal productPrice = product.getKey().getTaxPercent().multiply(product.getKey().getPrice());
            Integer quantity = product.getValue();
            totalTax = totalTax.add(productPrice.multiply((new BigDecimal(quantity))));
        }
        return totalTax;
    }

    public BigDecimal getTotal() {
        return getSubtotal().add(getTax());
    }
}
