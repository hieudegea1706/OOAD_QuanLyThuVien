package controllers;

import dao.SachDAO;
import dto.CuonSachDTO;
import dto.DauSachDTO;
import dto.KetQuaXuLy;
import java.util.List;

public class QuanLySachController {

    private final SachDAO sachDAO = new SachDAO();

    // ============================================================
    // ĐẦU SÁCH
    // ============================================================

    public List<DauSachDTO> layDanhSachDauSach() throws Exception {
        return sachDAO.layDanhSachDauSach();
    }

    public List<DauSachDTO> timKiemDauSach(String tuKhoa) throws Exception {
        tuKhoa = tuKhoa == null ? "" : tuKhoa.trim();

        if (tuKhoa.isEmpty()) {
            return sachDAO.layDanhSachDauSach();
        }

        return sachDAO.timKiemDauSach(tuKhoa);
    }

    public DauSachDTO timDauSachTheoId(String idDauSach) throws Exception {
        idDauSach = idDauSach == null ? "" : idDauSach.trim();

        if (idDauSach.isEmpty()) {
            return null;
        }

        return sachDAO.timDauSachTheoId(idDauSach);
    }

    public KetQuaXuLy themDauSach(
            String id,
            String ten,
            String tacGia,
            String theLoai,
            String nhaXuatBan,
            String namXuatBanStr,
            String tomTat
    ) {
        try {
            id = chuanHoa(id);
            ten = chuanHoa(ten);
            tacGia = chuanHoa(tacGia);
            theLoai = chuanHoa(theLoai);
            nhaXuatBan = chuanHoa(nhaXuatBan);
            tomTat = chuanHoa(tomTat);

            if (id.isEmpty() || ten.isEmpty()) {
                return KetQuaXuLy.thatBai("Mã đầu sách và tên sách không được để trống!");
            }

            Integer namXuatBan = parseNamXuatBan(namXuatBanStr);

            DauSachDTO ds = new DauSachDTO();
            ds.setIdDauSach(id);
            ds.setTenSach(ten);
            ds.setTacGia(tacGia);
            ds.setTheLoai(theLoai);
            ds.setNhaXuatBan(nhaXuatBan);
            ds.setNamXuatBan(namXuatBan);
            ds.setTomTat(tomTat);

            boolean ok = sachDAO.themDauSach(ds);

            if (ok) {
                return KetQuaXuLy.thanhCong("Thêm đầu sách thành công!");
            }

            return KetQuaXuLy.thatBai("Không thể thêm đầu sách!");

        } catch (NumberFormatException e) {
            return KetQuaXuLy.thatBai("Năm xuất bản phải là số!");
        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaXuLy.thatBai("Lỗi khi thêm đầu sách: " + e.getMessage());
        }
    }

    public KetQuaXuLy capNhatDauSach(
            String id,
            String ten,
            String tacGia,
            String theLoai,
            String nhaXuatBan,
            String namXuatBanStr,
            String tomTat
    ) {
        try {
            id = chuanHoa(id);
            ten = chuanHoa(ten);
            tacGia = chuanHoa(tacGia);
            theLoai = chuanHoa(theLoai);
            nhaXuatBan = chuanHoa(nhaXuatBan);
            tomTat = chuanHoa(tomTat);

            if (id.isEmpty()) {
                return KetQuaXuLy.thatBai("Vui lòng chọn hoặc nhập mã đầu sách cần cập nhật!");
            }

            if (ten.isEmpty()) {
                return KetQuaXuLy.thatBai("Tên sách không được để trống!");
            }

            Integer namXuatBan = parseNamXuatBan(namXuatBanStr);

            DauSachDTO ds = new DauSachDTO();
            ds.setIdDauSach(id);
            ds.setTenSach(ten);
            ds.setTacGia(tacGia);
            ds.setTheLoai(theLoai);
            ds.setNhaXuatBan(nhaXuatBan);
            ds.setNamXuatBan(namXuatBan);
            ds.setTomTat(tomTat);

            boolean ok = sachDAO.capNhatDauSach(ds);

            if (ok) {
                return KetQuaXuLy.thanhCong("Cập nhật đầu sách thành công!");
            }

            return KetQuaXuLy.thatBai("Không tìm thấy đầu sách cần cập nhật!");

        } catch (NumberFormatException e) {
            return KetQuaXuLy.thatBai("Năm xuất bản phải là số!");
        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaXuLy.thatBai("Lỗi khi cập nhật đầu sách: " + e.getMessage());
        }
    }

    public KetQuaXuLy ngungPhucVuDauSach(String idDauSach) {
        try {
            idDauSach = chuanHoa(idDauSach);

            if (idDauSach.isEmpty()) {
                return KetQuaXuLy.thatBai("Vui lòng chọn đầu sách cần ngừng phục vụ!");
            }

            boolean ok = sachDAO.ngungPhucVuDauSach(idDauSach);

            if (ok) {
                return KetQuaXuLy.thanhCong("Đã chuyển đầu sách sang trạng thái Ngừng phục vụ!");
            }

            return KetQuaXuLy.thatBai("Không tìm thấy đầu sách cần ngừng phục vụ!");

        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaXuLy.thatBai("Lỗi khi ngừng phục vụ đầu sách: " + e.getMessage());
        }
    }

