package com.praktikum.whitebox.service;

import com.praktikum.whitebox.model.Produk;
import com.praktikum.whitebox.repository.RepositoryProduk;
import com.praktikum.whitebox.util.ValidationUtils;
import java.util.List;
import java.util.Optional;

/**
 * Kelas layanan (Service) untuk mengelola logika bisnis inventaris produk.
 * Kelas ini bergantung pada RepositoryProduk untuk operasi data.
 */
public class ServiceInventaris {

    private final RepositoryProduk repositoryProduk;

    /**
     * Constructor dengan Dependency Injection untuk RepositoryProduk.
     * @param repositoryProduk Implementasi dari RepositoryProduk.
     */
    public ServiceInventaris(RepositoryProduk repositoryProduk) {
        this.repositoryProduk = repositoryProduk;
    }

    /**
     * Menambahkan produk baru ke inventaris.
     * @param produk Objek Produk yang akan ditambahkan.
     * @return true jika produk berhasil ditambahkan, false jika validasi gagal atau kode produk sudah ada.
     */
    public boolean tambahProduk(Produk produk) {
        if (!ValidationUtils.isValidProduk(produk)) {
            return false;
        }
        // Cek apakah produk dengan kode yang sama sudah ada
        Optional<Produk> produkExist = repositoryProduk.cariByKode(produk.getKode());
        if (produkExist.isPresent()) {
            return false;
        }
        return repositoryProduk.simpan(produk);
    }

    /**
     * Menghapus produk berdasarkan kode.
     * Produk tidak dapat dihapus jika masih memiliki stok > 0.
     * @param kode Kode produk yang akan dihapus.
     * @return true jika produk berhasil dihapus, false jika validasi gagal, produk tidak ditemukan, atau stok > 0.
     */
    public boolean hapusProduk(String kode) {
        if (!ValidationUtils.isValidKodeProduk(kode)) {
            return false;
        }
        Optional<Produk> produk = repositoryProduk.cariByKode(kode);
        if (produk.isEmpty()) {
            return false;
        }
        // Tidak bisa hapus produk yang masih ada stoknya
        if (produk.get().getStok() > 0) {
            return false;
        }
        return repositoryProduk.hapus(kode);
    }

    /**
     * Mencari produk berdasarkan kode unik.
     * @param kode Kode produk.
     * @return Optional yang berisi Produk jika ditemukan dan valid, atau Optional.empty().
     */
    public Optional<Produk> cariProdukByKode(String kode) {
        if (!ValidationUtils.isValidKodeProduk(kode)) {
            return Optional.empty();
        }
        return repositoryProduk.cariByKode(kode);
    }

    public List<Produk> cariProdukByNama(String nama) {
        return repositoryProduk.cariByNama(nama);
    }

    public List<Produk> cariProdukByKategori(String kategori) {
        return repositoryProduk.cariByKategori(kategori);
    }

    /**
     * Memperbarui nilai stok produk secara langsung.
     * @param kode Kode produk.
     * @param stokBaru Nilai stok yang baru.
     * @return true jika berhasil diperbarui, false jika validasi gagal atau produk tidak ditemukan.
     */
    public boolean updateStok(String kode, int stokBaru) {
        if (!ValidationUtils.isValidKodeProduk(kode) || stokBaru < 0) {
            return false;
        }
        Optional<Produk> produk = repositoryProduk.cariByKode(kode);
        if (produk.isEmpty()) {
            return false;
        }
        return repositoryProduk.updateStok(kode, stokBaru);
    }

    /**
     * Melakukan transaksi pengurangan stok (penjualan/pengambilan).
     * @param kode Kode produk.
     * @param jumlah Jumlah yang akan dikeluarkan.
     * @return true jika stok berhasil dikurangi, false jika validasi gagal, produk tidak aktif/ditemukan, atau stok tidak mencukupi.
     */
    public boolean keluarStok(String kode, int jumlah) {
        if (!ValidationUtils.isValidKodeProduk(kode) || jumlah <= 0) {
            return false;
        }
        Optional<Produk> produkOpt = repositoryProduk.cariByKode(kode);
        if (produkOpt.isEmpty() || !produkOpt.get().isAktif()) {
            return false;
        }

        Produk produk = produkOpt.get();
        if (produk.getStok() < jumlah) {
            return false; // Stok tidak mencukupi
        }

        int stokBaru = produk.getStok() - jumlah;
        return repositoryProduk.updateStok(kode, stokBaru);
    }

    /**
     * Melakukan transaksi penambahan stok (pembelian/masuk).
     * @param kode Kode produk.
     * @param jumlah Jumlah yang akan dimasukkan.
     * @return true jika stok berhasil ditambahkan, false jika validasi gagal atau produk tidak aktif/ditemukan.
     */
    public boolean masukStok(String kode, int jumlah) {
        if (!ValidationUtils.isValidKodeProduk(kode) || jumlah <= 0) {
            return false;
        }
        Optional<Produk> produk = repositoryProduk.cariByKode(kode);
        if (produk.isEmpty() || !produk.get().isAktif()) {
            return false;
        }

        int stokBaru = produk.get().getStok() + jumlah;
        return repositoryProduk.updateStok(kode, stokBaru);
    }

    public List<Produk> getProdukStokMenipis() {
        return repositoryProduk.cariProdukStokMenipis();
    }

    public List<Produk> getProdukStokHabis() {
        return repositoryProduk.cariProdukStokHabis();
    }

    /**
     * Menghitung total nilai moneter dari semua inventaris yang aktif.
     * (Harga * Stok) untuk setiap produk.
     * @return Total nilai inventaris dalam bentuk double.
     */
    public double hitungTotalNilaiInventaris() {
        List<Produk> semuaProduk = repositoryProduk.cariSemua();
        return semuaProduk.stream()
                .filter(Produk::isAktif)
                .mapToDouble(p -> p.getHarga() * p.getStok())
                .sum();
    }

    /**
     * Menghitung total jumlah unit stok dari semua produk yang aktif.
     * @return Total stok dalam bentuk integer.
     */
    public int hitungTotalStok() {
        List<Produk> semuaProduk = repositoryProduk.cariSemua();
        return semuaProduk.stream()
                .filter(Produk::isAktif)
                .mapToInt(Produk::getStok)
                .sum();
    }
}