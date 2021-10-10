package com.example.pencatatanpenduduk.model;

public class Penduduk {

    String nama_lengkap, alamat, tanggal_lahir, tanggal_tercatat, Jenis_kelamin,nomorTlp, gaji, agama, hobi, foto;
    int _id;

    public Penduduk(int _id,String nama_lengkap, String alamat, String tanggal_lahir, String tanggal_tercatat, String jenis_kelamin, String nomorTlp, String gaji, String agama, String hobi, String foto) {
        this._id = _id;
        this.nama_lengkap = nama_lengkap;
        this.alamat = alamat;
        this.tanggal_lahir = tanggal_lahir;
        this.tanggal_tercatat = tanggal_tercatat;
        Jenis_kelamin = jenis_kelamin;
        this.nomorTlp = nomorTlp;
        this.gaji = gaji;
        this.agama = agama;
        this.hobi = hobi;
        this.foto = foto;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public String getTanggal_tercatat() {
        return tanggal_tercatat;
    }

    public void setTanggal_tercatat(String tanggal_tercatat) {
        this.tanggal_tercatat = tanggal_tercatat;
    }

    public String getJenis_kelamin() {
        return Jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        Jenis_kelamin = jenis_kelamin;
    }

    public String getNomorTlp() {
        return nomorTlp;
    }

    public void setNomorTlp(String nomorTlp) {
        this.nomorTlp = nomorTlp;
    }

    public String getGaji() {
        return gaji;
    }

    public void setGaji(String gaji) {
        this.gaji = gaji;
    }

    public String getAgama() {
        return agama;
    }

    public void setAgama(String agama) {
        this.agama = agama;
    }

    public String getHobi() {
        return hobi;
    }

    public void setHobi(String hobi) {
        this.hobi = hobi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
