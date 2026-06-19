package views;

import components.ModernButton;
import components.ModernTextField;
import components.RoundedPanel;
import controllers.MuonTraController;
import dto.DocGiaMuonDTO;
import dto.KetQuaXuLy;
import dto.SachMuonDTO;
import dto.ThongTinTraSachDTO;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import utils.DatabaseHelper;
import utils.UITheme;
import javax.swing.JComboBox;

public class MuonTraSachPnl extends JPanel {
    
    private final MuonTraController muonTraController = new MuonTraController();

    private ModernTextField txtTenDangNhap;
    private ModernTextField txtMaCaBiet;
    private ModernTextField txtSoNgayMuon;

    private JLabel lblThongTinDocGia;
    private JLabel lblThongTinSach;

    private JTable tblSachMuonTam;
    private JTable tblPhieuMuon;
    private JTable tblChiTietPhieuMuon;
    
    private ModernTextField txtMaCaBietTra;
    private ModernTextField txtGhiChuTra;
    private JComboBox<String> cboTinhTrangTra;
    private JTable tblThongTinTra;

    private Integer idPhieuMuonDangTra = null;
    private String maCaBietDangTra = null;
    private String tenDangNhapDangTra = null;
    private int soNgayQuaHanDangTra = 0;

    private String docGiaDangChon = null;
    private boolean docGiaHopLe = false;

    public MuonTraSachPnl() {
        buildUI();
        taiDanhSachPhieuMuon();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_MAIN);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabs.addTab("  Lập phiếu mượn  ", buildLapPhieuMuonTab());
        tabs.addTab("  Trả sách  ", buildTraSachTab());
        tabs.addTab("  Danh sách phiếu mượn  ", buildDanhSachPhieuMuonTab());

