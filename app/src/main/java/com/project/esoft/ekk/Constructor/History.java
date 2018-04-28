package com.project.esoft.ekk.Constructor;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class History implements Serializable {

    private String notransaksi;
    private String tanggal;
    private String qty;
    private String status_penjualan;
    private String penjual;
    private String nama;
    private String jenis;


    public History(){

    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNotransaksi() {
        return notransaksi;
    }

    public void setNotransaksi(String notransaksi) {
        this.notransaksi = notransaksi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getPenjual() {
        return penjual;
    }

    public void setPenjual(String penjual) {
        this.penjual = penjual;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStatus_penjualan() {
        return status_penjualan;
    }

    public void setStatus_penjualan(String status_penjualan) {
        this.status_penjualan = status_penjualan;
    }

    @Override
    public String toString() {
        return " "+notransaksi+"\n" +
                " "+tanggal+"\n" +
                " "+ qty +"\n" +
                " "+ status_penjualan +"\n" +
                " "+ penjual ;
    }

    public History(String jenis, String nama, String penjual, String qty, String status_penjualan, String tanggal){
        this.jenis = jenis;
        this.nama = nama;
        this.penjual = penjual;
        this.qty = qty;
        this.status_penjualan = status_penjualan;
        this.tanggal = tanggal;
    }
}