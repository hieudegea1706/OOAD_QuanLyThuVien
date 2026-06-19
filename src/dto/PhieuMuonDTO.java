package dto;

import java.sql.Timestamp;

public class PhieuMuonDTO {
    private int idPhieuMuon;
    private String tenDangNhap;
    private String hoTen;
    private Timestamp ngayMuon;
    private Timestamp hanTra;
    private String trangThaiPhieu;
    private int soSach;

    public int getIdPhieuMuon() {
        return idPhieuMuon;
    }

    public void setIdPhieuMuon(int idPhieuMuon) {
        this.idPhieuMuon = idPhieuMuon;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public Timestamp getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(Timestamp ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public Timestamp getHanTra() {
        return hanTra;
    }

    public void setHanTra(Timestamp hanTra) {
        this.hanTra = hanTra;
    }

    public String getTrangThaiPhieu() {
        return trangThaiPhieu;
    }

    public void setTrangThaiPhieu(String trangThaiPhieu) {
        this.trangThaiPhieu = trangThaiPhieu;
    }

    public int getSoSach() {
        return soSach;
    }

    public void setSoSach(int soSach) {
        this.soSach = soSach;
    }
}