        add(tabs, BorderLayout.CENTER);
    }

    // =====================================================
    // TAB 1: LẬP PHIẾU MƯỢN
    // =====================================================

    private JPanel buildLapPhieuMuonTab() {
        JPanel root = new JPanel(new BorderLayout(16, 0));
        root.setBackground(UITheme.BG_MAIN);
        root.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        root.add(buildLeftFormPanel(), BorderLayout.WEST);
        root.add(buildSachMuonTamPanel(), BorderLayout.CENTER);

        return root;
    }

    private JPanel buildLeftFormPanel() {
        RoundedPanel card = new RoundedPanel(24, UITheme.BG_CARD);
        card.setLayout(new BorderLayout());
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));
        card.setPreferredSize(new Dimension(370, 0));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Lập phiếu mượn");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UITheme.TEXT_DARK);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("<html>Kiểm tra độc giả, thêm sách và lập phiếu mượn.</html>");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        txtTenDangNhap = new ModernTextField();
        txtMaCaBiet = new ModernTextField();
        txtSoNgayMuon = new ModernTextField();
        txtSoNgayMuon.setText("14");

        setFieldSize(txtTenDangNhap);
        setFieldSize(txtMaCaBiet);
        setFieldSize(txtSoNgayMuon);

        ModernButton btnKiemTraDocGia = new ModernButton("Kiểm tra độc giả", UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        ModernButton btnThemSach = new ModernButton("Thêm sách vào phiếu", UITheme.GREEN, UITheme.GREEN_DARK);
        ModernButton btnXoaSachTam = new ModernButton("Xóa sách đã chọn", UITheme.ORANGE, UITheme.ORANGE_DARK);
        ModernButton btnLapPhieu = new ModernButton("Lập phiếu mượn", UITheme.PURPLE, UITheme.PURPLE_DARK);
        ModernButton btnLamMoi = new ModernButton("Làm mới", UITheme.CYAN, UITheme.CYAN_DARK);

        setButtonSize(btnKiemTraDocGia);
        setButtonSize(btnThemSach);
        setButtonSize(btnXoaSachTam);
        setButtonSize(btnLapPhieu);
        setButtonSize(btnLamMoi);

        btnKiemTraDocGia.addActionListener(e -> kiemTraDocGia());
        btnThemSach.addActionListener(e -> themSachVaoPhieuTam());
        btnXoaSachTam.addActionListener(e -> xoaSachTamDaChon());
        btnLapPhieu.addActionListener(e -> lapPhieuMuon());
        btnLamMoi.addActionListener(e -> lamMoiLapPhieu());

        lblThongTinDocGia = new JLabel("Chưa kiểm tra độc giả");
        lblThongTinDocGia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblThongTinDocGia.setForeground(UITheme.TEXT_MUTED);
        lblThongTinDocGia.setAlignmentX(LEFT_ALIGNMENT);

        lblThongTinSach = new JLabel("Chưa kiểm tra sách");
        lblThongTinSach.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblThongTinSach.setForeground(UITheme.TEXT_MUTED);
        lblThongTinSach.setAlignmentX(LEFT_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(6));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(18));

        addField(content, "Tên đăng nhập độc giả / MSSV / CCCD", txtTenDangNhap);
        content.add(btnKiemTraDocGia);
        content.add(Box.createVerticalStrut(8));
        content.add(lblThongTinDocGia);

        content.add(Box.createVerticalStrut(20));

        addField(content, "Mã cá biệt cuốn sách", txtMaCaBiet);
        content.add(btnThemSach);
        content.add(Box.createVerticalStrut(8));
        content.add(lblThongTinSach);

        content.add(Box.createVerticalStrut(20));

        addField(content, "Số ngày mượn", txtSoNgayMuon);

        content.add(Box.createVerticalStrut(12));
        content.add(btnXoaSachTam);
        content.add(Box.createVerticalStrut(8));
        content.add(btnLapPhieu);
        content.add(Box.createVerticalStrut(8));
        content.add(btnLamMoi);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    private JPanel buildSachMuonTamPanel() {
        RoundedPanel card = new RoundedPanel(24, UITheme.BG_CARD);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Danh sách sách trong phiếu mượn");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel subtitle = new JLabel("Các cuốn sách sẽ được chuyển sang trạng thái Đang cho mượn sau khi lập phiếu.");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        tblSachMuonTam = new JTable();
        tblSachMuonTam.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Mã cá biệt",
                    "Mã đầu sách",
                    "Tên sách",
                    "Trạng thái",
                    "Tình trạng",
                    "Vị trí kệ"
                }
        ));

        styleTable(tblSachMuonTam);

        JScrollPane scrollPane = new JScrollPane(tblSachMuonTam);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        card.add(titleBox, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    // =====================================================
    // TAB 2: DANH SÁCH PHIẾU MƯỢN
    // =====================================================

    private JPanel buildDanhSachPhieuMuonTab() {
        
    JPanel root = new JPanel(new BorderLayout(0, 14));
    root.setBackground(UITheme.BG_MAIN);
    root.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

    RoundedPanel topCard = new RoundedPanel(24, UITheme.BG_CARD);
    topCard.setLayout(new BorderLayout());
    topCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));

    JPanel titleBox = new JPanel();
    titleBox.setOpaque(false);
    titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Danh sách phiếu mượn");
    title.setFont(new Font("Segoe UI", Font.BOLD, 28));
    title.setForeground(UITheme.TEXT_DARK);

    JLabel subtitle = new JLabel("Chọn một phiếu mượn để xem chi tiết các cuốn sách trong phiếu.");
    subtitle.setFont(UITheme.FONT_SUBTITLE);
    subtitle.setForeground(UITheme.TEXT_MUTED);

    titleBox.add(title);
    titleBox.add(Box.createVerticalStrut(5));
    titleBox.add(subtitle);

    ModernButton btnLamMoi = new ModernButton("Làm mới", UITheme.CYAN, UITheme.CYAN_DARK);
    btnLamMoi.setPreferredSize(new Dimension(120, 42));
    btnLamMoi.addActionListener(e -> {
        taiDanhSachPhieuMuon();
        lamMoiChiTietPhieuMuon();
    });

    topCard.add(titleBox, BorderLayout.CENTER);
    topCard.add(btnLamMoi, BorderLayout.EAST);

    // Vùng giữa gồm: bảng phiếu mượn ở trên + bảng chi tiết ở dưới
    JPanel centerBox = new JPanel(new BorderLayout(0, 14));
    centerBox.setOpaque(false);

    RoundedPanel tableCard = new RoundedPanel(24, UITheme.BG_CARD);
    tableCard.setLayout(new BorderLayout());
    tableCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));

    tblPhieuMuon = new JTable();
    tblPhieuMuon.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Mã phiếu",
                "Tên đăng nhập",
                "Họ tên",
                "Ngày mượn",
                "Hạn trả",
                "Trạng thái",
                "Số sách"
            }
    ));

    styleTable(tblPhieuMuon);

    tblPhieuMuon.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            taiChiTietPhieuMuonDaChon();
        }
    });

    JScrollPane scrollPane = new JScrollPane(tblPhieuMuon);
    scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

    tableCard.add(scrollPane, BorderLayout.CENTER);

    centerBox.add(tableCard, BorderLayout.CENTER);
    centerBox.add(buildChiTietPhieuMuonPanel(), BorderLayout.SOUTH);

    root.add(topCard, BorderLayout.NORTH);
    root.add(centerBox, BorderLayout.CENTER);

    return root;

    }

