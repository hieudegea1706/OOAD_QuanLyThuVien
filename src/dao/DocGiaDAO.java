package dao;

import dto.DocGiaDTO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DatabaseHelper;

public class DocGiaDAO {

    public List<DocGiaDTO> layDanhSachDocGia(String cheDo) throws Exception {
        List<DocGiaDTO> danhSach = new ArrayList<>();

        String sql = "SELECT "
                + "tk.ten_dang_nhap, "
                + "tk.ho_ten, "
                + "ISNULL(dgn.cccd, tk.cccd) AS cccd, "
                + "tk.so_dien_thoai, "
                + "tk.vai_tro, "
                + "tk.trang_thai_tai_khoan, "
                + "dgn.tien_coc, "
                + "dgn.ngay_dang_ky, "
                + "dgn.ngay_kich_hoat, "
                + "dgn.ghi_chu "
                + "FROM TaiKhoan tk "
                + "LEFT JOIN DocGiaNgoai dgn ON tk.ten_dang_nhap = dgn.ten_dang_nhap "
                + "WHERE tk.vai_tro <> N'Thủ thư' "
                + layDieuKienCheDo(cheDo)
                + "ORDER BY tk.vai_tro, tk.trang_thai_tai_khoan, tk.ho_ten";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapDocGia(rs));
            }
        }

        return danhSach;
    }

    public List<DocGiaDTO> timKiemDocGia(String cheDo, String tuKhoa) throws Exception {
        List<DocGiaDTO> danhSach = new ArrayList<>();

        String sql = "SELECT "
                + "tk.ten_dang_nhap, "
                + "tk.ho_ten, "
                + "ISNULL(dgn.cccd, tk.cccd) AS cccd, "
                + "tk.so_dien_thoai, "
                + "tk.vai_tro, "
                + "tk.trang_thai_tai_khoan, "
                + "dgn.tien_coc, "
                + "dgn.ngay_dang_ky, "
                + "dgn.ngay_kich_hoat, "
                + "dgn.ghi_chu "
                + "FROM TaiKhoan tk "
                + "LEFT JOIN DocGiaNgoai dgn ON tk.ten_dang_nhap = dgn.ten_dang_nhap "
                + "WHERE tk.vai_tro <> N'Thủ thư' "
                + layDieuKienCheDo(cheDo)
                + "AND (tk.ten_dang_nhap LIKE ? "
                + "OR tk.ho_ten LIKE ? "
                + "OR tk.cccd LIKE ? "
                + "OR dgn.cccd LIKE ? "
                + "OR tk.so_dien_thoai LIKE ? "
                + "OR tk.vai_tro LIKE ? "
                + "OR tk.trang_thai_tai_khoan LIKE ?) "
                + "ORDER BY tk.vai_tro, tk.trang_thai_tai_khoan, tk.ho_ten";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            String keyword = "%" + tuKhoa + "%";

            pstmt.setString(1, keyword);
            pstmt.setString(2, keyword);
            pstmt.setString(3, keyword);
            pstmt.setString(4, keyword);
            pstmt.setString(5, keyword);
            pstmt.setString(6, keyword);
            pstmt.setString(7, keyword);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(mapDocGia(rs));
                }
            }
        }

        return danhSach;
    }

    public DocGiaDTO timDocGiaTheoTenDangNhap(String tenDangNhap) throws Exception {
        String sql = "SELECT "
                + "tk.ten_dang_nhap, "
                + "tk.ho_ten, "
                + "ISNULL(dgn.cccd, tk.cccd) AS cccd, "
                + "tk.so_dien_thoai, "
                + "tk.vai_tro, "
                + "tk.trang_thai_tai_khoan, "
                + "dgn.tien_coc, "
                + "dgn.ngay_dang_ky, "
                + "dgn.ngay_kich_hoat, "
                + "dgn.ghi_chu "
                + "FROM TaiKhoan tk "
                + "LEFT JOIN DocGiaNgoai dgn ON tk.ten_dang_nhap = dgn.ten_dang_nhap "
                + "WHERE tk.ten_dang_nhap = ? "
                + "AND tk.vai_tro <> N'Thủ thư'";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, tenDangNhap);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapDocGia(rs);
                }
            }
        }

        return null;
    }

    public boolean duyetVaThuCoc(String tenDangNhap, BigDecimal tienCoc) throws Exception {
        Connection con = null;

        try {
            con = DatabaseHelper.getConnection();
            con.setAutoCommit(false);

            String updateTaiKhoanSql = "UPDATE TaiKhoan "
                    + "SET trang_thai_tai_khoan = N'Đang hoạt động' "
                    + "WHERE ten_dang_nhap = ?";

            int resultTaiKhoan;

            try (PreparedStatement pstmt = con.prepareStatement(updateTaiKhoanSql)) {
                pstmt.setString(1, tenDangNhap);
                resultTaiKhoan = pstmt.executeUpdate();
            }

            String updateDocGiaNgoaiSql = "UPDATE DocGiaNgoai "
                    + "SET tien_coc = ?, "
                    + "ngay_kich_hoat = GETDATE(), "
                    + "ghi_chu = ? "
                    + "WHERE ten_dang_nhap = ?";

            int resultDocGiaNgoai;

            try (PreparedStatement pstmt = con.prepareStatement(updateDocGiaNgoaiSql)) {
                pstmt.setBigDecimal(1, tienCoc);
                pstmt.setString(2, "Đã thu cọc và kích hoạt tài khoản");
                pstmt.setString(3, tenDangNhap);
                resultDocGiaNgoai = pstmt.executeUpdate();
            }

            if (resultTaiKhoan > 0 && resultDocGiaNgoai > 0) {
                con.commit();
                return true;
            }

            con.rollback();
            return false;

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

    public boolean capNhatHoSoDocGiaNgoai(
            String tenDangNhap,
            String hoTen,
            String cccd,
            String soDienThoai,
            String ghiChu
    ) throws Exception {
        Connection con = null;

        try {
            con = DatabaseHelper.getConnection();
            con.setAutoCommit(false);

            String updateTaiKhoanSql = "UPDATE TaiKhoan "
                    + "SET ho_ten = ?, "
                    + "cccd = ?, "
                    + "so_dien_thoai = ? "
                    + "WHERE ten_dang_nhap = ? "
                    + "AND vai_tro = N'Độc giả ngoài'";

            int resultTaiKhoan;

            try (PreparedStatement pstmt = con.prepareStatement(updateTaiKhoanSql)) {
                pstmt.setString(1, hoTen);
                pstmt.setString(2, cccd);
                pstmt.setString(3, soDienThoai);
                pstmt.setString(4, tenDangNhap);

                resultTaiKhoan = pstmt.executeUpdate();
            }

            String updateDocGiaNgoaiSql = "UPDATE DocGiaNgoai "
                    + "SET cccd = ?, "
                    + "so_dien_thoai = ?, "
                    + "ghi_chu = ? "
                    + "WHERE ten_dang_nhap = ?";

            int resultDocGiaNgoai;

            try (PreparedStatement pstmt = con.prepareStatement(updateDocGiaNgoaiSql)) {
                pstmt.setString(1, cccd);
                pstmt.setString(2, soDienThoai);
                pstmt.setString(3, ghiChu);
                pstmt.setString(4, tenDangNhap);

                resultDocGiaNgoai = pstmt.executeUpdate();
            }

            if (resultTaiKhoan > 0 && resultDocGiaNgoai > 0) {
                con.commit();
                return true;
            }

            con.rollback();
            return false;

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

    public boolean hoanTraCocVaHuyThe(String tenDangNhap) throws Exception {
        Connection con = null;

        try {
            con = DatabaseHelper.getConnection();
            con.setAutoCommit(false);

            int soSachDangMuon = demSoSachDangMuon(con, tenDangNhap);
            if (soSachDangMuon > 0) {
                throw new Exception("Không thể hoàn cọc vì độc giả còn " + soSachDangMuon + " sách đang mượn.");
            }

            int soPhieuPhatChuaThanhToan = demPhieuPhatChuaThanhToan(con, tenDangNhap);
            if (soPhieuPhatChuaThanhToan > 0) {
                throw new Exception("Không thể hoàn cọc vì độc giả còn " + soPhieuPhatChuaThanhToan + " phiếu phạt chưa thanh toán.");
            }

            BigDecimal tienCocHienTai = layTienCoc(con, tenDangNhap);
            if (tienCocHienTai == null || tienCocHienTai.compareTo(BigDecimal.ZERO) <= 0) {
                throw new Exception("Độc giả không còn tiền cọc để hoàn trả.");
            }

            String updateDocGiaNgoaiSql = "UPDATE DocGiaNgoai "
                    + "SET tien_coc = 0, "
                    + "ghi_chu = N'Đã hoàn trả tiền cọc và hủy thẻ' "
                    + "WHERE ten_dang_nhap = ?";

            int resultDocGiaNgoai;

            try (PreparedStatement pstmt = con.prepareStatement(updateDocGiaNgoaiSql)) {
                pstmt.setString(1, tenDangNhap);
                resultDocGiaNgoai = pstmt.executeUpdate();
            }

            String updateTaiKhoanSql = "UPDATE TaiKhoan "
                    + "SET trang_thai_tai_khoan = N'Bị khóa' "
                    + "WHERE ten_dang_nhap = ? "
                    + "AND vai_tro = N'Độc giả ngoài'";

            int resultTaiKhoan;

            try (PreparedStatement pstmt = con.prepareStatement(updateTaiKhoanSql)) {
                pstmt.setString(1, tenDangNhap);
                resultTaiKhoan = pstmt.executeUpdate();
            }

            if (resultDocGiaNgoai > 0 && resultTaiKhoan > 0) {
                con.commit();
                return true;
            }

            con.rollback();
            return false;

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

    public boolean capNhatTrangThaiTaiKhoan(String tenDangNhap, String trangThaiMoi) throws Exception {
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

    private int demSoSachDangMuon(Connection con, String tenDangNhap) throws Exception {
        String sql = "SELECT COUNT(*) "
                + "FROM PhieuMuon pm "
                + "JOIN ChiTietPhieuMuon ct ON pm.id_phieu_muon = ct.id_phieu_muon "
                + "WHERE pm.ten_dang_nhap = ? "
                + "AND ct.trang_thai_chi_tiet = N'Đang mượn'";

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

    private int demPhieuPhatChuaThanhToan(Connection con, String tenDangNhap) throws Exception {
        String sql = "SELECT COUNT(*) "
                + "FROM PhieuPhat "
                + "WHERE ten_dang_nhap = ? "
                + "AND trang_thai_thanh_toan = N'Chưa thanh toán'";

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

    private BigDecimal layTienCoc(Connection con, String tenDangNhap) throws Exception {
        String sql = "SELECT ISNULL(tien_coc, 0) "
                + "FROM DocGiaNgoai "
                + "WHERE ten_dang_nhap = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, tenDangNhap);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        }

        return BigDecimal.ZERO;
    }

    private String layDieuKienCheDo(String cheDo) {
        if ("DANG_HOAT_DONG".equals(cheDo)) {
            return " AND tk.trang_thai_tai_khoan = N'Đang hoạt động' ";
        }

        if ("CHO_DUYET".equals(cheDo)) {
            return " AND tk.trang_thai_tai_khoan = N'Chờ duyệt' "
                    + " AND tk.vai_tro = N'Độc giả ngoài' ";
        }

        if ("BI_KHOA".equals(cheDo)) {
            return " AND tk.trang_thai_tai_khoan = N'Bị khóa' ";
        }

        return "";
    }

    private DocGiaDTO mapDocGia(ResultSet rs) throws Exception {
        DocGiaDTO dg = new DocGiaDTO();

        dg.setTenDangNhap(rs.getString("ten_dang_nhap"));
        dg.setHoTen(rs.getString("ho_ten"));
        dg.setCccd(rs.getString("cccd"));
        dg.setSoDienThoai(rs.getString("so_dien_thoai"));
        dg.setVaiTro(rs.getString("vai_tro"));
        dg.setTrangThaiTaiKhoan(rs.getString("trang_thai_tai_khoan"));
        dg.setTienCoc(rs.getBigDecimal("tien_coc"));
        dg.setNgayDangKy(rs.getTimestamp("ngay_dang_ky"));
        dg.setNgayKichHoat(rs.getTimestamp("ngay_kich_hoat"));
        dg.setGhiChu(rs.getString("ghi_chu"));

        return dg;
    }
}