package views;

import components.ModernButton;
import components.ModernTextField;
import components.RoundedPanel;
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

    private String layDieuKienCheDo() {
        if ("DANG_HOAT_DONG".equals(cheDo)) {
            return " AND tk.trang_thai_tai_khoan = N'Đang hoạt động' ";
        }

        if ("CHO_DUYET".equals(cheDo)) {
            return " AND tk.trang_thai_tai_khoan = N'Chờ duyệt' "
                    + " AND tk.vai_tro = N'Độc giả ngoài' ";
        }

        if ("BI_KHOA".equals(cheDo)) {
            return " AND tk.trang_thai_tai_khoan = N'Bị khóa' ";
        }

        return "";
    }

    private void taiDuLieuDocGia() {
        DefaultTableModel model = (DefaultTableModel) tblDocGia.getModel();
        model.setRowCount(0);

        try {
            java.sql.Connection con = utils.DatabaseHelper.getConnection();

            String sql = "SELECT "
                    + "tk.ten_dang_nhap, "
                    + "tk.ho_ten, "
                    + "tk.so_dien_thoai, "
                    + "tk.vai_tro, "
                    + "tk.trang_thai_tai_khoan, "
                    + "dgn.tien_coc, "
                    + "dgn.ngay_dang_ky, "
                    + "dgn.ngay_kich_hoat "
                    + "FROM TaiKhoan tk "
                    + "LEFT JOIN DocGiaNgoai dgn ON tk.ten_dang_nhap = dgn.ten_dang_nhap "
                    + "WHERE tk.vai_tro <> N'Thủ thư' "
                    + layDieuKienCheDo()
                    + "ORDER BY tk.vai_tro, tk.trang_thai_tai_khoan, tk.ho_ten";

            java.sql.PreparedStatement pstmt = con.prepareStatement(sql);
            java.sql.ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("ten_dang_nhap"),
                    rs.getString("ho_ten"),
                    rs.getString("so_dien_thoai"),
                    rs.getString("vai_tro"),
                    rs.getString("trang_thai_tai_khoan"),
                    rs.getBigDecimal("tien_coc"),
                    rs.getTimestamp("ngay_dang_ky"),
                    rs.getTimestamp("ngay_kich_hoat")
                });
            }

            rs.close();
            pstmt.close();
            con.close();

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

        DefaultTableModel model = (DefaultTableModel) tblDocGia.getModel();
        model.setRowCount(0);

        try {
            java.sql.Connection con = utils.DatabaseHelper.getConnection();

            String sql = "SELECT "
                    + "tk.ten_dang_nhap, "
                    + "tk.ho_ten, "
                    + "tk.so_dien_thoai, "
                    + "tk.vai_tro, "
                    + "tk.trang_thai_tai_khoan, "
                    + "dgn.tien_coc, "
                    + "dgn.ngay_dang_ky, "
                    + "dgn.ngay_kich_hoat "
                    + "FROM TaiKhoan tk "
                    + "LEFT JOIN DocGiaNgoai dgn ON tk.ten_dang_nhap = dgn.ten_dang_nhap "
                    + "WHERE tk.vai_tro <> N'Thủ thư' "
                    + layDieuKienCheDo()
                    + "AND (tk.ten_dang_nhap LIKE ? "
                    + "OR tk.ho_ten LIKE ? "
                    + "OR tk.so_dien_thoai LIKE ? "
                    + "OR tk.vai_tro LIKE ? "
                    + "OR tk.trang_thai_tai_khoan LIKE ?) "
                    + "ORDER BY tk.vai_tro, tk.trang_thai_tai_khoan, tk.ho_ten";

            java.sql.PreparedStatement pstmt = con.prepareStatement(sql);

            String keyword = "%" + tuKhoa + "%";
            pstmt.setString(1, keyword);
            pstmt.setString(2, keyword);
            pstmt.setString(3, keyword);
            pstmt.setString(4, keyword);
            pstmt.setString(5, keyword);

            java.sql.ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("ten_dang_nhap"),
                    rs.getString("ho_ten"),
                    rs.getString("so_dien_thoai"),
                    rs.getString("vai_tro"),
                    rs.getString("trang_thai_tai_khoan"),
                    rs.getBigDecimal("tien_coc"),
                    rs.getTimestamp("ngay_dang_ky"),
                    rs.getTimestamp("ngay_kich_hoat")
                });
            }

            rs.close();
            pstmt.close();
            con.close();

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

        if (!vaiTro.equalsIgnoreCase("Độc giả ngoài")) {
            JOptionPane.showMessageDialog(this, "Chỉ tài khoản Độc giả ngoài mới cần duyệt và thu cọc!");
            return;
        }

        if (!trangThai.equalsIgnoreCase("Chờ duyệt")) {
            JOptionPane.showMessageDialog(this, "Chỉ có thể duyệt tài khoản đang ở trạng thái Chờ duyệt!");
            return;
        }

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
            tienCoc = new BigDecimal(input);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Số tiền cọc không hợp lệ!");
            return;
        }

        if (tienCoc.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this, "Tiền cọc phải lớn hơn 0!");
            return;
        }

        java.sql.Connection con = null;

        try {
            con = utils.DatabaseHelper.getConnection();
            con.setAutoCommit(false);

            String updateTaiKhoanSql = "UPDATE TaiKhoan "
                    + "SET trang_thai_tai_khoan = N'Đang hoạt động' "
                    + "WHERE ten_dang_nhap = ?";

            java.sql.PreparedStatement updateTaiKhoanStmt = con.prepareStatement(updateTaiKhoanSql);
            updateTaiKhoanStmt.setString(1, tenDangNhap);

            int resultTaiKhoan = updateTaiKhoanStmt.executeUpdate();

            String updateDocGiaNgoaiSql = "UPDATE DocGiaNgoai "
                    + "SET tien_coc = ?, ngay_kich_hoat = GETDATE(), ghi_chu = ? "
                    + "WHERE ten_dang_nhap = ?";

            java.sql.PreparedStatement updateDocGiaNgoaiStmt = con.prepareStatement(updateDocGiaNgoaiSql);
            updateDocGiaNgoaiStmt.setBigDecimal(1, tienCoc);
            updateDocGiaNgoaiStmt.setString(2, "Đã thu cọc và kích hoạt tài khoản");
            updateDocGiaNgoaiStmt.setString(3, tenDangNhap);

            int resultDocGiaNgoai = updateDocGiaNgoaiStmt.executeUpdate();

            if (resultTaiKhoan > 0 && resultDocGiaNgoai > 0) {
                con.commit();

                JOptionPane.showMessageDialog(
                        this,
                        "Duyệt tài khoản thành công!\n"
                        + "Đã thu cọc: " + tienCoc + " VNĐ"
                );

                taiDuLieuDocGia();
            } else {
                con.rollback();
                JOptionPane.showMessageDialog(this, "Duyệt thất bại! Không tìm thấy đủ dữ liệu tài khoản.");
            }

            updateDocGiaNgoaiStmt.close();
            updateTaiKhoanStmt.close();

            con.setAutoCommit(true);
            con.close();

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

            JOptionPane.showMessageDialog(this, "Lỗi khi duyệt tài khoản: " + e.getMessage());
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

        if (trangThaiHienTai.equalsIgnoreCase(trangThaiMoi)) {
            JOptionPane.showMessageDialog(this, "Tài khoản đã ở trạng thái: " + trangThaiMoi);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn cập nhật tài khoản này thành: " + trangThaiMoi + "?",
                "Xác nhận cập nhật",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            java.sql.Connection con = utils.DatabaseHelper.getConnection();

            String sql = "UPDATE TaiKhoan "
                    + "SET trang_thai_tai_khoan = ? "
                    + "WHERE ten_dang_nhap = ?";

            java.sql.PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, trangThaiMoi);
            pstmt.setString(2, tenDangNhap);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật trạng thái tài khoản thành công!");
                taiDuLieuDocGia();
            }

            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái: " + e.getMessage());
        }
    }
}