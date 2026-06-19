package controllers;

import dao.PhieuPhatDAO;
import dto.KetQuaXuLy;
import dto.PhieuPhatDTO;
import java.util.List;

public class PhieuPhatController {

    private final PhieuPhatDAO phieuPhatDAO = new PhieuPhatDAO();

    public List<PhieuPhatDTO> layDanhSachPhieuPhat() throws Exception {
        return phieuPhatDAO.layDanhSachPhieuPhat();
    }

    public List<PhieuPhatDTO> timKiemPhieuPhat(String tuKhoa) throws Exception {
        tuKhoa = tuKhoa == null ? "" : tuKhoa.trim();

        if (tuKhoa.isEmpty()) {
            return phieuPhatDAO.layDanhSachPhieuPhat();
        }

        return phieuPhatDAO.timKiemPhieuPhat(tuKhoa);
    }

    public KetQuaXuLy xacNhanThanhToan(int idPhieuPhat, String trangThaiHienTai) {
        try {
            if (idPhieuPhat <= 0) {
                return KetQuaXuLy.thatBai("Mã phiếu phạt không hợp lệ!");
            }

            if ("Đã thanh toán".equalsIgnoreCase(trangThaiHienTai)) {
                return KetQuaXuLy.thatBai("Phiếu phạt này đã được thanh toán rồi!");
            }

            boolean ok = phieuPhatDAO.xacNhanThanhToan(idPhieuPhat);

            if (ok) {
                return KetQuaXuLy.thanhCong("Đã xác nhận thanh toán phiếu phạt!");
            }

            return KetQuaXuLy.thatBai("Không tìm thấy phiếu phạt cần cập nhật hoặc phiếu đã thanh toán!");

        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaXuLy.thatBai("Lỗi khi xác nhận thanh toán: " + e.getMessage());
        }
    }
}