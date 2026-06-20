package views;

import components.ModernButton;
import components.ModernTextField;
import components.RoundedPanel;
import controllers.QuanLyDocGiaController;
import dto.DocGiaDTO;
import dto.KetQuaXuLy;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import utils.UITheme;

public class QuanLyDocGiaPnl extends JPanel {

    private JTable tblDocGia;
    private ModernTextField txtTimKiem;

    private JLabel lblTitle;
    private JLabel lblSubtitle;

    private ModernButton btnDuyet;
    private ModernButton btnCapNhatHoSo;
    private ModernButton btnHoanTraCoc;
    private ModernButton btnKhoa;
    private ModernButton btnMoKhoa;

    private String cheDo = "ALL";

    private final QuanLyDocGiaController quanLyDocGiaController = new QuanLyDocGiaController();
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public QuanLyDocGiaPnl() {
        buildUI();
        setCheDo("ALL");
    }

    public void setCheDo(String cheDoMoi) {
        this.cheDo = cheDoMoi;

        if (lblTitle != null && lblSubtitle != null) {
            if ("ALL".equals(cheDo)) {
                lblTitle.setText("Tất cả tài khoản độc giả");
                lblSubtitle.setText("Tìm kiếm, duyệt, cập nhật hồ sơ, hoàn trả cọc, khóa hoặc mở khóa tài khoản độc giả");
            } else if ("DANG_HOAT_DONG".equals(cheDo)) {
                lblTitle.setText("Tài khoản đang hoạt động");
                lblSubtitle.setText("Danh sách tài khoản đang được phép sử dụng thư viện");
            } else if ("CHO_DUYET".equals(cheDo)) {
                lblTitle.setText("Tài khoản chờ duyệt & thu cọc");
                lblSubtitle.setText("Duyệt độc giả ngoài, nhập tiền cọc và kích hoạt tài khoản");
            } else if ("BI_KHOA".equals(cheDo)) {
                lblTitle.setText("Tài khoản bị khóa");
                lblSubtitle.setText("Danh sách tài khoản đang bị khóa, có thể mở khóa khi đủ điều kiện");
            }
        }

        capNhatNutTheoCheDo();

        if (txtTimKiem != null) {
            txtTimKiem.setText("");
        }

        taiDuLieuDocGia();
    }

    private void capNhatNutTheoCheDo() {
        if (btnDuyet == null
                || btnCapNhatHoSo == null
                || btnHoanTraCoc == null
                || btnKhoa == null
                || btnMoKhoa == null) {
            return;
        }

        if ("ALL".equals(cheDo)) {
            btnDuyet.setVisible(true);
            btnCapNhatHoSo.setVisible(true);
            btnHoanTraCoc.setVisible(true);
            btnKhoa.setVisible(true);
            btnMoKhoa.setVisible(true);

        } else if ("DANG_HOAT_DONG".equals(cheDo)) {
            btnDuyet.setVisible(false);
            btnCapNhatHoSo.setVisible(true);
            btnHoanTraCoc.setVisible(true);
            btnKhoa.setVisible(true);
            btnMoKhoa.setVisible(false);

        } else if ("CHO_DUYET".equals(cheDo)) {
            btnDuyet.setVisible(true);
            btnCapNhatHoSo.setVisible(true);
            btnHoanTraCoc.setVisible(false);
            btnKhoa.setVisible(false);
            btnMoKhoa.setVisible(false);

        } else if ("BI_KHOA".equals(cheDo)) {
            btnDuyet.setVisible(false);
            btnCapNhatHoSo.setVisible(true);
            btnHoanTraCoc.setVisible(false);
            btnKhoa.setVisible(false);
            btnMoKhoa.setVisible(true);
        }

        revalidate();
        repaint();
    }

    private void buildUI() {
        setLayout(new BorderLayout(0, 16));
        setBackground(UITheme.BG_MAIN);

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
    }

