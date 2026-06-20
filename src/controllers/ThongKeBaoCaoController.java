package controllers;

import dao.BaoCaoDAO;
import dto.SachQuaHanDTO;
import dto.TongQuanThongKeDTO;
import dto.TopSachMuonDTO;
import java.util.List;

public class ThongKeBaoCaoController {

    private final BaoCaoDAO baoCaoDAO = new BaoCaoDAO();

    public TongQuanThongKeDTO layTongQuanThongKe() throws Exception {
        return baoCaoDAO.layTongQuanThongKe();
    }

    public List<SachQuaHanDTO> layDanhSachQuaHan() throws Exception {
        return baoCaoDAO.layDanhSachQuaHan();
    }

    public List<TopSachMuonDTO> layTopSachMuonNhieu() throws Exception {
        return baoCaoDAO.layTopSachMuonNhieu();
    }
}