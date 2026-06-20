package dao;

import dto.CuonSachDTO;
import dto.DauSachDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import utils.DatabaseHelper;

public class SachDAO {

    // ============================================================
    // ĐẦU SÁCH
    // ============================================================

    public List<DauSachDTO> layDanhSachDauSach() throws Exception {
        List<DauSachDTO> danhSach = new ArrayList<>();

        String sql = "SELECT id_dau_sach, ten_sach, tac_gia, the_loai, "
                + "nha_xuat_ban, nam_xuat_ban, tom_tat, trang_thai "
                + "FROM DauSach "
                + "ORDER BY id_dau_sach";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapDauSach(rs));
            }
        }

        return danhSach;
    }

    public List<DauSachDTO> timKiemDauSach(String tuKhoa) throws Exception {
        List<DauSachDTO> danhSach = new ArrayList<>();

        String sql = "SELECT id_dau_sach, ten_sach, tac_gia, the_loai, "
                + "nha_xuat_ban, nam_xuat_ban, tom_tat, trang_thai "
                + "FROM DauSach "
                + "WHERE id_dau_sach LIKE ? "
                + "OR ten_sach LIKE ? "
                + "OR tac_gia LIKE ? "
                + "OR the_loai LIKE ? "
                + "OR nha_xuat_ban LIKE ? "
                + "ORDER BY id_dau_sach";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            String keyword = "%" + tuKhoa + "%";

            pstmt.setString(1, keyword);
            pstmt.setString(2, keyword);
            pstmt.setString(3, keyword);
            pstmt.setString(4, keyword);
            pstmt.setString(5, keyword);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(mapDauSach(rs));
                }
            }
        }

        return danhSach;
    }

    public DauSachDTO timDauSachTheoId(String idDauSach) throws Exception {
        String sql = "SELECT id_dau_sach, ten_sach, tac_gia, the_loai, "
                + "nha_xuat_ban, nam_xuat_ban, tom_tat, trang_thai "
                + "FROM DauSach "
                + "WHERE id_dau_sach = ?";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, idDauSach);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapDauSach(rs);
                }
            }
        }

        return null;
    }

    public boolean themDauSach(DauSachDTO ds) throws Exception {
        String sql = "INSERT INTO DauSach "
                + "(id_dau_sach, ten_sach, tac_gia, the_loai, nha_xuat_ban, nam_xuat_ban, tom_tat, trang_thai) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, N'Còn phục vụ')";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, ds.getIdDauSach());
            pstmt.setString(2, ds.getTenSach());
            pstmt.setString(3, ds.getTacGia());
            pstmt.setString(4, ds.getTheLoai());
            pstmt.setString(5, ds.getNhaXuatBan());

            if (ds.getNamXuatBan() == null) {
                pstmt.setNull(6, Types.INTEGER);
            } else {
                pstmt.setInt(6, ds.getNamXuatBan());
            }

            pstmt.setString(7, ds.getTomTat());

            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean capNhatDauSach(DauSachDTO ds) throws Exception {
        String sql = "UPDATE DauSach "
                + "SET ten_sach = ?, "
                + "tac_gia = ?, "
                + "the_loai = ?, "
                + "nha_xuat_ban = ?, "
                + "nam_xuat_ban = ?, "
                + "tom_tat = ? "
                + "WHERE id_dau_sach = ?";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, ds.getTenSach());
            pstmt.setString(2, ds.getTacGia());
            pstmt.setString(3, ds.getTheLoai());
            pstmt.setString(4, ds.getNhaXuatBan());

            if (ds.getNamXuatBan() == null) {
                pstmt.setNull(5, Types.INTEGER);
            } else {
                pstmt.setInt(5, ds.getNamXuatBan());
            }

            pstmt.setString(6, ds.getTomTat());
            pstmt.setString(7, ds.getIdDauSach());

            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean ngungPhucVuDauSach(String idDauSach) throws Exception {
        String sql = "UPDATE DauSach "
                + "SET trang_thai = N'Ngừng phục vụ' "
                + "WHERE id_dau_sach = ?";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, idDauSach);
            return pstmt.executeUpdate() > 0;
        }
    }

    // ============================================================
    // CUỐN SÁCH
    // ============================================================

    public List<CuonSachDTO> layDanhSachCuonSach() throws Exception {
        List<CuonSachDTO> danhSach = new ArrayList<>();

        String sql = "SELECT "
                + "cs.id_ca_biet, "
                + "cs.id_dau_sach, "
                + "ds.ten_sach, "
                + "cs.trang_thai_luu_thong, "
                + "cs.tinh_trang_vat_ly, "
                + "cs.vi_tri_ke, "
                + "cs.ngay_nhap_kho, "
                + "cs.ghi_chu "
                + "FROM CuonSach cs "
                + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                + "ORDER BY cs.id_ca_biet";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapCuonSach(rs));
            }
        }

        return danhSach;
    }

    public List<CuonSachDTO> timKiemCuonSach(String tuKhoa) throws Exception {
        List<CuonSachDTO> danhSach = new ArrayList<>();

        String sql = "SELECT "
                + "cs.id_ca_biet, "
                + "cs.id_dau_sach, "
                + "ds.ten_sach, "
                + "cs.trang_thai_luu_thong, "
                + "cs.tinh_trang_vat_ly, "
                + "cs.vi_tri_ke, "
                + "cs.ngay_nhap_kho, "
                + "cs.ghi_chu "
                + "FROM CuonSach cs "
                + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                + "WHERE cs.id_ca_biet LIKE ? "
                + "OR cs.id_dau_sach LIKE ? "
                + "OR ds.ten_sach LIKE ? "
                + "OR cs.trang_thai_luu_thong LIKE ? "
                + "OR cs.tinh_trang_vat_ly LIKE ? "
                + "OR cs.vi_tri_ke LIKE ? "
                + "ORDER BY cs.id_ca_biet";

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
                    danhSach.add(mapCuonSach(rs));
                }
            }
        }

        return danhSach;
    }

    public boolean themCuonSach(CuonSachDTO cs) throws Exception {
        String sql = "INSERT INTO CuonSach "
                + "(id_ca_biet, id_dau_sach, trang_thai_luu_thong, tinh_trang_vat_ly, vi_tri_ke, ghi_chu) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, cs.getIdCaBiet());
            pstmt.setString(2, cs.getIdDauSach());
            pstmt.setString(3, cs.getTrangThaiLuuThong());
            pstmt.setString(4, cs.getTinhTrangVatLy());
            pstmt.setString(5, cs.getViTriKe());
            pstmt.setString(6, cs.getGhiChu());

            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean capNhatCuonSach(CuonSachDTO cs) throws Exception {
        String sql = "UPDATE CuonSach "
                + "SET id_dau_sach = ?, "
                + "trang_thai_luu_thong = ?, "
                + "tinh_trang_vat_ly = ?, "
                + "vi_tri_ke = ?, "
                + "ghi_chu = ? "
                + "WHERE id_ca_biet = ?";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, cs.getIdDauSach());
            pstmt.setString(2, cs.getTrangThaiLuuThong());
            pstmt.setString(3, cs.getTinhTrangVatLy());
            pstmt.setString(4, cs.getViTriKe());
            pstmt.setString(5, cs.getGhiChu());
            pstmt.setString(6, cs.getIdCaBiet());

            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean thanhLyCuonSach(String idCaBiet) throws Exception {
        String sql = "UPDATE CuonSach "
                + "SET trang_thai_luu_thong = N'Đã thanh lý', "
                + "tinh_trang_vat_ly = N'Hư hỏng', "
                + "ghi_chu = N'Đã thanh lý khỏi kho lưu thông' "
                + "WHERE id_ca_biet = ?";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, idCaBiet);
            return pstmt.executeUpdate() > 0;
        }
    }

    private DauSachDTO mapDauSach(ResultSet rs) throws Exception {
        DauSachDTO ds = new DauSachDTO();

        ds.setIdDauSach(rs.getString("id_dau_sach"));
        ds.setTenSach(rs.getString("ten_sach"));
        ds.setTacGia(rs.getString("tac_gia"));
        ds.setTheLoai(rs.getString("the_loai"));
        ds.setNhaXuatBan(rs.getString("nha_xuat_ban"));

        Object nam = rs.getObject("nam_xuat_ban");
        ds.setNamXuatBan(nam == null ? null : rs.getInt("nam_xuat_ban"));

        ds.setTomTat(rs.getString("tom_tat"));
        ds.setTrangThai(rs.getString("trang_thai"));

        return ds;
    }

    private CuonSachDTO mapCuonSach(ResultSet rs) throws Exception {
        CuonSachDTO cs = new CuonSachDTO();

        cs.setIdCaBiet(rs.getString("id_ca_biet"));
        cs.setIdDauSach(rs.getString("id_dau_sach"));
        cs.setTenSach(rs.getString("ten_sach"));
        cs.setTrangThaiLuuThong(rs.getString("trang_thai_luu_thong"));
        cs.setTinhTrangVatLy(rs.getString("tinh_trang_vat_ly"));
        cs.setViTriKe(rs.getString("vi_tri_ke"));
        cs.setNgayNhapKho(rs.getTimestamp("ngay_nhap_kho"));
        cs.setGhiChu(rs.getString("ghi_chu"));

        return cs;
    }
}