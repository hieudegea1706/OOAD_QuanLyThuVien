package controllers;

import dao.MuonTraDAO;
import dto.DocGiaMuonDTO;
import dto.KetQuaXuLy;
import dto.SachMuonDTO;
import dto.ThongTinTraSachDTO;
import java.math.BigDecimal;
import java.util.List;

public class MuonTraController {

    private MuonTraDAO muonTraDAO = new MuonTraDAO();

    public DocGiaMuonDTO kiemTraDocGia(String tenDangNhap) {
        DocGiaMuonDTO ketQua = new DocGiaMuonDTO();

        try {
            tenDangNhap = tenDangNhap == null ? "" : tenDangNhap.trim();

            if (tenDangNhap.isEmpty()) {
                ketQua.setHopLe(false);
                ketQua.setThongBao("Vui lòng nhập tên đăng nhập độc giả!");
                return ketQua;
            }

            DocGiaMuonDTO docGia = muonTraDAO.timDocGiaMuon(tenDangNhap);

            if (docGia == null) {
                ketQua.setHopLe(false);
                ketQua.setThongBao("Không tìm thấy độc giả.");
                return ketQua;
            }

            if (!"Đang hoạt động".equalsIgnoreCase(docGia.getTrangThaiTaiKhoan())) {
                docGia.setHopLe(false);
                docGia.setThongBao(
                        "Tài khoản chưa được phép mượn. Trạng thái: "
                        + docGia.getTrangThaiTaiKhoan()
                );
                return docGia;
            }

            docGia.setHopLe(true);
            docGia.setThongBao(
                    "Hợp lệ: " + docGia.getHoTen()
                    + " - " + docGia.getVaiTro()
            );

            return docGia;

        } catch (Exception e) {
            e.printStackTrace();
            ketQua.setHopLe(false);
            ketQua.setThongBao("Lỗi khi kiểm tra độc giả: " + e.getMessage());
            return ketQua;
        }
    }

    public SachMuonDTO kiemTraSachMuon(String idCaBiet) {
        SachMuonDTO ketQua = new SachMuonDTO();

        try {
            idCaBiet = idCaBiet == null ? "" : idCaBiet.trim();

            if (idCaBiet.isEmpty()) {
                ketQua.setCoTheMuon(false);
                ketQua.setThongBao("Vui lòng nhập mã cá biệt cuốn sách!");
                return ketQua;
            }

            SachMuonDTO sach = muonTraDAO.timSachMuon(idCaBiet);

            if (sach == null) {
                ketQua.setCoTheMuon(false);
                ketQua.setThongBao("Không tìm thấy cuốn sách.");
                return ketQua;
            }

            if (!"Sẵn sàng".equalsIgnoreCase(sach.getTrangThaiLuuThong())) {
                sach.setCoTheMuon(false);
                sach.setThongBao("Không thể mượn. Trạng thái: " + sach.getTrangThaiLuuThong());
                return sach;
            }

            if ("Hư hỏng".equalsIgnoreCase(sach.getTinhTrangVatLy())
                    || "Mất".equalsIgnoreCase(sach.getTinhTrangVatLy())) {
                sach.setCoTheMuon(false);
                sach.setThongBao("Không thể mượn. Tình trạng vật lý: " + sach.getTinhTrangVatLy());
                return sach;
            }

            sach.setCoTheMuon(true);
            sach.setThongBao("Đã thêm sách vào phiếu tạm.");
            return sach;

        } catch (Exception e) {
            e.printStackTrace();
            ketQua.setCoTheMuon(false);
            ketQua.setThongBao("Lỗi khi kiểm tra sách: " + e.getMessage());
            return ketQua;
        }
    }

