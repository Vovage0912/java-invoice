package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private Collection<Product> products;

    public Invoice() {
        this.products = new ArrayList<>();
    };

    public void addProduct(Product product) {
        products.add(product);
    }

    public void addProduct(Product product, Integer quantity) {
        for (int i = 0; i<quantity; i++) {
            products.add(product);
        }
    }

    public BigDecimal getSubtotal() {
        BigDecimal subTotal = new BigDecimal("0");
        for (Product product : products) {
         subTotal = subTotal.add(product.getPrice());
        }
        return subTotal;
    }

    public BigDecimal getTax() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotal() {
        return BigDecimal.ZERO;
    }
}
