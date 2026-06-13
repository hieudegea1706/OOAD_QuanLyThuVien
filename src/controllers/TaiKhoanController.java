package controllers;

import dao.TaiKhoanDAO;
import dto.KetQuaDangKy;

public class TaiKhoanController {

    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    public KetQuaDangKy dangKyDocGiaNgoai(
            String hoTen,
            String cccd,
            String sdt,
            String matKhau,
            String xacNhanMatKhau
    ) {
        try {
            hoTen = hoTen == null ? "" : hoTen.trim();
            cccd = cccd == null ? "" : cccd.trim();
            sdt = sdt == null ? "" : sdt.trim();
            matKhau = matKhau == null ? "" : matKhau;
            xacNhanMatKhau = xacNhanMatKhau == null ? "" : xacNhanMatKhau;

            if (hoTen.isEmpty() || cccd.isEmpty() || sdt.isEmpty() || matKhau.isEmpty()) {
                return KetQuaDangKy.thatBai("Vui lòng nhập đầy đủ thông tin!");
            }

            if (!matKhau.equals(xacNhanMatKhau)) {
                return KetQuaDangKy.thatBai("Mật khẩu xác nhận không trùng khớp!");
            }

            if (cccd.length() < 9 || cccd.length() > 12) {
                return KetQuaDangKy.thatBai("Số CCCD không hợp lệ!");
            }

            if (taiKhoanDAO.tonTaiTaiKhoan(cccd)) {
                return KetQuaDangKy.thatBai("Số CCCD này đã được đăng ký trong hệ thống!");
            }

            boolean ok = taiKhoanDAO.dangKyDocGiaNgoai(hoTen, cccd, sdt, matKhau);

            if (ok) {
                return KetQuaDangKy.thanhCong(
                        "Đăng ký thành công!\n"
                        + "Trạng thái: CHỜ DUYỆT.\n"
                        + "Vui lòng mang CCCD và tiền cọc đến quầy Thủ thư để kích hoạt tài khoản."
                );
            }

            return KetQuaDangKy.thatBai("Đăng ký thất bại, dữ liệu chưa được lưu!");

        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaDangKy.thatBai("Đã xảy ra lỗi trong quá trình đăng ký: " + e.getMessage());
        }
    }
}