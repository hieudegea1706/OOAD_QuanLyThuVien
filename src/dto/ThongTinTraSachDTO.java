package dto;

import java.sql.Timestamp;

public class ThongTinTraSachDTO {
    private boolean timThay;
    private String thongBao;

    private int idPhieuMuon;
    private String tenDangNhap;
    private String hoTen;
    private String idCaBiet;
    private String tenSach;
    private Timestamp ngayMuon;
    private Timestamp hanTra;
    private int soNgayQuaHan;
    private String trangThaiChiTiet;

    public boolean isTimThay() {
        return timThay;
    }

    public void setTimThay(boolean timThay) {
        this.timThay = timThay;
    }

    public String getThongBao() {
        return thongBao;
    }

    public void setThongBao(String thongBao) {
        this.thongBao = thongBao;
    }

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

    public int getSoNgayQuaHan() {
        return soNgayQuaHan;
    }

    public void setSoNgayQuaHan(int soNgayQuaHan) {
        this.soNgayQuaHan = soNgayQuaHan;
    }

    public String getTrangThaiChiTiet() {
        return trangThaiChiTiet;
    }

    public void setTrangThaiChiTiet(String trangThaiChiTiet) {
        this.trangThaiChiTiet = trangThaiChiTiet;
    }
}