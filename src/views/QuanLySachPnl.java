package views;

import components.ModernButton;
import components.ModernTextField;
import components.RoundedPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import utils.DatabaseHelper;
import utils.UITheme;

public class QuanLySachPnl extends JPanel {

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
        DefaultTableModel model = (DefaultTableModel) tblDauSach.getModel();
        model.setRowCount(0);

        try {
            Connection con = DatabaseHelper.getConnection();

            String sql = "SELECT id_dau_sach, ten_sach, tac_gia, the_loai, "
                    + "nha_xuat_ban, nam_xuat_ban, trang_thai "
                    + "FROM DauSach "
                    + "ORDER BY id_dau_sach";

            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_dau_sach"),
                    rs.getString("ten_sach"),
                    rs.getString("tac_gia"),
                    rs.getString("the_loai"),
                    rs.getString("nha_xuat_ban"),
                    rs.getObject("nam_xuat_ban"),
                    rs.getString("trang_thai")
                });
            }

            rs.close();
            pstmt.close();
            con.close();

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

        DefaultTableModel model = (DefaultTableModel) tblDauSach.getModel();
        model.setRowCount(0);

        try {
            Connection con = DatabaseHelper.getConnection();

            String sql = "SELECT id_dau_sach, ten_sach, tac_gia, the_loai, "
                    + "nha_xuat_ban, nam_xuat_ban, trang_thai "
                    + "FROM DauSach "
                    + "WHERE id_dau_sach LIKE ? "
                    + "OR ten_sach LIKE ? "
                    + "OR tac_gia LIKE ? "
                    + "OR the_loai LIKE ? "
                    + "OR nha_xuat_ban LIKE ? "
                    + "ORDER BY id_dau_sach";

            PreparedStatement pstmt = con.prepareStatement(sql);

            String keyword = "%" + tuKhoa + "%";
            pstmt.setString(1, keyword);
            pstmt.setString(2, keyword);
            pstmt.setString(3, keyword);
            pstmt.setString(4, keyword);
            pstmt.setString(5, keyword);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_dau_sach"),
                    rs.getString("ten_sach"),
                    rs.getString("tac_gia"),
                    rs.getString("the_loai"),
                    rs.getString("nha_xuat_ban"),
                    rs.getObject("nam_xuat_ban"),
                    rs.getString("trang_thai")
                });
            }

            rs.close();
            pstmt.close();
            con.close();

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
            Connection con = DatabaseHelper.getConnection();

            String sql = "SELECT * FROM DauSach WHERE id_dau_sach = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                txtMaDauSach.setText(rs.getString("id_dau_sach"));
                txtTenSach.setText(rs.getString("ten_sach"));
                txtTacGia.setText(rs.getString("tac_gia"));
                txtTheLoai.setText(rs.getString("the_loai"));
                txtNhaXuatBan.setText(rs.getString("nha_xuat_ban"));

                Object nam = rs.getObject("nam_xuat_ban");
                txtNamXuatBan.setText(nam == null ? "" : String.valueOf(nam));

                txtTomTat.setText(rs.getString("tom_tat"));
                txtTrangThaiDauSach.setText(rs.getString("trang_thai"));
            }

            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy chi tiết đầu sách: " + e.getMessage());
        }
    }

    private void themDauSach() {
        try {
            String id = txtMaDauSach.getText().trim();
            String ten = txtTenSach.getText().trim();
            String tacGia = txtTacGia.getText().trim();
            String theLoai = txtTheLoai.getText().trim();
            String nxb = txtNhaXuatBan.getText().trim();
            String namStr = txtNamXuatBan.getText().trim();
            String tomTat = txtTomTat.getText().trim();

            if (id.isEmpty() || ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã đầu sách và tên sách không được để trống!");
                return;
            }

            Integer nam = null;
            if (!namStr.isEmpty()) {
                nam = Integer.parseInt(namStr);
            }

            Connection con = DatabaseHelper.getConnection();

            String sql = "INSERT INTO DauSach "
                    + "(id_dau_sach, ten_sach, tac_gia, the_loai, nha_xuat_ban, nam_xuat_ban, tom_tat, trang_thai) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, N'Còn phục vụ')";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, ten);
            pstmt.setString(3, tacGia);
            pstmt.setString(4, theLoai);
            pstmt.setString(5, nxb);

            if (nam == null) {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(6, nam);
            }

            pstmt.setString(7, tomTat);

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Thêm đầu sách thành công!");

            pstmt.close();
            con.close();

            lamMoiFormDauSach();
            taiDuLieuDauSach();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Năm xuất bản phải là số!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm đầu sách: " + e.getMessage());
        }
    }

    private void suaDauSach() {
        try {
            String id = txtMaDauSach.getText().trim();
            String ten = txtTenSach.getText().trim();
            String tacGia = txtTacGia.getText().trim();
            String theLoai = txtTheLoai.getText().trim();
            String nxb = txtNhaXuatBan.getText().trim();
            String namStr = txtNamXuatBan.getText().trim();
            String tomTat = txtTomTat.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hoặc nhập mã đầu sách cần cập nhật!");
                return;
            }

            Integer nam = null;
            if (!namStr.isEmpty()) {
                nam = Integer.parseInt(namStr);
            }

            Connection con = DatabaseHelper.getConnection();

            String sql = "UPDATE DauSach "
                    + "SET ten_sach = ?, tac_gia = ?, the_loai = ?, nha_xuat_ban = ?, nam_xuat_ban = ?, tom_tat = ? "
                    + "WHERE id_dau_sach = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, ten);
            pstmt.setString(2, tacGia);
            pstmt.setString(3, theLoai);
            pstmt.setString(4, nxb);

            if (nam == null) {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(5, nam);
            }

            pstmt.setString(6, tomTat);
            pstmt.setString(7, id);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật đầu sách thành công!");
                taiDuLieuDauSach();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy đầu sách cần cập nhật!");
            }

            pstmt.close();
            con.close();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Năm xuất bản phải là số!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật đầu sách: " + e.getMessage());
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

        try {
            Connection con = DatabaseHelper.getConnection();

            String sql = "UPDATE DauSach SET trang_thai = N'Ngừng phục vụ' WHERE id_dau_sach = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, id);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Đã chuyển đầu sách sang trạng thái Ngừng phục vụ!");
                taiDuLieuDauSach();
            }

            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi ngừng phục vụ đầu sách: " + e.getMessage());
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
        DefaultTableModel model = (DefaultTableModel) tblCuonSach.getModel();
        model.setRowCount(0);

        try {
            Connection con = DatabaseHelper.getConnection();

            String sql = "SELECT "
                    + "cs.id_ca_biet, "
                    + "cs.id_dau_sach, "
                    + "ds.ten_sach, "
                    + "cs.trang_thai_luu_thong, "
                    + "cs.tinh_trang_vat_ly, "
                    + "cs.vi_tri_ke, "
                    + "cs.ngay_nhap_kho, "
                    + "cs.ghi_chu "
                    + "FROM CuonSach cs "
                    + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                    + "ORDER BY cs.id_ca_biet";

            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_ca_biet"),
                    rs.getString("id_dau_sach"),
                    rs.getString("ten_sach"),
                    rs.getString("trang_thai_luu_thong"),
                    rs.getString("tinh_trang_vat_ly"),
                    rs.getString("vi_tri_ke"),
                    rs.getTimestamp("ngay_nhap_kho"),
                    rs.getString("ghi_chu")
                });
            }

            rs.close();
            pstmt.close();
            con.close();

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

        DefaultTableModel model = (DefaultTableModel) tblCuonSach.getModel();
        model.setRowCount(0);

        try {
            Connection con = DatabaseHelper.getConnection();

            String sql = "SELECT "
                    + "cs.id_ca_biet, "
                    + "cs.id_dau_sach, "
                    + "ds.ten_sach, "
                    + "cs.trang_thai_luu_thong, "
                    + "cs.tinh_trang_vat_ly, "
                    + "cs.vi_tri_ke, "
                    + "cs.ngay_nhap_kho, "
                    + "cs.ghi_chu "
                    + "FROM CuonSach cs "
                    + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                    + "WHERE cs.id_ca_biet LIKE ? "
                    + "OR cs.id_dau_sach LIKE ? "
                    + "OR ds.ten_sach LIKE ? "
                    + "OR cs.trang_thai_luu_thong LIKE ? "
                    + "OR cs.tinh_trang_vat_ly LIKE ? "
                    + "OR cs.vi_tri_ke LIKE ? "
                    + "ORDER BY cs.id_ca_biet";

            PreparedStatement pstmt = con.prepareStatement(sql);

            String keyword = "%" + tuKhoa + "%";
            pstmt.setString(1, keyword);
            pstmt.setString(2, keyword);
            pstmt.setString(3, keyword);
            pstmt.setString(4, keyword);
            pstmt.setString(5, keyword);
            pstmt.setString(6, keyword);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_ca_biet"),
                    rs.getString("id_dau_sach"),
                    rs.getString("ten_sach"),
                    rs.getString("trang_thai_luu_thong"),
                    rs.getString("tinh_trang_vat_ly"),
                    rs.getString("vi_tri_ke"),
                    rs.getTimestamp("ngay_nhap_kho"),
                    rs.getString("ghi_chu")
                });
            }

            rs.close();
            pstmt.close();
            con.close();

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
        try {
            String idCaBiet = txtMaCaBiet.getText().trim();
            String idDauSach = txtMaDauSachCuaCuon.getText().trim();
            String trangThai = String.valueOf(cboTrangThaiLuuThong.getSelectedItem());
            String tinhTrang = String.valueOf(cboTinhTrangVatLy.getSelectedItem());
            String viTri = txtViTriKe.getText().trim();
            String ghiChu = txtGhiChuCuonSach.getText().trim();

            if (idCaBiet.isEmpty() || idDauSach.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã cá biệt và mã đầu sách không được để trống!");
                return;
            }

            Connection con = DatabaseHelper.getConnection();

            String sql = "INSERT INTO CuonSach "
                    + "(id_ca_biet, id_dau_sach, trang_thai_luu_thong, tinh_trang_vat_ly, vi_tri_ke, ghi_chu) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, idCaBiet);
            pstmt.setString(2, idDauSach);
            pstmt.setString(3, trangThai);
            pstmt.setString(4, tinhTrang);
            pstmt.setString(5, viTri);
            pstmt.setString(6, ghiChu);

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Thêm cuốn sách thành công!");

            pstmt.close();
            con.close();

            lamMoiFormCuonSach();
            taiDuLieuCuonSach();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm cuốn sách: " + e.getMessage());
        }
    }

    private void suaCuonSach() {
        try {
            String idCaBiet = txtMaCaBiet.getText().trim();
            String idDauSach = txtMaDauSachCuaCuon.getText().trim();
            String trangThai = String.valueOf(cboTrangThaiLuuThong.getSelectedItem());
            String tinhTrang = String.valueOf(cboTinhTrangVatLy.getSelectedItem());
            String viTri = txtViTriKe.getText().trim();
            String ghiChu = txtGhiChuCuonSach.getText().trim();

            if (idCaBiet.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hoặc nhập mã cá biệt cần cập nhật!");
                return;
            }

            Connection con = DatabaseHelper.getConnection();

            String sql = "UPDATE CuonSach "
                    + "SET id_dau_sach = ?, trang_thai_luu_thong = ?, tinh_trang_vat_ly = ?, vi_tri_ke = ?, ghi_chu = ? "
                    + "WHERE id_ca_biet = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, idDauSach);
            pstmt.setString(2, trangThai);
            pstmt.setString(3, tinhTrang);
            pstmt.setString(4, viTri);
            pstmt.setString(5, ghiChu);
            pstmt.setString(6, idCaBiet);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật cuốn sách thành công!");
                taiDuLieuCuonSach();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy cuốn sách cần cập nhật!");
            }

            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật cuốn sách: " + e.getMessage());
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

        try {
            Connection con = DatabaseHelper.getConnection();

            String sql = "UPDATE CuonSach "
                    + "SET trang_thai_luu_thong = N'Đã thanh lý', "
                    + "tinh_trang_vat_ly = N'Hư hỏng', "
                    + "ghi_chu = N'Đã thanh lý khỏi kho lưu thông' "
                    + "WHERE id_ca_biet = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, idCaBiet);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Đã thanh lý cuốn sách!");
                taiDuLieuCuonSach();
            }

            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thanh lý cuốn sách: " + e.getMessage());
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