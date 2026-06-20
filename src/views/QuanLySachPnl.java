package views;

import components.ModernButton;
import components.ModernTextField;
import components.RoundedPanel;
import controllers.QuanLySachController;
import dto.CuonSachDTO;
import dto.DauSachDTO;
import dto.KetQuaXuLy;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import utils.UITheme;

public class QuanLySachPnl extends JPanel {
    private final QuanLySachController quanLySachController = new QuanLySachController();

    // =========================
    // TAB ĐẦU SÁCH
    // =========================
    private JTable tblDauSach;
    private ModernTextField txtTimKiemDauSach;

    private ModernTextField txtMaDauSach;
    private ModernTextField txtTenSach;
    private ModernTextField txtTacGia;
    private ModernTextField txtTheLoai;
    private ModernTextField txtNhaXuatBan;
    private ModernTextField txtNamXuatBan;
    private ModernTextField txtTomTat;
    private ModernTextField txtTrangThaiDauSach;

    // =========================
    // TAB CUỐN SÁCH
    // =========================
    private JTable tblCuonSach;
    private ModernTextField txtTimKiemCuonSach;

    private ModernTextField txtMaCaBiet;
    private ModernTextField txtMaDauSachCuaCuon;
    private JComboBox<String> cboTrangThaiLuuThong;
    private JComboBox<String> cboTinhTrangVatLy;
    private ModernTextField txtViTriKe;
    private ModernTextField txtGhiChuCuonSach;

