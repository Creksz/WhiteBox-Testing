package com.praktikum.whitebox.service;

/**
 * Kelas layanan untuk menghitung diskon dan harga total berdasarkan
 * harga, kuantitas, dan tipe pelanggan.
 */
public class KalkulatorDiskon {

    /**
     * Menghitung total nilai diskon (dalam Rupiah/Mata Uang) yang diberikan.
     * @param harga Harga per unit.
     * @param kuantitas Jumlah unit.
     * @param tipePelanggan Tipe pelanggan ("PREMIUM", "REGULER", "BARU", atau lainnya).
     * @return Nilai diskon total.
     * @throws IllegalArgumentException jika harga atau kuantitas tidak positif.
     */
    public double hitungDiskon(double harga, int kuantitas, String tipePelanggan) {
        if (harga <= 0 || kuantitas <= 0) {
            throw new IllegalArgumentException("Harga dan kuantitas harus positif");
        }

        double persentaseDiskon = 0.0;

        // --- Diskon berdasarkan kuantitas ---
        if (kuantitas >= 100) {
            persentaseDiskon += 0.20; // 20%
        } else if (kuantitas >= 50) {
            persentaseDiskon += 0.15; // 15%
        } else if (kuantitas >= 10) {
            persentaseDiskon += 0.10; // 10%
        } else if (kuantitas >= 5) {
            persentaseDiskon += 0.05; // 5%
        }

        // --- Diskon berdasarkan tipe pelanggan ---
        if ("PREMIUM".equalsIgnoreCase(tipePelanggan)) {
            persentaseDiskon += 0.10; // 10% tambahan
        } else if ("REGULER".equalsIgnoreCase(tipePelanggan)) {
            persentaseDiskon += 0.05; // 5% tambahan
        } else if ("BARU".equalsIgnoreCase(tipePelanggan)) {
            persentaseDiskon += 0.02; // 2% tambahan
        }

        // Maksimal diskon 30%
        persentaseDiskon = Math.min(persentaseDiskon, 0.30);
        
        // Menghitung nilai diskon dalam mata uang
        return harga * kuantitas * persentaseDiskon;
    }

    /**
     * Menghitung harga total yang harus dibayar setelah diskon diterapkan.
     * @param harga Harga per unit.
     * @param kuantitas Jumlah unit.
     * @param tipePelanggan Tipe pelanggan.
     * @return Harga total setelah diskon.
     * @throws IllegalArgumentException jika harga atau kuantitas tidak positif (dipicu oleh hitungDiskon).
     */
    public double hitungHargaSetelahDiskon(double harga, int kuantitas, String tipePelanggan) {
        double totalSebelumDiskon = harga * kuantitas;
        double nilaiDiskon = hitungDiskon(harga, kuantitas, tipePelanggan);
        
        return totalSebelumDiskon - nilaiDiskon;
    }

    /**
     * Menentukan kategori diskon berdasarkan persentase diskon yang diberikan.
     * @param persentaseDiskon Persentase diskon (dalam bentuk desimal, mis. 0.15).
     * @return Kategori diskon dalam bentuk String.
     */
    public String getKategoriDiskon(double persentaseDiskon) {
        if (persentaseDiskon <= 0) {
            return "TANPA_DISKON";
        } else if (persentaseDiskon < 0.10) { // Diskon 0.01 - 0.0999...
            return "DISKON_RINGAN";
        } else if (persentaseDiskon < 0.20) { // Diskon 0.10 - 0.1999...
            return "DISKON_SEDANG";
        } else { // Diskon 0.20 ke atas
            return "DISKON_BESAR";
        }
    }
}