package dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class LichSuMuonTraDocGiaDTO {

    private int idPhieuMuon;
    private String idCaBiet;
    private String tenSach;
    private Timestamp ngayMuon;
    private Timestamp hanTra;
    private Timestamp ngayTraThucTe;
    private String trangThaiChiTiet;
    private String loaiViPham;
    private BigDecimal soTienPhat;
    private String trangThaiThanhToan;

    public int getIdPhieuMuon() {
        return idPhieuMuon;
    }

    public void setIdPhieuMuon(int idPhieuMuon) {
        this.idPhieuMuon = idPhieuMuon;
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