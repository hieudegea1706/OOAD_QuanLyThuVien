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
    public boolean dangKyDocGiaNgoai(String hoTen, String cccd, String sdt, String matKhau) throws Exception {
    Connection con = null;

    try {
        con = DatabaseHelper.getConnection();
        con.setAutoCommit(false);

        String insertTaiKhoanSql = "INSERT INTO TaiKhoan "
                + "(ten_dang_nhap, mat_khau, ho_ten, cccd, so_dien_thoai, vai_tro, trang_thai_tai_khoan) "
                + "VALUES (?, ?, ?, ?, ?, N'Độc giả ngoài', N'Chờ duyệt')";

        try (PreparedStatement ps = con.prepareStatement(insertTaiKhoanSql)) {
            ps.setString(1, cccd);
            ps.setString(2, matKhau);
            ps.setString(3, hoTen);
            ps.setString(4, cccd);
            ps.setString(5, sdt);
            ps.executeUpdate();
        }

        String insertDocGiaNgoaiSql = "INSERT INTO DocGiaNgoai "
                + "(ten_dang_nhap, cccd, so_dien_thoai, tien_coc, ghi_chu) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(insertDocGiaNgoaiSql)) {
            ps.setString(1, cccd);
            ps.setString(2, cccd);
            ps.setString(3, sdt);
            ps.setBigDecimal(4, new java.math.BigDecimal("0"));
            ps.setString(5, "Đăng ký online - chờ thủ thư duyệt và thu cọc");
            ps.executeUpdate();
        }

        con.commit();
        return true;

    } catch (Exception e) {
        if (con != null) {
            con.rollback();
        }
        throw e;
    } finally {
        if (con != null) {
            con.setAutoCommit(true);
            con.close();
        }
    }
}
   
}