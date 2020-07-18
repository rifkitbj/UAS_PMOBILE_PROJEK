package com.example.databaseapiapp.model;

import java.sql.Date;

public class Jadwal {
    private Integer id;
    private String nama;
    private String tempat;
    private Date waktu;

    public Jadwal() {
        super();
    }

    public Jadwal(Integer id, String nama, String tempat, Date waktu) {
        super();
        this.id = id;
        this.nama = nama;
        this.tempat = tempat;
        this.waktu = waktu;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat = tempat;
    }

    public Date getWaktu() {
        return waktu;
    }

    public void setWaktu(Date waktu) {
        this.waktu = waktu;
    }
}
