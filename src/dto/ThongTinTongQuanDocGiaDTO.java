package dto;

import java.math.BigDecimal;

public class ThongTinTongQuanDocGiaDTO {

    private String tenDangNhap;
    private String hoTen;
    private String vaiTro;
    private String trangThaiTaiKhoan;
    private BigDecimal tienCoc;

    private int soSachDangMuon;
    private int soSachQuaHan;
    private int soPhieuPhatChuaThanhToan;
    private BigDecimal tienPhatChuaThanhToan;

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

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getTrangThaiTaiKhoan() {
        return trangThaiTaiKhoan;
    }

    public void setTrangThaiTaiKhoan(String trangThaiTaiKhoan) {
        this.trangThaiTaiKhoan = trangThaiTaiKhoan;
    }

    public BigDecimal getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(BigDecimal tienCoc) {
        this.tienCoc = tienCoc;
    }

    public int getSoSachDangMuon() {
        return soSachDangMuon;
    }

    public void setSoSachDangMuon(int soSachDangMuon) {
        this.soSachDangMuon = soSachDangMuon;
    }

    public int getSoSachQuaHan() {
        return soSachQuaHan;
    }

    public void setSoSachQuaHan(int soSachQuaHan) {
        this.soSachQuaHan = soSachQuaHan;
    }

    public int getSoPhieuPhatChuaThanhToan() {
        return soPhieuPhatChuaThanhToan;
    }

    public void setSoPhieuPhatChuaThanhToan(int soPhieuPhatChuaThanhToan) {
        this.soPhieuPhatChuaThanhToan = soPhieuPhatChuaThanhToan;
    }

    public BigDecimal getTienPhatChuaThanhToan() {
        return tienPhatChuaThanhToan;
    }

    public void setTienPhatChuaThanhToan(BigDecimal tienPhatChuaThanhToan) {
        this.tienPhatChuaThanhToan = tienPhatChuaThanhToan;
    }
}