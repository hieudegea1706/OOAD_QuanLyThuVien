package dao;

import dto.DocGiaMuonDTO;
import dto.SachMuonDTO;
import dto.ThongTinTraSachDTO;
import dto.PhieuMuonDTO;
import dto.ChiTietPhieuMuonDTO;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import utils.DatabaseHelper;

public class MuonTraDAO {

    public DocGiaMuonDTO timDocGiaMuon(String tenDangNhap) throws Exception {
        String sql = "SELECT "
                + "tk.ten_dang_nhap, "
                + "tk.ho_ten, "
                + "tk.vai_tro, "
                + "tk.trang_thai_tai_khoan, "
                + "ISNULL(dgn.tien_coc, 0) AS tien_coc "
                + "FROM TaiKhoan tk "
                + "LEFT JOIN DocGiaNgoai dgn ON tk.ten_dang_nhap = dgn.ten_dang_nhap "
                + "WHERE tk.ten_dang_nhap = ? "
                + "AND tk.vai_tro <> N'Thủ thư'";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, tenDangNhap);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    DocGiaMuonDTO dg = new DocGiaMuonDTO();
                    dg.setTenDangNhap(rs.getString("ten_dang_nhap"));
                    dg.setHoTen(rs.getString("ho_ten"));
                    dg.setVaiTro(rs.getString("vai_tro"));
                    dg.setTrangThaiTaiKhoan(rs.getString("trang_thai_tai_khoan"));
                    dg.setTienCoc(rs.getBigDecimal("tien_coc"));
                    return dg;
                }
            }
        }

        return null;
    }

    public SachMuonDTO timSachMuon(String idCaBiet) throws Exception {
        String sql = "SELECT "
                + "cs.id_ca_biet, "
                + "cs.id_dau_sach, "
                + "ds.ten_sach, "
                + "cs.trang_thai_luu_thong, "
                + "cs.tinh_trang_vat_ly, "
                + "cs.vi_tri_ke "
                + "FROM CuonSach cs "
                + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                + "WHERE cs.id_ca_biet = ?";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, idCaBiet);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    SachMuonDTO sach = new SachMuonDTO();
                    sach.setIdCaBiet(rs.getString("id_ca_biet"));
                    sach.setIdDauSach(rs.getString("id_dau_sach"));
                    sach.setTenSach(rs.getString("ten_sach"));
                    sach.setTrangThaiLuuThong(rs.getString("trang_thai_luu_thong"));
                    sach.setTinhTrangVatLy(rs.getString("tinh_trang_vat_ly"));
                    sach.setViTriKe(rs.getString("vi_tri_ke"));
                    return sach;
                }
            }
        }

        return null;
    }

    public int demSoSachDangMuon(String tenDangNhap) throws Exception {
        String sql = "SELECT COUNT(*) AS so_sach_dang_muon "
                + "FROM PhieuMuon pm "
                + "JOIN ChiTietPhieuMuon ct ON pm.id_phieu_muon = ct.id_phieu_muon "
                + "WHERE pm.ten_dang_nhap = ? "
                + "AND ct.trang_thai_chi_tiet = N'Đang mượn'";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, tenDangNhap);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("so_sach_dang_muon");
                }
            }
        }

        return 0;
    }

    public int taoPhieuMuon(String tenDangNhap, int soNgayMuon, List<String> danhSachIdCaBiet) throws Exception {
        Connection con = null;

        try {
            con = DatabaseHelper.getConnection();
            con.setAutoCommit(false);

            String insertPhieuSql = "INSERT INTO PhieuMuon "
                    + "(ten_dang_nhap, ngay_muon, han_tra, trang_thai_phieu, ghi_chu) "
                    + "VALUES (?, GETDATE(), DATEADD(DAY, ?, GETDATE()), N'Đang mượn', ?)";

            int idPhieuMuon;

            try (PreparedStatement insertPhieuStmt = con.prepareStatement(
                    insertPhieuSql,
                    Statement.RETURN_GENERATED_KEYS
            )) {
                insertPhieuStmt.setString(1, tenDangNhap);
                insertPhieuStmt.setInt(2, soNgayMuon);
                insertPhieuStmt.setString(3, "Phiếu mượn lập từ ứng dụng thủ thư");

                int resultPhieu = insertPhieuStmt.executeUpdate();

                if (resultPhieu <= 0) {
                    con.rollback();
                    throw new Exception("Không thể tạo phiếu mượn!");
                }

                try (ResultSet generatedKeys = insertPhieuStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idPhieuMuon = generatedKeys.getInt(1);
                    } else {
                        con.rollback();
                        throw new Exception("Không lấy được mã phiếu mượn vừa tạo!");
                    }
                }
            }

            String insertChiTietSql = "INSERT INTO ChiTietPhieuMuon "
                    + "(id_phieu_muon, id_ca_biet, trang_thai_chi_tiet, ghi_chu) "
                    + "VALUES (?, ?, N'Đang mượn', ?)";

            String updateCuonSachSql = "UPDATE CuonSach "
                    + "SET trang_thai_luu_thong = N'Đang cho mượn' "
                    + "WHERE id_ca_biet = ? "
                    + "AND trang_thai_luu_thong = N'Sẵn sàng'";

            try (PreparedStatement insertChiTietStmt = con.prepareStatement(insertChiTietSql);
                 PreparedStatement updateCuonSachStmt = con.prepareStatement(updateCuonSachSql)) {

                for (String idCaBiet : danhSachIdCaBiet) {
                    insertChiTietStmt.setInt(1, idPhieuMuon);
                    insertChiTietStmt.setString(2, idCaBiet);
                    insertChiTietStmt.setString(3, "Chi tiết phiếu mượn");
                    insertChiTietStmt.executeUpdate();

                    updateCuonSachStmt.setString(1, idCaBiet);
                    int updated = updateCuonSachStmt.executeUpdate();

                    if (updated <= 0) {
                        con.rollback();
                        throw new Exception(
                                "Không thể cập nhật trạng thái cuốn sách: " + idCaBiet
                                + ". Có thể sách đã được mượn bởi phiếu khác."
                        );
                    }
                }
            }

            con.commit();
            return idPhieuMuon;

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
    
    public ThongTinTraSachDTO timSachDangMuonDeTra(String idCaBiet) throws Exception {
    String sql = "SELECT "
            + "pm.id_phieu_muon, "
            + "pm.ten_dang_nhap, "
            + "tk.ho_ten, "
            + "ct.id_ca_biet, "
            + "ds.ten_sach, "
            + "pm.ngay_muon, "
            + "pm.han_tra, "
            + "ct.trang_thai_chi_tiet, "
            + "CASE WHEN DATEDIFF(DAY, pm.han_tra, GETDATE()) > 0 "
            + "THEN DATEDIFF(DAY, pm.han_tra, GETDATE()) ELSE 0 END AS so_ngay_qua_han "
            + "FROM ChiTietPhieuMuon ct "
            + "JOIN PhieuMuon pm ON ct.id_phieu_muon = pm.id_phieu_muon "
            + "JOIN TaiKhoan tk ON pm.ten_dang_nhap = tk.ten_dang_nhap "
            + "JOIN CuonSach cs ON ct.id_ca_biet = cs.id_ca_biet "
            + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
            + "WHERE ct.id_ca_biet = ? "
            + "AND ct.trang_thai_chi_tiet = N'Đang mượn'";

    try (Connection con = DatabaseHelper.getConnection();
         PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, idCaBiet);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                ThongTinTraSachDTO dto = new ThongTinTraSachDTO();

                dto.setTimThay(true);
                dto.setIdPhieuMuon(rs.getInt("id_phieu_muon"));
                dto.setTenDangNhap(rs.getString("ten_dang_nhap"));
                dto.setHoTen(rs.getString("ho_ten"));
                dto.setIdCaBiet(rs.getString("id_ca_biet"));
                dto.setTenSach(rs.getString("ten_sach"));
                dto.setNgayMuon(rs.getTimestamp("ngay_muon"));
                dto.setHanTra(rs.getTimestamp("han_tra"));
                dto.setSoNgayQuaHan(rs.getInt("so_ngay_qua_han"));
                dto.setTrangThaiChiTiet(rs.getString("trang_thai_chi_tiet"));

                return dto;
            }
        }
    }

    return null;
}
    public String xuLyTraSach(
        int idPhieuMuon,
        String idCaBiet,
        String tenDangNhap,
        String tinhTrangTra,
        String ghiChuTra,
        boolean canLapPhieuPhat,
        BigDecimal tienPhat,
        String loaiViPham,
        int soNgayQuaHan,
        String moTaViPham,
        String trangThaiLuuThongMoi
) throws Exception {
    Connection con = null;

    try {
        con = DatabaseHelper.getConnection();
        con.setAutoCommit(false);

        String updateChiTietSql = "UPDATE ChiTietPhieuMuon "
                + "SET trang_thai_chi_tiet = N'Đã trả', "
                + "ngay_tra_thuc_te = GETDATE(), "
                + "ghi_chu = ? "
                + "WHERE id_phieu_muon = ? "
                + "AND id_ca_biet = ? "
                + "AND trang_thai_chi_tiet = N'Đang mượn'";

        try (PreparedStatement pstmt = con.prepareStatement(updateChiTietSql)) {
            pstmt.setString(1, ghiChuTra == null || ghiChuTra.isEmpty()
                    ? "Đã xử lý trả sách"
                    : ghiChuTra);
            pstmt.setInt(2, idPhieuMuon);
            pstmt.setString(3, idCaBiet);

            int result = pstmt.executeUpdate();

            if (result <= 0) {
                con.rollback();
                throw new Exception("Không thể cập nhật chi tiết phiếu mượn!");
            }
        }

        if (canLapPhieuPhat) {
            String insertPhatSql = "INSERT INTO PhieuPhat "
                    + "(id_phieu_muon, id_ca_biet, ten_dang_nhap, loai_vi_pham, "
                    + "so_ngay_qua_han, mo_ta_vi_pham, so_tien_phat, trang_thai_thanh_toan, ghi_chu) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, N'Chưa thanh toán', ?)";

            try (PreparedStatement pstmt = con.prepareStatement(insertPhatSql)) {
                pstmt.setInt(1, idPhieuMuon);
                pstmt.setString(2, idCaBiet);
                pstmt.setString(3, tenDangNhap);
                pstmt.setString(4, loaiViPham);
                pstmt.setInt(5, soNgayQuaHan);
                pstmt.setString(6, moTaViPham);
                pstmt.setBigDecimal(7, tienPhat);
                pstmt.setString(8, "Phiếu phạt phát sinh khi xử lý trả sách");

                pstmt.executeUpdate();
            }
        }

        String updateCuonSachSql = "UPDATE CuonSach "
                + "SET trang_thai_luu_thong = ?, "
                + "tinh_trang_vat_ly = ?, "
                + "ghi_chu = ? "
                + "WHERE id_ca_biet = ?";

        try (PreparedStatement pstmt = con.prepareStatement(updateCuonSachSql)) {
            pstmt.setString(1, trangThaiLuuThongMoi);
            pstmt.setString(2, tinhTrangTra);
            pstmt.setString(3, ghiChuTra == null || ghiChuTra.isEmpty()
                    ? "Cập nhật khi trả sách"
                    : ghiChuTra);
            pstmt.setString(4, idCaBiet);

            int result = pstmt.executeUpdate();

            if (result <= 0) {
                con.rollback();
                throw new Exception("Không thể cập nhật trạng thái cuốn sách!");
            }
        }

        int soSachDangMuon = 0;

        String countDangMuonSql = "SELECT COUNT(*) AS so_sach_dang_muon "
                + "FROM ChiTietPhieuMuon "
                + "WHERE id_phieu_muon = ? "
                + "AND trang_thai_chi_tiet = N'Đang mượn'";

        try (PreparedStatement pstmt = con.prepareStatement(countDangMuonSql)) {
            pstmt.setInt(1, idPhieuMuon);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    soSachDangMuon = rs.getInt("so_sach_dang_muon");
                }
            }
        }

        String trangThaiPhieuMoi = soSachDangMuon == 0 ? "Đã trả" : "Đã trả một phần";

        String updatePhieuSql = "UPDATE PhieuMuon "
                + "SET trang_thai_phieu = ? "
                + "WHERE id_phieu_muon = ?";

        try (PreparedStatement pstmt = con.prepareStatement(updatePhieuSql)) {
            pstmt.setString(1, trangThaiPhieuMoi);
            pstmt.setInt(2, idPhieuMuon);
            pstmt.executeUpdate();
        }

        con.commit();
        return trangThaiPhieuMoi;

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
    public List<PhieuMuonDTO> layDanhSachPhieuMuon() throws Exception {
    List<PhieuMuonDTO> danhSach = new ArrayList<>();

    String sql = "SELECT "
            + "pm.id_phieu_muon, "
            + "pm.ten_dang_nhap, "
            + "tk.ho_ten, "
            + "pm.ngay_muon, "
            + "pm.han_tra, "
            + "pm.trang_thai_phieu, "
            + "COUNT(ct.id_chi_tiet) AS so_sach "
            + "FROM PhieuMuon pm "
            + "JOIN TaiKhoan tk ON pm.ten_dang_nhap = tk.ten_dang_nhap "
            + "LEFT JOIN ChiTietPhieuMuon ct ON pm.id_phieu_muon = ct.id_phieu_muon "
            + "GROUP BY pm.id_phieu_muon, pm.ten_dang_nhap, tk.ho_ten, "
            + "pm.ngay_muon, pm.han_tra, pm.trang_thai_phieu "
            + "ORDER BY pm.id_phieu_muon DESC";

    try (Connection con = DatabaseHelper.getConnection();
         PreparedStatement pstmt = con.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            PhieuMuonDTO pm = new PhieuMuonDTO();

            pm.setIdPhieuMuon(rs.getInt("id_phieu_muon"));
            pm.setTenDangNhap(rs.getString("ten_dang_nhap"));
            pm.setHoTen(rs.getString("ho_ten"));
            pm.setNgayMuon(rs.getTimestamp("ngay_muon"));
            pm.setHanTra(rs.getTimestamp("han_tra"));
            pm.setTrangThaiPhieu(rs.getString("trang_thai_phieu"));
            pm.setSoSach(rs.getInt("so_sach"));

            danhSach.add(pm);
        }
    }

    return danhSach;
}
    public List<ChiTietPhieuMuonDTO> layChiTietPhieuMuon(int idPhieuMuon) throws Exception {
    List<ChiTietPhieuMuonDTO> danhSach = new ArrayList<>();

    String sql = "SELECT "
            + "ct.id_ca_biet, "
            + "ds.ten_sach, "
            + "ct.ngay_tra_thuc_te, "
            + "ct.trang_thai_chi_tiet, "
            + "ct.ghi_chu AS ghi_chu_chi_tiet, "
            + "pp.loai_vi_pham, "
            + "pp.so_tien_phat, "
            + "pp.trang_thai_thanh_toan "
            + "FROM ChiTietPhieuMuon ct "
            + "JOIN CuonSach cs ON ct.id_ca_biet = cs.id_ca_biet "
            + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
            + "LEFT JOIN PhieuPhat pp "
            + "ON pp.id_phieu_muon = ct.id_phieu_muon "
            + "AND pp.id_ca_biet = ct.id_ca_biet "
            + "WHERE ct.id_phieu_muon = ? "
            + "ORDER BY ct.id_ca_biet";

    try (Connection con = DatabaseHelper.getConnection();
         PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setInt(1, idPhieuMuon);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ChiTietPhieuMuonDTO ct = new ChiTietPhieuMuonDTO();

                ct.setIdCaBiet(rs.getString("id_ca_biet"));
                ct.setTenSach(rs.getString("ten_sach"));
                ct.setNgayTraThucTe(rs.getTimestamp("ngay_tra_thuc_te"));
                ct.setTrangThaiChiTiet(rs.getString("trang_thai_chi_tiet"));
                ct.setGhiChuChiTiet(rs.getString("ghi_chu_chi_tiet"));
                ct.setLoaiViPham(rs.getString("loai_vi_pham"));
                ct.setSoTienPhat(rs.getBigDecimal("so_tien_phat"));
                ct.setTrangThaiThanhToan(rs.getString("trang_thai_thanh_toan"));

                danhSach.add(ct);
            }
        }
    }

    return danhSach;
}
}