package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
    public static final double EXCISE = 5.56;
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    protected Product(String name, BigDecimal price, BigDecimal tax) {
        if ((name == null) || name.equals("") || (price == null) || (tax == null)
                || (tax.compareTo(new BigDecimal(0)) < 0)
                || (price.compareTo(new BigDecimal(0)) < 0)) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.price = price;
        this.taxPercent = tax;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public BigDecimal getPriceWithTax() {
        if (this.getClass() == BottleOfWine.class || this.getClass() == FuelCanister.class) {
            return (price.multiply(taxPercent).add(price)).add(BigDecimal.valueOf(EXCISE));
        }  else {
            return price.multiply(taxPercent).add(price);
        }
    }
}
