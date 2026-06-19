package dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PhieuPhatDTO {
    private int idPhieuPhat;
    private int idPhieuMuon;
    private String tenDangNhap;
    private String hoTen;
    private String idCaBiet;
    private String tenSach;
    private String loaiViPham;
    private int soNgayQuaHan;
    private BigDecimal soTienPhat;
    private String trangThaiThanhToan;
    private Timestamp ngayLap;
    private String moTaViPham;

    public int getIdPhieuPhat() {
        return idPhieuPhat;
    }

    public void setIdPhieuPhat(int idPhieuPhat) {
        this.idPhieuPhat = idPhieuPhat;
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

    public String getLoaiViPham() {
        return loaiViPham;
    }

    public void setLoaiViPham(String loaiViPham) {
        this.loaiViPham = loaiViPham;
    }

    public int getSoNgayQuaHan() {
        return soNgayQuaHan;
    }

    public void setSoNgayQuaHan(int soNgayQuaHan) {
        this.soNgayQuaHan = soNgayQuaHan;
    }

    public BigDecimal getSoTienPhat() {
        return soTienPhat;
    }

    public void setSoTienPhat(BigDecimal soTienPhat) {
        this.soTienPhat = soTienPhat;
    }

    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(String trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public Timestamp getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Timestamp ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getMoTaViPham() {
        return moTaViPham;
    }

    public void setMoTaViPham(String moTaViPham) {
        this.moTaViPham = moTaViPham;
    }
}