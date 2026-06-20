package utils;

public class AppSession {

    private static String tenDangNhap;
    private static String hoTen;
    private static String vaiTro;

    public static void dangNhap(String tenDangNhapMoi, String hoTenMoi, String vaiTroMoi) {
        tenDangNhap = tenDangNhapMoi;
        hoTen = hoTenMoi;
        vaiTro = vaiTroMoi;
    }

    public static void dangXuat() {
        tenDangNhap = null;
        hoTen = null;
        vaiTro = null;
    }

    public static boolean daDangNhap() {
        return tenDangNhap != null && !tenDangNhap.trim().isEmpty();
    }

    public static String getTenDangNhap() {
        return tenDangNhap;
    }

    public static String getHoTen() {
        return hoTen;
    }

    public static String getVaiTro() {
        return vaiTro;
    }
}