    public KetQuaXuLy lapPhieuMuon(String tenDangNhap, int soNgayMuon, List<String> danhSachIdCaBiet) {
        try {
            tenDangNhap = tenDangNhap == null ? "" : tenDangNhap.trim();

            if (tenDangNhap.isEmpty()) {
                return KetQuaXuLy.thatBai("Vui lòng kiểm tra độc giả hợp lệ trước khi lập phiếu!");
            }

            if (danhSachIdCaBiet == null || danhSachIdCaBiet.isEmpty()) {
                return KetQuaXuLy.thatBai("Vui lòng thêm ít nhất một cuốn sách vào phiếu!");
            }

            if (soNgayMuon <= 0) {
                return KetQuaXuLy.thatBai("Số ngày mượn phải lớn hơn 0!");
            }

            DocGiaMuonDTO docGia = muonTraDAO.timDocGiaMuon(tenDangNhap);

            if (docGia == null) {
                return KetQuaXuLy.thatBai("Không tìm thấy thông tin độc giả để lập phiếu!");
            }

            if (!"Đang hoạt động".equalsIgnoreCase(docGia.getTrangThaiTaiKhoan())) {
                return KetQuaXuLy.thatBai(
                        "Độc giả chưa được phép mượn sách. Trạng thái: "
                        + docGia.getTrangThaiTaiKhoan()
                );
            }

            for (String idCaBiet : danhSachIdCaBiet) {
                SachMuonDTO sach = kiemTraSachMuon(idCaBiet);

                if (!sach.isCoTheMuon()) {
                    return KetQuaXuLy.thatBai(
                            "Không thể lập phiếu vì sách " + idCaBiet + " không hợp lệ.\n"
                            + sach.getThongBao()
                    );
                }
            }

            int hanMucMuon = tinhHanMucMuon(docGia.getVaiTro(), docGia.getTienCoc());
            int soSachDangMuon = muonTraDAO.demSoSachDangMuon(tenDangNhap);
            int tongSauKhiMuon = soSachDangMuon + danhSachIdCaBiet.size();

            if (hanMucMuon <= 0) {
                return KetQuaXuLy.thatBai(
                        "Độc giả này chưa đủ điều kiện mượn sách.\n"
                        + "Họ tên: " + docGia.getHoTen() + "\n"
                        + "Vai trò: " + docGia.getVaiTro() + "\n"
                        + "Tiền cọc hiện có: " + formatTien(docGia.getTienCoc()) + " VNĐ\n\n"
                        + "Quy định: Độc giả ngoài cần cọc tối thiểu 100.000 VNĐ để được mượn sách."
                );
            }

            if (tongSauKhiMuon > hanMucMuon) {
                return KetQuaXuLy.thatBai(
                        "Không thể lập phiếu mượn vì vượt hạn mức mượn sách.\n\n"
                        + "Độc giả: " + docGia.getHoTen() + "\n"
                        + "Vai trò: " + docGia.getVaiTro() + "\n"
                        + "Tiền cọc: " + formatTien(docGia.getTienCoc()) + " VNĐ\n"
                        + "Hạn mức được mượn: " + hanMucMuon + " cuốn\n"
                        + "Đang mượn hiện tại: " + soSachDangMuon + " cuốn\n"
                        + "Số sách trong phiếu mới: " + danhSachIdCaBiet.size() + " cuốn\n"
                        + "Tổng sau khi mượn: " + tongSauKhiMuon + " cuốn"
                );
            }

            int idPhieuMuon = muonTraDAO.taoPhieuMuon(tenDangNhap, soNgayMuon, danhSachIdCaBiet);

            return KetQuaXuLy.thanhCong(
                    "Lập phiếu mượn thành công!\n"
                    + "Mã phiếu mượn: " + idPhieuMuon
            );

        } catch (Exception e) {
            e.printStackTrace();
            return KetQuaXuLy.thatBai("Lỗi khi lập phiếu mượn: " + e.getMessage());
        }
    }

    private int tinhHanMucMuon(String vaiTro, BigDecimal tienCoc) {
        if (tienCoc == null) {
            tienCoc = BigDecimal.ZERO;
        }

        if (vaiTro != null && vaiTro.equalsIgnoreCase("Độc giả ngoài")) {
            if (tienCoc.compareTo(new BigDecimal("100000")) < 0) {
                return 0;
            }

            if (tienCoc.compareTo(new BigDecimal("200000")) < 0) {
                return 1;
            }

            if (tienCoc.compareTo(new BigDecimal("300000")) < 0) {
                return 2;
            }

            return 3;
        }

        return 5;
    }

    private String formatTien(BigDecimal soTien) {
        if (soTien == null) {
            return "0";
        }

        return String.format("%,.0f", soTien.doubleValue());
    }
    
