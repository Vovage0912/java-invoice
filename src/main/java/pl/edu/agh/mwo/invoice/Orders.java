package pl.edu.agh.mwo.invoice;

import pl.edu.agh.mwo.invoice.product.BottleOfWine;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.FuelCanister;
import pl.edu.agh.mwo.invoice.product.Product;

import java.math.BigDecimal;

public class Orders {

    public static final double POTATO_PRICE_NETTO = 2.25;
    public static final double BANANA_PRICE_NETTO = 3.45;
    public static final double PINEAPPLE_PRICE_NETTO = 4.55;
    public static final double WINE_PRICE_NETTO = 10.10;
    public static final double FUEL_PRICE_NETTO = 30.500;

    public static void main(String[] args) {
        Product pomidor = new DairyProduct("pomidor", BigDecimal.valueOf(POTATO_PRICE_NETTO));
        Product banan = new DairyProduct("banan", BigDecimal.valueOf(BANANA_PRICE_NETTO));
        Product ananas = new DairyProduct("ananas", BigDecimal.valueOf(PINEAPPLE_PRICE_NETTO));


        Invoice faktura1 = new Invoice();
        faktura1.addProduct(pomidor);
        faktura1.addProduct(banan);
        faktura1.addProduct(ananas, 2); //a
        faktura1.addProduct(pomidor, 3); // b
        faktura1.addProduct(banan, 2); //c

        Product wino = new BottleOfWine("amarena", BigDecimal.valueOf(WINE_PRICE_NETTO));
        Product paliwo = new FuelCanister("Pb95", BigDecimal.valueOf(FUEL_PRICE_NETTO));

        Invoice faktura2 = new Invoice();
        faktura2.addProduct(wino, 1);
        faktura2.addProduct(paliwo);
        faktura2.addProduct(pomidor, 4); //a
        faktura2.addProduct(banan, 1); //b
        faktura2.addProduct(ananas, 6); //c
        faktura2.addProduct(wino, 2);

        faktura1.printInvoice();
        faktura2.printInvoice();

    }
}