    public QuanLySachPnl() {
        buildUI();
        taiDuLieuDauSach();
        taiDuLieuCuonSach();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_MAIN);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 17));
        tabs.addTab("Đầu sách", buildDauSachTab());
        tabs.addTab("Cuốn sách / Bản sao vật lý", buildCuonSachTab());

        add(tabs, BorderLayout.CENTER);
    }
    
    private void doDanhSachDauSachLenBang(List<DauSachDTO> danhSach) {
    DefaultTableModel model = (DefaultTableModel) tblDauSach.getModel();
    model.setRowCount(0);

    for (DauSachDTO ds : danhSach) {
        model.addRow(new Object[]{
            ds.getIdDauSach(),
            ds.getTenSach(),
            ds.getTacGia(),
            ds.getTheLoai(),
            ds.getNhaXuatBan(),
            ds.getNamXuatBan(),
            ds.getTrangThai()
        });
    }
}
    
    private void doDanhSachCuonSachLenBang(List<CuonSachDTO> danhSach) {
    DefaultTableModel model = (DefaultTableModel) tblCuonSach.getModel();
    model.setRowCount(0);

    for (CuonSachDTO cs : danhSach) {
        model.addRow(new Object[]{
            cs.getIdCaBiet(),
            cs.getIdDauSach(),
            cs.getTenSach(),
            cs.getTrangThaiLuuThong(),
            cs.getTinhTrangVatLy(),
            cs.getViTriKe(),
            cs.getNgayNhapKho(),
            cs.getGhiChu()
        });
    }
}

    // ============================================================
    // TAB 1: ĐẦU SÁCH
    // ============================================================

    private JPanel buildDauSachTab() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBackground(UITheme.BG_MAIN);
        root.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));

        root.add(buildDauSachTopPanel(), BorderLayout.NORTH);
        root.add(buildDauSachCenterPanel(), BorderLayout.CENTER);

        return root;
    }

    private JPanel buildDauSachTopPanel() {
        RoundedPanel topCard = new RoundedPanel(22, UITheme.BG_CARD);
        topCard.setLayout(new BorderLayout(12, 12));
        topCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Quản lý đầu sách");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel subtitle = new JLabel("Quản lý thông tin chung của từng tựa sách trong thư viện");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);

        txtTimKiemDauSach = new ModernTextField();
        txtTimKiemDauSach.setPreferredSize(new Dimension(240, 40));

        ModernButton btnTimKiem = new ModernButton("Tìm kiếm", UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        btnTimKiem.setPreferredSize(new Dimension(120, 40));
        btnTimKiem.addActionListener(e -> timKiemDauSach());

        ModernButton btnLamMoi = new ModernButton("Làm mới", UITheme.CYAN, UITheme.CYAN_DARK);
        btnLamMoi.setPreferredSize(new Dimension(120, 40));
        btnLamMoi.addActionListener(e -> {
            lamMoiFormDauSach();
            taiDuLieuDauSach();
        });

        JPanel btnBox = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        btnBox.setOpaque(false);
        btnBox.add(btnTimKiem);
        btnBox.add(btnLamMoi);

        searchPanel.add(txtTimKiemDauSach, BorderLayout.CENTER);
        searchPanel.add(btnBox, BorderLayout.EAST);

        topCard.add(titleBox, BorderLayout.NORTH);
        topCard.add(searchPanel, BorderLayout.SOUTH);

        return topCard;
    }

    private JPanel buildDauSachCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(16, 0));
        center.setOpaque(false);

        center.add(buildDauSachTablePanel(), BorderLayout.CENTER);
        center.add(buildDauSachFormPanel(), BorderLayout.EAST);

        return center;
    }

    private JPanel buildDauSachTablePanel() {
        RoundedPanel tableCard = new RoundedPanel(22, UITheme.BG_CARD);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14));

        tblDauSach = new JTable();
        tblDauSach.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Mã đầu sách",
                    "Tên sách",
                    "Tác giả",
                    "Thể loại",
                    "Nhà XB",
                    "Năm XB",
                    "Trạng thái"
                }
        ));

        styleTable(tblDauSach);

        tblDauSach.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                doDuLieuDauSachLenForm();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblDauSach);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        tableCard.add(scrollPane, BorderLayout.CENTER);

        return tableCard;
    }

    private JPanel buildDauSachFormPanel() {
        RoundedPanel formCard = new RoundedPanel(22, UITheme.BG_CARD);
        formCard.setLayout(new BorderLayout(0, 10));
        formCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 16, 14, 16));
        formCard.setPreferredSize(new Dimension(360, 0));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel formTitle = new JLabel("Thông tin đầu sách");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 21));
        formTitle.setForeground(UITheme.TEXT_DARK);
        formTitle.setAlignmentX(LEFT_ALIGNMENT);

        txtMaDauSach = new ModernTextField();
        txtTenSach = new ModernTextField();
        txtTacGia = new ModernTextField();
        txtTheLoai = new ModernTextField();
        txtNhaXuatBan = new ModernTextField();
        txtNamXuatBan = new ModernTextField();
        txtTomTat = new ModernTextField();
        txtTrangThaiDauSach = new ModernTextField();
        txtTrangThaiDauSach.setEditable(false);

        setFieldSize(txtMaDauSach);
        setFieldSize(txtTenSach);
        setFieldSize(txtTacGia);
        setFieldSize(txtTheLoai);
        setFieldSize(txtNhaXuatBan);
        setFieldSize(txtNamXuatBan);
        setFieldSize(txtTomTat);
        setFieldSize(txtTrangThaiDauSach);

        ModernButton btnThem = new ModernButton("Thêm đầu sách", UITheme.GREEN, UITheme.GREEN_DARK);
        ModernButton btnSua = new ModernButton("Cập nhật đầu sách", UITheme.ORANGE, UITheme.ORANGE_DARK);
        ModernButton btnNgungPhucVu = new ModernButton("Ngừng phục vụ", UITheme.RED, UITheme.RED_DARK);

        setButtonSize(btnThem);
        setButtonSize(btnSua);
        setButtonSize(btnNgungPhucVu);

        btnThem.addActionListener(e -> themDauSach());
        btnSua.addActionListener(e -> suaDauSach());
        btnNgungPhucVu.addActionListener(e -> ngungPhucVuDauSach());

        content.add(formTitle);
        content.add(Box.createVerticalStrut(14));

        addField(content, "Mã đầu sách", txtMaDauSach);
        addField(content, "Tên sách", txtTenSach);
        addField(content, "Tác giả", txtTacGia);
        addField(content, "Thể loại", txtTheLoai);
        addField(content, "Nhà xuất bản", txtNhaXuatBan);
        addField(content, "Năm xuất bản", txtNamXuatBan);
        addField(content, "Tóm tắt", txtTomTat);
        addField(content, "Trạng thái", txtTrangThaiDauSach);

        content.add(Box.createVerticalStrut(10));
        content.add(btnThem);
        content.add(Box.createVerticalStrut(8));
        content.add(btnSua);
        content.add(Box.createVerticalStrut(8));
        content.add(btnNgungPhucVu);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        formCard.add(scroll, BorderLayout.CENTER);

        return formCard;
    }

    // ============================================================
    // TAB 2: CUỐN SÁCH
    // ============================================================

    private JPanel buildCuonSachTab() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBackground(UITheme.BG_MAIN);
        root.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));

        root.add(buildCuonSachTopPanel(), BorderLayout.NORTH);
        root.add(buildCuonSachCenterPanel(), BorderLayout.CENTER);

        return root;
    }

    private JPanel buildCuonSachTopPanel() {
        RoundedPanel topCard = new RoundedPanel(22, UITheme.BG_CARD);
        topCard.setLayout(new BorderLayout(12, 12));
        topCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Quản lý cuốn sách / bản sao vật lý");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel subtitle = new JLabel("Theo dõi từng quyển sách cụ thể bằng mã cá biệt, trạng thái lưu thông và vị trí kệ");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);

        txtTimKiemCuonSach = new ModernTextField();
        txtTimKiemCuonSach.setPreferredSize(new Dimension(240, 40));

        ModernButton btnTimKiem = new ModernButton("Tìm kiếm", UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        btnTimKiem.setPreferredSize(new Dimension(120, 40));
        btnTimKiem.addActionListener(e -> timKiemCuonSach());

        ModernButton btnLamMoi = new ModernButton("Làm mới", UITheme.CYAN, UITheme.CYAN_DARK);
        btnLamMoi.setPreferredSize(new Dimension(120, 40));
        btnLamMoi.addActionListener(e -> {
            lamMoiFormCuonSach();
            taiDuLieuCuonSach();
        });

        JPanel btnBox = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        btnBox.setOpaque(false);
        btnBox.add(btnTimKiem);
        btnBox.add(btnLamMoi);

        searchPanel.add(txtTimKiemCuonSach, BorderLayout.CENTER);
        searchPanel.add(btnBox, BorderLayout.EAST);

        topCard.add(titleBox, BorderLayout.NORTH);
        topCard.add(searchPanel, BorderLayout.SOUTH);

        return topCard;
    }

    private JPanel buildCuonSachCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(16, 0));
        center.setOpaque(false);

        center.add(buildCuonSachTablePanel(), BorderLayout.CENTER);
        center.add(buildCuonSachFormPanel(), BorderLayout.EAST);

        return center;
    }

    private JPanel buildCuonSachTablePanel() {
        RoundedPanel tableCard = new RoundedPanel(22, UITheme.BG_CARD);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14));

        tblCuonSach = new JTable();
        tblCuonSach.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Mã cá biệt",
                    "Mã đầu sách",
                    "Tên sách",
                    "Trạng thái lưu thông",
                    "Tình trạng vật lý",
                    "Vị trí kệ",
                    "Ngày nhập kho",
                    "Ghi chú"
                }
        ));

        styleTable(tblCuonSach);
        tblCuonSach.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tblCuonSach.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblCuonSach.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblCuonSach.getColumnModel().getColumn(2).setPreferredWidth(210);
        tblCuonSach.getColumnModel().getColumn(3).setPreferredWidth(150);
        tblCuonSach.getColumnModel().getColumn(4).setPreferredWidth(130);
        tblCuonSach.getColumnModel().getColumn(5).setPreferredWidth(130);
        tblCuonSach.getColumnModel().getColumn(6).setPreferredWidth(160);
        tblCuonSach.getColumnModel().getColumn(7).setPreferredWidth(180);

        tblCuonSach.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                doDuLieuCuonSachLenForm();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblCuonSach);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        tableCard.add(scrollPane, BorderLayout.CENTER);

        return tableCard;
    }

    private JPanel buildCuonSachFormPanel() {
        RoundedPanel formCard = new RoundedPanel(22, UITheme.BG_CARD);
        formCard.setLayout(new BorderLayout(0, 10));
        formCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 16, 14, 16));
        formCard.setPreferredSize(new Dimension(360, 0));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel formTitle = new JLabel("Thông tin cuốn sách");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 21));
        formTitle.setForeground(UITheme.TEXT_DARK);
        formTitle.setAlignmentX(LEFT_ALIGNMENT);

        txtMaCaBiet = new ModernTextField();
        txtMaDauSachCuaCuon = new ModernTextField();
        txtViTriKe = new ModernTextField();
        txtGhiChuCuonSach = new ModernTextField();

        cboTrangThaiLuuThong = new JComboBox<>(new String[]{
            "Sẵn sàng",
            "Đang cho mượn",
            "Đã thanh lý",
            "Mất"
        });

        cboTinhTrangVatLy = new JComboBox<>(new String[]{
            "Tốt",
            "Rách nhẹ",
            "Hư hỏng",
            "Mất"
        });

        setFieldSize(txtMaCaBiet);
        setFieldSize(txtMaDauSachCuaCuon);
        setFieldSize(txtViTriKe);
        setFieldSize(txtGhiChuCuonSach);
        setComboSize(cboTrangThaiLuuThong);
        setComboSize(cboTinhTrangVatLy);

        ModernButton btnThem = new ModernButton("Thêm cuốn sách", UITheme.GREEN, UITheme.GREEN_DARK);
        ModernButton btnSua = new ModernButton("Cập nhật cuốn sách", UITheme.ORANGE, UITheme.ORANGE_DARK);
        ModernButton btnThanhLy = new ModernButton("Thanh lý / Mất", UITheme.RED, UITheme.RED_DARK);

        setButtonSize(btnThem);
        setButtonSize(btnSua);
        setButtonSize(btnThanhLy);

        btnThem.addActionListener(e -> themCuonSach());
        btnSua.addActionListener(e -> suaCuonSach());
        btnThanhLy.addActionListener(e -> thanhLyCuonSach());

        content.add(formTitle);
        content.add(Box.createVerticalStrut(14));

        addField(content, "Mã cá biệt / mã vạch", txtMaCaBiet);
        addField(content, "Mã đầu sách", txtMaDauSachCuaCuon);
        addComboField(content, "Trạng thái lưu thông", cboTrangThaiLuuThong);
        addComboField(content, "Tình trạng vật lý", cboTinhTrangVatLy);
        addField(content, "Vị trí kệ", txtViTriKe);
        addField(content, "Ghi chú", txtGhiChuCuonSach);

        content.add(Box.createVerticalStrut(10));
        content.add(btnThem);
        content.add(Box.createVerticalStrut(8));
        content.add(btnSua);
        content.add(Box.createVerticalStrut(8));
        content.add(btnThanhLy);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

