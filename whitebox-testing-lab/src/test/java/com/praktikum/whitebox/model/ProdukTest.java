package com.praktikum.whitebox.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Class Produk - White Box Testing")
public class ProdukTest {

    private Produk produk;

    @BeforeEach
    void setUp() {
        // Setup objek Produk awal sebelum setiap test
        produk = new Produk("PROD001",
                            "Laptop Gaming",
                            "Elektronik",
                            15000000,
                            10, // Stok awal
                            5);  // Stok minimum
    }

    // --- Testing Status Stok (isStokAman, isStokMenipis, isStokHabis) ---

    @Test
    @DisplayName("Test status stok - stok aman (stok > stokMinimum)")
    void testStokAman() {
        produk.setStok(10);
        produk.setStokMinimum(5);
        
        assertTrue(produk.isStokAman());
        assertFalse(produk.isStokMenipis());
        assertFalse(produk.isStokHabis());
    }

    @Test
    @DisplayName("Test status stok - stok menipis (0 < stok <= stokMinimum)")
    void testStokMenipis() {
        produk.setStok(5); // Kasus batas: stok = stokMinimum
        produk.setStokMinimum(5);

        assertFalse(produk.isStokAman());
        assertTrue(produk.isStokMenipis());
        assertFalse(produk.isStokHabis());
    }

    @Test
    @DisplayName("Test status stok - stok habis (stok = 0)")
    void testStokHabis() {
        produk.setStok(0);
        produk.setStokMinimum(5);
        
        assertFalse(produk.isStokAman());
        assertFalse(produk.isStokMenipis());
        assertTrue(produk.isStokHabis());
    }

    // --- Testing Business Logic: kurangiStok ---

    @ParameterizedTest
    @DisplayName("Test kurangi stok dengan berbagai nilai valid")
    @CsvSource({
            "5, 5",  // kurangi 5 dari 10, sisa 5
            "3, 7",  // kurangi 3 dari 10, sisa 7
            "10, 0" // kurangi semua stok
    })
    void testKurangiStokValid(int jumlah, int expectedStok) {
        produk.kurangiStok(jumlah);
        assertEquals(expectedStok, produk.getStok());
    }

    @Test
    @DisplayName("Test kurangi stok - jumlah negatif (harus throw exception)")
    void testKurangiStokNegatif() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produk.kurangiStok(-5);
        });
        assertEquals("Jumlah harus positif", exception.getMessage());
    }

    @Test
    @DisplayName("Test kurangi stok - stok tidak mencukupi (harus throw exception)")
    void testKurangiStokTidakMencukupi() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produk.kurangiStok(15); // Stok awal 10
        });
        assertEquals("Stok tidak mencukupi", exception.getMessage());
    }

    // --- Testing Business Logic: tambahStok ---

    @Test
    @DisplayName("Test tambah stok valid")
    void testTambahStokValid() {
        produk.tambahStok(5); // Stok awal 10 + 5
        assertEquals(15, produk.getStok());
    }

    @Test
    @DisplayName("Test tambah stok - jumlah negatif (harus throw exception)")
    void testTambahStokNegatif() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produk.tambahStok(-5);
        });
        assertEquals("Jumlah harus positif", exception.getMessage());
    }

    // --- Testing Business Logic: hitungTotalHarga ---

    @ParameterizedTest
    @DisplayName("Test hitung total harga dengan berbagai kuantitas")
    @CsvSource({
            "1, 15000000",
            "2, 30000000",
            "5, 75000000"
    })
    void testHitungTotalHarga(int jumlah, double expectedTotal) {
        double total = produk.hitungTotalHarga(jumlah);
        // Menggunakan delta 0.001 untuk perbandingan double
        assertEquals(expectedTotal, total, 0.001);
    }

    @Test
    @DisplayName("Test hitung total harga - jumlah negatif (harus throw exception)")
    void testHitungTotalHargaNegatif() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produk.hitungTotalHarga(-1);
        });
        assertEquals("Jumlah harus positif", exception.getMessage());
    }

    // --- Testing Utility Methods ---

    @Test
    @DisplayName("Test equals dan hashCode berdasarkan kode")
    void testEqualsAndHashCode() {
        Produk produk1 = new Produk("PROD001", "Laptop A", "Elektronik", 1000000, 5, 2);
        Produk produk2 = new Produk("PROD001", "Laptop B", "Elektronik", 1200000, 3, 1);
        Produk produk3 = new Produk("PROD002", "Mouse", "Elektronik", 50000, 10, 5);

        // 1. assertEquals (Kode sama, atribut lain beda)
        assertEquals(produk1, produk2, "Produk dengan kode sama harus dianggap sama."); 
        
        // 2. assertNotEquals (Kode berbeda)
        assertNotEquals(produk1, produk3, "Produk dengan kode berbeda tidak boleh sama."); 
        
        // 3. HashCode (Harus sama jika equals true)
        assertEquals(produk1.hashCode(), produk2.hashCode(), "HashCode harus sama untuk objek yang equals.");
    }
}