package dto;

public class TopSachMuonDTO {
    private String idDauSach;
    private String tenSach;
    private String tacGia;
    private String theLoai;
    private int soLuotMuon;

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

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public int getSoLuotMuon() {
        return soLuotMuon;
    }

    public void setSoLuotMuon(int soLuotMuon) {
        this.soLuotMuon = soLuotMuon;
    }
}