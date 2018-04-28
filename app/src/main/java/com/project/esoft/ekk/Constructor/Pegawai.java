package com.project.esoft.ekk.Constructor;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Herdi_WORK on 18.06.17.
 */

@IgnoreExtraProperties
public class Pegawai implements Serializable{

    private String uid;
    private String nama;
    private String noid;
    private String email;
    private String status;
    private String type;

    public Pegawai(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoid() {
        return noid;
    }

    public void setNoid(String noid) {
        this.noid = noid;
    }

    @Override
    public String toString() {
        return " "+uid+"\n" +
                " "+noid+"\n" +
                " "+ nama +"\n" +
                " "+ email +"\n" +
                " "+ type;
    }

    public Pegawai(String noid_, String nm, String mail, String stat, String tipe){
        nama = nm;
        email = mail;
        noid = noid_;
        status = stat;
        type=tipe;
    }
}