    // ============================================================
    // CUỐN SÁCH
    // ============================================================

    public List<CuonSachDTO> layDanhSachCuonSach() throws Exception {
        return sachDAO.layDanhSachCuonSach();
    }

    public List<CuonSachDTO> timKiemCuonSach(String tuKhoa) throws Exception {
        tuKhoa = tuKhoa == null ? "" : tuKhoa.trim();

        if (tuKhoa.isEmpty()) {
            return sachDAO.layDanhSachCuonSach();
        }

        return sachDAO.timKiemCuonSach(tuKhoa);
    }

    public KetQuaXuLy themCuonSach(
            String idCaBiet,
            String idDauSach,
            String trangThai,
            String tinhTrang,
            String viTri,
            String ghiChu
    ) {
        try {
            idCaBiet = chuanHoa(idCaBiet);
            idDauSach = chuanHoa(idDauSach);
            trangThai = chuanHoa(trangThai);
            tinhTrang = chuanHoa(tinhTrang);
            viTri = chuanHoa(viTri);
            ghiChu = chuanHoa(ghiChu);

            if (idCaBiet.isEmpty() || idDauSach.isEmpty()) {
                return KetQuaXuLy.thatBai("Mã cá biệt và mã đầu sách không được để trống!");
            }

            if (sachDAO.timDauSachTheoId(idDauSach) == null) {
                return KetQuaXuLy.thatBai("Không tìm thấy đầu sách có mã: " + idDauSach);
            }

            CuonSachDTO cs = new CuonSachDTO();
            cs.setIdCaBiet(idCaBiet);
            cs.setIdDauSach(idDauSach);
            cs.setTrangThaiLuuThong(trangThai);
            cs.setTinhTrangVatLy(tinhTrang);
            cs.setViTriKe(viTri);
            cs.setGhiChu(ghiChu);

            boolean ok = sachDAO.themCuonSach(cs);

            if (ok) {
                return KetQuaXuLy.thanhCong("Thêm cuốn sách thành công!");
            }

            return KetQuaXuLy.thatBai("Không thể thêm cuốn sách!");

        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaXuLy.thatBai("Lỗi khi thêm cuốn sách: " + e.getMessage());
        }
    }

    public KetQuaXuLy capNhatCuonSach(
            String idCaBiet,
            String idDauSach,
            String trangThai,
            String tinhTrang,
            String viTri,
            String ghiChu
    ) {
        try {
            idCaBiet = chuanHoa(idCaBiet);
            idDauSach = chuanHoa(idDauSach);
            trangThai = chuanHoa(trangThai);
            tinhTrang = chuanHoa(tinhTrang);
            viTri = chuanHoa(viTri);
            ghiChu = chuanHoa(ghiChu);

            if (idCaBiet.isEmpty()) {
                return KetQuaXuLy.thatBai("Vui lòng chọn hoặc nhập mã cá biệt cần cập nhật!");
            }

            if (idDauSach.isEmpty()) {
                return KetQuaXuLy.thatBai("Mã đầu sách không được để trống!");
            }

            if (sachDAO.timDauSachTheoId(idDauSach) == null) {
                return KetQuaXuLy.thatBai("Không tìm thấy đầu sách có mã: " + idDauSach);
            }

            CuonSachDTO cs = new CuonSachDTO();
            cs.setIdCaBiet(idCaBiet);
            cs.setIdDauSach(idDauSach);
            cs.setTrangThaiLuuThong(trangThai);
            cs.setTinhTrangVatLy(tinhTrang);
            cs.setViTriKe(viTri);
            cs.setGhiChu(ghiChu);

            boolean ok = sachDAO.capNhatCuonSach(cs);

            if (ok) {
                return KetQuaXuLy.thanhCong("Cập nhật cuốn sách thành công!");
            }

            return KetQuaXuLy.thatBai("Không tìm thấy cuốn sách cần cập nhật!");

        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaXuLy.thatBai("Lỗi khi cập nhật cuốn sách: " + e.getMessage());
        }
    }

    public KetQuaXuLy thanhLyCuonSach(String idCaBiet) {
        try {
            idCaBiet = chuanHoa(idCaBiet);

            if (idCaBiet.isEmpty()) {
                return KetQuaXuLy.thatBai("Vui lòng chọn cuốn sách cần thanh lý!");
            }

            boolean ok = sachDAO.thanhLyCuonSach(idCaBiet);

            if (ok) {
                return KetQuaXuLy.thanhCong("Đã thanh lý cuốn sách!");
            }

            return KetQuaXuLy.thatBai("Không tìm thấy cuốn sách cần thanh lý!");

        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaXuLy.thatBai("Lỗi khi thanh lý cuốn sách: " + e.getMessage());
        }
    }

    private String chuanHoa(String value) {
        return value == null ? "" : value.trim();
    }

    private Integer parseNamXuatBan(String namXuatBanStr) {
        namXuatBanStr = chuanHoa(namXuatBanStr);

        if (namXuatBanStr.isEmpty()) {
            return null;
        }

        return Integer.parseInt(namXuatBanStr);
    }
}