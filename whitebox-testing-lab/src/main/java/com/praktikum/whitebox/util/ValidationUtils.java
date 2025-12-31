package com.praktikum.whitebox.util;

import com.praktikum.whitebox.model.Kategori;
import com.praktikum.whitebox.model.Produk;

/**
 * Kelas utilitas statis untuk melakukan validasi berbagai properti
 * entitas seperti Produk dan Kategori.
 */
public class ValidationUtils {

    /**
     * Memvalidasi kode produk: tidak boleh kosong, 3-10 karakter,
     * hanya boleh huruf dan angka.
     */
    public static boolean isValidKodeProduk(String kode) {
        if (kode == null || kode.trim().isEmpty()) {
            return false;
        }
        String kodeBersih = kode.trim();
        // Regex: Hanya huruf (A-Z, a-z) dan angka (0-9), panjang 3 sampai 10.
        return kodeBersih.matches("^[A-Za-z0-9]{3,10}$");
    }

    /**
     * Memvalidasi nama (untuk Produk atau Kategori): tidak boleh kosong,
     * panjang 3-100 karakter.
     */
    public static boolean isValidNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            return false;
        }
        String namaBersih = nama.trim();
        return namaBersih.length() >= 3 && namaBersih.length() <= 100;
    }

    /**
     * Memvalidasi harga: harus bernilai positif (> 0).
     */
    public static boolean isValidHarga(double harga) {
        return harga > 0;
    }

    /**
     * Memvalidasi stok: harus bernilai non-negatif (>= 0).
     */
    public static boolean isValidStok(int stok) {
        return stok >= 0;
    }

    /**
     * Memvalidasi stok minimum: harus bernilai non-negatif (>= 0).
     */
    public static boolean isValidStokMinimum(int stokMinimum) {
        return stokMinimum >= 0;
    }

    /**
     * Memvalidasi semua properti wajib dari objek Produk.
     */
    public static boolean isValidProduk(Produk produk) {
        if (produk == null) {
            return false;
        }

        // Catatan: isValidStok dan isValidStokMinimum sudah melakukan cek >= 0.
        return isValidKodeProduk(produk.getKode()) &&
               isValidNama(produk.getNama()) &&
               // Asumsi nama kategori juga menggunakan aturan isValidNama
               isValidNama(produk.getKategori()) &&
               isValidHarga(produk.getHarga()) &&
               isValidStok(produk.getStok()) &&
               isValidStokMinimum(produk.getStokMinimum());
               // Baris produk.getStok() >= 0 dan produk.getStokMinimum() >= 0 
               // pada kode asli bersifat redundan karena sudah dicakup oleh isValidStok/Minimum.
    }

    /**
     * Memvalidasi semua properti wajib dari objek Kategori.
     */
    public static boolean isValidKategori(Kategori kategori) {
        if (kategori == null) {
            return false;
        }

        return isValidKodeProduk(kategori.getKode()) &&
               isValidNama(kategori.getNama()) &&
               // Deskripsi boleh null atau harus <= 500 karakter
               (kategori.getDeskripsi() == null ||
                kategori.getDeskripsi().length() <= 500);
    }

    /**
     * Memvalidasi persentase: harus dalam rentang 0 hingga 100.
     */
    public static boolean isValidPersentase(double persentase) {
        return persentase >= 0 && persentase <= 100;
    }

    /**
     * Memvalidasi kuantitas: harus bernilai positif (> 0).
     */
    public static boolean isValidKuantitas(int kuantitas) {
        return kuantitas > 0;
    }
}