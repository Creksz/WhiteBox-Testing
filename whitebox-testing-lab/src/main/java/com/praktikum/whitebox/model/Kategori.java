package com.praktikum.whitebox.model;

import java.util.Objects;

public class Kategori {

    // Fields (Atribut)
    private String kode;
    private String nama;
    private String deskripsi;
    private boolean aktif;

    // Constructor Default
    public Kategori() {
        // Default constructor
    }

    // Constructor dengan Parameter
    public Kategori(String kode, String nama, String deskripsi) {
        this.kode = kode;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.aktif = true; // Kategori baru defaultnya aktif
    }

    // --- Getters and Setters ---

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    // --- Utility Methods (equals, hashCode, toString) ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kategori kategori = (Kategori) o;
        // Hanya membandingkan berdasarkan kode kategori
        return Objects.equals(kode, kategori.kode);
    }

    @Override
    public int hashCode() {
        // Hash code hanya berdasarkan kode kategori
        return Objects.hash(kode);
    }

    @Override
    public String toString() {
        return "Kategori{" +
                "kode='" + kode + '\'' +
                ", nama='" + nama + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", aktif=" + aktif +
                '}';
    }
}