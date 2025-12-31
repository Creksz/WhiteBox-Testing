package com.praktikum.whitebox.service;

import com.praktikum.whitebox.model.Produk;
import com.praktikum.whitebox.repository.RepositoryProduk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceInventarisTest {

    @Mock
    private RepositoryProduk repositoryProduk; // Dependensi yang di-mock

    @InjectMocks
    private ServiceInventaris serviceInventaris; // Kelas yang akan diuji, dengan mock di-inject

    private Produk produkValid;

    @BeforeEach
    void setUp() {
        // Membuat objek produk valid yang akan digunakan berulang kali
        produkValid = new Produk();
        produkValid.setKode("P001");
        produkValid.setNama("Laptop ABC");
        produkValid.setHarga(10000);
        produkValid.setStok(10);
        produkValid.setKategori("Elektronik");
        produkValid.setStokMinimum(2);
        produkValid.setAktif(true);
    }

    // === Test tambahProduk ===

    @Test
    @DisplayName("tambahProduk: Sukses menambahkan produk baru")
    void testTambahProduk_Success() {
        // Atur mock: produk belum ada dan simpan berhasil
        when(repositoryProduk.cariByKode("P001")).thenReturn(Optional.empty());
        when(repositoryProduk.simpan(produkValid)).thenReturn(true);

        assertTrue(serviceInventaris.tambahProduk(produkValid));

        verify(repositoryProduk).cariByKode("P001");
        verify(repositoryProduk).simpan(produkValid);
    }

    @Test
    @DisplayName("tambahProduk: Gagal karena produk tidak valid")
    void testTambahProduk_Gagal_ProdukTidakValid() {
        Produk produkTidakValid = new Produk(); // Kode null, tidak valid
        assertFalse(serviceInventaris.tambahProduk(produkTidakValid));
        verify(repositoryProduk, never()).cariByKode(any()); // Verifikasi tidak ada interaksi repo
    }

    @Test
    @DisplayName("tambahProduk: Gagal karena kode produk sudah ada")
    void testTambahProduk_Gagal_KodeSudahAda() {
        // Atur mock: produk sudah ada di repo
        when(repositoryProduk.cariByKode("P001")).thenReturn(Optional.of(produkValid));

        assertFalse(serviceInventaris.tambahProduk(produkValid));
        verify(repositoryProduk, never()).simpan(any()); // Verifikasi simpan() tidak pernah dipanggil
    }

    // === Test hapusProduk ===

    @Test
    @DisplayName("hapusProduk: Sukses menghapus produk (stok 0)")
    void testHapusProduk_Success() {
        produkValid.setStok(0);
        when(repositoryProduk.cariByKode("P001")).thenReturn(Optional.of(produkValid));
        when(repositoryProduk.hapus("P001")).thenReturn(true);

        assertTrue(serviceInventaris.hapusProduk("P001"));
        verify(repositoryProduk).hapus("P001");
    }

    @Test
    @DisplayName("hapusProduk: Gagal karena kode tidak valid")
    void testHapusProduk_Gagal_KodeTidakValid() {
        assertFalse(serviceInventaris.hapusProduk("P1")); // Kode terlalu pendek
        verify(repositoryProduk, never()).cariByKode(any());
    }

    @Test
    @DisplayName("hapusProduk: Gagal karena produk tidak ditemukan")
    void testHapusProduk_Gagal_TidakDitemukan() {
        when(repositoryProduk.cariByKode("P001")).thenReturn(Optional.empty());
        assertFalse(serviceInventaris.hapusProduk("P001"));
        verify(repositoryProduk, never()).hapus(any());
    }

    @Test
    @DisplayName("hapusProduk: Gagal karena stok masih ada (> 0)")
    void testHapusProduk_Gagal_StokMasihAda() {
        produkValid.setStok(5); // Stok > 0
        when(repositoryProduk.cariByKode("P001")).thenReturn(Optional.of(produkValid));
        assertFalse(serviceInventaris.hapusProduk("P001"));
        verify(repositoryProduk, never()).hapus(any());
    }
    
    // === Test cariProdukByKode ===

    @Test
    @DisplayName("cariProdukByKode: Sukses menemukan produk")
    void testCariProdukByKode_Success() {
        when(repositoryProduk.cariByKode("P001")).thenReturn(Optional.of(produkValid));
        Optional<Produk> hasil = serviceInventaris.cariProdukByKode("P001");
        assertTrue(hasil.isPresent());
        assertEquals("Laptop ABC", hasil.get().getNama());
    }

    @Test
    @DisplayName("cariProdukByKode: Gagal karena kode tidak valid")
    void testCariProdukByKode_KodeTidakValid() {
        Optional<Produk> hasil = serviceInventaris.cariProdukByKode("P1");
        assertTrue(hasil.isEmpty());
    }

    // === Test updateStok ===
    
    @Test
    @DisplayName("updateStok: Sukses memperbarui stok")
    void testUpdateStok_Success() {
        when(repositoryProduk.cariByKode("P001")).thenReturn(Optional.of(produkValid));
        when(repositoryProduk.updateStok("P001", 50)).thenReturn(true);
        assertTrue(serviceInventaris.updateStok("P001", 50));
        verify(repositoryProduk).updateStok("P001", 50);
    }
    
    @Test
    @DisplayName("updateStok: Gagal karena input tidak valid")
    void testUpdateStok_Gagal_InputTidakValid() {
        assertFalse(serviceInventaris.updateStok("P1", 50)); // Kode tidak valid
        assertFalse(serviceInventaris.updateStok("P001", -1)); // Stok tidak valid
        verify(repositoryProduk, never()).cariByKode(anyString());
    }
    
    @Test
    @DisplayName("updateStok: Gagal karena produk tidak ditemukan")
    void testUpdateStok_Gagal_TidakDitemukan() {
        when(repositoryProduk.cariByKode("P001")).thenReturn(Optional.empty());
        assertFalse(serviceInventaris.updateStok("P001", 50));
        verify(repositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    // === Test keluarStok ===

    @Test
    @DisplayName("keluarStok: Sukses mengurangi stok")
    void testKeluarStok_Success() {
        when(repositoryProduk.cariByKode("P001")).thenReturn(Optional.of(produkValid));
        when(repositoryProduk.updateStok("P001", 5)).thenReturn(true); // 10 - 5 = 5
        assertTrue(serviceInventaris.keluarStok("P001", 5));
        verify(repositoryProduk).updateStok("P001", 5);
    }

    @Test
    @DisplayName("keluarStok: Gagal karena input tidak valid")
    void testKeluarStok_Gagal_InputTidakValid() {
        assertFalse(serviceInventaris.keluarStok("P1", 5)); // Kode tidak valid
        assertFalse(serviceInventaris.keluarStok("P001", 0)); // Jumlah tidak valid
        assertFalse(serviceInventaris.keluarStok("P001", -1)); // Jumlah tidak valid
    }

    @Test
    @DisplayName("keluarStok: Gagal karena produk tidak aktif atau tidak ditemukan")
    void testKeluarStok_Gagal_ProdukTidakAktifAtauDitemukan() {
        // Tidak ditemukan
        when(repositoryProduk.cariByKode("P999")).thenReturn(Optional.empty());
        assertFalse(serviceInventaris.keluarStok("P999", 5));

        // Ditemukan tapi tidak aktif
        produkValid.setAktif(false);
        when(repositoryProduk.cariByKode("P001")).thenReturn(Optional.of(produkValid));
        assertFalse(serviceInventaris.keluarStok("P001", 5));
    }
    
    @Test
    @DisplayName("keluarStok: Gagal karena stok tidak mencukupi")
    void testKeluarStok_Gagal_StokTidakCukup() {
        when(repositoryProduk.cariByKode("P001")).thenReturn(Optional.of(produkValid));
        // Stok produk 10, mau keluar 11
        assertFalse(serviceInventaris.keluarStok("P001", 11));
        verify(repositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    // === Test masukStok ===
    
    @Test
    @DisplayName("masukStok: Sukses menambah stok")
    void testMasukStok_Success() {
        when(repositoryProduk.cariByKode("P001")).thenReturn(Optional.of(produkValid));
        when(repositoryProduk.updateStok("P001", 15)).thenReturn(true); // 10 + 5 = 15
        assertTrue(serviceInventaris.masukStok("P001", 5));
        verify(repositoryProduk).updateStok("P001", 15);
    }

    @Test
    @DisplayName("masukStok: Gagal karena produk tidak aktif atau tidak ditemukan")
    void testMasukStok_Gagal_ProdukTidakAktifAtauDitemukan() {
        // Tidak ditemukan
        when(repositoryProduk.cariByKode("P999")).thenReturn(Optional.empty());
        assertFalse(serviceInventaris.masukStok("P999", 5));

        // Ditemukan tapi tidak aktif
        produkValid.setAktif(false);
        when(repositoryProduk.cariByKode("P001")).thenReturn(Optional.of(produkValid));
        assertFalse(serviceInventaris.masukStok("P001", 5));
    }
    
    @Test
    @DisplayName("masukStok: Gagal karena input tidak valid")
    void testMasukStok_Gagal_InputTidakValid() {
        assertFalse(serviceInventaris.masukStok("P1", 5)); // Kode tidak valid
        assertFalse(serviceInventaris.masukStok("P001", 0)); // Jumlah tidak valid
    }

    // === Test Hitungan dan Pencarian Sederhana ===

    @Test
    @DisplayName("hitungTotalNilaiInventaris: Menghitung nilai dengan benar")
    void testHitungTotalNilaiInventaris() {
        Produk produk2 = new Produk();
        produk2.setHarga(5000);
        produk2.setStok(4);
        produk2.setAktif(true);

        Produk produk3 = new Produk(); // Produk tidak aktif
        produk3.setHarga(1000);
        produk3.setStok(100);
        produk3.setAktif(false);

        // Atur mock: repo mengembalikan list produk
        when(repositoryProduk.cariSemua()).thenReturn(List.of(produkValid, produk2, produk3));

        // Perhitungan: (10000 * 10) + (5000 * 4) = 100000 + 20000 = 120000
        // produk3 diabaikan karena tidak aktif
        double totalNilai = serviceInventaris.hitungTotalNilaiInventaris();
        assertEquals(120000.0, totalNilai);

        // Skenario list kosong
        when(repositoryProduk.cariSemua()).thenReturn(Collections.emptyList());
        assertEquals(0.0, serviceInventaris.hitungTotalNilaiInventaris());
    }

    @Test
    @DisplayName("hitungTotalStok: Menghitung total stok dengan benar")
    void testHitungTotalStok() {
        Produk produk2 = new Produk();
        produk2.setStok(4);
        produk2.setAktif(true);
        
        Produk produk3 = new Produk();
        produk3.setStok(100);
        produk3.setAktif(false); // Tidak aktif, diabaikan

        when(repositoryProduk.cariSemua()).thenReturn(List.of(produkValid, produk2, produk3));

        // Perhitungan: 10 + 4 = 14
        assertEquals(14, serviceInventaris.hitungTotalStok());

        // Skenario list kosong
        when(repositoryProduk.cariSemua()).thenReturn(Collections.emptyList());
        assertEquals(0, serviceInventaris.hitungTotalStok());
    }
    
    // Metode pass-through sederhana lainnya, cukup verifikasi pemanggilan
    @Test
    @DisplayName("Metode Pencarian: Memanggil metode repository yang sesuai")
    void testMetodePencarian() {
        serviceInventaris.cariProdukByNama("Laptop");
        verify(repositoryProduk).cariByNama("Laptop");

        serviceInventaris.cariProdukByKategori("Elektronik");
        verify(repositoryProduk).cariByKategori("Elektronik");

        serviceInventaris.getProdukStokMenipis();
        verify(repositoryProduk).cariProdukStokMenipis();

        serviceInventaris.getProdukStokHabis();
        verify(repositoryProduk).cariProdukStokHabis();
    }
}