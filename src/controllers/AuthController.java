package controllers;

import dao.TaiKhoanDAO;
import dto.KetQuaDangNhap;
import models.TaiKhoan;

public class AuthController {

    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    public KetQuaDangNhap dangNhap(String tenDangNhap, String matKhau) {
        try {
            if (tenDangNhap == null || tenDangNhap.trim().isEmpty()
                    || matKhau == null || matKhau.trim().isEmpty()) {
                return KetQuaDangNhap.thatBai("Vui lòng nhập đầy đủ thông tin!");
            }

            TaiKhoan taiKhoan = taiKhoanDAO.timTheoTenDangNhap(tenDangNhap.trim());

            if (taiKhoan == null) {
                return KetQuaDangNhap.thatBai("Sai tên đăng nhập hoặc mật khẩu!");
            }

            if (!taiKhoan.getMatKhau().equals(matKhau)) {
                return KetQuaDangNhap.thatBai("Sai tên đăng nhập hoặc mật khẩu!");
            }

            String trangThai = taiKhoan.getTrangThaiTaiKhoan();
            String vaiTro = taiKhoan.getVaiTro();

            if ("Bị khóa".equalsIgnoreCase(trangThai)) {
                return KetQuaDangNhap.thatBai("Tài khoản của bạn đã bị khóa! Vui lòng liên hệ Thư viện.");
            }

            if ("Chờ duyệt".equalsIgnoreCase(trangThai)) {
                return KetQuaDangNhap.thatBai(
                        "Tài khoản đang CHỜ DUYỆT!\n"
                        + "Vui lòng mang CCCD và tiền cọc đến quầy Thủ thư để kích hoạt."
                );
            }

            if ("Chưa kích hoạt".equalsIgnoreCase(trangThai)) {
                if (!"Sinh viên".equalsIgnoreCase(vaiTro)) {
                    return KetQuaDangNhap.thatBai(
                            "Tài khoản chưa kích hoạt nhưng không phải tài khoản sinh viên.\n"
                            + "Vui lòng liên hệ Thủ thư để kiểm tra."
                    );
                }

                boolean updated = taiKhoanDAO.capNhatTrangThai(tenDangNhap, "Đang hoạt động");

                if (!updated) {
                    return KetQuaDangNhap.thatBai("Kích hoạt thất bại, vui lòng thử lại!");
                }

                return KetQuaDangNhap.thanhCong(
                        taiKhoan.getTenDangNhap(),
                        taiKhoan.getHoTen(),
                        taiKhoan.getVaiTro()
                );
            }

            if ("Đang hoạt động".equalsIgnoreCase(trangThai)) {
                return KetQuaDangNhap.thanhCong(
                        taiKhoan.getTenDangNhap(),
                        taiKhoan.getHoTen(),
                        taiKhoan.getVaiTro()
                );
            }

            return KetQuaDangNhap.thatBai("Trạng thái tài khoản không hợp lệ: " + trangThai);

        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaDangNhap.thatBai("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }
}