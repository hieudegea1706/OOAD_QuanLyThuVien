package dao;

import dto.PhieuPhatDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DatabaseHelper;

public class PhieuPhatDAO {

    public List<PhieuPhatDTO> layDanhSachPhieuPhat() throws Exception {
        List<PhieuPhatDTO> danhSach = new ArrayList<>();

        String sql = "SELECT "
                + "pp.id_phieu_phat, "
                + "pp.id_phieu_muon, "
                + "pp.ten_dang_nhap, "
                + "tk.ho_ten, "
                + "pp.id_ca_biet, "
                + "ds.ten_sach, "
                + "pp.loai_vi_pham, "
                + "pp.so_ngay_qua_han, "
                + "pp.so_tien_phat, "
                + "pp.trang_thai_thanh_toan, "
                + "pp.ngay_lap, "
                + "pp.mo_ta_vi_pham "
                + "FROM PhieuPhat pp "
                + "JOIN TaiKhoan tk ON pp.ten_dang_nhap = tk.ten_dang_nhap "
                + "JOIN CuonSach cs ON pp.id_ca_biet = cs.id_ca_biet "
                + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                + "ORDER BY pp.id_phieu_phat DESC";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapPhieuPhat(rs));
            }
        }

        return danhSach;
    }

    public List<PhieuPhatDTO> timKiemPhieuPhat(String tuKhoa) throws Exception {
        List<PhieuPhatDTO> danhSach = new ArrayList<>();

        String sql = "SELECT "
                + "pp.id_phieu_phat, "
                + "pp.id_phieu_muon, "
                + "pp.ten_dang_nhap, "
                + "tk.ho_ten, "
                + "pp.id_ca_biet, "
                + "ds.ten_sach, "
                + "pp.loai_vi_pham, "
                + "pp.so_ngay_qua_han, "
                + "pp.so_tien_phat, "
                + "pp.trang_thai_thanh_toan, "
                + "pp.ngay_lap, "
                + "pp.mo_ta_vi_pham "
                + "FROM PhieuPhat pp "
                + "JOIN TaiKhoan tk ON pp.ten_dang_nhap = tk.ten_dang_nhap "
                + "JOIN CuonSach cs ON pp.id_ca_biet = cs.id_ca_biet "
                + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                + "WHERE pp.ten_dang_nhap LIKE ? "
                + "OR tk.ho_ten LIKE ? "
                + "OR pp.id_ca_biet LIKE ? "
                + "OR ds.ten_sach LIKE ? "
                + "OR pp.loai_vi_pham LIKE ? "
                + "OR pp.trang_thai_thanh_toan LIKE ? "
                + "ORDER BY pp.id_phieu_phat DESC";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            String keyword = "%" + tuKhoa + "%";

            pstmt.setString(1, keyword);
            pstmt.setString(2, keyword);
            pstmt.setString(3, keyword);
            pstmt.setString(4, keyword);
            pstmt.setString(5, keyword);
            pstmt.setString(6, keyword);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(mapPhieuPhat(rs));
                }
            }
        }

        return danhSach;
    }

    public boolean xacNhanThanhToan(int idPhieuPhat) throws Exception {
        String sql = "UPDATE PhieuPhat "
                + "SET trang_thai_thanh_toan = N'Đã thanh toán' "
                + "WHERE id_phieu_phat = ? "
                + "AND trang_thai_thanh_toan <> N'Đã thanh toán'";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, idPhieuPhat);
            return pstmt.executeUpdate() > 0;
        }
    }

    private PhieuPhatDTO mapPhieuPhat(ResultSet rs) throws Exception {
        PhieuPhatDTO pp = new PhieuPhatDTO();

        pp.setIdPhieuPhat(rs.getInt("id_phieu_phat"));
        pp.setIdPhieuMuon(rs.getInt("id_phieu_muon"));
        pp.setTenDangNhap(rs.getString("ten_dang_nhap"));
        pp.setHoTen(rs.getString("ho_ten"));
        pp.setIdCaBiet(rs.getString("id_ca_biet"));
        pp.setTenSach(rs.getString("ten_sach"));
        pp.setLoaiViPham(rs.getString("loai_vi_pham"));
        pp.setSoNgayQuaHan(rs.getInt("so_ngay_qua_han"));
        pp.setSoTienPhat(rs.getBigDecimal("so_tien_phat"));
        pp.setTrangThaiThanhToan(rs.getString("trang_thai_thanh_toan"));
        pp.setNgayLap(rs.getTimestamp("ngay_lap"));
        pp.setMoTaViPham(rs.getString("mo_ta_vi_pham"));

        return pp;
    }
}