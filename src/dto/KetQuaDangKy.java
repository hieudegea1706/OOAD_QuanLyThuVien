package dto;

public class KetQuaDangKy {
    private boolean thanhCong;
    private String thongBao;

    private KetQuaDangKy(boolean thanhCong, String thongBao) {
        this.thanhCong = thanhCong;
        this.thongBao = thongBao;
    }

    public static KetQuaDangKy thanhCong(String thongBao) {
        return new KetQuaDangKy(true, thongBao);
    }

    public static KetQuaDangKy thatBai(String thongBao) {
        return new KetQuaDangKy(false, thongBao);
    }

    public boolean isThanhCong() {
        return thanhCong;
    }

    public String getThongBao() {
        return thongBao;
    }
}