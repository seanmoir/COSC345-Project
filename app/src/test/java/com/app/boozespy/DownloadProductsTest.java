package com.app.boozespy;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests downloading of web data with a broad search
 *
 * @author Sean Moir
 */
public class DownloadProductsTest {

    /**
     * Checks New World data that it returns results
     *
     * @throws IOException connection issues
     */
    @Test
    public void newWorldData_ReturnsNonEmpty() throws IOException {
        assertNotNull(DownloadProducts.NewWorldProducts("h"));
        assertNotEquals(DownloadProducts.NewWorldProducts("h").size(), 0);
    }

    /**
     * Checks Liqourland data that it returns results
     *
     * @throws IOException connection issues
     */
    @Test
    public void liqourlandData_ReturnsNonEmpty() throws IOException {
        assertNotNull(DownloadProducts.LiqourLandProducts("h"));
        assertNotEquals(DownloadProducts.LiqourLandProducts("h").size(), 0);
    }

    /**
     * Checks Pak 'n' Save data that it returns results
     *
     * @throws IOException connection issues
     */
    @Test
    public void paknSaveData_ReturnsNonEmpty() throws IOException {
        assertNotNull(DownloadProducts.PaknSaveProducts("h"));
        assertNotEquals(DownloadProducts.PaknSaveProducts("h").size(), 0);
    }
}