private JPanel buildChiTietPhieuMuonPanel() {
    RoundedPanel detailCard = new RoundedPanel(24, UITheme.BG_CARD);
    detailCard.setLayout(new BorderLayout(0, 10));
    detailCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 16, 14, 16));
    detailCard.setPreferredSize(new Dimension(0, 235));

    JPanel titleBox = new JPanel();
    titleBox.setOpaque(false);
    titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Chi tiết phiếu mượn");
    title.setFont(new Font("Segoe UI", Font.BOLD, 21));
    title.setForeground(UITheme.TEXT_DARK);

    JLabel subtitle = new JLabel("Hiển thị từng cuốn sách trong phiếu, trạng thái trả và thông tin phạt nếu có.");
    subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    subtitle.setForeground(UITheme.TEXT_MUTED);

    titleBox.add(title);
    titleBox.add(Box.createVerticalStrut(4));
    titleBox.add(subtitle);

    tblChiTietPhieuMuon = new JTable();
    tblChiTietPhieuMuon.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Mã cá biệt",
                "Tên sách",
                "Ngày trả thực tế",
                "Trạng thái chi tiết",
                "Ghi chú mượn/trả",
                "Loại vi phạm",
                "Tiền phạt",
                "Thanh toán"
            }
    ));

    styleTable(tblChiTietPhieuMuon);
    tblChiTietPhieuMuon.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    tblChiTietPhieuMuon.getColumnModel().getColumn(0).setPreferredWidth(100);
    tblChiTietPhieuMuon.getColumnModel().getColumn(1).setPreferredWidth(220);
    tblChiTietPhieuMuon.getColumnModel().getColumn(2).setPreferredWidth(160);
    tblChiTietPhieuMuon.getColumnModel().getColumn(3).setPreferredWidth(140);
    tblChiTietPhieuMuon.getColumnModel().getColumn(4).setPreferredWidth(220);
    tblChiTietPhieuMuon.getColumnModel().getColumn(5).setPreferredWidth(150);
    tblChiTietPhieuMuon.getColumnModel().getColumn(6).setPreferredWidth(110);
    tblChiTietPhieuMuon.getColumnModel().getColumn(7).setPreferredWidth(140);

    JScrollPane scrollPane = new JScrollPane(tblChiTietPhieuMuon);
    scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

    detailCard.add(titleBox, BorderLayout.NORTH);
    detailCard.add(scrollPane, BorderLayout.CENTER);

    return detailCard;
}

    // =====================================================
// TAB 3: TRẢ SÁCH
// =====================================================

private JPanel buildTraSachTab() {
    JPanel root = new JPanel(new BorderLayout(16, 0));
    root.setBackground(UITheme.BG_MAIN);
    root.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

    root.add(buildTraSachLeftPanel(), BorderLayout.WEST);
    root.add(buildTraSachInfoPanel(), BorderLayout.CENTER);

    return root;
}

