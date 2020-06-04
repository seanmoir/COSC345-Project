package com.app.boozespy;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class DownloadProductsTest {
    @Test
    public void newWorldData_ReturnsNonEmpty() throws IOException {
        assertNotNull(DownloadProducts.NewWorldProducts("h"));
        assertNotEquals(DownloadProducts.NewWorldProducts("h").size(), 0);
    }

    @Test
    public void liqourlandData_ReturnsNonEmpty() throws IOException {
        assertNotNull(DownloadProducts.LiqourLandProducts("h"));
        assertNotEquals(DownloadProducts.LiqourLandProducts("h").size(), 0);
    }

    @Test
    public void paknSaveData_ReturnsNonEmpty() throws IOException {
        assertNotNull(DownloadProducts.PaknSaveProducts("h"));
        assertNotEquals(DownloadProducts.PaknSaveProducts("h").size(), 0);
    }
}
