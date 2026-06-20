package dto;

import java.sql.Timestamp;

public class CuonSachDTO {
    private String idCaBiet;
    private String idDauSach;
    private String tenSach;
    private String trangThaiLuuThong;
    private String tinhTrangVatLy;
    private String viTriKe;
    private Timestamp ngayNhapKho;
    private String ghiChu;

    public String getIdCaBiet() {
        return idCaBiet;
    }

    public void setIdCaBiet(String idCaBiet) {
        this.idCaBiet = idCaBiet;
    }

    public String getIdDauSach() {
        return idDauSach;
    }

    public void setIdDauSach(String idDauSach) {
        this.idDauSach = idDauSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTrangThaiLuuThong() {
        return trangThaiLuuThong;
    }

    public void setTrangThaiLuuThong(String trangThaiLuuThong) {
        this.trangThaiLuuThong = trangThaiLuuThong;
    }

    public String getTinhTrangVatLy() {
        return tinhTrangVatLy;
    }

    public void setTinhTrangVatLy(String tinhTrangVatLy) {
        this.tinhTrangVatLy = tinhTrangVatLy;
    }

    public String getViTriKe() {
        return viTriKe;
    }

    public void setViTriKe(String viTriKe) {
        this.viTriKe = viTriKe;
    }

    public Timestamp getNgayNhapKho() {
        return ngayNhapKho;
    }

    public void setNgayNhapKho(Timestamp ngayNhapKho) {
        this.ngayNhapKho = ngayNhapKho;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}