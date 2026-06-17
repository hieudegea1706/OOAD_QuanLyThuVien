package views;

import components.ModernButton;
import components.ModernTextField;
import components.RoundedPanel;
import controllers.QuanLyDocGiaController;
import dto.DocGiaDTO;
import dto.KetQuaXuLy;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.math.BigDecimal;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import utils.UITheme;

public class QuanLyDocGiaPnl extends JPanel {
    private JTable tblDocGia;
    private ModernTextField txtTimKiem;

    private JLabel lblTitle;
    private JLabel lblSubtitle;

    private ModernButton btnDuyet;
    private ModernButton btnKhoa;
    private ModernButton btnMoKhoa;

    private String cheDo = "ALL";
    
    private final QuanLyDocGiaController quanLyDocGiaController = new QuanLyDocGiaController();

    public QuanLyDocGiaPnl() {
        buildUI();
        setCheDo("ALL");
    }

    public void setCheDo(String cheDoMoi) {
        this.cheDo = cheDoMoi;

        if (lblTitle != null && lblSubtitle != null) {
            if ("ALL".equals(cheDo)) {
                lblTitle.setText("Tất cả tài khoản độc giả");
                lblSubtitle.setText("Tìm kiếm, duyệt, khóa hoặc mở khóa tài khoản độc giả");
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
        if (btnDuyet == null || btnKhoa == null || btnMoKhoa == null) {
            return;
        }

        if ("ALL".equals(cheDo)) {
            btnDuyet.setVisible(true);
            btnKhoa.setVisible(true);
            btnMoKhoa.setVisible(true);
        } else if ("DANG_HOAT_DONG".equals(cheDo)) {
            btnDuyet.setVisible(false);
            btnKhoa.setVisible(true);
            btnMoKhoa.setVisible(false);
        } else if ("CHO_DUYET".equals(cheDo)) {
            btnDuyet.setVisible(true);
            btnKhoa.setVisible(false);
            btnMoKhoa.setVisible(false);
        } else if ("BI_KHOA".equals(cheDo)) {
            btnDuyet.setVisible(false);
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

        lblSubtitle = new JLabel("Tìm kiếm, duyệt, khóa hoặc mở khóa tài khoản độc giả");
        lblSubtitle.setFont(UITheme.FONT_SUBTITLE);
        lblSubtitle.setForeground(UITheme.TEXT_MUTED);
        lblSubtitle.setAlignmentX(LEFT_ALIGNMENT);

        titleBox.add(lblTitle);
        titleBox.add(Box.createVerticalStrut(6));
        titleBox.add(lblSubtitle);

        JPanel actionPanel = new JPanel(new BorderLayout(12, 0));
        actionPanel.setOpaque(false);

        txtTimKiem = new ModernTextField();
        txtTimKiem.setPreferredSize(new Dimension(240, 42));

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

        btnKhoa = new ModernButton("Khóa", UITheme.RED, UITheme.RED_DARK);
        btnKhoa.setPreferredSize(new Dimension(90, 42));
        btnKhoa.addActionListener(e -> capNhatTrangThaiTaiKhoan("Bị khóa"));

        btnMoKhoa = new ModernButton("Mở khóa", UITheme.ORANGE, UITheme.ORANGE_DARK);
        btnMoKhoa.setPreferredSize(new Dimension(110, 42));
        btnMoKhoa.addActionListener(e -> capNhatTrangThaiTaiKhoan("Đang hoạt động"));

        JPanel buttonBox = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        buttonBox.setOpaque(false);
        buttonBox.add(btnTimKiem);
        buttonBox.add(btnLamMoi);
        buttonBox.add(btnDuyet);
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
                    "Số điện thoại",
                    "Vai trò",
                    "Trạng thái",
                    "Tiền cọc",
                    "Ngày đăng ký",
                    "Ngày kích hoạt"
                }
        ));

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
        tblDocGia.getColumnModel().getColumn(4).setPreferredWidth(140);
        tblDocGia.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblDocGia.getColumnModel().getColumn(6).setPreferredWidth(160);
        tblDocGia.getColumnModel().getColumn(7).setPreferredWidth(160);
    }


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

    private void duyetVaThuCoc() {
    int selectedRow = tblDocGia.getSelectedRow();

    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài khoản cần duyệt!");
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblDocGia.getModel();

    String tenDangNhap = String.valueOf(model.getValueAt(selectedRow, 0));
    String vaiTro = String.valueOf(model.getValueAt(selectedRow, 3));
    String trangThai = String.valueOf(model.getValueAt(selectedRow, 4));

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
        tienCoc = new BigDecimal(input.replace(",", ""));
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

    private void capNhatTrangThaiTaiKhoan(String trangThaiMoi) {
    int selectedRow = tblDocGia.getSelectedRow();

    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài khoản trong bảng!");
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblDocGia.getModel();

    String tenDangNhap = String.valueOf(model.getValueAt(selectedRow, 0));
    String trangThaiHienTai = String.valueOf(model.getValueAt(selectedRow, 4));

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
    private void doDanhSachLenBang(List<DocGiaDTO> danhSach) {
    DefaultTableModel model = (DefaultTableModel) tblDocGia.getModel();
    model.setRowCount(0);

    for (DocGiaDTO dg : danhSach) {
        model.addRow(new Object[]{
            dg.getTenDangNhap(),
            dg.getHoTen(),
            dg.getSoDienThoai(),
            dg.getVaiTro(),
            dg.getTrangThaiTaiKhoan(),
            dg.getTienCoc(),
            dg.getNgayDangKy(),
            dg.getNgayKichHoat()
        });
    }
}
}