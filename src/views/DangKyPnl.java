package views;

import components.ModernButton;
import components.ModernPasswordField;
import components.ModernTextField;
import components.RoundedPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import utils.UITheme;

public class DangKyPnl extends JPanel {
    private MainFrm main;

    private ModernTextField txtHoTen;
    private ModernTextField txtCCCD;
    private ModernTextField txtSDT;
    private ModernPasswordField txtMatKhau;
    private ModernPasswordField txtXacNhanMatKhau;

    public DangKyPnl(MainFrm mainFrm) {
        this.main = mainFrm;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_MAIN);

        JPanel root = new JPanel(new java.awt.GridBagLayout());
        root.setOpaque(false);
        root.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 24, 24, 24));

        RoundedPanel card = new RoundedPanel(34, UITheme.BG_CARD);
        card.setLayout(new BorderLayout());
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(26, 42, 26, 42));
        card.setPreferredSize(new Dimension(560, 720));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel badge = new JLabel("REGISTER");
        badge.setOpaque(true);
        badge.setBackground(UITheme.PURPLE);
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 15));
        badge.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 18, 8, 18));
        badge.setAlignmentX(CENTER_ALIGNMENT);

        JLabel title = new JLabel("Đăng ký độc giả ngoài");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(UITheme.TEXT_DARK);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sau khi đăng ký, tài khoản sẽ chờ thủ thư duyệt");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        txtHoTen = new ModernTextField();
        txtCCCD = new ModernTextField();
        txtSDT = new ModernTextField();
        txtMatKhau = new ModernPasswordField();
        txtXacNhanMatKhau = new ModernPasswordField();

        setFieldSize(txtHoTen);
        setFieldSize(txtCCCD);
        setFieldSize(txtSDT);
        setFieldSize(txtMatKhau);
        setFieldSize(txtXacNhanMatKhau);

        ModernButton btnDangKy = new ModernButton("Tạo tài khoản", UITheme.GREEN, UITheme.GREEN_DARK);
        btnDangKy.setMaximumSize(new Dimension(430, 46));
        btnDangKy.setPreferredSize(new Dimension(430, 46));
        btnDangKy.setMinimumSize(new Dimension(430, 46));
        btnDangKy.setAlignmentX(LEFT_ALIGNMENT);
        btnDangKy.addActionListener(e -> xuLyDangKy());

        ModernButton btnQuayLai = new ModernButton("Quay lại đăng nhập", UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        btnQuayLai.setMaximumSize(new Dimension(430, 46));
        btnQuayLai.setPreferredSize(new Dimension(430, 46));
        btnQuayLai.setMinimumSize(new Dimension(430, 46));
        btnQuayLai.setAlignmentX(LEFT_ALIGNMENT);
        btnQuayLai.addActionListener(e -> main.moManHinhDangNhapAnToan());

        content.add(badge);
        content.add(Box.createVerticalStrut(18));
        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(22));

        JPanel formBox = new JPanel();
        formBox.setOpaque(false);
        formBox.setLayout(new BoxLayout(formBox, BoxLayout.Y_AXIS));
        formBox.setMaximumSize(new Dimension(430, 540));
        formBox.setPreferredSize(new Dimension(430, 540));
        formBox.setAlignmentX(CENTER_ALIGNMENT);
        
        addField(formBox, "Họ và tên", txtHoTen);
        addField(formBox, "Số CCCD / Tên đăng nhập", txtCCCD);
        addField(formBox, "Số điện thoại", txtSDT);
        addField(formBox, "Mật khẩu", txtMatKhau);
        addField(formBox, "Xác nhận mật khẩu", txtXacNhanMatKhau);

        formBox.add(Box.createVerticalStrut(12));
        formBox.add(btnDangKy);
        formBox.add(Box.createVerticalStrut(12));
        formBox.add(btnQuayLai);

        content.add(formBox);

        card.add(content, BorderLayout.CENTER);
        root.add(card);

        add(root, BorderLayout.CENTER);
    }

    private void setFieldSize(javax.swing.JComponent field) {
        field.setMaximumSize(new Dimension(430, 42));
        field.setPreferredSize(new Dimension(430, 42));
        field.setMinimumSize(new Dimension(430, 42));
        field.setAlignmentX(LEFT_ALIGNMENT);
    }

    private void addField(JPanel content, String labelText, javax.swing.JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(UITheme.FONT_BOLD);
        label.setForeground(UITheme.TEXT_NORMAL);
        label.setAlignmentX(LEFT_ALIGNMENT);

        content.add(label);
        content.add(Box.createVerticalStrut(5));
        content.add(field);
        content.add(Box.createVerticalStrut(11));
    }

    private void xuLyDangKy() {
    java.sql.Connection con = null;

    try {
        String hoTen = txtHoTen.getText().trim();
        String cccd = txtCCCD.getText().trim();
        String sdt = txtSDT.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());
        String xacNhanMK = new String(txtXacNhanMatKhau.getPassword());

        if (hoTen.isEmpty() || cccd.isEmpty() || sdt.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!matKhau.equals(xacNhanMK)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không trùng khớp!");
            return;
        }

        con = utils.DatabaseHelper.getConnection();

        // Kiểm tra trùng tài khoản trong TaiKhoan
        String checkSql = "SELECT * FROM TaiKhoan WHERE ten_dang_nhap = ?";
        java.sql.PreparedStatement checkStmt = con.prepareStatement(checkSql);
        checkStmt.setString(1, cccd);

        java.sql.ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Số CCCD này đã được đăng ký trong hệ thống!");
            rs.close();
            checkStmt.close();
            con.close();
            return;
        }

        rs.close();
        checkStmt.close();

        // Bắt đầu transaction
        con.setAutoCommit(false);

        // 1. Insert vào TaiKhoan
        String insertTaiKhoanSql = "INSERT INTO TaiKhoan "
                + "(ten_dang_nhap, mat_khau, ho_ten, cccd, so_dien_thoai, vai_tro, trang_thai_tai_khoan) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        java.sql.PreparedStatement insertTaiKhoanStmt = con.prepareStatement(insertTaiKhoanSql);

        insertTaiKhoanStmt.setString(1, cccd);
        insertTaiKhoanStmt.setString(2, matKhau);
        insertTaiKhoanStmt.setString(3, hoTen);
        insertTaiKhoanStmt.setString(4, cccd);
        insertTaiKhoanStmt.setString(5, sdt);
        insertTaiKhoanStmt.setString(6, "Độc giả ngoài");
        insertTaiKhoanStmt.setString(7, "Chờ duyệt");

        int resultTaiKhoan = insertTaiKhoanStmt.executeUpdate();

        // 2. Insert vào DocGiaNgoai
        String insertDocGiaNgoaiSql = "INSERT INTO DocGiaNgoai "
                + "(ten_dang_nhap, cccd, so_dien_thoai, tien_coc, ghi_chu) "
                + "VALUES (?, ?, ?, ?, ?)";

        java.sql.PreparedStatement insertDocGiaNgoaiStmt = con.prepareStatement(insertDocGiaNgoaiSql);

        insertDocGiaNgoaiStmt.setString(1, cccd);
        insertDocGiaNgoaiStmt.setString(2, cccd);
        insertDocGiaNgoaiStmt.setString(3, sdt);
        insertDocGiaNgoaiStmt.setBigDecimal(4, new java.math.BigDecimal("0"));
        insertDocGiaNgoaiStmt.setString(5, "Đăng ký online - chờ thủ thư duyệt và thu cọc");

        int resultDocGiaNgoai = insertDocGiaNgoaiStmt.executeUpdate();

        if (resultTaiKhoan > 0 && resultDocGiaNgoai > 0) {
            con.commit();

            JOptionPane.showMessageDialog(this,
                    "Đăng ký thành công!\n"
                    + "Trạng thái: CHỜ DUYỆT.\n"
                    + "Vui lòng mang CCCD và tiền cọc đến quầy Thủ thư để kích hoạt tài khoản."
            );

            xoaForm();
            main.moManHinhDangNhapAnToan();
        } else {
            con.rollback();
            JOptionPane.showMessageDialog(this, "Đăng ký thất bại, dữ liệu chưa được lưu!");
        }

        insertDocGiaNgoaiStmt.close();
        insertTaiKhoanStmt.close();
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

        JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi trong quá trình đăng ký: " + e.getMessage());
    }
}

    private void xoaForm() {
        txtHoTen.setText("");
        txtCCCD.setText("");
        txtSDT.setText("");
        txtMatKhau.setText("");
        txtXacNhanMatKhau.setText("");
    }
}