private JPanel buildTraSachLeftPanel() {
    RoundedPanel card = new RoundedPanel(24, UITheme.BG_CARD);
    card.setLayout(new BorderLayout());
    card.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
    card.setPreferredSize(new Dimension(320, 0));
    card.setBackground(UITheme.BG_CARD);

    JPanel content = new JPanel();
    content.setOpaque(true);
    content.setBackground(UITheme.BG_CARD);
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Xử lý trả sách");
    title.setFont(new Font("Segoe UI", Font.BOLD, 24));
    title.setForeground(UITheme.TEXT_DARK);
    title.setAlignmentX(LEFT_ALIGNMENT);

    JLabel subtitle = new JLabel(
            "<html><div style='width:300px;'>"
            + "Nhập mã cá biệt, kiểm tra tình trạng<br>"
            + "sách và xử lý trả."
            + "</div></html>"
    );
    subtitle.setFont(UITheme.FONT_SUBTITLE);
    subtitle.setForeground(UITheme.TEXT_MUTED);
    subtitle.setAlignmentX(LEFT_ALIGNMENT);

    txtMaCaBietTra = new ModernTextField();
    txtGhiChuTra = new ModernTextField();

    setReturnFieldSize(txtMaCaBietTra);
    setReturnFieldSize(txtGhiChuTra);

    cboTinhTrangTra = new JComboBox<>(new String[]{
        "Tốt",
        "Rách nhẹ",
        "Hư hỏng",
        "Mất"
    });
    cboTinhTrangTra.setFont(UITheme.FONT_NORMAL);
    cboTinhTrangTra.setMaximumSize(new Dimension(300, 40));
    cboTinhTrangTra.setPreferredSize(new Dimension(300, 40));
    cboTinhTrangTra.setMinimumSize(new Dimension(300, 40));
    cboTinhTrangTra.setAlignmentX(LEFT_ALIGNMENT);

    ModernButton btnTimSachTra = new ModernButton("Tìm sách đang mượn", UITheme.PRIMARY, UITheme.PRIMARY_DARK);
    ModernButton btnTraSach = new ModernButton("Xử lý trả sách", UITheme.GREEN, UITheme.GREEN_DARK);
    ModernButton btnLamMoi = new ModernButton("Làm mới", UITheme.CYAN, UITheme.CYAN_DARK);

    setReturnButtonSize(btnTimSachTra);
    setReturnButtonSize(btnTraSach);
    setReturnButtonSize(btnLamMoi);

    btnTimSachTra.addActionListener(e -> timSachDangMuonDeTra());
    btnTraSach.addActionListener(e -> xuLyTraSach());
    btnLamMoi.addActionListener(e -> lamMoiTraSach());

    content.add(title);
    content.add(Box.createVerticalStrut(6));
    content.add(subtitle);
    content.add(Box.createVerticalStrut(18));

    addField(content, "Mã cá biệt cuốn sách trả", txtMaCaBietTra);
    content.add(btnTimSachTra);

    content.add(Box.createVerticalStrut(18));

    JLabel lblTinhTrang = new JLabel("Tình trạng sách khi trả");
    lblTinhTrang.setFont(new Font("Segoe UI", Font.BOLD, 13));
    lblTinhTrang.setForeground(UITheme.TEXT_NORMAL);
    lblTinhTrang.setAlignmentX(LEFT_ALIGNMENT);

    content.add(lblTinhTrang);
    content.add(Box.createVerticalStrut(5));
    content.add(cboTinhTrangTra);
    content.add(Box.createVerticalStrut(10));

    addField(content, "Ghi chú kiểm tra", txtGhiChuTra);

    content.add(Box.createVerticalStrut(10));
    content.add(btnTraSach);
    content.add(Box.createVerticalStrut(8));
    content.add(btnLamMoi);

    content.add(Box.createVerticalStrut(16));

    JLabel note = new JLabel(
            "<html><div style='width:300px; color:#64748b;'>"
            + "Nếu sách quá hạn, rách, hư hỏng hoặc mất,<br>"
            + "hệ thống sẽ tự lập phiếu phạt."
            + "</div></html>"
    );
    note.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    note.setAlignmentX(LEFT_ALIGNMENT);
    content.add(note);

    JScrollPane scroll = new JScrollPane(content);
    scroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.getVerticalScrollBar().setUnitIncrement(16);
    scroll.setOpaque(false);
    scroll.getViewport().setOpaque(true);
    scroll.getViewport().setBackground(UITheme.BG_CARD);

    card.add(scroll, BorderLayout.CENTER);

    return card;
}