formCard.add(scroll, BorderLayout.CENTER);

        return formCard;
    }

    // ============================================================
    // STYLE HELPERS
    // ============================================================

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
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setPreferredSize(new Dimension(300, 38));
        field.setMinimumSize(new Dimension(120, 38));
        field.setAlignmentX(LEFT_ALIGNMENT);
    }

    private void setComboSize(JComboBox<String> combo) {
        combo.setFont(UITheme.FONT_NORMAL);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        combo.setPreferredSize(new Dimension(300, 38));
        combo.setMinimumSize(new Dimension(120, 38));
        combo.setAlignmentX(LEFT_ALIGNMENT);
    }

    private void setButtonSize(javax.swing.JComponent btn) {
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setPreferredSize(new Dimension(300, 40));
        btn.setMinimumSize(new Dimension(120, 40));
        btn.setAlignmentX(LEFT_ALIGNMENT);
    }

    private void addField(JPanel content, String labelText, javax.swing.JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(UITheme.TEXT_NORMAL);
        label.setAlignmentX(LEFT_ALIGNMENT);

        content.add(label);
        content.add(Box.createVerticalStrut(4));
        content.add(field);
        content.add(Box.createVerticalStrut(8));
    }

    private void addComboField(JPanel content, String labelText, JComboBox<String> combo) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(UITheme.TEXT_NORMAL);
        label.setAlignmentX(LEFT_ALIGNMENT);

        content.add(label);
        content.add(Box.createVerticalStrut(4));
        content.add(combo);
        content.add(Box.createVerticalStrut(8));
    }

    // ============================================================
    // ĐẦU SÁCH - LOGIC
    // ============================================================

    private void taiDuLieuDauSach() {
    try {
        List<DauSachDTO> danhSach = quanLySachController.layDanhSachDauSach();
        doDanhSachDauSachLenBang(danhSach);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu đầu sách: " + e.getMessage());
    }
}

    private void timKiemDauSach() {
    String tuKhoa = txtTimKiemDauSach.getText().trim();

    if (tuKhoa.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!");
        return;
    }

    try {
        List<DauSachDTO> danhSach = quanLySachController.timKiemDauSach(tuKhoa);
        doDanhSachDauSachLenBang(danhSach);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm đầu sách: " + e.getMessage());
    }
}

    private void doDuLieuDauSachLenForm() {
    int row = tblDauSach.getSelectedRow();

    if (row < 0) {
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblDauSach.getModel();
    String id = String.valueOf(model.getValueAt(row, 0));

    try {
        DauSachDTO ds = quanLySachController.timDauSachTheoId(id);

        if (ds == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết đầu sách!");
            return;
        }

        txtMaDauSach.setText(ds.getIdDauSach());
        txtTenSach.setText(ds.getTenSach());
        txtTacGia.setText(ds.getTacGia());
        txtTheLoai.setText(ds.getTheLoai());
        txtNhaXuatBan.setText(ds.getNhaXuatBan());
        txtNamXuatBan.setText(ds.getNamXuatBan() == null ? "" : String.valueOf(ds.getNamXuatBan()));
        txtTomTat.setText(ds.getTomTat());
        txtTrangThaiDauSach.setText(ds.getTrangThai());

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi lấy chi tiết đầu sách: " + e.getMessage());
    }
}

    private void themDauSach() {
    String id = txtMaDauSach.getText().trim();
    String ten = txtTenSach.getText().trim();
    String tacGia = txtTacGia.getText().trim();
    String theLoai = txtTheLoai.getText().trim();
    String nxb = txtNhaXuatBan.getText().trim();
    String namStr = txtNamXuatBan.getText().trim();
    String tomTat = txtTomTat.getText().trim();

    KetQuaXuLy ketQua = quanLySachController.themDauSach(
            id,
            ten,
            tacGia,
            theLoai,
            nxb,
            namStr,
            tomTat
    );

    JOptionPane.showMessageDialog(this, ketQua.getThongBao());

    if (ketQua.isThanhCong()) {
        lamMoiFormDauSach();
        taiDuLieuDauSach();
    }
}

    private void suaDauSach() {
    String id = txtMaDauSach.getText().trim();
    String ten = txtTenSach.getText().trim();
    String tacGia = txtTacGia.getText().trim();
    String theLoai = txtTheLoai.getText().trim();
    String nxb = txtNhaXuatBan.getText().trim();
    String namStr = txtNamXuatBan.getText().trim();
    String tomTat = txtTomTat.getText().trim();

    KetQuaXuLy ketQua = quanLySachController.capNhatDauSach(
            id,
            ten,
            tacGia,
            theLoai,
            nxb,
            namStr,
            tomTat
    );

    JOptionPane.showMessageDialog(this, ketQua.getThongBao());

    if (ketQua.isThanhCong()) {
        taiDuLieuDauSach();
    }
}

    private void ngungPhucVuDauSach() {
    String id = txtMaDauSach.getText().trim();

    if (id.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn đầu sách cần ngừng phục vụ!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn chuyển đầu sách này sang trạng thái 'Ngừng phục vụ' không?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    KetQuaXuLy ketQua = quanLySachController.ngungPhucVuDauSach(id);

    JOptionPane.showMessageDialog(this, ketQua.getThongBao());

    if (ketQua.isThanhCong()) {
        taiDuLieuDauSach();
    }
}

    private void lamMoiFormDauSach() {
        txtMaDauSach.setText("");
        txtTenSach.setText("");
        txtTacGia.setText("");
        txtTheLoai.setText("");
        txtNhaXuatBan.setText("");
        txtNamXuatBan.setText("");
        txtTomTat.setText("");
        txtTrangThaiDauSach.setText("");
        txtTimKiemDauSach.setText("");
    }

    // ============================================================
    // CUỐN SÁCH - LOGIC
    // ============================================================

    private void taiDuLieuCuonSach() {
    try {
        List<CuonSachDTO> danhSach = quanLySachController.layDanhSachCuonSach();
        doDanhSachCuonSachLenBang(danhSach);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu cuốn sách: " + e.getMessage());
    }
}

    private void timKiemCuonSach() {
    String tuKhoa = txtTimKiemCuonSach.getText().trim();

    if (tuKhoa.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!");
        return;
    }

    try {
        List<CuonSachDTO> danhSach = quanLySachController.timKiemCuonSach(tuKhoa);
        doDanhSachCuonSachLenBang(danhSach);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm cuốn sách: " + e.getMessage());
    }
}

    private void doDuLieuCuonSachLenForm() {
        int row = tblCuonSach.getSelectedRow();

        if (row < 0) {
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblCuonSach.getModel();

        txtMaCaBiet.setText(String.valueOf(model.getValueAt(row, 0)));
        txtMaDauSachCuaCuon.setText(String.valueOf(model.getValueAt(row, 1)));
        cboTrangThaiLuuThong.setSelectedItem(String.valueOf(model.getValueAt(row, 3)));
        cboTinhTrangVatLy.setSelectedItem(String.valueOf(model.getValueAt(row, 4)));
        txtViTriKe.setText(String.valueOf(model.getValueAt(row, 5)));

        Object ghiChu = model.getValueAt(row, 7);
        txtGhiChuCuonSach.setText(ghiChu == null ? "" : String.valueOf(ghiChu));
    }

    private void themCuonSach() {
    String idCaBiet = txtMaCaBiet.getText().trim();
    String idDauSach = txtMaDauSachCuaCuon.getText().trim();
    String trangThai = String.valueOf(cboTrangThaiLuuThong.getSelectedItem());
    String tinhTrang = String.valueOf(cboTinhTrangVatLy.getSelectedItem());
    String viTri = txtViTriKe.getText().trim();
    String ghiChu = txtGhiChuCuonSach.getText().trim();

    KetQuaXuLy ketQua = quanLySachController.themCuonSach(
            idCaBiet,
            idDauSach,
            trangThai,
            tinhTrang,
            viTri,
            ghiChu
    );

    JOptionPane.showMessageDialog(this, ketQua.getThongBao());

    if (ketQua.isThanhCong()) {
        lamMoiFormCuonSach();
        taiDuLieuCuonSach();
    }
}

    private void suaCuonSach() {
    String idCaBiet = txtMaCaBiet.getText().trim();
    String idDauSach = txtMaDauSachCuaCuon.getText().trim();
    String trangThai = String.valueOf(cboTrangThaiLuuThong.getSelectedItem());
    String tinhTrang = String.valueOf(cboTinhTrangVatLy.getSelectedItem());
    String viTri = txtViTriKe.getText().trim();
    String ghiChu = txtGhiChuCuonSach.getText().trim();

    KetQuaXuLy ketQua = quanLySachController.capNhatCuonSach(
            idCaBiet,
            idDauSach,
            trangThai,
            tinhTrang,
            viTri,
            ghiChu
    );

    JOptionPane.showMessageDialog(this, ketQua.getThongBao());

    if (ketQua.isThanhCong()) {
        taiDuLieuCuonSach();
    }
}

    private void thanhLyCuonSach() {
    String idCaBiet = txtMaCaBiet.getText().trim();

    if (idCaBiet.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn cuốn sách cần thanh lý!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn muốn chuyển cuốn sách này sang trạng thái 'Đã thanh lý' không?",
            "Xác nhận thanh lý",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    KetQuaXuLy ketQua = quanLySachController.thanhLyCuonSach(idCaBiet);

    JOptionPane.showMessageDialog(this, ketQua.getThongBao());

    if (ketQua.isThanhCong()) {
        taiDuLieuCuonSach();
    }
}

    private void lamMoiFormCuonSach() {
        txtMaCaBiet.setText("");
        txtMaDauSachCuaCuon.setText("");
        cboTrangThaiLuuThong.setSelectedItem("Sẵn sàng");
        cboTinhTrangVatLy.setSelectedItem("Tốt");
        txtViTriKe.setText("");
        txtGhiChuCuonSach.setText("");
        txtTimKiemCuonSach.setText("");
    }
}