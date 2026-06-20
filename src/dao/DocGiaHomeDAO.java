package dao;

import dto.LichSuMuonTraDocGiaDTO;
import dto.SachXuHuongDTO;
import dto.ThongTinTongQuanDocGiaDTO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DatabaseHelper;

public class DocGiaHomeDAO {

    public ThongTinTongQuanDocGiaDTO layTongQuanDocGia(String tenDangNhap) throws Exception {
        ThongTinTongQuanDocGiaDTO dto = null;

        String sqlThongTin = "SELECT "
                + "tk.ten_dang_nhap, "
                + "tk.ho_ten, "
                + "tk.vai_tro, "
                + "tk.trang_thai_tai_khoan, "
                + "ISNULL(dgn.tien_coc, 0) AS tien_coc "
                + "FROM TaiKhoan tk "
                + "LEFT JOIN DocGiaNgoai dgn ON tk.ten_dang_nhap = dgn.ten_dang_nhap "
                + "WHERE tk.ten_dang_nhap = ?";

        try (Connection con = DatabaseHelper.getConnection()) {
            try (PreparedStatement pstmt = con.prepareStatement(sqlThongTin)) {
                pstmt.setString(1, tenDangNhap);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        dto = new ThongTinTongQuanDocGiaDTO();

                        dto.setTenDangNhap(rs.getString("ten_dang_nhap"));
                        dto.setHoTen(rs.getString("ho_ten"));
                        dto.setVaiTro(rs.getString("vai_tro"));
                        dto.setTrangThaiTaiKhoan(rs.getString("trang_thai_tai_khoan"));
                        dto.setTienCoc(rs.getBigDecimal("tien_coc"));
                    }
                }
            }

            if (dto == null) {
                return null;
            }

            dto.setSoSachDangMuon(demGiaTri(con,
                    "SELECT COUNT(*) "
                    + "FROM PhieuMuon pm "
                    + "JOIN ChiTietPhieuMuon ct ON pm.id_phieu_muon = ct.id_phieu_muon "
                    + "WHERE pm.ten_dang_nhap = ? "
                    + "AND ct.trang_thai_chi_tiet = N'Đang mượn'",
                    tenDangNhap));

            dto.setSoSachQuaHan(demGiaTri(con,
                    "SELECT COUNT(*) "
                    + "FROM PhieuMuon pm "
                    + "JOIN ChiTietPhieuMuon ct ON pm.id_phieu_muon = ct.id_phieu_muon "
                    + "WHERE pm.ten_dang_nhap = ? "
                    + "AND ct.trang_thai_chi_tiet = N'Đang mượn' "
                    + "AND pm.han_tra < GETDATE()",
                    tenDangNhap));

            dto.setSoPhieuPhatChuaThanhToan(demGiaTri(con,
                    "SELECT COUNT(*) "
                    + "FROM PhieuPhat "
                    + "WHERE ten_dang_nhap = ? "
                    + "AND trang_thai_thanh_toan = N'Chưa thanh toán'",
                    tenDangNhap));

