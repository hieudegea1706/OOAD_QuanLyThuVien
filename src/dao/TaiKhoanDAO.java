package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import models.TaiKhoan;
import utils.DatabaseHelper;

public class TaiKhoanDAO {

    public TaiKhoan timTheoTenDangNhap(String tenDangNhap) throws Exception {
        String sql = "SELECT ten_dang_nhap, mat_khau, ho_ten, vai_tro, trang_thai_tai_khoan "
                   + "FROM TaiKhoan "
                   + "WHERE ten_dang_nhap = ?";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, tenDangNhap);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setTenDangNhap(rs.getString("ten_dang_nhap"));
                    tk.setMatKhau(rs.getString("mat_khau"));
                    tk.setHoTen(rs.getString("ho_ten"));
                    tk.setVaiTro(rs.getString("vai_tro"));
                    tk.setTrangThaiTaiKhoan(rs.getString("trang_thai_tai_khoan"));
                    return tk;
                }
            }
        }

        return null;
    }

    public boolean capNhatTrangThai(String tenDangNhap, String trangThaiMoi) throws Exception {
        String sql = "UPDATE TaiKhoan "
                   + "SET trang_thai_tai_khoan = ? "
                   + "WHERE ten_dang_nhap = ?";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, trangThaiMoi);
            pstmt.setString(2, tenDangNhap);

            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean tonTaiTaiKhoan(String tenDangNhap) throws Exception {
        String sql = "SELECT 1 FROM TaiKhoan WHERE ten_dang_nhap = ?";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, tenDangNhap);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}