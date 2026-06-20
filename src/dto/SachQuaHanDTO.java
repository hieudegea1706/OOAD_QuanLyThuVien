package dto;

import java.sql.Timestamp;

public class SachQuaHanDTO {
    private int idPhieuMuon;
    private String hoTen;
    private String idCaBiet;
    private String tenSach;
    private Timestamp hanTra;
    private int soNgayQuaHan;

    public int getIdPhieuMuon() {
        return idPhieuMuon;
    }

    public void setIdPhieuMuon(int idPhieuMuon) {
        this.idPhieuMuon = idPhieuMuon;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getIdCaBiet() {
        return idCaBiet;
    }

    public void setIdCaBiet(String idCaBiet) {
        this.idCaBiet = idCaBiet;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public Timestamp getHanTra() {
        return hanTra;
    }

    public void setHanTra(Timestamp hanTra) {
        this.hanTra = hanTra;
    }

    public int getSoNgayQuaHan() {
        return soNgayQuaHan;
    }

    public void setSoNgayQuaHan(int soNgayQuaHan) {
        this.soNgayQuaHan = soNgayQuaHan;
    }
}