            dto.setTienPhatChuaThanhToan(layTongTien(con,
                    "SELECT ISNULL(SUM(so_tien_phat), 0) "
                    + "FROM PhieuPhat "
                    + "WHERE ten_dang_nhap = ? "
                    + "AND trang_thai_thanh_toan = N'Chưa thanh toán'",
                    tenDangNhap));
        }

        return dto;
    }

    public List<SachXuHuongDTO> layTopSachMuonNhieu() throws Exception {
        List<SachXuHuongDTO> danhSach = new ArrayList<>();

        String sql = "SELECT TOP 10 "
                + "ds.id_dau_sach, "
                + "ds.ten_sach, "
                + "ds.tac_gia, "
                + "ds.the_loai, "
                + "COUNT(ct.id_chi_tiet) AS so_luot_muon, "
                + "(SELECT COUNT(*) "
                + " FROM CuonSach cs2 "
                + " WHERE cs2.id_dau_sach = ds.id_dau_sach "
                + " AND cs2.trang_thai_luu_thong = N'Sẵn sàng') AS so_ban_san_sang "
                + "FROM DauSach ds "
                + "JOIN CuonSach cs ON ds.id_dau_sach = cs.id_dau_sach "
                + "LEFT JOIN ChiTietPhieuMuon ct ON cs.id_ca_biet = ct.id_ca_biet "
                + "GROUP BY ds.id_dau_sach, ds.ten_sach, ds.tac_gia, ds.the_loai "
                + "ORDER BY so_luot_muon DESC, ds.ten_sach";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                SachXuHuongDTO dto = new SachXuHuongDTO();

                dto.setIdDauSach(rs.getString("id_dau_sach"));
                dto.setTenSach(rs.getString("ten_sach"));
                dto.setTacGia(rs.getString("tac_gia"));
                dto.setTheLoai(rs.getString("the_loai"));
                dto.setSoLuotMuon(rs.getInt("so_luot_muon"));
                dto.setSoBanSanSang(rs.getInt("so_ban_san_sang"));

                danhSach.add(dto);
            }
        }

        return danhSach;
    }

    public List<LichSuMuonTraDocGiaDTO> layLichSuMuonTra(String tenDangNhap) throws Exception {
        List<LichSuMuonTraDocGiaDTO> danhSach = new ArrayList<>();

        String sql = "SELECT "
                + "pm.id_phieu_muon, "
                + "ct.id_ca_biet, "
                + "ds.ten_sach, "
                + "pm.ngay_muon, "
                + "pm.han_tra, "
                + "ct.ngay_tra_thuc_te, "
                + "ct.trang_thai_chi_tiet, "
                + "pp.loai_vi_pham, "
                + "pp.so_tien_phat, "
                + "pp.trang_thai_thanh_toan "
                + "FROM PhieuMuon pm "
                + "JOIN ChiTietPhieuMuon ct ON pm.id_phieu_muon = ct.id_phieu_muon "
                + "JOIN CuonSach cs ON ct.id_ca_biet = cs.id_ca_biet "
                + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                + "LEFT JOIN PhieuPhat pp "
                + "ON pp.id_phieu_muon = ct.id_phieu_muon "
                + "AND pp.id_ca_biet = ct.id_ca_biet "
                + "WHERE pm.ten_dang_nhap = ? "
                + "ORDER BY pm.ngay_muon DESC, pm.id_phieu_muon DESC";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, tenDangNhap);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LichSuMuonTraDocGiaDTO dto = new LichSuMuonTraDocGiaDTO();

                    dto.setIdPhieuMuon(rs.getInt("id_phieu_muon"));
                    dto.setIdCaBiet(rs.getString("id_ca_biet"));
                    dto.setTenSach(rs.getString("ten_sach"));
                    dto.setNgayMuon(rs.getTimestamp("ngay_muon"));
                    dto.setHanTra(rs.getTimestamp("han_tra"));
                    dto.setNgayTraThucTe(rs.getTimestamp("ngay_tra_thuc_te"));
                    dto.setTrangThaiChiTiet(rs.getString("trang_thai_chi_tiet"));
                    dto.setLoaiViPham(rs.getString("loai_vi_pham"));
                    dto.setSoTienPhat(rs.getBigDecimal("so_tien_phat"));
                    dto.setTrangThaiThanhToan(rs.getString("trang_thai_thanh_toan"));

                    danhSach.add(dto);
                }
            }
        }

        return danhSach;
    }

    private int demGiaTri(Connection con, String sql, String tenDangNhap) throws Exception {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, tenDangNhap);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    private BigDecimal layTongTien(Connection con, String sql, String tenDangNhap) throws Exception {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, tenDangNhap);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal value = rs.getBigDecimal(1);
                    return value == null ? BigDecimal.ZERO : value;
                }
            }
        }

        return BigDecimal.ZERO;
    }
}