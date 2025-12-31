package com.praktikum.whitebox.repository;

import com.praktikum.whitebox.model.Produk;
import java.util.List;
import java.util.Optional;

/**
 * Interface untuk mendefinisikan operasi-operasi dasar (CRUD)
 * dan query khusus untuk entitas Produk.
 */
public interface RepositoryProduk {

    /**
     * Menyimpan produk baru atau memperbarui produk yang sudah ada.
     * @param produk Objek Produk yang akan disimpan.
     * @return true jika berhasil disimpan/diperbarui, false jika gagal.
     */
    boolean simpan(Produk produk);

    /**
     * Mencari produk berdasarkan kode uniknya.
     * @param kode Kode produk.
     * @return Optional yang berisi Produk jika ditemukan, atau Optional kosong.
     */
    Optional<Produk> cariByKode(String kode);

    /**
     * Mencari produk berdasarkan nama (pencarian parsial/fuzzy).
     * @param nama Nama produk atau bagian dari nama.
     * @return List Produk yang cocok.
     */
    List<Produk> cariByNama(String nama);

    /**
     * Mencari produk berdasarkan kategori.
     * @param kategori Kategori produk.
     * @return List Produk dalam kategori tersebut.
     */
    List<Produk> cariByKategori(String kategori);

    /**
     * Mencari semua produk yang stoknya menipis (stok <= stokMinimum).
     * @return List Produk yang stoknya menipis.
     */
    List<Produk> cariProdukStokMenipis();

    /**
     * Mencari semua produk yang stoknya habis (stok = 0).
     * @return List Produk yang stoknya habis.
     */
    List<Produk> cariProdukStokHabis();

    /**
     * Menghapus produk berdasarkan kode.
     * @param kode Kode produk yang akan dihapus.
     * @return true jika berhasil dihapus, false jika kode tidak ditemukan.
     */
    boolean hapus(String kode);

    /**
     * Memperbarui nilai stok produk tertentu.
     * @param kode Kode produk yang akan diperbarui.
     * @param stokBaru Nilai stok yang baru.
     * @return true jika berhasil diperbarui, false jika kode tidak ditemukan.
     */
    boolean updateStok(String kode, int stokBaru);

    /**
     * Mengambil semua produk yang ada.
     * @return List semua Produk.
     */
    List<Produk> cariSemua();
}