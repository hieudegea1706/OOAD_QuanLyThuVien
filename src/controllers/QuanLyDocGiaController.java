package controllers;

import dao.DocGiaDAO;
import dto.DocGiaDTO;
import dto.KetQuaXuLy;
import java.math.BigDecimal;
import java.util.List;

public class QuanLyDocGiaController {

    private DocGiaDAO docGiaDAO = new DocGiaDAO();

    public List<DocGiaDTO> layDanhSachDocGia(String cheDo) throws Exception {
        return docGiaDAO.layDanhSachDocGia(cheDo);
    }

    public List<DocGiaDTO> timKiemDocGia(String cheDo, String tuKhoa) throws Exception {
        tuKhoa = tuKhoa == null ? "" : tuKhoa.trim();

        if (tuKhoa.isEmpty()) {
            return docGiaDAO.layDanhSachDocGia(cheDo);
        }

        return docGiaDAO.timKiemDocGia(cheDo, tuKhoa);
    }

    public KetQuaXuLy duyetVaThuCoc(
            String tenDangNhap,
            String vaiTro,
            String trangThaiHienTai,
            BigDecimal tienCoc
    ) {
        try {
            if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
                return KetQuaXuLy.thatBai("Vui lòng chọn một tài khoản cần duyệt!");
            }

            if (!"Độc giả ngoài".equalsIgnoreCase(vaiTro)) {
                return KetQuaXuLy.thatBai("Chỉ tài khoản Độc giả ngoài mới cần duyệt và thu cọc!");
            }

            if (!"Chờ duyệt".equalsIgnoreCase(trangThaiHienTai)) {
                return KetQuaXuLy.thatBai("Chỉ có thể duyệt tài khoản đang ở trạng thái Chờ duyệt!");
            }

            if (tienCoc == null || tienCoc.compareTo(BigDecimal.ZERO) <= 0) {
                return KetQuaXuLy.thatBai("Tiền cọc phải lớn hơn 0!");
            }

            boolean ok = docGiaDAO.duyetVaThuCoc(tenDangNhap, tienCoc);

            if (ok) {
                return KetQuaXuLy.thanhCong(
                        "Duyệt tài khoản thành công!\n"
                        + "Đã thu cọc: " + tienCoc + " VNĐ"
                );
            }

            return KetQuaXuLy.thatBai("Duyệt thất bại! Không tìm thấy đủ dữ liệu tài khoản.");

        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaXuLy.thatBai("Lỗi khi duyệt tài khoản: " + e.getMessage());
        }
    }

    public KetQuaXuLy capNhatTrangThaiTaiKhoan(
            String tenDangNhap,
            String trangThaiHienTai,
            String trangThaiMoi
    ) {
        try {
            if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
                return KetQuaXuLy.thatBai("Vui lòng chọn một tài khoản trong bảng!");
            }

            if (trangThaiHienTai != null && trangThaiHienTai.equalsIgnoreCase(trangThaiMoi)) {
                return KetQuaXuLy.thatBai("Tài khoản đã ở trạng thái: " + trangThaiMoi);
            }

            boolean ok = docGiaDAO.capNhatTrangThaiTaiKhoan(tenDangNhap, trangThaiMoi);

            if (ok) {
                return KetQuaXuLy.thanhCong("Cập nhật trạng thái tài khoản thành công!");
            }

            return KetQuaXuLy.thatBai("Không tìm thấy tài khoản cần cập nhật!");

        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaXuLy.thatBai("Lỗi khi cập nhật trạng thái: " + e.getMessage());
        }
    }
}