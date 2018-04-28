package com.project.esoft.ekk.Constructor;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class DataPenjualan implements Serializable {

    private String typeJual;
    private String durasi;
    private String mulai;
    private String target;
    private String terjual;

    public DataPenjualan(){

    }

    public String getTypeJual() {
        return typeJual;
    }

    public void setTypeJual(String typeJual) {
        this.typeJual = typeJual;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }

    public String getMulai() {
        return mulai;
    }

    public void setMulai(String mulai) {
        this.mulai = mulai;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTerjual() {
        return terjual;
    }

    public void setTerjual(String terjual) {
        this.terjual = terjual;
    }

    @Override
    public String toString() {
        return " "+typeJual+"\n" +
                " "+durasi+"\n" +
                " "+mulai+"\n" +
                " "+target+"\n" +
                " "+ terjual ;
    }

    public DataPenjualan(String durasi, String mulai, String target, String terjual){
        this.durasi = durasi;
        this.mulai = mulai;
        this.target = target;
        this.terjual = terjual;
    }
}