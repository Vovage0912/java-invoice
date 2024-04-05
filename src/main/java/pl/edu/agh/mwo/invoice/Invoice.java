package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    public static final int SPACE1 = 5;
    public static final int SPACE2 = 4;
    private Map<Product, Integer> products = new HashMap<Product, Integer>();

    private static int invoiceCounter;

    private int invoiceNumber;

    public Invoice() {
        invoiceNumber = ++ invoiceCounter;
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        if (products.containsKey(product)) {
            int ilosc = products.get(product);
            products.replace(product, ilosc + quantity);
        } else {
            products.put(product, quantity);
        }
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }


    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public void printInvoice() {
        int liczbaPozycji = 0;
        BigDecimal razemNetto = BigDecimal.ZERO;
        BigDecimal razemBrutto = BigDecimal.ZERO;
        System.out.println("\nFaktura nr.: " + getInvoiceNumber());
        System.out.println("--------------------------------------------"
                + "-------------------------------------------");
        for (Map.Entry<Product, Integer> produkt : products.entrySet()) {
            Product p = produkt.getKey();
            Integer i = produkt.getValue();
            BigDecimal cenaNetto = p.getPrice().multiply(BigDecimal.valueOf(i));
            BigDecimal cenaBrutto = p.getPriceWithTax().multiply(BigDecimal.valueOf(i));
            System.out.println("Nazwa Pozycji: " + p.getName() + String.format("%"
                    + (longestProductName() + SPACE1 - p.getName().length()) + "s", " ")
                    + " ilość sztuk: " + i + String.format("%" + (theLargestQuantity() + SPACE2)
                    + "s", " ") + " razem netto: " + cenaNetto + " PLN");
            razemNetto = razemNetto.add(cenaNetto);
            razemBrutto = razemBrutto.add(cenaBrutto);
            liczbaPozycji++;
        }
        System.out.println("------------------------------------------"
                + "---------------------------------------------");
        System.out.println("Razem netto: " + razemNetto.setScale(2, RoundingMode.HALF_UP) + " PLN");
        System.out.println("Razem brutto: " + razemBrutto.setScale(2, RoundingMode.UP) + " PLN");
        System.out.println("Liczba pozycji na fakturze: " + liczbaPozycji);
    }

    private int longestProductName() {
        int longestName = 0;
        for (Product produkt : products.keySet()) {
            int nameLength = produkt.getName().length();
            if (nameLength > longestName) {
                longestName = nameLength;
            }
        }
        return longestName;
    }

    private int theLargestQuantity() {
        int largestQuantity = 0;
        for (Integer ilosc : products.values()) {
            if (ilosc > largestQuantity) {
                largestQuantity = ilosc;
            }
        }
        return largestQuantity;
    }

    public int getAllPositions() {
        return products.size();
    }
}
