package com.praktikum.whitebox.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.praktikum.whitebox.model.Kategori;
import com.praktikum.whitebox.model.Produk;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    // --- isValidKodeProduk ---
    @Test
    @DisplayName("isValidKodeProduk: Kode valid")
    void testIsValidKodeProduk_Valid() {
        assertTrue(ValidationUtils.isValidKodeProduk("P001"));
        assertTrue(ValidationUtils.isValidKodeProduk("PROD123456")); // 10 chars
        assertTrue(ValidationUtils.isValidKodeProduk("  ABC  ")); // With spaces
    }

    @Test
    @DisplayName("isValidKodeProduk: Kode tidak valid (null, kosong, pendek, panjang, simbol)")
    void testIsValidKodeProduk_Invalid() {
        assertFalse(ValidationUtils.isValidKodeProduk(null)); // Covers null branch
        assertFalse(ValidationUtils.isValidKodeProduk("")); // Covers empty branch
        assertFalse(ValidationUtils.isValidKodeProduk("   ")); // Covers blank branch
        assertFalse(ValidationUtils.isValidKodeProduk("P1")); // Too short
        assertFalse(ValidationUtils.isValidKodeProduk("PROD1234567")); // Too long
        assertFalse(ValidationUtils.isValidKodeProduk("P-001")); // Invalid characters
    }

    // --- isValidNama ---
    @Test
    @DisplayName("isValidNama: Nama valid")
    void testIsValidNama_Valid() {
        assertTrue(ValidationUtils.isValidNama("Buku Tulis"));
        assertTrue(ValidationUtils.isValidNama("ABC")); // Min length
        assertTrue(ValidationUtils.isValidNama("a".repeat(100))); // Max length
    }

    @Test
    @DisplayName("isValidNama: Nama tidak valid (null, kosong, pendek, panjang)")
    void testIsValidNama_Invalid() {
        assertFalse(ValidationUtils.isValidNama(null));
        assertFalse(ValidationUtils.isValidNama(""));
        assertFalse(ValidationUtils.isValidNama("  "));
        assertFalse(ValidationUtils.isValidNama("AB")); // Too short
        assertFalse(ValidationUtils.isValidNama("a".repeat(101))); // Too long
    }

    // --- isValidHarga ---
    @Test
    @DisplayName("isValidHarga: Harga valid dan tidak valid")
    void testIsValidHarga() {
        assertTrue(ValidationUtils.isValidHarga(1000.0));
        assertFalse(ValidationUtils.isValidHarga(0.0)); // Boundary condition
        assertFalse(ValidationUtils.isValidHarga(-500.0));
    }

    // --- isValidStok ---
    @Test
    @DisplayName("isValidStok: Stok valid dan tidak valid")
    void testIsValidStok() {
        assertTrue(ValidationUtils.isValidStok(10));
        assertTrue(ValidationUtils.isValidStok(0)); // Boundary condition
        assertFalse(ValidationUtils.isValidStok(-1));
    }

    // --- isValidStokMinimum ---
    @Test
    @DisplayName("isValidStokMinimum: Stok minimum valid dan tidak valid")
    void testIsValidStokMinimum() {
        assertTrue(ValidationUtils.isValidStokMinimum(5));
        assertTrue(ValidationUtils.isValidStokMinimum(0)); // Boundary condition
        assertFalse(ValidationUtils.isValidStokMinimum(-1));
    }

    // --- isValidProduk ---
    @Test
    @DisplayName("isValidProduk: Produk valid")
    void testIsValidProduk_Valid() {
        Produk produk = new Produk();
        produk.setKode("P001");
        produk.setNama("Laptop");
        produk.setKategori("Elektronik");
        produk.setHarga(15000000);
        produk.setStok(10);
        produk.setStokMinimum(2);
        assertTrue(ValidationUtils.isValidProduk(produk));
    }

    @Test
    @DisplayName("isValidProduk: Produk tidak valid karena berbagai alasan")
    void testIsValidProduk_Invalid() {
        // Case 1: Produk is null
        assertFalse(ValidationUtils.isValidProduk(null));

        // Create a valid base product
        Produk produk = new Produk();
        produk.setKode("P001");
        produk.setNama("Laptop");
        produk.setKategori("Elektronik");
        produk.setHarga(15000000);
        produk.setStok(10);
        produk.setStokMinimum(2);

        // Test each property's invalidity to cover all short-circuit paths
        produk.setKode("P1"); // Invalid code
        assertFalse(ValidationUtils.isValidProduk(produk));
        produk.setKode("P001"); // Reset

        produk.setNama("La"); // Invalid name
        assertFalse(ValidationUtils.isValidProduk(produk));
        produk.setNama("Laptop"); // Reset

        produk.setKategori("El"); // Invalid category name
        assertFalse(ValidationUtils.isValidProduk(produk));
        produk.setKategori("Elektronik"); // Reset

        produk.setHarga(0); // Invalid price
        assertFalse(ValidationUtils.isValidProduk(produk));
        produk.setHarga(15000000); // Reset

        produk.setStok(-1); // Invalid stock
        assertFalse(ValidationUtils.isValidProduk(produk));
        produk.setStok(10); // Reset

        produk.setStokMinimum(-1); // Invalid min stock
        assertFalse(ValidationUtils.isValidProduk(produk));
    }

    // --- isValidKategori ---
    @Test
    @DisplayName("isValidKategori: Kategori valid (dengan dan tanpa deskripsi)")
    void testIsValidKategori_Valid() {
        Kategori kategori1 = new Kategori();
        kategori1.setKode("KAT01");
        kategori1.setNama("Elektronik");
        kategori1.setDeskripsi("Perangkat elektronik modern."); // Deskripsi valid
        assertTrue(ValidationUtils.isValidKategori(kategori1));

        Kategori kategori2 = new Kategori();
        kategori2.setKode("KAT02");
        kategori2.setNama("Pakaian");
        kategori2.setDeskripsi(null); // Deskripsi null (valid)
        assertTrue(ValidationUtils.isValidKategori(kategori2));
    }

    @Test
    @DisplayName("isValidKategori: Kategori tidak valid")
    void testIsValidKategori_Invalid() {
        // Case 1: Kategori is null
        assertFalse(ValidationUtils.isValidKategori(null));

        Kategori kategori = new Kategori();
        kategori.setKode("KAT01");
        kategori.setNama("Elektronik");

        // Case 2: Invalid Kode
        kategori.setKode("K1");
        assertFalse(ValidationUtils.isValidKategori(kategori));
        kategori.setKode("KAT01"); // Reset

        // Case 3: Invalid Nama
        kategori.setNama("El");
        assertFalse(ValidationUtils.isValidKategori(kategori));
        kategori.setNama("Elektronik"); // Reset

        // Case 4: Invalid Deskripsi (terlalu panjang)
        kategori.setDeskripsi("a".repeat(501));
        assertFalse(ValidationUtils.isValidKategori(kategori));
    }
    
    // --- isValidPersentase ---
    @Test
    @DisplayName("isValidPersentase: Persentase valid dan tidak valid")
    void testIsValidPersentase() {
        assertTrue(ValidationUtils.isValidPersentase(0.0));
        assertTrue(ValidationUtils.isValidPersentase(50.5));
        assertTrue(ValidationUtils.isValidPersentase(100.0));
        assertFalse(ValidationUtils.isValidPersentase(-0.1));
        assertFalse(ValidationUtils.isValidPersentase(100.1));
    }
    
    // --- isValidKuantitas ---
    @Test
    @DisplayName("isValidKuantitas: Kuantitas valid dan tidak valid")
    void testIsValidKuantitas() {
        assertTrue(ValidationUtils.isValidKuantitas(1));
        assertFalse(ValidationUtils.isValidKuantitas(0));
        assertFalse(ValidationUtils.isValidKuantitas(-1));
    }
}