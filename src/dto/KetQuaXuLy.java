package dto;

public class KetQuaXuLy {
    private boolean thanhCong;
    private String thongBao;

    private KetQuaXuLy(boolean thanhCong, String thongBao) {
        this.thanhCong = thanhCong;
        this.thongBao = thongBao;
    }

    public static KetQuaXuLy thanhCong(String thongBao) {
        return new KetQuaXuLy(true, thongBao);
    }

    public static KetQuaXuLy thatBai(String thongBao) {
        return new KetQuaXuLy(false, thongBao);
    }

    public boolean isThanhCong() {
        return thanhCong;
    }

    public String getThongBao() {
        return thongBao;
    }
}