package dto;

import java.math.BigDecimal;

public class TongQuanThongKeDTO {
    private int tongDauSach;
    private int tongCuonSach;
    private int sachSanSang;
    private int sachDangMuon;
    private int phieuDangMuon;
    private int phieuPhatChuaThanhToan;
    private BigDecimal tienPhatChuaThu;
    private BigDecimal tienCocDangGiu;

    public int getTongDauSach() {
        return tongDauSach;
    }

    public void setTongDauSach(int tongDauSach) {
        this.tongDauSach = tongDauSach;
    }

    public int getTongCuonSach() {
        return tongCuonSach;
    }

    public void setTongCuonSach(int tongCuonSach) {
        this.tongCuonSach = tongCuonSach;
    }

    public int getSachSanSang() {
        return sachSanSang;
    }

    public void setSachSanSang(int sachSanSang) {
        this.sachSanSang = sachSanSang;
    }

    public int getSachDangMuon() {
        return sachDangMuon;
    }

    public void setSachDangMuon(int sachDangMuon) {
        this.sachDangMuon = sachDangMuon;
    }

    public int getPhieuDangMuon() {
        return phieuDangMuon;
    }

    public void setPhieuDangMuon(int phieuDangMuon) {
        this.phieuDangMuon = phieuDangMuon;
    }

    public int getPhieuPhatChuaThanhToan() {
        return phieuPhatChuaThanhToan;
    }

    public void setPhieuPhatChuaThanhToan(int phieuPhatChuaThanhToan) {
        this.phieuPhatChuaThanhToan = phieuPhatChuaThanhToan;
    }

    public BigDecimal getTienPhatChuaThu() {
        return tienPhatChuaThu;
    }

    public void setTienPhatChuaThu(BigDecimal tienPhatChuaThu) {
        this.tienPhatChuaThu = tienPhatChuaThu;
    }

    public BigDecimal getTienCocDangGiu() {
        return tienCocDangGiu;
    }

    public void setTienCocDangGiu(BigDecimal tienCocDangGiu) {
        this.tienCocDangGiu = tienCocDangGiu;
    }
}