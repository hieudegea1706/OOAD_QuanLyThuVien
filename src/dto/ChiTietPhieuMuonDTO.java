package dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ChiTietPhieuMuonDTO {
    private String idCaBiet;
    private String tenSach;
    private Timestamp ngayTraThucTe;
    private String trangThaiChiTiet;
    private String ghiChuChiTiet;
    private String loaiViPham;
    private BigDecimal soTienPhat;
    private String trangThaiThanhToan;

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

    public Timestamp getNgayTraThucTe() {
        return ngayTraThucTe;
    }

    public void setNgayTraThucTe(Timestamp ngayTraThucTe) {
        this.ngayTraThucTe = ngayTraThucTe;
    }

    public String getTrangThaiChiTiet() {
        return trangThaiChiTiet;
    }

    public void setTrangThaiChiTiet(String trangThaiChiTiet) {
        this.trangThaiChiTiet = trangThaiChiTiet;
    }

    public String getGhiChuChiTiet() {
        return ghiChuChiTiet;
    }

    public void setGhiChuChiTiet(String ghiChuChiTiet) {
        this.ghiChuChiTiet = ghiChuChiTiet;
    }

    public String getLoaiViPham() {
        return loaiViPham;
    }

    public void setLoaiViPham(String loaiViPham) {
        this.loaiViPham = loaiViPham;
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
}