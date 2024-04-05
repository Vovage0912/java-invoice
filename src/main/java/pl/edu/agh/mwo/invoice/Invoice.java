package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private Map<Product, Integer> products = new HashMap<Product, Integer>();

    private static int invoiceCounter;

    private int invoiceNumber;

    public Invoice () {
        invoiceNumber=++invoiceCounter;
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
            products.replace(product,ilosc + quantity);
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
        int liczba_pozycji = 0;
        BigDecimal razem_netto = BigDecimal.ZERO;
        BigDecimal razem_brutto = BigDecimal.ZERO;
        System.out.println("\nFaktura nr.: " + getInvoiceNumber());
        System.out.println("---------------------------------------------------------------------------------------");
        for (Map.Entry<Product, Integer> produkt : products.entrySet()) {
            Product p = produkt.getKey();
            Integer i = produkt.getValue();
            BigDecimal cena_netto = p.getPrice().multiply(BigDecimal.valueOf(i));
            BigDecimal cena_brutto = p.getPriceWithTax().multiply(BigDecimal.valueOf(i));
            System.out.println("Nazwa Pozycji: " + p.getName() + String.format("%"+(longestProductName()
                    +5-p.getName().length())+"s", " ") +  " ilość sztuk: " + i +
                    String.format("%"+(theLargestQuantity()+4)+"s"," ") + " razem netto: " + cena_netto +" PLN");
            razem_netto = razem_netto.add(cena_netto);
            razem_brutto = razem_brutto.add(cena_brutto);
            liczba_pozycji ++;
        }
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("Razem netto: " + razem_netto.setScale(2, RoundingMode.HALF_UP) + " PLN");
        System.out.println("Razem brutto: " + razem_brutto.setScale(2,RoundingMode.UP) + " PLN");
        System.out.println("Liczba pozycji na fakturze: " + liczba_pozycji);
    }

    private int longestProductName () {
        int longestName = 0;
        for (Product produkt : products.keySet()) {
            int nameLength = produkt.getName().length();
            if (nameLength>longestName) {
                longestName = nameLength;
            }
        }
        return longestName;
    }

    private int theLargestQuantity () {
        int largestQuantity = 0;
        for (Integer ilosc : products.values()) {
            if (ilosc>largestQuantity) {
                largestQuantity = ilosc;
            }
        }
        return largestQuantity;
    }

    public int getAllPositions () {
        return products.size();
    }
}
