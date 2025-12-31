package com.praktikum.whitebox.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KategoriTest {

    private Kategori kategori;
    private final String KODE = "K01";
    private final String NAMA = "Elektronik";
    private final String DESKRIPSI = "Perangkat elektronik rumah tangga";

    @BeforeEach
    void setUp() {
        // Inisialisasi objek Kategori sebelum setiap test
        kategori = new Kategori(KODE, NAMA, DESKRIPSI);
    }

    @Test
    @DisplayName("Test Default Constructor")
    void testDefaultConstructor() {
        // Test ini memastikan constructor default menginisialisasi objek dengan benar
        Kategori kategoriDefault = new Kategori();
        assertNull(kategoriDefault.getKode(), "Kode harus null untuk constructor default");
        assertNull(kategoriDefault.getNama(), "Nama harus null untuk constructor default");
        assertNull(kategoriDefault.getDeskripsi(), "Deskripsi harus null untuk constructor default");
        assertFalse(kategoriDefault.isAktif(), "Status aktif harus false untuk constructor default");
    }

    @Test
    @DisplayName("Test Parameterized Constructor")
    void testParameterizedConstructor() {
        // Test ini memastikan constructor dengan parameter menginisialisasi semua field dengan benar
        assertEquals(KODE, kategori.getKode());
        assertEquals(NAMA, kategori.getNama());
        assertEquals(DESKRIPSI, kategori.getDeskripsi());
        assertTrue(kategori.isAktif(), "Kategori baru seharusnya default aktif (true)");
    }

    @Test
    @DisplayName("Test Getters and Setters")
    void testGettersAndSetters() {
        // Test ini memverifikasi fungsionalitas semua metode getter dan setter
        Kategori kat = new Kategori();

        kat.setKode("K02");
        assertEquals("K02", kat.getKode());

        kat.setNama("Buku");
        assertEquals("Buku", kat.getNama());

        kat.setDeskripsi("Berbagai macam buku");
        assertEquals("Berbagai macam buku", kat.getDeskripsi());

        kat.setAktif(false);
        assertFalse(kat.isAktif());
    }

    @Test
    @DisplayName("Test toString Method")
    void testToString() {
        // Test ini memastikan format output dari metode toString() sesuai harapan
        String expectedString = "Kategori{" +
                "kode='" + KODE + '\'' +
                ", nama='" + NAMA + '\'' +
                ", deskripsi='" + DESKRIPSI + '\'' +
                ", aktif=" + true +
                '}';
        assertEquals(expectedString, kategori.toString());
    }

    @Test
    @DisplayName("Test hashCode Consistency")
    void testHashCode() {
        // Test ini memverifikasi konsistensi hashCode berdasarkan 'kode'
        Kategori kategoriSama = new Kategori(KODE, "Nama Lain", "Deskripsi Lain");
        assertEquals(kategori.hashCode(), kategoriSama.hashCode(), "hashCode harus sama untuk objek dengan kode yang sama");

        Kategori kategoriBeda = new Kategori("K99", NAMA, DESKRIPSI);
        assertNotEquals(kategori.hashCode(), kategoriBeda.hashCode(), "hashCode harus berbeda untuk objek dengan kode yang berbeda");
        
        // Menangani kasus kode null
        Kategori kategoriNull1 = new Kategori();
        Kategori kategoriNull2 = new Kategori();
        assertEquals(kategoriNull1.hashCode(), kategoriNull2.hashCode(), "hashCode harus sama untuk objek dengan kode null");
    }

    @Test
    @DisplayName("Test equals Logic for All Branches")
    void testEqualsLogic() {
        // Test ini secara spesifik dirancang untuk mencakup SEMUA cabang di metode equals()

        // Cabang 1: Objek sama dengan dirinya sendiri (this == o) -> true
        assertTrue(kategori.equals(kategori), "Objek harus sama dengan dirinya sendiri");

        // Cabang 2: Objek dibandingkan dengan null (o == null) -> false
        assertFalse(kategori.equals(null), "Objek tidak boleh sama dengan null");

        // Cabang 3: Objek dibandingkan dengan kelas yang berbeda (getClass() != o.getClass()) -> false
        assertFalse(kategori.equals(new String("test")), "Objek tidak boleh sama dengan objek dari kelas yang berbeda");

        // Cabang 4: Objek dengan kode yang sama -> true
        Kategori kategoriSama = new Kategori(KODE, "Nama Lain", "Deskripsi Lain");
        assertTrue(kategori.equals(kategoriSama), "Objek dengan kode yang sama harus dianggap sama");

        // Cabang 5: Objek dengan kode yang berbeda -> false
        Kategori kategoriBeda = new Kategori("K99", NAMA, DESKRIPSI);
        assertFalse(kategori.equals(kategoriBeda), "Objek dengan kode yang berbeda tidak boleh sama");
        
        // Cabang Tambahan: Menangani kode yang null
        Kategori kategoriKodeNull1 = new Kategori();
        Kategori kategoriKodeNull2 = new Kategori();
        assertTrue(kategoriKodeNull1.equals(kategoriKodeNull2), "Dua objek dengan kode null harus dianggap sama");
        assertFalse(kategori.equals(kategoriKodeNull1), "Objek dengan kode non-null tidak boleh sama dengan objek dengan kode null");
    }
}