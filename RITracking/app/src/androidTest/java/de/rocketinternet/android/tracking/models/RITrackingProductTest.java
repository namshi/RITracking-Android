package de.rocketinternet.android.tracking.models;

import android.test.InstrumentationTestCase;

/**
 * @author alessandro.balocco
 */
public class RITrackingProductTest extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInitializationWithValues() {
        RITrackingProduct product = new RITrackingProduct("product5", "productName", 5, 5.0, "eur",
                "productCategory", "productSubCategory", "productBrand", 20, 4.0f);

        assertEquals("product5", product.getIdentifier());
        assertEquals("productName", product.getName());
        assertEquals(5, product.getQuantity());
        assertEquals(5.0, product.getPrice());
        assertEquals("eur", product.getCurrency());
        assertEquals("productCategory", product.getCategory());
        assertEquals("productSubCategory", product.getSubCategory());
        assertEquals("productBrand", product.getBrand());
        assertEquals(20, product.getDiscount());
        assertEquals(4.0f, product.getAverageRating());
    }

    public void testSetters() {
        RITrackingProduct product = new RITrackingProduct();

        product.setIdentifier("product5");
        assertEquals("product5", product.getIdentifier());

        product.setName("productName");
        assertEquals("productName", product.getName());

        product.setQuantity(5);
        assertEquals(5, product.getQuantity());

        product.setPrice(5.0);
        assertEquals(5.0, product.getPrice());

        product.setCurrency("eur");
        assertEquals("eur", product.getCurrency());

        product.setCategory("productCategory");
        assertEquals("productCategory", product.getCategory());

        product.setSubCategory("productSubCategory");
        assertEquals("productSubCategory", product.getSubCategory());

        product.setBrand("productBrand");
        assertEquals("productBrand", product.getBrand());

        product.setDiscount(20);
        assertEquals(20, product.getDiscount());

        product.setAverageRating(4.0f);
        assertEquals(4.0f, product.getAverageRating());
    }
}
