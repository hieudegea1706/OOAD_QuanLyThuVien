package dto;

public class SachMuonDTO {
    private String idCaBiet;
    private String idDauSach;
    private String tenSach;
    private String trangThaiLuuThong;
    private String tinhTrangVatLy;
    private String viTriKe;

    private boolean coTheMuon;
    private String thongBao;

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

    public boolean isCoTheMuon() {
        return coTheMuon;
    }

    public void setCoTheMuon(boolean coTheMuon) {
        this.coTheMuon = coTheMuon;
    }

    public String getThongBao() {
        return thongBao;
    }

    public void setThongBao(String thongBao) {
        this.thongBao = thongBao;
    }
}