    private JPanel buildTopPanel() {
        RoundedPanel topCard = new RoundedPanel(24, UITheme.BG_CARD);
        topCard.setLayout(new BorderLayout(12, 14));
        topCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        lblTitle = new JLabel("Tất cả tài khoản độc giả");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(UITheme.TEXT_DARK);
        lblTitle.setAlignmentX(LEFT_ALIGNMENT);

        lblSubtitle = new JLabel("Tìm kiếm, duyệt, cập nhật hồ sơ, hoàn trả cọc, khóa hoặc mở khóa tài khoản độc giả");
        lblSubtitle.setFont(UITheme.FONT_SUBTITLE);
        lblSubtitle.setForeground(UITheme.TEXT_MUTED);
        lblSubtitle.setAlignmentX(LEFT_ALIGNMENT);

        titleBox.add(lblTitle);
        titleBox.add(Box.createVerticalStrut(6));
        titleBox.add(lblSubtitle);

        JPanel actionPanel = new JPanel(new BorderLayout(12, 0));
        actionPanel.setOpaque(false);

        txtTimKiem = new ModernTextField();
        txtTimKiem.setPreferredSize(new Dimension(250, 42));
        txtTimKiem.addActionListener(e -> timKiemDocGia());

        ModernButton btnTimKiem = new ModernButton("Tìm kiếm", UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        btnTimKiem.setPreferredSize(new Dimension(115, 42));
        btnTimKiem.addActionListener(e -> timKiemDocGia());

        ModernButton btnLamMoi = new ModernButton("Làm mới", UITheme.CYAN, UITheme.CYAN_DARK);
        btnLamMoi.setPreferredSize(new Dimension(110, 42));
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            taiDuLieuDocGia();
        });

        btnDuyet = new ModernButton("Duyệt & thu cọc", UITheme.GREEN, UITheme.GREEN_DARK);
        btnDuyet.setPreferredSize(new Dimension(155, 42));
        btnDuyet.addActionListener(e -> duyetVaThuCoc());

        btnCapNhatHoSo = new ModernButton("Cập nhật hồ sơ", UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        btnCapNhatHoSo.setPreferredSize(new Dimension(155, 42));
        btnCapNhatHoSo.addActionListener(e -> capNhatHoSoDocGiaNgoai());

        btnHoanTraCoc = new ModernButton("Hoàn trả cọc", UITheme.ORANGE, UITheme.ORANGE_DARK);
        btnHoanTraCoc.setPreferredSize(new Dimension(140, 42));
        btnHoanTraCoc.addActionListener(e -> hoanTraCocVaHuyThe());

        btnKhoa = new ModernButton("Khóa", UITheme.RED, UITheme.RED_DARK);
        btnKhoa.setPreferredSize(new Dimension(90, 42));
        btnKhoa.addActionListener(e -> capNhatTrangThaiTaiKhoan("Bị khóa"));

        btnMoKhoa = new ModernButton("Mở khóa", UITheme.PURPLE, UITheme.PURPLE_DARK);
        btnMoKhoa.setPreferredSize(new Dimension(110, 42));
        btnMoKhoa.addActionListener(e -> capNhatTrangThaiTaiKhoan("Đang hoạt động"));

        JPanel buttonBox = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        buttonBox.setOpaque(false);
        buttonBox.add(btnTimKiem);
        buttonBox.add(btnLamMoi);
        buttonBox.add(btnDuyet);
        buttonBox.add(btnCapNhatHoSo);
        buttonBox.add(btnHoanTraCoc);
        buttonBox.add(btnKhoa);
        buttonBox.add(btnMoKhoa);

        actionPanel.add(txtTimKiem, BorderLayout.CENTER);
        actionPanel.add(buttonBox, BorderLayout.EAST);

        topCard.add(titleBox, BorderLayout.NORTH);
        topCard.add(actionPanel, BorderLayout.SOUTH);

        return topCard;
    }

