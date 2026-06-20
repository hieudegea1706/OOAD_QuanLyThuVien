package controllers;

import dao.DocGiaDAO;
import dto.DocGiaDTO;
import dto.KetQuaXuLy;
import java.math.BigDecimal;
import java.util.List;

public class QuanLyDocGiaController {

    private final DocGiaDAO docGiaDAO = new DocGiaDAO();

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

    public DocGiaDTO timDocGiaTheoTenDangNhap(String tenDangNhap) throws Exception {
        tenDangNhap = chuanHoa(tenDangNhap);

        if (tenDangNhap.isEmpty()) {
            return null;
        }

        return docGiaDAO.timDocGiaTheoTenDangNhap(tenDangNhap);
    }

    public KetQuaXuLy duyetVaThuCoc(
            String tenDangNhap,
            String vaiTro,
            String trangThaiHienTai,
            BigDecimal tienCoc
    ) {
        try {
            tenDangNhap = chuanHoa(tenDangNhap);
            vaiTro = chuanHoa(vaiTro);
            trangThaiHienTai = chuanHoa(trangThaiHienTai);

            if (tenDangNhap.isEmpty()) {
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
                        + "Đã thu cọc: " + formatTien(tienCoc) + " VNĐ"
                );
            }

            return KetQuaXuLy.thatBai("Duyệt thất bại! Không tìm thấy đủ dữ liệu tài khoản.");

        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaXuLy.thatBai("Lỗi khi duyệt tài khoản: " + e.getMessage());
        }
    }

    public KetQuaXuLy capNhatHoSoDocGiaNgoai(
            String tenDangNhap,
            String hoTen,
            String cccd,
            String soDienThoai,
            String ghiChu
    ) {
        try {
            tenDangNhap = chuanHoa(tenDangNhap);
            hoTen = chuanHoa(hoTen);
            cccd = chuanHoa(cccd);
            soDienThoai = chuanHoa(soDienThoai);
            ghiChu = chuanHoa(ghiChu);

            if (tenDangNhap.isEmpty()) {
                return KetQuaXuLy.thatBai("Vui lòng chọn độc giả ngoài cần cập nhật hồ sơ!");
            }

            if (hoTen.isEmpty()) {
                return KetQuaXuLy.thatBai("Họ tên không được để trống!");
            }

            if (cccd.isEmpty()) {
                return KetQuaXuLy.thatBai("Số CCCD không được để trống!");
            }

            if (soDienThoai.isEmpty()) {
                return KetQuaXuLy.thatBai("Số điện thoại không được để trống!");
            }

            DocGiaDTO docGia = docGiaDAO.timDocGiaTheoTenDangNhap(tenDangNhap);

            if (docGia == null) {
                return KetQuaXuLy.thatBai("Không tìm thấy độc giả cần cập nhật!");
            }

            if (!"Độc giả ngoài".equalsIgnoreCase(docGia.getVaiTro())) {
                return KetQuaXuLy.thatBai("UC10.1 chỉ áp dụng cho độc giả ngoài!");
            }

            boolean ok = docGiaDAO.capNhatHoSoDocGiaNgoai(
                    tenDangNhap,
                    hoTen,
                    cccd,
                    soDienThoai,
                    ghiChu
            );

            if (ok) {
                return KetQuaXuLy.thanhCong("Cập nhật hồ sơ độc giả ngoài thành công!");
            }

            return KetQuaXuLy.thatBai("Cập nhật thất bại! Không tìm thấy dữ liệu độc giả ngoài.");

        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaXuLy.thatBai("Lỗi khi cập nhật hồ sơ: " + e.getMessage());
        }
    }

    public KetQuaXuLy hoanTraCocVaHuyThe(String tenDangNhap) {
        try {
            tenDangNhap = chuanHoa(tenDangNhap);

            if (tenDangNhap.isEmpty()) {
                return KetQuaXuLy.thatBai("Vui lòng chọn độc giả ngoài cần hoàn trả cọc!");
            }

            DocGiaDTO docGia = docGiaDAO.timDocGiaTheoTenDangNhap(tenDangNhap);

            if (docGia == null) {
                return KetQuaXuLy.thatBai("Không tìm thấy độc giả cần xử lý!");
            }

            if (!"Độc giả ngoài".equalsIgnoreCase(docGia.getVaiTro())) {
                return KetQuaXuLy.thatBai("UC10.2 chỉ áp dụng cho độc giả ngoài!");
            }

            if (docGia.getTienCoc() == null || docGia.getTienCoc().compareTo(BigDecimal.ZERO) <= 0) {
                return KetQuaXuLy.thatBai("Độc giả này không còn tiền cọc để hoàn trả!");
            }

            BigDecimal tienHoanTra = docGia.getTienCoc();

            boolean ok = docGiaDAO.hoanTraCocVaHuyThe(tenDangNhap);

            if (ok) {
                return KetQuaXuLy.thanhCong(
                        "Hoàn trả tiền cọc & hủy thẻ thành công!\n\n"
                        + "Độc giả: " + docGia.getHoTen() + "\n"
                        + "Số tiền hoàn trả: " + formatTien(tienHoanTra) + " VNĐ\n"
                        + "Tài khoản đã được chuyển sang trạng thái Bị khóa."
                );
            }

            return KetQuaXuLy.thatBai("Hoàn trả cọc thất bại!");

        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaXuLy.thatBai("Lỗi khi hoàn trả cọc & hủy thẻ: " + e.getMessage());
        }
    }

    public KetQuaXuLy capNhatTrangThaiTaiKhoan(
            String tenDangNhap,
            String trangThaiHienTai,
            String trangThaiMoi
    ) {
        try {
            tenDangNhap = chuanHoa(tenDangNhap);
            trangThaiHienTai = chuanHoa(trangThaiHienTai);
            trangThaiMoi = chuanHoa(trangThaiMoi);

            if (tenDangNhap.isEmpty()) {
                return KetQuaXuLy.thatBai("Vui lòng chọn một tài khoản trong bảng!");
            }

            if (trangThaiHienTai.equalsIgnoreCase(trangThaiMoi)) {
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

    private String chuanHoa(String value) {
        return value == null ? "" : value.trim();
    }

    private String formatTien(BigDecimal soTien) {
        if (soTien == null) {
            return "0";
        }

        return String.format("%,.0f", soTien.doubleValue());
    }
}