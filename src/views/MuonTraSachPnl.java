package views;

import components.ModernButton;
import components.ModernTextField;
import components.RoundedPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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

    if (maCaBiet.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập mã cá biệt cuốn sách cần trả!");
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblThongTinTra.getModel();
    model.setRowCount(0);

    idPhieuMuonDangTra = null;
    maCaBietDangTra = null;
    tenDangNhapDangTra = null;
    soNgayQuaHanDangTra = 0;

    try {
        Connection con = DatabaseHelper.getConnection();

        String sql = "SELECT "
                + "pm.id_phieu_muon, "
                + "pm.ten_dang_nhap, "
                + "tk.ho_ten, "
                + "ct.id_ca_biet, "
                + "ds.ten_sach, "
                + "pm.ngay_muon, "
                + "pm.han_tra, "
                + "ct.trang_thai_chi_tiet, "
                + "CASE WHEN DATEDIFF(DAY, pm.han_tra, GETDATE()) > 0 "
                + "THEN DATEDIFF(DAY, pm.han_tra, GETDATE()) ELSE 0 END AS so_ngay_qua_han "
                + "FROM ChiTietPhieuMuon ct "
                + "JOIN PhieuMuon pm ON ct.id_phieu_muon = pm.id_phieu_muon "
                + "JOIN TaiKhoan tk ON pm.ten_dang_nhap = tk.ten_dang_nhap "
                + "JOIN CuonSach cs ON ct.id_ca_biet = cs.id_ca_biet "
                + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                + "WHERE ct.id_ca_biet = ? "
                + "AND ct.trang_thai_chi_tiet = N'Đang mượn'";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, maCaBiet);

        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            idPhieuMuonDangTra = rs.getInt("id_phieu_muon");
            maCaBietDangTra = rs.getString("id_ca_biet");
            tenDangNhapDangTra = rs.getString("ten_dang_nhap");
            soNgayQuaHanDangTra = rs.getInt("so_ngay_qua_han");

            model.addRow(new Object[]{
                rs.getInt("id_phieu_muon"),
                rs.getString("ten_dang_nhap"),
                rs.getString("ho_ten"),
                rs.getString("id_ca_biet"),
                rs.getString("ten_sach"),
                rs.getTimestamp("ngay_muon"),
                rs.getTimestamp("han_tra"),
                soNgayQuaHanDangTra,
                rs.getString("trang_thai_chi_tiet")
            });

            if (soNgayQuaHanDangTra > 0) {
                JOptionPane.showMessageDialog(this,
                        "Sách đang quá hạn " + soNgayQuaHanDangTra + " ngày."
                        + "Khi xử lý trả, hệ thống sẽ lập phiếu phạt."
                );
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy cuốn sách đang mượn với mã: " + maCaBiet
                    + "Có thể sách chưa được mượn hoặc đã được trả."
            );
        }

        rs.close();
        pstmt.close();
        con.close();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tìm sách đang mượn: " + e.getMessage());
    }
}

