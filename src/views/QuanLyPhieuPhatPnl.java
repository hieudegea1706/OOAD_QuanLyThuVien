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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import utils.DatabaseHelper;
import utils.UITheme;

public class QuanLyPhieuPhatPnl extends JPanel {

    private ModernTextField txtTimKiem;
    private JTable tblPhieuPhat;

    private JLabel lblMaPhat;
    private JLabel lblMaPhieuMuon;
    private JLabel lblDocGia;
    private JLabel lblMaSach;
    private JLabel lblTenSach;
    private JLabel lblLoaiViPham;
    private JLabel lblSoNgayQuaHan;
    private JLabel lblTienPhat;
    private JLabel lblTrangThai;
    private JTextArea txtMoTa;

    public QuanLyPhieuPhatPnl() {
        buildUI();
        taiDanhSachPhieuPhat();
    }

    private void buildUI() {
        setLayout(new BorderLayout(0, 16));
        setBackground(UITheme.BG_MAIN);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
    }

    private JPanel buildTopPanel() {
        RoundedPanel topCard = new RoundedPanel(24, UITheme.BG_CARD);
        topCard.setLayout(new BorderLayout(12, 12));
        topCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Quản lý phiếu phạt");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel subtitle = new JLabel("Theo dõi vi phạm, tiền phạt và trạng thái thanh toán của độc giả");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);

        txtTimKiem = new ModernTextField();
        txtTimKiem.setPreferredSize(new Dimension(260, 40));

        ModernButton btnTimKiem = new ModernButton("Tìm kiếm", UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        btnTimKiem.setPreferredSize(new Dimension(120, 40));
        btnTimKiem.addActionListener(e -> timKiemPhieuPhat());

        ModernButton btnLamMoi = new ModernButton("Làm mới", UITheme.CYAN, UITheme.CYAN_DARK);
        btnLamMoi.setPreferredSize(new Dimension(120, 40));
        btnLamMoi.addActionListener(e -> {
            lamMoiThongTin();
            taiDanhSachPhieuPhat();
        });

        JPanel btnBox = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        btnBox.setOpaque(false);
        btnBox.add(btnTimKiem);
        btnBox.add(btnLamMoi);

        searchPanel.add(txtTimKiem, BorderLayout.CENTER);
        searchPanel.add(btnBox, BorderLayout.EAST);

        topCard.add(titleBox, BorderLayout.NORTH);
        topCard.add(searchPanel, BorderLayout.SOUTH);

        return topCard;
    }

    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(16, 0));
        center.setOpaque(false);

        center.add(buildTablePanel(), BorderLayout.CENTER);
        center.add(buildDetailPanel(), BorderLayout.EAST);

