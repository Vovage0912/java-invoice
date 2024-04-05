package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.*;

public class InvoiceTest {
    private Invoice invoice;
    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
        int product1PriceNetto = 10;
        product1 = new DairyProduct("Product1", BigDecimal.valueOf(product1PriceNetto));
        int product2PriceNetto = 20;
        product2 = new DairyProduct("Product2", BigDecimal.valueOf(product2PriceNetto));
        double fuelPriceNetto = 40.5;
        product3 = new FuelCanister("Fuel", BigDecimal.valueOf(fuelPriceNetto));
        double winePriceNetto = 10.5;
        product4 = new BottleOfWine("Wine", BigDecimal.valueOf(winePriceNetto));

    }


    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertThat(new BigDecimal("20"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertThat(new BigDecimal("1000"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }


    @Test
    public void testInvoiceHasNumberGreaterThanZero() {
        Assert.assertThat(new Invoice().getInvoiceNumber(), Matchers.greaterThan(0));
    }

    @Test
    public void testInvoiceHasDiffNumbers() {
        int number1 = new Invoice().getInvoiceNumber();
        int number2 = new Invoice().getInvoiceNumber();
        Assert.assertThat(number2, Matchers.greaterThan(number1));
    }

    @Test
    public void testPrintInvoice() {
        // Dodanie produktów do faktury
        invoice.addProduct(product1, 3);
        invoice.addProduct(product2, 2);

        Assert.assertThat(2, Matchers.equalTo(invoice.getAllPositions()));
    }

    @Test
    public void testPrintInvoiceWithDuplicates() {
        // Dodanie produktów do faktury
        invoice.addProduct(product1, 3);
        invoice.addProduct(product1, 3);
        invoice.addProduct(product2, 2);
        invoice.addProduct(product2, 2);

        Assert.assertThat(2, Matchers.equalTo(invoice.getAllPositions()));
    }

    @Test
    public void testPriceWithExcise() {
        invoice.addProduct(product3, 1);
        invoice.addProduct(product4, 1);

        double excise = 5.56;
        BigDecimal fuelPriceWithExcise = product3.getPrice().add(BigDecimal.valueOf(excise));
        BigDecimal winePriceWithVatAndExcise = (product4.getPrice().multiply(BigDecimal.valueOf(1.23))).add(BigDecimal.valueOf(excise));


        Assert.assertThat(fuelPriceWithExcise, Matchers.equalTo(product3.getPriceWithTax().setScale(2, RoundingMode.UP)));
        Assert.assertThat(winePriceWithVatAndExcise, Matchers.equalTo(product4.getPriceWithTax()));
    }
}