private void xuLyTraSach() {
    if (idPhieuMuonDangTra == null || maCaBietDangTra == null || tenDangNhapDangTra == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng tìm sách đang mượn trước khi xử lý trả!");
        return;
    }

    String tinhTrangTra = String.valueOf(cboTinhTrangTra.getSelectedItem());
    String ghiChuTra = txtGhiChuTra.getText().trim();

    boolean quaHan = soNgayQuaHanDangTra > 0;
    boolean coHuHong = !tinhTrangTra.equalsIgnoreCase("Tốt");

    BigDecimal tienPhat = tinhTienPhat(soNgayQuaHanDangTra, tinhTrangTra);
    String loaiViPham = taoLoaiViPham(quaHan, tinhTrangTra);
    String moTaViPham = taoMoTaViPham(soNgayQuaHanDangTra, tinhTrangTra, ghiChuTra);

    String thongBao = "Xác nhận trả cuốn sách " + maCaBietDangTra
            + " cho phiếu mượn " + idPhieuMuonDangTra + "?";

    if (quaHan || coHuHong) {
        thongBao += "Phát hiện vi phạm: " + loaiViPham
                + "Số tiền phạt dự kiến: " + tienPhat + " VNĐ"
                + "Hệ thống sẽ lập phiếu phạt.";
    }

    int confirm = JOptionPane.showConfirmDialog(
            this,
            thongBao,
            "Xác nhận xử lý trả sách",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    Connection con = null;

    try {
        con = DatabaseHelper.getConnection();
        con.setAutoCommit(false);

        String updateChiTietSql = "UPDATE ChiTietPhieuMuon "
                + "SET trang_thai_chi_tiet = N'Đã trả', "
                + "ngay_tra_thuc_te = GETDATE(), "
                + "ghi_chu = ? "
                + "WHERE id_phieu_muon = ? "
                + "AND id_ca_biet = ? "
                + "AND trang_thai_chi_tiet = N'Đang mượn'";

        PreparedStatement updateChiTietStmt = con.prepareStatement(updateChiTietSql);
        updateChiTietStmt.setString(1, ghiChuTra.isEmpty() ? "Đã xử lý trả sách" : ghiChuTra);
        updateChiTietStmt.setInt(2, idPhieuMuonDangTra);
        updateChiTietStmt.setString(3, maCaBietDangTra);

        int resultChiTiet = updateChiTietStmt.executeUpdate();

        if (resultChiTiet <= 0) {
            con.rollback();
            JOptionPane.showMessageDialog(this, "Không thể cập nhật chi tiết phiếu mượn!");
            updateChiTietStmt.close();
            con.close();
            return;
        }

        if (quaHan || coHuHong) {
            String insertPhatSql = "INSERT INTO PhieuPhat "
                    + "(id_phieu_muon, id_ca_biet, ten_dang_nhap, loai_vi_pham, "
                    + "so_ngay_qua_han, mo_ta_vi_pham, so_tien_phat, trang_thai_thanh_toan, ghi_chu) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, N'Chưa thanh toán', ?)";

            PreparedStatement insertPhatStmt = con.prepareStatement(insertPhatSql);
            insertPhatStmt.setInt(1, idPhieuMuonDangTra);
            insertPhatStmt.setString(2, maCaBietDangTra);
            insertPhatStmt.setString(3, tenDangNhapDangTra);
            insertPhatStmt.setString(4, loaiViPham);
            insertPhatStmt.setInt(5, soNgayQuaHanDangTra);
            insertPhatStmt.setString(6, moTaViPham);
            insertPhatStmt.setBigDecimal(7, tienPhat);
            insertPhatStmt.setString(8, "Phiếu phạt phát sinh khi xử lý trả sách");

            insertPhatStmt.executeUpdate();
            insertPhatStmt.close();
        }

        String trangThaiLuuThongMoi;

        if (tinhTrangTra.equalsIgnoreCase("Tốt")) {
            trangThaiLuuThongMoi = "Sẵn sàng";
        } else if (tinhTrangTra.equalsIgnoreCase("Rách nhẹ")) {
            trangThaiLuuThongMoi = "Sẵn sàng";
        } else if (tinhTrangTra.equalsIgnoreCase("Hư hỏng")) {
            trangThaiLuuThongMoi = "Tạm giữ xử lý";
        } else {
            trangThaiLuuThongMoi = "Mất";
        }

        String updateCuonSachSql = "UPDATE CuonSach "
                + "SET trang_thai_luu_thong = ?, "
                + "tinh_trang_vat_ly = ?, "
                + "ghi_chu = ? "
                + "WHERE id_ca_biet = ?";

        PreparedStatement updateCuonSachStmt = con.prepareStatement(updateCuonSachSql);
        updateCuonSachStmt.setString(1, trangThaiLuuThongMoi);
        updateCuonSachStmt.setString(2, tinhTrangTra);
        updateCuonSachStmt.setString(3, ghiChuTra.isEmpty() ? "Cập nhật khi trả sách" : ghiChuTra);
        updateCuonSachStmt.setString(4, maCaBietDangTra);

        int resultCuonSach = updateCuonSachStmt.executeUpdate();

        if (resultCuonSach <= 0) {
            con.rollback();
            JOptionPane.showMessageDialog(this, "Không thể cập nhật trạng thái cuốn sách!");
            updateChiTietStmt.close();
            updateCuonSachStmt.close();
            con.close();
            return;
        }

        String countDangMuonSql = "SELECT COUNT(*) AS so_sach_dang_muon "
                + "FROM ChiTietPhieuMuon "
                + "WHERE id_phieu_muon = ? "
                + "AND trang_thai_chi_tiet = N'Đang mượn'";

        PreparedStatement countStmt = con.prepareStatement(countDangMuonSql);
        countStmt.setInt(1, idPhieuMuonDangTra);

        ResultSet rs = countStmt.executeQuery();

        int soSachDangMuon = 0;

        if (rs.next()) {
            soSachDangMuon = rs.getInt("so_sach_dang_muon");
        }

        rs.close();
        countStmt.close();

        String trangThaiPhieuMoi = soSachDangMuon == 0 ? "Đã trả" : "Đã trả một phần";

        String updatePhieuSql = "UPDATE PhieuMuon "
                + "SET trang_thai_phieu = ? "
                + "WHERE id_phieu_muon = ?";

        PreparedStatement updatePhieuStmt = con.prepareStatement(updatePhieuSql);
        updatePhieuStmt.setString(1, trangThaiPhieuMoi);
        updatePhieuStmt.setInt(2, idPhieuMuonDangTra);
        updatePhieuStmt.executeUpdate();
        updatePhieuStmt.close();

        con.commit();

        String message = "Trả sách thành công!"
                + "Mã sách: " + maCaBietDangTra
                + "Trạng thái sách mới: " + trangThaiLuuThongMoi
                + "Trạng thái phiếu: " + trangThaiPhieuMoi;

        if (quaHan || coHuHong) {
            message += "Đã lập phiếu phạt."
                    + "Loại vi phạm: " + loaiViPham
                    + "Tiền phạt: " + tienPhat + " VNĐ";
        }

        JOptionPane.showMessageDialog(this, message);

        updateChiTietStmt.close();
        updateCuonSachStmt.close();

        con.setAutoCommit(true);
        con.close();

        lamMoiTraSach();
        taiDanhSachPhieuMuon();

    } catch (Exception e) {
        e.printStackTrace();

        try {
            if (con != null) {
                con.rollback();
                con.close();
            }
        } catch (Exception rollbackEx) {
            rollbackEx.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, "Lỗi khi xử lý trả sách: " + e.getMessage());
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

        if (tenDangNhap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập độc giả!");
            return;
        }

        try {
            Connection con = DatabaseHelper.getConnection();

            String sql = "SELECT ten_dang_nhap, ho_ten, vai_tro, trang_thai_tai_khoan "
                    + "FROM TaiKhoan "
                    + "WHERE ten_dang_nhap = ? AND vai_tro <> N'Thủ thư'";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, tenDangNhap);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String hoTen = rs.getString("ho_ten");
                String vaiTro = rs.getString("vai_tro");
                String trangThai = rs.getString("trang_thai_tai_khoan");

                if (!trangThai.equalsIgnoreCase("Đang hoạt động")) {
                    docGiaHopLe = false;
                    docGiaDangChon = null;

                    lblThongTinDocGia.setText("<html><span style='color:red;'>Tài khoản chưa được phép mượn. Trạng thái: "
                            + trangThai + "</span></html>");
                } else {
                    docGiaHopLe = true;
                    docGiaDangChon = tenDangNhap;

                    lblThongTinDocGia.setText("<html><span style='color:green;'>Hợp lệ: "
                            + hoTen + " - " + vaiTro + "</span></html>");
                }
            } else {
                docGiaHopLe = false;
                docGiaDangChon = null;
                lblThongTinDocGia.setText("<html><span style='color:red;'>Không tìm thấy độc giả.</span></html>");
            }

            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra độc giả: " + e.getMessage());
        }
    }

    private void themSachVaoPhieuTam() {
        String maCaBiet = txtMaCaBiet.getText().trim();

        if (maCaBiet.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã cá biệt cuốn sách!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblSachMuonTam.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            String maTrongBang = String.valueOf(model.getValueAt(i, 0));
            if (maTrongBang.equalsIgnoreCase(maCaBiet)) {
                JOptionPane.showMessageDialog(this, "Cuốn sách này đã có trong phiếu mượn tạm!");
                return;
            }
        }

        try {
            Connection con = DatabaseHelper.getConnection();

            String sql = "SELECT "
                    + "cs.id_ca_biet, "
                    + "cs.id_dau_sach, "
                    + "ds.ten_sach, "
                    + "cs.trang_thai_luu_thong, "
                    + "cs.tinh_trang_vat_ly, "
                    + "cs.vi_tri_ke "
                    + "FROM CuonSach cs "
                    + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                    + "WHERE cs.id_ca_biet = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, maCaBiet);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String trangThai = rs.getString("trang_thai_luu_thong");
                String tinhTrang = rs.getString("tinh_trang_vat_ly");

                if (!trangThai.equalsIgnoreCase("Sẵn sàng")) {
                    lblThongTinSach.setText("<html><span style='color:red;'>Không thể mượn. Trạng thái: "
                            + trangThai + "</span></html>");
                    rs.close();
                    pstmt.close();
                    con.close();
                    return;
                }

                if (tinhTrang.equalsIgnoreCase("Hư hỏng") || tinhTrang.equalsIgnoreCase("Mất")) {
                    lblThongTinSach.setText("<html><span style='color:red;'>Không thể mượn. Tình trạng vật lý: "
                            + tinhTrang + "</span></html>");
                    rs.close();
                    pstmt.close();
                    con.close();
                    return;
                }

                model.addRow(new Object[]{
                    rs.getString("id_ca_biet"),
                    rs.getString("id_dau_sach"),
                    rs.getString("ten_sach"),
                    rs.getString("trang_thai_luu_thong"),
                    rs.getString("tinh_trang_vat_ly"),
                    rs.getString("vi_tri_ke")
                });

                lblThongTinSach.setText("<html><span style='color:green;'>Đã thêm sách vào phiếu tạm.</span></html>");
                txtMaCaBiet.setText("");

            } else {
                lblThongTinSach.setText("<html><span style='color:red;'>Không tìm thấy cuốn sách.</span></html>");
            }

            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra sách: " + e.getMessage());
        }
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

        if (soNgayMuon <= 0) {
            JOptionPane.showMessageDialog(this, "Số ngày mượn phải lớn hơn 0!");
            return;
        }

        Connection con = null;

        try {
            con = DatabaseHelper.getConnection();

            // Kiểm tra hạn mức mượn theo vai trò và tiền cọc trước khi tạo phiếu.
            // Phần này chỉ kiểm tra nghiệp vụ, không tác động bố cục giao diện.
            if (!kiemTraHanMucMuon(con, docGiaDangChon, model.getRowCount())) {
                con.close();
                return;
            }

            con.setAutoCommit(false);

            String insertPhieuSql = "INSERT INTO PhieuMuon "
                    + "(ten_dang_nhap, ngay_muon, han_tra, trang_thai_phieu, ghi_chu) "
                    + "VALUES (?, GETDATE(), DATEADD(DAY, ?, GETDATE()), N'Đang mượn', ?)";

            PreparedStatement insertPhieuStmt = con.prepareStatement(insertPhieuSql, Statement.RETURN_GENERATED_KEYS);
            insertPhieuStmt.setString(1, docGiaDangChon);
            insertPhieuStmt.setInt(2, soNgayMuon);
            insertPhieuStmt.setString(3, "Phiếu mượn lập từ ứng dụng thủ thư");

            int resultPhieu = insertPhieuStmt.executeUpdate();

            if (resultPhieu <= 0) {
                con.rollback();
                JOptionPane.showMessageDialog(this, "Không thể tạo phiếu mượn!");
                insertPhieuStmt.close();
                con.close();
                return;
            }

            ResultSet generatedKeys = insertPhieuStmt.getGeneratedKeys();

            int idPhieuMuon;

            if (generatedKeys.next()) {
                idPhieuMuon = generatedKeys.getInt(1);
            } else {
                con.rollback();
                JOptionPane.showMessageDialog(this, "Không lấy được mã phiếu mượn vừa tạo!");
                generatedKeys.close();
                insertPhieuStmt.close();
                con.close();
                return;
            }

            String insertChiTietSql = "INSERT INTO ChiTietPhieuMuon "
                    + "(id_phieu_muon, id_ca_biet, trang_thai_chi_tiet, ghi_chu) "
                    + "VALUES (?, ?, N'Đang mượn', ?)";

            String updateCuonSachSql = "UPDATE CuonSach "
                    + "SET trang_thai_luu_thong = N'Đang cho mượn' "
                    + "WHERE id_ca_biet = ? AND trang_thai_luu_thong = N'Sẵn sàng'";

            PreparedStatement insertChiTietStmt = con.prepareStatement(insertChiTietSql);
            PreparedStatement updateCuonSachStmt = con.prepareStatement(updateCuonSachSql);

            for (int i = 0; i < model.getRowCount(); i++) {
                String maCaBiet = String.valueOf(model.getValueAt(i, 0));

                insertChiTietStmt.setInt(1, idPhieuMuon);
                insertChiTietStmt.setString(2, maCaBiet);
                insertChiTietStmt.setString(3, "Chi tiết phiếu mượn");
                insertChiTietStmt.executeUpdate();

                updateCuonSachStmt.setString(1, maCaBiet);
                int updated = updateCuonSachStmt.executeUpdate();

                if (updated <= 0) {
                    con.rollback();
                    JOptionPane.showMessageDialog(this,
                            "Không thể cập nhật trạng thái cuốn sách: " + maCaBiet
                            + "\nCó thể sách đã được mượn bởi phiếu khác."
                    );

                    insertChiTietStmt.close();
                    updateCuonSachStmt.close();
                    generatedKeys.close();
                    insertPhieuStmt.close();
                    con.close();
                    return;
                }
            }

            con.commit();

            JOptionPane.showMessageDialog(this,
                    "Lập phiếu mượn thành công!\n"
                    + "Mã phiếu mượn: " + idPhieuMuon
            );

            insertChiTietStmt.close();
            updateCuonSachStmt.close();
            generatedKeys.close();
            insertPhieuStmt.close();

            con.setAutoCommit(true);
            con.close();

            lamMoiLapPhieu();
            taiDanhSachPhieuMuon();

        } catch (Exception e) {
            e.printStackTrace();

            try {
                if (con != null) {
                    con.rollback();
                    con.close();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }

            JOptionPane.showMessageDialog(this, "Lỗi khi lập phiếu mượn: " + e.getMessage());
        }
    }

    private boolean kiemTraHanMucMuon(Connection con, String tenDangNhap, int soSachMuonMoi) throws Exception {
        String sqlDocGia = "SELECT "
                + "tk.ho_ten, "
                + "tk.vai_tro, "
                + "ISNULL(dgn.tien_coc, 0) AS tien_coc "
                + "FROM TaiKhoan tk "
                + "LEFT JOIN DocGiaNgoai dgn ON tk.ten_dang_nhap = dgn.ten_dang_nhap "
                + "WHERE tk.ten_dang_nhap = ?";

        PreparedStatement pstmtDocGia = con.prepareStatement(sqlDocGia);
        pstmtDocGia.setString(1, tenDangNhap);

        ResultSet rsDocGia = pstmtDocGia.executeQuery();

        if (!rsDocGia.next()) {
            rsDocGia.close();
            pstmtDocGia.close();

            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin độc giả để kiểm tra hạn mức mượn!");
            return false;
        }

        String hoTen = rsDocGia.getString("ho_ten");
        String vaiTro = rsDocGia.getString("vai_tro");
        BigDecimal tienCoc = rsDocGia.getBigDecimal("tien_coc");

        rsDocGia.close();
        pstmtDocGia.close();

        if (tienCoc == null) {
            tienCoc = BigDecimal.ZERO;
        }

        int hanMucMuon = tinhHanMucMuon(vaiTro, tienCoc);
        int soSachDangMuon = demSoSachDangMuon(con, tenDangNhap);
        int tongSauKhiMuon = soSachDangMuon + soSachMuonMoi;

        if (hanMucMuon <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Độc giả này chưa đủ điều kiện mượn sách.\n"
                    + "Họ tên: " + hoTen + "\n"
                    + "Vai trò: " + vaiTro + "\n"
                    + "Tiền cọc hiện có: " + formatTien(tienCoc) + " VNĐ\n\n"
                    + "Quy định: Độc giả ngoài cần cọc tối thiểu 100.000 VNĐ để được mượn sách."
            );
            return false;
        }

        if (tongSauKhiMuon > hanMucMuon) {
            JOptionPane.showMessageDialog(this,
                    "Không thể lập phiếu mượn vì vượt hạn mức mượn sách.\n\n"
                    + "Độc giả: " + hoTen + "\n"
                    + "Vai trò: " + vaiTro + "\n"
                    + "Tiền cọc: " + formatTien(tienCoc) + " VNĐ\n"
                    + "Hạn mức được mượn: " + hanMucMuon + " cuốn\n"
                    + "Đang mượn hiện tại: " + soSachDangMuon + " cuốn\n"
                    + "Số sách trong phiếu mới: " + soSachMuonMoi + " cuốn\n"
                    + "Tổng sau khi mượn: " + tongSauKhiMuon + " cuốn"
            );
            return false;
        }

        return true;
    }

    private int tinhHanMucMuon(String vaiTro, BigDecimal tienCoc) {
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

        // Độc giả nội bộ / sinh viên / giảng viên / cán bộ.
        return 5;
    }

    private int demSoSachDangMuon(Connection con, String tenDangNhap) throws Exception {
        String sql = "SELECT COUNT(*) AS so_sach_dang_muon "
                + "FROM PhieuMuon pm "
                + "JOIN ChiTietPhieuMuon ct ON pm.id_phieu_muon = ct.id_phieu_muon "
                + "WHERE pm.ten_dang_nhap = ? "
                + "AND ct.trang_thai_chi_tiet = N'Đang mượn'";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, tenDangNhap);

        ResultSet rs = pstmt.executeQuery();

        int soSachDangMuon = 0;

        if (rs.next()) {
            soSachDangMuon = rs.getInt("so_sach_dang_muon");
        }

        rs.close();
        pstmt.close();

        return soSachDangMuon;
    }

    private String formatTien(BigDecimal soTien) {
        if (soTien == null) {
            return "0";
        }

        return String.format("%,.0f", soTien.doubleValue());
    }