        return center;
    }

    private JPanel buildTablePanel() {
        RoundedPanel tableCard = new RoundedPanel(24, UITheme.BG_CARD);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14));

        tblPhieuPhat = new JTable();
        tblPhieuPhat.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Mã phạt",
                    "Mã phiếu",
                    "Tên đăng nhập",
                    "Họ tên",
                    "Mã sách",
                    "Tên sách",
                    "Loại vi phạm",
                    "Quá hạn",
                    "Tiền phạt",
                    "Thanh toán",
                    "Ngày lập",
                    "Mô tả"
                }
        ));

        styleTable(tblPhieuPhat);
        tblPhieuPhat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tblPhieuPhat.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblPhieuPhat.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblPhieuPhat.getColumnModel().getColumn(2).setPreferredWidth(130);
        tblPhieuPhat.getColumnModel().getColumn(3).setPreferredWidth(160);
        tblPhieuPhat.getColumnModel().getColumn(4).setPreferredWidth(90);
        tblPhieuPhat.getColumnModel().getColumn(5).setPreferredWidth(220);
        tblPhieuPhat.getColumnModel().getColumn(6).setPreferredWidth(150);
        tblPhieuPhat.getColumnModel().getColumn(7).setPreferredWidth(80);
        tblPhieuPhat.getColumnModel().getColumn(8).setPreferredWidth(120);
        tblPhieuPhat.getColumnModel().getColumn(9).setPreferredWidth(140);
        tblPhieuPhat.getColumnModel().getColumn(10).setPreferredWidth(160);
        tblPhieuPhat.getColumnModel().getColumn(11).setPreferredWidth(260);

        tblPhieuPhat.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                doDuLieuLenChiTiet();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblPhieuPhat);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        tableCard.add(scrollPane, BorderLayout.CENTER);

        return tableCard;
    }

    private JPanel buildDetailPanel() {
        RoundedPanel detailCard = new RoundedPanel(24, UITheme.BG_CARD);
        detailCard.setLayout(new BorderLayout());
        detailCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 18, 16, 18));
        detailCard.setPreferredSize(new Dimension(360, 0));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Chi tiết phiếu phạt");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UITheme.TEXT_DARK);
        title.setAlignmentX(LEFT_ALIGNMENT);

        lblMaPhat = createValueLabel();
        lblMaPhieuMuon = createValueLabel();
        lblDocGia = createValueLabel();
        lblMaSach = createValueLabel();
        lblTenSach = createValueLabel();
        lblLoaiViPham = createValueLabel();
        lblSoNgayQuaHan = createValueLabel();
        lblTienPhat = createValueLabel();
        lblTrangThai = createValueLabel();

        txtMoTa = new JTextArea();
        txtMoTa.setFont(UITheme.FONT_NORMAL);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setEditable(false);
        txtMoTa.setBackground(UITheme.BG_CARD);
        txtMoTa.setForeground(UITheme.TEXT_NORMAL);
        txtMoTa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(203, 213, 225)));
        txtMoTa.setMaximumSize(new Dimension(320, 80));
        txtMoTa.setPreferredSize(new Dimension(320, 80));
        txtMoTa.setAlignmentX(LEFT_ALIGNMENT);

        ModernButton btnThanhToan = new ModernButton("Xác nhận thanh toán", UITheme.GREEN, UITheme.GREEN_DARK);
        btnThanhToan.setMaximumSize(new Dimension(320, 42));
        btnThanhToan.setPreferredSize(new Dimension(320, 42));
        btnThanhToan.setMinimumSize(new Dimension(120, 42));
        btnThanhToan.setAlignmentX(LEFT_ALIGNMENT);
        btnThanhToan.addActionListener(e -> xacNhanThanhToan());

        content.add(title);
        content.add(Box.createVerticalStrut(16));

        addInfo(content, "Mã phiếu phạt", lblMaPhat);
        addInfo(content, "Mã phiếu mượn", lblMaPhieuMuon);
        addInfo(content, "Độc giả", lblDocGia);
        addInfo(content, "Mã sách", lblMaSach);
        addInfo(content, "Tên sách", lblTenSach);
        addInfo(content, "Loại vi phạm", lblLoaiViPham);
        addInfo(content, "Số ngày quá hạn", lblSoNgayQuaHan);
        addInfo(content, "Số tiền phạt", lblTienPhat);
        addInfo(content, "Trạng thái thanh toán", lblTrangThai);

        JLabel lblMoTa = createLabel("Mô tả vi phạm");
        content.add(lblMoTa);
        content.add(Box.createVerticalStrut(5));
        content.add(txtMoTa);

        content.add(Box.createVerticalStrut(16));
        content.add(btnThanhToan);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        detailCard.add(scroll, BorderLayout.CENTER);

        return detailCard;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(UITheme.TEXT_NORMAL);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createValueLabel() {
        JLabel label = new JLabel("-");
        label.setFont(UITheme.FONT_NORMAL);
        label.setForeground(UITheme.TEXT_DARK);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private void addInfo(JPanel panel, String labelText, JLabel valueLabel) {
        panel.add(createLabel(labelText));
        panel.add(Box.createVerticalStrut(3));
        panel.add(valueLabel);
        panel.add(Box.createVerticalStrut(10));
    }

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

    // =====================================================
    // LOAD DATA
    // =====================================================

    private void taiDanhSachPhieuPhat() {
        DefaultTableModel model = (DefaultTableModel) tblPhieuPhat.getModel();
        model.setRowCount(0);

        try {
            Connection con = DatabaseHelper.getConnection();

            String sql = "SELECT "
                    + "pp.id_phieu_phat, "
                    + "pp.id_phieu_muon, "
                    + "pp.ten_dang_nhap, "
                    + "tk.ho_ten, "
                    + "pp.id_ca_biet, "
                    + "ds.ten_sach, "
                    + "pp.loai_vi_pham, "
                    + "pp.so_ngay_qua_han, "
                    + "pp.so_tien_phat, "
                    + "pp.trang_thai_thanh_toan, "
                    + "pp.ngay_lap, "
                    + "pp.mo_ta_vi_pham "
                    + "FROM PhieuPhat pp "
                    + "JOIN TaiKhoan tk ON pp.ten_dang_nhap = tk.ten_dang_nhap "
                    + "JOIN CuonSach cs ON pp.id_ca_biet = cs.id_ca_biet "
                    + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                    + "ORDER BY pp.id_phieu_phat DESC";

            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_phieu_phat"),
                    rs.getInt("id_phieu_muon"),
                    rs.getString("ten_dang_nhap"),
                    rs.getString("ho_ten"),
                    rs.getString("id_ca_biet"),
                    rs.getString("ten_sach"),
                    rs.getString("loai_vi_pham"),
                    rs.getInt("so_ngay_qua_han"),
                    formatTien(rs.getBigDecimal("so_tien_phat")),
                    rs.getString("trang_thai_thanh_toan"),
                    rs.getTimestamp("ngay_lap"),
                    rs.getString("mo_ta_vi_pham")
                });
            }

            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách phiếu phạt: " + e.getMessage());
        }
    }

    private void timKiemPhieuPhat() {
        String tuKhoa = txtTimKiem.getText().trim();

        if (tuKhoa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblPhieuPhat.getModel();
        model.setRowCount(0);

        try {
            Connection con = DatabaseHelper.getConnection();

            String sql = "SELECT "
                    + "pp.id_phieu_phat, "
                    + "pp.id_phieu_muon, "
                    + "pp.ten_dang_nhap, "
                    + "tk.ho_ten, "
                    + "pp.id_ca_biet, "
                    + "ds.ten_sach, "
                    + "pp.loai_vi_pham, "
                    + "pp.so_ngay_qua_han, "
                    + "pp.so_tien_phat, "
                    + "pp.trang_thai_thanh_toan, "
                    + "pp.ngay_lap, "
                    + "pp.mo_ta_vi_pham "
                    + "FROM PhieuPhat pp "
                    + "JOIN TaiKhoan tk ON pp.ten_dang_nhap = tk.ten_dang_nhap "
                    + "JOIN CuonSach cs ON pp.id_ca_biet = cs.id_ca_biet "
                    + "JOIN DauSach ds ON cs.id_dau_sach = ds.id_dau_sach "
                    + "WHERE pp.ten_dang_nhap LIKE ? "
                    + "OR tk.ho_ten LIKE ? "
                    + "OR pp.id_ca_biet LIKE ? "
                    + "OR ds.ten_sach LIKE ? "
                    + "OR pp.loai_vi_pham LIKE ? "
                    + "OR pp.trang_thai_thanh_toan LIKE ? "
                    + "ORDER BY pp.id_phieu_phat DESC";

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
                    rs.getInt("id_phieu_phat"),
                    rs.getInt("id_phieu_muon"),
                    rs.getString("ten_dang_nhap"),
                    rs.getString("ho_ten"),
                    rs.getString("id_ca_biet"),
                    rs.getString("ten_sach"),
                    rs.getString("loai_vi_pham"),
                    rs.getInt("so_ngay_qua_han"),
                    formatTien(rs.getBigDecimal("so_tien_phat")),
                    rs.getString("trang_thai_thanh_toan"),
                    rs.getTimestamp("ngay_lap"),
                    rs.getString("mo_ta_vi_pham")
                });
            }

            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm phiếu phạt: " + e.getMessage());
        }
    }

    private void doDuLieuLenChiTiet() {
        int row = tblPhieuPhat.getSelectedRow();

        if (row < 0) {
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblPhieuPhat.getModel();

        lblMaPhat.setText(String.valueOf(model.getValueAt(row, 0)));
        lblMaPhieuMuon.setText(String.valueOf(model.getValueAt(row, 1)));

        String tenDangNhap = String.valueOf(model.getValueAt(row, 2));
        String hoTen = String.valueOf(model.getValueAt(row, 3));
        lblDocGia.setText(hoTen + " (" + tenDangNhap + ")");

        lblMaSach.setText(String.valueOf(model.getValueAt(row, 4)));
        lblTenSach.setText(String.valueOf(model.getValueAt(row, 5)));
        lblLoaiViPham.setText(String.valueOf(model.getValueAt(row, 6)));
        lblSoNgayQuaHan.setText(String.valueOf(model.getValueAt(row, 7)) + " ngày");
        lblTienPhat.setText(String.valueOf(model.getValueAt(row, 8)) + " VNĐ");
        lblTrangThai.setText(String.valueOf(model.getValueAt(row, 9)));

        Object moTa = model.getValueAt(row, 11);
        txtMoTa.setText(moTa == null ? "" : String.valueOf(moTa));
    }

    private void xacNhanThanhToan() {
        int row = tblPhieuPhat.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu phạt cần xác nhận thanh toán!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblPhieuPhat.getModel();

        int idPhieuPhat = Integer.parseInt(String.valueOf(model.getValueAt(row, 0)));
        String trangThai = String.valueOf(model.getValueAt(row, 9));

        if (trangThai.equalsIgnoreCase("Đã thanh toán")) {
            JOptionPane.showMessageDialog(this, "Phiếu phạt này đã được thanh toán rồi!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xác nhận đã thu tiền phạt cho phiếu phạt #" + idPhieuPhat + "?",
                "Xác nhận thanh toán",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Connection con = DatabaseHelper.getConnection();

            String sql = "UPDATE PhieuPhat "
                    + "SET trang_thai_thanh_toan = N'Đã thanh toán' "
                    + "WHERE id_phieu_phat = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, idPhieuPhat);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Đã xác nhận thanh toán phiếu phạt!");
                taiDanhSachPhieuPhat();
                lamMoiThongTin();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu phạt cần cập nhật!");
            }

            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xác nhận thanh toán: " + e.getMessage());
        }
    }

    private void lamMoiThongTin() {
        txtTimKiem.setText("");

        lblMaPhat.setText("-");
        lblMaPhieuMuon.setText("-");
        lblDocGia.setText("-");
        lblMaSach.setText("-");
        lblTenSach.setText("-");
        lblLoaiViPham.setText("-");
        lblSoNgayQuaHan.setText("-");
        lblTienPhat.setText("-");
        lblTrangThai.setText("-");
        txtMoTa.setText("");
    }

    private String formatTien(BigDecimal soTien) {
        if (soTien == null) {
            return "0";
        }

        return String.format("%,.0f", soTien.doubleValue());
    }
}