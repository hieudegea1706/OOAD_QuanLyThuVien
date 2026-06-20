package controllers;

import dao.DocGiaHomeDAO;
import dto.LichSuMuonTraDocGiaDTO;
import dto.SachXuHuongDTO;
import dto.ThongTinTongQuanDocGiaDTO;
import java.util.List;

public class DocGiaHomeController {

    private final DocGiaHomeDAO docGiaHomeDAO = new DocGiaHomeDAO();

    public ThongTinTongQuanDocGiaDTO layTongQuanDocGia(String tenDangNhap) throws Exception {
        tenDangNhap = tenDangNhap == null ? "" : tenDangNhap.trim();

        if (tenDangNhap.isEmpty()) {
            return null;
        }

        return docGiaHomeDAO.layTongQuanDocGia(tenDangNhap);
    }

    public List<SachXuHuongDTO> layTopSachMuonNhieu() throws Exception {
        return docGiaHomeDAO.layTopSachMuonNhieu();
    }

    public List<LichSuMuonTraDocGiaDTO> layLichSuMuonTra(String tenDangNhap) throws Exception {
        tenDangNhap = tenDangNhap == null ? "" : tenDangNhap.trim();

        if (tenDangNhap.isEmpty()) {
            throw new Exception("Không xác định được độc giả đang đăng nhập!");
        }

        return docGiaHomeDAO.layLichSuMuonTra(tenDangNhap);
    }
}