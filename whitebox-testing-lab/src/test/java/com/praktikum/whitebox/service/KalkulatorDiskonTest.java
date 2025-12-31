package com.praktikum.whitebox.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Kalkulator Diskon - Path Coverage")
public class KalkulatorDiskonTest {

    private KalkulatorDiskon kalkulatorDiskon;

    @BeforeEach
    void setUp() {
        kalkulatorDiskon = new KalkulatorDiskon();
    }

    // --- Test hitungDiskon(harga, kuantitas, tipePelanggan) ---

    @ParameterizedTest
    @DisplayName("Test hitung diskon - berbagai kombinasi kuantitas dan tipe pelanggan")
    @CsvSource({
            // kuantitas, tipePelanggan, expectedDiskon (harga dasar 1000)
            "1,     'BARU',      20",     // Kuantitas: < 5 (0%) + Pelanggan: BARU (2%) = 2% dari 1000*1 = 20
            "5,     'BARU',      350",    // Kuantitas: >= 5 (5%) + Pelanggan: BARU (2%) = 7% dari 1000*5 = 350
            "10,    'REGULER',   1500",   // Kuantitas: >= 10 (10%) + Pelanggan: REGULER (5%) = 15% dari 1000*10 = 1500
            "50,    'PREMIUM',   12500",  // Kuantitas: >= 50 (15%) + Pelanggan: PREMIUM (10%) = 25% dari 1000*50 = 12500
            "100,   'PREMIUM',   30000",  // Kuantitas: >= 100 (20%) + Pelanggan: PREMIUM (10%) = 30% (maksimal) dari 1000*100 = 30000
            "200,   'PREMIUM',   60000"   // 30% (maksimal) dari 1000*200 = 60000
    })
    void testHitungDiskonVariousCases(int kuantitas, String tipePelanggan, double expectedDiskon) {
        double harga = 1000;
        double diskon = kalkulatorDiskon.hitungDiskon(harga, kuantitas, tipePelanggan);
        assertEquals(expectedDiskon, diskon, 0.001);
    }

    @Test
    @DisplayName("Test hitung diskon - parameter invalid (harus throw exception)")
    void testHitungDiskonInvalidParameters() {
        // Test 1: Harga negatif
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            kalkulatorDiskon.hitungDiskon(-1000, 5, "REGULER");
        });
        assertEquals("Harga dan kuantitas harus positif", exception.getMessage());

        // Test 2: Kuantitas nol
        exception = assertThrows(IllegalArgumentException.class, () -> {
            kalkulatorDiskon.hitungDiskon(1000, 0, "REGULER");
        });
        assertEquals("Harga dan kuantitas harus positif", exception.getMessage());
    }

    @Test
    @DisplayName("Test boundary values untuk kuantitas diskon")
    void testBoundaryValuesKuantitas() {
        double harga = 1000;
        String tipePelanggan = "BARU"; // Diskon dasar 2%

        // Boundary: 4 (5% off) -> 5 (mulai dapat diskon 5% = total 7%)
        double diskon4 = kalkulatorDiskon.hitungDiskon(harga, 4, tipePelanggan);
        double diskon5 = kalkulatorDiskon.hitungDiskon(harga, 5, tipePelanggan);
        assertTrue(diskon5 > diskon4, "Diskon 5 harus lebih besar dari diskon 4");

        // Boundary: 9 (7% off) -> 10 (naik ke diskon 10% = total 12%)
        double diskon9 = kalkulatorDiskon.hitungDiskon(harga, 9, tipePelanggan);
        double diskon10 = kalkulatorDiskon.hitungDiskon(harga, 10, tipePelanggan);
        assertTrue(diskon10 > diskon9, "Diskon 10 harus lebih besar dari diskon 9");

        // Boundary: 49 (12% off) -> 50 (naik ke diskon 15% = total 17%)
        double diskon49 = kalkulatorDiskon.hitungDiskon(harga, 49, tipePelanggan);
        double diskon50 = kalkulatorDiskon.hitungDiskon(harga, 50, tipePelanggan);
        assertTrue(diskon50 > diskon49, "Diskon 50 harus lebih besar dari diskon 49");

        // Boundary: 99 (17% off) -> 100 (naik ke diskon 20% = total 22%)
        double diskon99 = kalkulatorDiskon.hitungDiskon(harga, 99, tipePelanggan);
        double diskon100 = kalkulatorDiskon.hitungDiskon(harga, 100, tipePelanggan);
        assertTrue(diskon100 > diskon99, "Diskon 100 harus lebih besar dari diskon 99");
    }

    // --- Test hitungHargaSetelahDiskon ---

    @Test
    @DisplayName("Test hitung harga setelah diskon")
    void testHitungHargaSetelahDiskon() {
        double harga = 1000;
        int kuantitas = 10;
        String tipePelanggan = "REGULER"; // Diskon 15% total

        double hargaSetelahDiskon = kalkulatorDiskon.hitungHargaSetelahDiskon(harga, kuantitas, tipePelanggan);
        
        double expectedTotal = 1000 * 10;          // Total awal: 10000
        double expectedDiskon = expectedTotal * 0.15; // Diskon (10% + 5%): 1500
        double expectedHargaAkhir = expectedTotal - expectedDiskon; // Hasil: 8500

        assertEquals(expectedHargaAkhir, hargaSetelahDiskon, 0.001);
    }

    // --- Test getKategoriDiskon ---

    @ParameterizedTest
    @DisplayName("Test kategori diskon berdasarkan persentase")
    @CsvSource({
            "0.0,   'TANPA_DISKON'", // Batas bawah
            "0.05,  'DISKON_RINGAN'", // Di dalam range
            "0.09,  'DISKON_RINGAN'", // Batas atas (tepat di bawah 0.10)
            "0.10,  'DISKON_SEDANG'", // Batas bawah (tepat 0.10)
            "0.15,  'DISKON_SEDANG'", // Di dalam range
            "0.19,  'DISKON_SEDANG'", // Batas atas (tepat di bawah 0.20)
            "0.20,  'DISKON_BESAR'",  // Batas bawah (tepat 0.20)
            "0.25,  'DISKON_BESAR'",  // Di dalam range
            "0.30,  'DISKON_BESAR'"   // Maksimal
    })
    void testGetKategoriDiskon(double persentaseDiskon, String expectedKategori) {
        String kategori = kalkulatorDiskon.getKategoriDiskon(persentaseDiskon);
        assertEquals(expectedKategori, kategori);
    }
}