    public ThongTinTraSachDTO timSachDangMuonDeTra(String idCaBiet) {
    ThongTinTraSachDTO ketQua = new ThongTinTraSachDTO();

    try {
        idCaBiet = idCaBiet == null ? "" : idCaBiet.trim();

        if (idCaBiet.isEmpty()) {
            ketQua.setTimThay(false);
            ketQua.setThongBao("Vui lòng nhập mã cá biệt cuốn sách cần trả!");
            return ketQua;
        }

        ThongTinTraSachDTO thongTin = muonTraDAO.timSachDangMuonDeTra(idCaBiet);

        if (thongTin == null) {
            ketQua.setTimThay(false);
            ketQua.setThongBao(
                    "Không tìm thấy cuốn sách đang mượn với mã: " + idCaBiet
                    + "\nCó thể sách chưa được mượn hoặc đã được trả."
            );
            return ketQua;
        }

        thongTin.setTimThay(true);

        if (thongTin.getSoNgayQuaHan() > 0) {
            thongTin.setThongBao(
                    "Sách đang quá hạn " + thongTin.getSoNgayQuaHan()
                    + " ngày.\nKhi xử lý trả, hệ thống sẽ lập phiếu phạt."
            );
        } else {
            thongTin.setThongBao("Tìm thấy sách đang mượn.");
        }

        return thongTin;

    } catch (Exception e) {
        e.printStackTrace();
        ketQua.setTimThay(false);
        ketQua.setThongBao("Lỗi khi tìm sách đang mượn: " + e.getMessage());
        return ketQua;
    }
}

public String taoThongBaoXacNhanTra(
        int idPhieuMuon,
        String idCaBiet,
        int soNgayQuaHan,
        String tinhTrangTra
) {
    boolean quaHan = soNgayQuaHan > 0;
    boolean coHuHong = !"Tốt".equalsIgnoreCase(tinhTrangTra);

    BigDecimal tienPhat = tinhTienPhat(soNgayQuaHan, tinhTrangTra);
    String loaiViPham = taoLoaiViPham(quaHan, tinhTrangTra);

    String thongBao = "Xác nhận trả cuốn sách " + idCaBiet
            + " cho phiếu mượn " + idPhieuMuon + "?";

    if (quaHan || coHuHong) {
        thongBao += "\n\nPhát hiện vi phạm: " + loaiViPham
                + "\nSố tiền phạt dự kiến: " + formatTien(tienPhat) + " VNĐ"
                + "\nHệ thống sẽ lập phiếu phạt.";
    }

    return thongBao;
}

public KetQuaXuLy xuLyTraSach(
        int idPhieuMuon,
        String idCaBiet,
        String tenDangNhap,
        int soNgayQuaHan,
        String tinhTrangTra,
        String ghiChuTra
) {
    try {
        boolean quaHan = soNgayQuaHan > 0;
        boolean coHuHong = !"Tốt".equalsIgnoreCase(tinhTrangTra);
        boolean canLapPhieuPhat = quaHan || coHuHong;

        BigDecimal tienPhat = tinhTienPhat(soNgayQuaHan, tinhTrangTra);
        String loaiViPham = taoLoaiViPham(quaHan, tinhTrangTra);
        String moTaViPham = taoMoTaViPham(soNgayQuaHan, tinhTrangTra, ghiChuTra);
        String trangThaiLuuThongMoi = xacDinhTrangThaiLuuThongMoi(tinhTrangTra);

        String trangThaiPhieuMoi = muonTraDAO.xuLyTraSach(
                idPhieuMuon,
                idCaBiet,
                tenDangNhap,
                tinhTrangTra,
                ghiChuTra,
                canLapPhieuPhat,
                tienPhat,
                loaiViPham,
                soNgayQuaHan,
                moTaViPham,
                trangThaiLuuThongMoi
        );

        String message = "Trả sách thành công!\n"
                + "Mã sách: " + idCaBiet + "\n"
                + "Trạng thái sách mới: " + trangThaiLuuThongMoi + "\n"
                + "Trạng thái phiếu: " + trangThaiPhieuMoi;

        if (canLapPhieuPhat) {
            message += "\n\nĐã lập phiếu phạt."
                    + "\nLoại vi phạm: " + loaiViPham
                    + "\nTiền phạt: " + formatTien(tienPhat) + " VNĐ";
        }

        return KetQuaXuLy.thanhCong(message);

    } catch (Exception e) {
        e.printStackTrace();
        return KetQuaXuLy.thatBai("Lỗi khi xử lý trả sách: " + e.getMessage());
    }
}

private BigDecimal tinhTienPhat(int soNgayQuaHan, String tinhTrangTra) {
    BigDecimal tienPhat = BigDecimal.ZERO;

    if (soNgayQuaHan > 0) {
        tienPhat = tienPhat.add(new BigDecimal(soNgayQuaHan * 5000L));
    }

    if ("Rách nhẹ".equalsIgnoreCase(tinhTrangTra)) {
        tienPhat = tienPhat.add(new BigDecimal("20000"));
    } else if ("Hư hỏng".equalsIgnoreCase(tinhTrangTra)) {
        tienPhat = tienPhat.add(new BigDecimal("50000"));
    } else if ("Mất".equalsIgnoreCase(tinhTrangTra)) {
        tienPhat = tienPhat.add(new BigDecimal("100000"));
    }

    return tienPhat;
}

private String taoLoaiViPham(boolean quaHan, String tinhTrangTra) {
    boolean coHuHong = !"Tốt".equalsIgnoreCase(tinhTrangTra);

    if (quaHan && coHuHong) {
        return "Quá hạn + " + tinhTrangTra;
    }

    if (quaHan) {
        return "Quá hạn";
    }

    return tinhTrangTra;
}

private String taoMoTaViPham(int soNgayQuaHan, String tinhTrangTra, String ghiChuTra) {
    String moTa = "";

    if (soNgayQuaHan > 0) {
        moTa += "Quá hạn " + soNgayQuaHan + " ngày. ";
    }

    if (!"Tốt".equalsIgnoreCase(tinhTrangTra)) {
        moTa += "Tình trạng sách khi trả: " + tinhTrangTra + ". ";
    }

    if (ghiChuTra != null && !ghiChuTra.trim().isEmpty()) {
        moTa += "Ghi chú: " + ghiChuTra.trim();
    }

    return moTa.trim();
}

private String xacDinhTrangThaiLuuThongMoi(String tinhTrangTra) {
    if ("Tốt".equalsIgnoreCase(tinhTrangTra)) {
        return "Sẵn sàng";
    }

    if ("Rách nhẹ".equalsIgnoreCase(tinhTrangTra)) {
        return "Sẵn sàng";
    }

    if ("Hư hỏng".equalsIgnoreCase(tinhTrangTra)) {
        return "Tạm giữ xử lý";
    }

    return "Mất";
}
}