private BigDecimal tinhTienPhat(int soNgayQuaHan, String tinhTrangTra) {
    BigDecimal tienPhat = BigDecimal.ZERO;

    if (soNgayQuaHan > 0) {
        tienPhat = tienPhat.add(new BigDecimal(soNgayQuaHan * 5000L));
    }

    if (tinhTrangTra.equalsIgnoreCase("Rách nhẹ")) {
        tienPhat = tienPhat.add(new BigDecimal("20000"));
    } else if (tinhTrangTra.equalsIgnoreCase("Hư hỏng")) {
        tienPhat = tienPhat.add(new BigDecimal("50000"));
    } else if (tinhTrangTra.equalsIgnoreCase("Mất")) {
        tienPhat = tienPhat.add(new BigDecimal("100000"));
    }

    return tienPhat;
}

private String taoLoaiViPham(boolean quaHan, String tinhTrangTra) {
    boolean coHuHong = !tinhTrangTra.equalsIgnoreCase("Tốt");

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

    if (!tinhTrangTra.equalsIgnoreCase("Tốt")) {
        moTa += "Tình trạng sách khi trả: " + tinhTrangTra + ". ";
    }

    if (ghiChuTra != null && !ghiChuTra.trim().isEmpty()) {
        moTa += "Ghi chú: " + ghiChuTra.trim();
    }

    return moTa.trim();
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