package pl.edu.agh.mwo.invoice;

import pl.edu.agh.mwo.invoice.product.BottleOfWine;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.FuelCanister;
import pl.edu.agh.mwo.invoice.product.Product;

import java.math.BigDecimal;

public class Orders {
    public static void main(String[] args) {
        double potatoPriceNetto = 2.25;
        Product pomidor = new DairyProduct("pomidor", BigDecimal.valueOf(potatoPriceNetto));
        double bananaPriceNetto = 3.45;
        Product banan = new DairyProduct("banan", BigDecimal.valueOf(bananaPriceNetto));
        double pineapplePriceNetto = 4.55;
        Product ananas = new DairyProduct("ananas", BigDecimal.valueOf(pineapplePriceNetto));


        Invoice faktura1 = new Invoice();
        faktura1.addProduct(pomidor);
        faktura1.addProduct(banan);
        faktura1.addProduct(ananas, 2);
        faktura1.addProduct(pomidor, 3);
        faktura1.addProduct(banan, 2);

        double winePriceNetto = 10.10;
        Product wino = new BottleOfWine("amarena", BigDecimal.valueOf(winePriceNetto));
        double fuelPriceNetto = 30.500;
        Product paliwo = new FuelCanister("Pb95", BigDecimal.valueOf(fuelPriceNetto));

        Invoice faktura2 = new Invoice();
        faktura2.addProduct(wino, 1);
        faktura2.addProduct(paliwo);
        faktura2.addProduct(pomidor, 4);
        faktura2.addProduct(banan, 1);
        faktura2.addProduct(ananas, 6);
        faktura2.addProduct(wino, 2);

        faktura1.printInvoice();
        faktura2.printInvoice();

    }
}