private void timSachDangMuonDeTra() {
    String maCaBiet = txtMaCaBietTra.getText().trim();

    DefaultTableModel model = (DefaultTableModel) tblThongTinTra.getModel();
    model.setRowCount(0);

    idPhieuMuonDangTra = null;
    maCaBietDangTra = null;
    tenDangNhapDangTra = null;
    soNgayQuaHanDangTra = 0;

    ThongTinTraSachDTO ketQua = muonTraController.timSachDangMuonDeTra(maCaBiet);

    if (!ketQua.isTimThay()) {
        JOptionPane.showMessageDialog(this, ketQua.getThongBao());
        return;
    }

    idPhieuMuonDangTra = ketQua.getIdPhieuMuon();
    maCaBietDangTra = ketQua.getIdCaBiet();
    tenDangNhapDangTra = ketQua.getTenDangNhap();
    soNgayQuaHanDangTra = ketQua.getSoNgayQuaHan();

    model.addRow(new Object[]{
        ketQua.getIdPhieuMuon(),
        ketQua.getTenDangNhap(),
        ketQua.getHoTen(),
        ketQua.getIdCaBiet(),
        ketQua.getTenSach(),
        ketQua.getNgayMuon(),
        ketQua.getHanTra(),
        ketQua.getSoNgayQuaHan(),
        ketQua.getTrangThaiChiTiet()
    });

    if (ketQua.getSoNgayQuaHan() > 0) {
        JOptionPane.showMessageDialog(this, ketQua.getThongBao());
    }
}

