package dao;

import dto.SachQuaHanDTO;
import dto.TongQuanThongKeDTO;
import dto.TopSachMuonDTO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DatabaseHelper;

public class BaoCaoDAO {

    public TongQuanThongKeDTO layTongQuanThongKe() throws Exception {
        TongQuanThongKeDTO dto = new TongQuanThongKeDTO();

        try (Connection con = DatabaseHelper.getConnection()) {
            dto.setTongDauSach(demGiaTri(con, "SELECT COUNT(*) FROM DauSach"));

            dto.setTongCuonSach(demGiaTri(con, "SELECT COUNT(*) FROM CuonSach"));

            dto.setSachSanSang(demGiaTri(con,
                    "SELECT COUNT(*) FROM CuonSach WHERE trang_thai_luu_thong = N'Sẵn sàng'"));

            dto.setSachDangMuon(demGiaTri(con,
                    "SELECT COUNT(*) FROM CuonSach WHERE trang_thai_luu_thong = N'Đang cho mượn'"));

            dto.setPhieuDangMuon(demGiaTri(con,
                    "SELECT COUNT(*) FROM PhieuMuon WHERE trang_thai_phieu = N'Đang mượn'"));

            dto.setPhieuPhatChuaThanhToan(demGiaTri(con,
                    "SELECT COUNT(*) FROM PhieuPhat WHERE trang_thai_thanh_toan = N'Chưa thanh toán'"));

            dto.setTienPhatChuaThu(layTongTien(con,
                    "SELECT ISNULL(SUM(so_tien_phat), 0) "
                    + "FROM PhieuPhat "
                    + "WHERE trang_thai_thanh_toan = N'Chưa thanh toán'"));

            dto.setTienCocDangGiu(layTongTien(con,
                    "SELECT ISNULL(SUM(tien_coc), 0) FROM DocGiaNgoai"));
        }

        return dto;
    }

    public List<SachQuaHanDTO> layDanhSachQuaHan() throws Exception {
        List<SachQuaHanDTO> danhSach = new ArrayList<>();

        String sql = "SELECT "
                + "pm.id_phieu_muon, "
                + "tk.ho_ten, "
                + "ct.id_ca_biet, "
                + "ds.ten_sach, "
                + "pm.han_tra, "
                + "DATEDIFF(DAY, pm.han_tra, GETDATE()) AS so_ngay_qua_han "
                + "FROM PhieuMuon pm "
                + "JOIN TaiKhoan tk ON pm.ten_dang_nhap = tk.ten_dang_nhap "
                + "JOIN ChiTietPhieuMuon ct ON pm.id_phieu_muon = ct.id_phieu_muon "
                + "JOIN CuonSach cs ON ct.id_ca_biet = cs.id_ca_biet "
                + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                + "WHERE ct.trang_thai_chi_tiet = N'Đang mượn' "
                + "AND pm.han_tra < GETDATE() "
                + "ORDER BY so_ngay_qua_han DESC";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                SachQuaHanDTO dto = new SachQuaHanDTO();

                dto.setIdPhieuMuon(rs.getInt("id_phieu_muon"));
                dto.setHoTen(rs.getString("ho_ten"));
                dto.setIdCaBiet(rs.getString("id_ca_biet"));
                dto.setTenSach(rs.getString("ten_sach"));
                dto.setHanTra(rs.getTimestamp("han_tra"));
                dto.setSoNgayQuaHan(rs.getInt("so_ngay_qua_han"));

                danhSach.add(dto);
            }
        }

        return danhSach;
    }

    public List<TopSachMuonDTO> layTopSachMuonNhieu() throws Exception {
        List<TopSachMuonDTO> danhSach = new ArrayList<>();

        String sql = "SELECT TOP 10 "
                + "ds.id_dau_sach, "
                + "ds.ten_sach, "
                + "ds.tac_gia, "
                + "ds.the_loai, "
                + "COUNT(ct.id_chi_tiet) AS so_luot_muon "
                + "FROM ChiTietPhieuMuon ct "
                + "JOIN CuonSach cs ON ct.id_ca_biet = cs.id_ca_biet "
                + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                + "GROUP BY ds.id_dau_sach, ds.ten_sach, ds.tac_gia, ds.the_loai "
                + "ORDER BY so_luot_muon DESC";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                TopSachMuonDTO dto = new TopSachMuonDTO();

                dto.setIdDauSach(rs.getString("id_dau_sach"));
                dto.setTenSach(rs.getString("ten_sach"));
                dto.setTacGia(rs.getString("tac_gia"));
                dto.setTheLoai(rs.getString("the_loai"));
                dto.setSoLuotMuon(rs.getInt("so_luot_muon"));

                danhSach.add(dto);
            }
        }

        return danhSach;
    }

    private int demGiaTri(Connection con, String sql) throws Exception {
        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    private BigDecimal layTongTien(Connection con, String sql) throws Exception {
        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                BigDecimal value = rs.getBigDecimal(1);
                return value == null ? BigDecimal.ZERO : value;
            }
        }

        return BigDecimal.ZERO;
    }
}