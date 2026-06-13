package dto;

public class KetQuaDangNhap {
    private boolean thanhCong;
    private String thongBao;
    private String tenDangNhap;
    private String hoTen;
    private String vaiTro;

    private KetQuaDangNhap(boolean thanhCong, String thongBao, String tenDangNhap, String hoTen, String vaiTro) {
        this.thanhCong = thanhCong;
        this.thongBao = thongBao;
        this.tenDangNhap = tenDangNhap;
        this.hoTen = hoTen;
        this.vaiTro = vaiTro;
    }

    public static KetQuaDangNhap thanhCong(String tenDangNhap, String hoTen, String vaiTro) {
        return new KetQuaDangNhap(true, "Đăng nhập thành công", tenDangNhap, hoTen, vaiTro);
    }

    public static KetQuaDangNhap thatBai(String thongBao) {
        return new KetQuaDangNhap(false, thongBao, null, null, null);
    }

    public boolean isThanhCong() {
        return thanhCong;
    }

    public String getThongBao() {
        return thongBao;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getVaiTro() {
        return vaiTro;
    }
}