    private JPanel buildTablePanel() {
        RoundedPanel tableCard = new RoundedPanel(24, UITheme.BG_CARD);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));

        tblDocGia = new JTable();

        tblDocGia.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Tên đăng nhập",
                    "Họ tên",
                    "CCCD",
                    "Số điện thoại",
                    "Vai trò",
                    "Trạng thái",
                    "Tiền cọc",
                    "Ngày đăng ký",
                    "Ngày kích hoạt",
                    "Ghi chú"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        styleTable();

        JScrollPane scrollPane = new JScrollPane(tblDocGia);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        tableCard.add(scrollPane, BorderLayout.CENTER);

        return tableCard;
    }

    private void styleTable() {
        tblDocGia.setFont(UITheme.FONT_NORMAL);
        tblDocGia.setRowHeight(38);
        tblDocGia.setGridColor(new java.awt.Color(226, 232, 240));
        tblDocGia.setSelectionBackground(UITheme.PRIMARY_LIGHT);
        tblDocGia.setSelectionForeground(UITheme.TEXT_DARK);
        tblDocGia.setShowVerticalLines(false);
        tblDocGia.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tblDocGia.getTableHeader().setFont(UITheme.FONT_BOLD);
        tblDocGia.getTableHeader().setBackground(new java.awt.Color(226, 232, 240));
        tblDocGia.getTableHeader().setForeground(UITheme.TEXT_DARK);
        tblDocGia.getTableHeader().setReorderingAllowed(false);

        tblDocGia.getColumnModel().getColumn(0).setPreferredWidth(130);
        tblDocGia.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblDocGia.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblDocGia.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblDocGia.getColumnModel().getColumn(4).setPreferredWidth(120);
        tblDocGia.getColumnModel().getColumn(5).setPreferredWidth(140);
        tblDocGia.getColumnModel().getColumn(6).setPreferredWidth(120);
        tblDocGia.getColumnModel().getColumn(7).setPreferredWidth(160);
        tblDocGia.getColumnModel().getColumn(8).setPreferredWidth(160);
        tblDocGia.getColumnModel().getColumn(9).setPreferredWidth(260);
    }

    // ============================================================
    // LOAD / SEARCH
    // ============================================================

    private void taiDuLieuDocGia() {
        try {
            List<DocGiaDTO> danhSach = quanLyDocGiaController.layDanhSachDocGia(cheDo);
            doDanhSachLenBang(danhSach);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu độc giả: " + e.getMessage());
        }
    }

    private void timKiemDocGia() {
        String tuKhoa = txtTimKiem.getText().trim();

        if (tuKhoa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }

        try {
            List<DocGiaDTO> danhSach = quanLyDocGiaController.timKiemDocGia(cheDo, tuKhoa);
            doDanhSachLenBang(danhSach);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm độc giả: " + e.getMessage());
        }
    }

    private void doDanhSachLenBang(List<DocGiaDTO> danhSach) {
        DefaultTableModel model = (DefaultTableModel) tblDocGia.getModel();
        model.setRowCount(0);

        for (DocGiaDTO dg : danhSach) {
            model.addRow(new Object[]{
                dg.getTenDangNhap(),
                dg.getHoTen(),
                safe(dg.getCccd()),
                safe(dg.getSoDienThoai()),
                dg.getVaiTro(),
                dg.getTrangThaiTaiKhoan(),
                formatTien(dg.getTienCoc()),
                formatNgay(dg.getNgayDangKy()),
                formatNgay(dg.getNgayKichHoat()),
                safe(dg.getGhiChu())
            });
        }
    }

    // ============================================================
    // UC DUYỆT & THU CỌC
    // ============================================================

    private void duyetVaThuCoc() {
        int selectedRow = tblDocGia.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài khoản cần duyệt!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblDocGia.getModel();

        String tenDangNhap = String.valueOf(model.getValueAt(selectedRow, 0));
        String vaiTro = String.valueOf(model.getValueAt(selectedRow, 4));
        String trangThai = String.valueOf(model.getValueAt(selectedRow, 5));

        String input = JOptionPane.showInputDialog(
                this,
                "Nhập số tiền cọc đã thu từ độc giả ngoài:",
                "Duyệt tài khoản & thu cọc",
                JOptionPane.PLAIN_MESSAGE
        );

        if (input == null) {
            return;
        }

        input = input.trim();

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền cọc!");
            return;
        }

        BigDecimal tienCoc;

        try {
            tienCoc = new BigDecimal(input.replace(".", "").replace(",", ""));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Số tiền cọc không hợp lệ!");
            return;
        }

        KetQuaXuLy ketQua = quanLyDocGiaController.duyetVaThuCoc(
                tenDangNhap,
                vaiTro,
                trangThai,
                tienCoc
        );

        JOptionPane.showMessageDialog(this, ketQua.getThongBao());

        if (ketQua.isThanhCong()) {
            taiDuLieuDocGia();
        }
    }

    // ============================================================
    // UC10.1 - CẬP NHẬT HỒ SƠ ĐỘC GIẢ NGOÀI
    // ============================================================

    private void capNhatHoSoDocGiaNgoai() {
        int selectedRow = tblDocGia.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn độc giả ngoài cần cập nhật hồ sơ!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblDocGia.getModel();

        String tenDangNhap = String.valueOf(model.getValueAt(selectedRow, 0));

        DocGiaDTO docGia;

        try {
            docGia = quanLyDocGiaController.timDocGiaTheoTenDangNhap(tenDangNhap);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin độc giả: " + e.getMessage());
            return;
        }

        if (docGia == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy độc giả cần cập nhật!");
            return;
        }

        if (!"Độc giả ngoài".equalsIgnoreCase(docGia.getVaiTro())) {
            JOptionPane.showMessageDialog(this, "UC10.1 chỉ áp dụng cho độc giả ngoài!");
            return;
        }

        JTextField txtHoTen = new JTextField(safeForInput(docGia.getHoTen()));
        JTextField txtCccd = new JTextField(safeForInput(docGia.getCccd()));
        JTextField txtSoDienThoai = new JTextField(safeForInput(docGia.getSoDienThoai()));
        JTextField txtGhiChu = new JTextField(safeForInput(docGia.getGhiChu()));

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 6));
        form.add(new JLabel("Tên đăng nhập: " + docGia.getTenDangNhap()));
        form.add(new JLabel("Họ tên:"));
        form.add(txtHoTen);
        form.add(new JLabel("Số CCCD:"));
        form.add(txtCccd);
        form.add(new JLabel("Số điện thoại:"));
        form.add(txtSoDienThoai);
        form.add(new JLabel("Ghi chú:"));
        form.add(txtGhiChu);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                form,
                "UC10.1 - Cập nhật hồ sơ độc giả ngoài",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (confirm != JOptionPane.OK_OPTION) {
            return;
        }

        KetQuaXuLy ketQua = quanLyDocGiaController.capNhatHoSoDocGiaNgoai(
                docGia.getTenDangNhap(),
                txtHoTen.getText(),
                txtCccd.getText(),
                txtSoDienThoai.getText(),
                txtGhiChu.getText()
        );

        JOptionPane.showMessageDialog(this, ketQua.getThongBao());

        if (ketQua.isThanhCong()) {
            taiDuLieuDocGia();
        }
    }

    // ============================================================
    // UC10.2 - HOÀN TRẢ TIỀN CỌC & HỦY THẺ
    // ============================================================

    private void hoanTraCocVaHuyThe() {
        int selectedRow = tblDocGia.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn độc giả ngoài cần hoàn trả cọc!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblDocGia.getModel();

        String tenDangNhap = String.valueOf(model.getValueAt(selectedRow, 0));

        DocGiaDTO docGia;

        try {
            docGia = quanLyDocGiaController.timDocGiaTheoTenDangNhap(tenDangNhap);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin độc giả: " + e.getMessage());
            return;
        }

        if (docGia == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy độc giả cần xử lý!");
            return;
        }

        if (!"Độc giả ngoài".equalsIgnoreCase(docGia.getVaiTro())) {
            JOptionPane.showMessageDialog(this, "UC10.2 chỉ áp dụng cho độc giả ngoài!");
            return;
        }

        String message = "Bạn có chắc muốn hoàn trả tiền cọc và hủy thẻ của độc giả này không?\n\n"
                + "Tên đăng nhập: " + docGia.getTenDangNhap() + "\n"
                + "Họ tên: " + safe(docGia.getHoTen()) + "\n"
                + "Tiền cọc hiện tại: " + formatTien(docGia.getTienCoc()) + "\n\n"
                + "Sau khi xử lý:\n"
                + "- Tiền cọc sẽ được đưa về 0\n"
                + "- Thẻ độc giả ngoài được hủy\n"
                + "- Tài khoản sẽ chuyển sang trạng thái Bị khóa";

        int confirm = JOptionPane.showConfirmDialog(
                this,
                message,
                "UC10.2 - Hoàn trả tiền cọc & hủy thẻ",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        KetQuaXuLy ketQua = quanLyDocGiaController.hoanTraCocVaHuyThe(tenDangNhap);

        JOptionPane.showMessageDialog(this, ketQua.getThongBao());

        if (ketQua.isThanhCong()) {
            taiDuLieuDocGia();
        }
    }

    // ============================================================
    // KHÓA / MỞ KHÓA
    // ============================================================

    private void capNhatTrangThaiTaiKhoan(String trangThaiMoi) {
        int selectedRow = tblDocGia.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài khoản trong bảng!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblDocGia.getModel();

        String tenDangNhap = String.valueOf(model.getValueAt(selectedRow, 0));
        String trangThaiHienTai = String.valueOf(model.getValueAt(selectedRow, 5));

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn cập nhật tài khoản này thành: " + trangThaiMoi + "?",
                "Xác nhận cập nhật",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        KetQuaXuLy ketQua = quanLyDocGiaController.capNhatTrangThaiTaiKhoan(
                tenDangNhap,
                trangThaiHienTai,
                trangThaiMoi
        );

        JOptionPane.showMessageDialog(this, ketQua.getThongBao());

        if (ketQua.isThanhCong()) {
            taiDuLieuDocGia();
        }
    }

    // ============================================================
    // HELPER
    // ============================================================

    private String formatNgay(java.sql.Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }

        return dateFormat.format(timestamp);
    }

    private String formatTien(BigDecimal value) {
        if (value == null) {
            return "0 VNĐ";
        }

        return numberFormat.format(value) + " VNĐ";
    }

    private String safe(String value) {
        return value == null || value.trim().isEmpty() ? "--" : value;
    }

    private String safeForInput(String value) {
        return value == null ? "" : value;
    }
}