private void xuLyTraSach() {
    if (idPhieuMuonDangTra == null || maCaBietDangTra == null || tenDangNhapDangTra == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng tìm sách đang mượn trước khi xử lý trả!");
        return;
    }

    String tinhTrangTra = String.valueOf(cboTinhTrangTra.getSelectedItem());
    String ghiChuTra = txtGhiChuTra.getText().trim();

    String thongBaoXacNhan = muonTraController.taoThongBaoXacNhanTra(
            idPhieuMuonDangTra,
            maCaBietDangTra,
            soNgayQuaHanDangTra,
            tinhTrangTra
    );

    int confirm = JOptionPane.showConfirmDialog(
            this,
            thongBaoXacNhan,
            "Xác nhận xử lý trả sách",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    KetQuaXuLy ketQua = muonTraController.xuLyTraSach(
            idPhieuMuonDangTra,
            maCaBietDangTra,
            tenDangNhapDangTra,
            soNgayQuaHanDangTra,
            tinhTrangTra,
            ghiChuTra
    );

    JOptionPane.showMessageDialog(this, ketQua.getThongBao());

    if (ketQua.isThanhCong()) {
        lamMoiTraSach();
        taiDanhSachPhieuMuon();
    }
}

private void lamMoiTraSach() {
    txtMaCaBietTra.setText("");
    txtGhiChuTra.setText("");
    cboTinhTrangTra.setSelectedItem("Tốt");

    idPhieuMuonDangTra = null;
    maCaBietDangTra = null;
    tenDangNhapDangTra = null;
    soNgayQuaHanDangTra = 0;

    DefaultTableModel model = (DefaultTableModel) tblThongTinTra.getModel();
    model.setRowCount(0);
}

private JPanel buildTraSachInfoPanel() {
    RoundedPanel card = new RoundedPanel(24, UITheme.BG_CARD);
    card.setLayout(new BorderLayout(0, 12));
    card.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));

    JPanel titleBox = new JPanel();
    titleBox.setOpaque(false);
    titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Thông tin phiếu mượn cần trả");
    title.setFont(new Font("Segoe UI", Font.BOLD, 24));
    title.setForeground(UITheme.TEXT_DARK);

    JLabel subtitle = new JLabel("Hệ thống hiển thị số ngày quá hạn trước khi xác nhận trả sách.");
    subtitle.setFont(UITheme.FONT_SUBTITLE);
    subtitle.setForeground(UITheme.TEXT_MUTED);

    titleBox.add(title);
    titleBox.add(Box.createVerticalStrut(5));
    titleBox.add(subtitle);

    tblThongTinTra = new JTable();
    tblThongTinTra.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Mã phiếu",
                "Tên đăng nhập",
                "Họ tên",
                "Mã cá biệt",
                "Tên sách",
                "Ngày mượn",
                "Hạn trả",
                "Quá hạn",
                "Trạng thái"
            }
    ));

    styleTable(tblThongTinTra);
    tblThongTinTra.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    tblThongTinTra.getColumnModel().getColumn(0).setPreferredWidth(90);
    tblThongTinTra.getColumnModel().getColumn(1).setPreferredWidth(130);
    tblThongTinTra.getColumnModel().getColumn(2).setPreferredWidth(170);
    tblThongTinTra.getColumnModel().getColumn(3).setPreferredWidth(100);
    tblThongTinTra.getColumnModel().getColumn(4).setPreferredWidth(220);
    tblThongTinTra.getColumnModel().getColumn(5).setPreferredWidth(160);
    tblThongTinTra.getColumnModel().getColumn(6).setPreferredWidth(160);
    tblThongTinTra.getColumnModel().getColumn(7).setPreferredWidth(90);
    tblThongTinTra.getColumnModel().getColumn(8).setPreferredWidth(120);

    JScrollPane scrollPane = new JScrollPane(tblThongTinTra);
    scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

    card.add(titleBox, BorderLayout.NORTH);
    card.add(scrollPane, BorderLayout.CENTER);

    return card;
}
    
    // =====================================================
    // LOGIC
    // =====================================================

    private void kiemTraDocGia() {
    String tenDangNhap = txtTenDangNhap.getText().trim();

    DocGiaMuonDTO ketQua = muonTraController.kiemTraDocGia(tenDangNhap);

    if (ketQua.isHopLe()) {
        docGiaHopLe = true;
        docGiaDangChon = ketQua.getTenDangNhap();

        lblThongTinDocGia.setText(
                "<html><span style='color:green;'>"
                + ketQua.getThongBao()
                + "</span></html>"
        );
    } else {
        docGiaHopLe = false;
        docGiaDangChon = null;

        lblThongTinDocGia.setText(
                "<html><span style='color:red;'>"
                + ketQua.getThongBao()
                + "</span></html>"
        );
    }
}

    private void themSachVaoPhieuTam() {
    String maCaBiet = txtMaCaBiet.getText().trim();

    DefaultTableModel model = (DefaultTableModel) tblSachMuonTam.getModel();

    for (int i = 0; i < model.getRowCount(); i++) {
        String maTrongBang = String.valueOf(model.getValueAt(i, 0));

        if (maTrongBang.equalsIgnoreCase(maCaBiet)) {
            JOptionPane.showMessageDialog(this, "Cuốn sách này đã có trong phiếu mượn tạm!");
            return;
        }
    }

    SachMuonDTO sach = muonTraController.kiemTraSachMuon(maCaBiet);

    if (!sach.isCoTheMuon()) {
        lblThongTinSach.setText(
                "<html><span style='color:red;'>"
                + sach.getThongBao()
                + "</span></html>"
        );
        return;
    }

    model.addRow(new Object[]{
        sach.getIdCaBiet(),
        sach.getIdDauSach(),
        sach.getTenSach(),
        sach.getTrangThaiLuuThong(),
        sach.getTinhTrangVatLy(),
        sach.getViTriKe()
    });

    lblThongTinSach.setText(
            "<html><span style='color:green;'>"
            + sach.getThongBao()
            + "</span></html>"
    );

    txtMaCaBiet.setText("");
}

    private void xoaSachTamDaChon() {
        int row = tblSachMuonTam.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sách cần xóa khỏi phiếu tạm!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblSachMuonTam.getModel();
        model.removeRow(row);
    }

    private void lapPhieuMuon() {
    if (!docGiaHopLe || docGiaDangChon == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng kiểm tra độc giả hợp lệ trước khi lập phiếu!");
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblSachMuonTam.getModel();

    if (model.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Vui lòng thêm ít nhất một cuốn sách vào phiếu!");
        return;
    }

    int soNgayMuon;

    try {
        soNgayMuon = Integer.parseInt(txtSoNgayMuon.getText().trim());
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Số ngày mượn phải là số nguyên!");
        return;
    }

    List<String> danhSachIdCaBiet = new ArrayList<>();

    for (int i = 0; i < model.getRowCount(); i++) {
        danhSachIdCaBiet.add(String.valueOf(model.getValueAt(i, 0)));
    }

    KetQuaXuLy ketQua = muonTraController.lapPhieuMuon(
            docGiaDangChon,
            soNgayMuon,
            danhSachIdCaBiet
    );

    JOptionPane.showMessageDialog(this, ketQua.getThongBao());

    if (ketQua.isThanhCong()) {
        lamMoiLapPhieu();
        taiDanhSachPhieuMuon();
    }
}

    private String formatTien(BigDecimal soTien) {
        if (soTien == null) {
            return "0";
        }

        return String.format("%,.0f", soTien.doubleValue());
    }

private void taiChiTietPhieuMuonDaChon() {
    int row = tblPhieuMuon.getSelectedRow();

    if (row < 0) {
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblPhieuMuon.getModel();
    int idPhieuMuon = Integer.parseInt(String.valueOf(model.getValueAt(row, 0)));

    taiChiTietPhieuMuon(idPhieuMuon);
}

private void taiChiTietPhieuMuon(int idPhieuMuon) {
    if (tblChiTietPhieuMuon == null) {
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblChiTietPhieuMuon.getModel();
    model.setRowCount(0);

    try {
        Connection con = DatabaseHelper.getConnection();

        String sql = "SELECT "
                + "ct.id_ca_biet, "
                + "ds.ten_sach, "
                + "ct.ngay_tra_thuc_te, "
                + "ct.trang_thai_chi_tiet, "
                + "ct.ghi_chu AS ghi_chu_chi_tiet, "
                + "pp.loai_vi_pham, "
                + "pp.so_tien_phat, "
                + "pp.trang_thai_thanh_toan "
                + "FROM ChiTietPhieuMuon ct "
                + "JOIN CuonSach cs ON ct.id_ca_biet = cs.id_ca_biet "
                + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                + "LEFT JOIN PhieuPhat pp "
                + "ON pp.id_phieu_muon = ct.id_phieu_muon "
                + "AND pp.id_ca_biet = ct.id_ca_biet "
                + "WHERE ct.id_phieu_muon = ? "
                + "ORDER BY ct.id_ca_biet";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, idPhieuMuon);

        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            BigDecimal tienPhat = rs.getBigDecimal("so_tien_phat");

            model.addRow(new Object[]{
                rs.getString("id_ca_biet"),
                rs.getString("ten_sach"),
                rs.getTimestamp("ngay_tra_thuc_te"),
                rs.getString("trang_thai_chi_tiet"),
                rs.getString("ghi_chu_chi_tiet"),
                rs.getString("loai_vi_pham") == null ? "-" : rs.getString("loai_vi_pham"),
                tienPhat == null ? "-" : formatTien(tienPhat),
                rs.getString("trang_thai_thanh_toan") == null ? "-" : rs.getString("trang_thai_thanh_toan")
            });
        }

        rs.close();
        pstmt.close();
        con.close();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết phiếu mượn: " + e.getMessage());
    }
}

private void lamMoiChiTietPhieuMuon() {
    if (tblChiTietPhieuMuon == null) {
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblChiTietPhieuMuon.getModel();
    model.setRowCount(0);
}

    private void taiDanhSachPhieuMuon() {
        if (tblPhieuMuon == null) {
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblPhieuMuon.getModel();
        model.setRowCount(0);
        lamMoiChiTietPhieuMuon();

        try {
            Connection con = DatabaseHelper.getConnection();

            String sql = "SELECT "
                    + "pm.id_phieu_muon, "
                    + "pm.ten_dang_nhap, "
                    + "tk.ho_ten, "
                    + "pm.ngay_muon, "
                    + "pm.han_tra, "
                    + "pm.trang_thai_phieu, "
                    + "COUNT(ct.id_chi_tiet) AS so_sach "
                    + "FROM PhieuMuon pm "
                    + "JOIN TaiKhoan tk ON pm.ten_dang_nhap = tk.ten_dang_nhap "
                    + "LEFT JOIN ChiTietPhieuMuon ct ON pm.id_phieu_muon = ct.id_phieu_muon "
                    + "GROUP BY pm.id_phieu_muon, pm.ten_dang_nhap, tk.ho_ten, "
                    + "pm.ngay_muon, pm.han_tra, pm.trang_thai_phieu "
                    + "ORDER BY pm.id_phieu_muon DESC";

            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_phieu_muon"),
                    rs.getString("ten_dang_nhap"),
                    rs.getString("ho_ten"),
                    rs.getTimestamp("ngay_muon"),
                    rs.getTimestamp("han_tra"),
                    rs.getString("trang_thai_phieu"),
                    rs.getInt("so_sach")
                });
            }

            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách phiếu mượn: " + e.getMessage());
        }
    }

    private void lamMoiLapPhieu() {
        txtTenDangNhap.setText("");
        txtMaCaBiet.setText("");
        txtSoNgayMuon.setText("14");

        lblThongTinDocGia.setText("Chưa kiểm tra độc giả");
        lblThongTinSach.setText("Chưa kiểm tra sách");

        docGiaDangChon = null;
        docGiaHopLe = false;

        DefaultTableModel model = (DefaultTableModel) tblSachMuonTam.getModel();
        model.setRowCount(0);
    }

    // =====================================================
    // STYLE HELPERS
    // =====================================================

    private void styleTable(JTable table) {
        table.setFont(UITheme.FONT_NORMAL);
        table.setRowHeight(36);
        table.setGridColor(new java.awt.Color(226, 232, 240));
        table.setSelectionBackground(UITheme.PRIMARY_LIGHT);
        table.setSelectionForeground(UITheme.TEXT_DARK);
        table.setShowVerticalLines(false);

        table.getTableHeader().setFont(UITheme.FONT_BOLD);
        table.getTableHeader().setBackground(new java.awt.Color(226, 232, 240));
        table.getTableHeader().setForeground(UITheme.TEXT_DARK);
        table.getTableHeader().setReorderingAllowed(false);
    }

    private void setFieldSize(javax.swing.JComponent field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(300, 40));
        field.setMinimumSize(new Dimension(120, 40));
        field.setAlignmentX(LEFT_ALIGNMENT);
    }

    private void setButtonSize(javax.swing.JComponent btn) {
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setPreferredSize(new Dimension(300, 42));
        btn.setMinimumSize(new Dimension(120, 42));
        btn.setAlignmentX(LEFT_ALIGNMENT);
    }

    private void addField(JPanel content, String labelText, javax.swing.JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(UITheme.TEXT_NORMAL);
        label.setAlignmentX(LEFT_ALIGNMENT);

        content.add(label);
        content.add(Box.createVerticalStrut(5));
        content.add(field);
        content.add(Box.createVerticalStrut(10));
    }
    private void setReturnFieldSize(javax.swing.JComponent field) {
    field.setMaximumSize(new Dimension(295, 40));
    field.setPreferredSize(new Dimension(295, 40));
    field.setMinimumSize(new Dimension(295, 40));
    field.setAlignmentX(LEFT_ALIGNMENT);
}

    private void setReturnButtonSize(javax.swing.JComponent btn) {
        btn.setMaximumSize(new Dimension(295, 40));
        btn.setPreferredSize(new Dimension(295, 40));
        btn.setMinimumSize(new Dimension(295, 40));
        btn.setAlignmentX(LEFT_ALIGNMENT);
    }
}