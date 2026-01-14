package com.example.datasantri;

public class Santri {
    private final String nama;
    private final String kelas;
    private final String asal;
    private final String noHp;

    public Santri(String nama, String kelas, String asal, String noHp) {
        this.nama = nama;
        this.kelas = kelas;
        this.asal = asal;
        this.noHp = noHp;
    }

    public String getNama() {
        return nama;
    }

    public String getKelas() {
        return kelas;
    }

    public String getAsal() {
        return asal;
    }

    public String getNoHp() {
        return noHp;
    }

    @Override
    public String toString() {
        return nama + " - Kelas " + kelas + " - " + asal + " - " + noHp;
    }
}
