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

public class DangNhapPnl extends JPanel {
    private MainFrm main;

    private ModernTextField txtTenDangNhap;
    private ModernPasswordField txtMatKhau;

    public DangNhapPnl(MainFrm mainFrm) {
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
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(32, 42, 32, 42));
        card.setPreferredSize(new Dimension(500, 590));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel badge = new JLabel("LOGIN");
        badge.setOpaque(true);
        badge.setBackground(UITheme.PRIMARY);
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 15));
        badge.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 18, 8, 18));
        badge.setAlignmentX(CENTER_ALIGNMENT);

        JLabel title = new JLabel("Đăng nhập hệ thống");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(UITheme.TEXT_DARK);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Nhập tài khoản để tiếp tục sử dụng hệ thống");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        txtTenDangNhap = new ModernTextField();
        txtMatKhau = new ModernPasswordField();

        setFieldSize(txtTenDangNhap);
        setFieldSize(txtMatKhau);

        ModernButton btnDangNhap = new ModernButton("Đăng nhập", UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        btnDangNhap.setMaximumSize(new Dimension(330, 46));
        btnDangNhap.setPreferredSize(new Dimension(330, 46));
        btnDangNhap.setMinimumSize(new Dimension(330, 46));
        btnDangNhap.setAlignmentX(LEFT_ALIGNMENT);
        btnDangNhap.addActionListener(e -> xuLyDangNhap());

        ModernButton btnDangKy = new ModernButton("Đăng ký tài khoản", UITheme.PURPLE, UITheme.PURPLE_DARK);
        btnDangKy.setMaximumSize(new Dimension(330, 46));
        btnDangKy.setPreferredSize(new Dimension(330, 46));
        btnDangKy.setMinimumSize(new Dimension(330, 46));
        btnDangKy.setAlignmentX(LEFT_ALIGNMENT);
        btnDangKy.addActionListener(e -> main.chuyenManHinh("cardDangKy"));

        ModernButton btnQuayLai = new ModernButton("Quay lại", UITheme.CYAN, UITheme.CYAN_DARK);
        btnQuayLai.setMaximumSize(new Dimension(330, 46));
        btnQuayLai.setPreferredSize(new Dimension(330, 46));
        btnQuayLai.setMinimumSize(new Dimension(330, 46));
        btnQuayLai.setAlignmentX(LEFT_ALIGNMENT);
        btnQuayLai.addActionListener(e -> main.chuyenManHinh("cardChaoMung"));

        content.add(badge);
        content.add(Box.createVerticalStrut(24));
        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(34));

        JPanel formBox = new JPanel();
        formBox.setOpaque(false);
        formBox.setLayout(new BoxLayout(formBox, BoxLayout.Y_AXIS));
        formBox.setMaximumSize(new Dimension(330, 360));
        formBox.setPreferredSize(new Dimension(330, 360));
        formBox.setAlignmentX(CENTER_ALIGNMENT);

        addField(formBox, "Tên đăng nhập", txtTenDangNhap);
        addField(formBox, "Mật khẩu", txtMatKhau);

        formBox.add(Box.createVerticalStrut(18));
        formBox.add(btnDangNhap);
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
        field.setMaximumSize(new Dimension(330, 42));
        field.setPreferredSize(new Dimension(330, 42));
        field.setMinimumSize(new Dimension(330, 42));
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
        content.add(Box.createVerticalStrut(14));
    }

    private void xuLyDangNhap() {
    try {
        String username = txtTenDangNhap.getText().trim();
        String password = new String(txtMatKhau.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        java.sql.Connection con = utils.DatabaseHelper.getConnection();

        String sql = "SELECT * FROM TaiKhoan WHERE ten_dang_nhap = ? AND mat_khau = ?";
        java.sql.PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);

        java.sql.ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String trangThai = rs.getString("trang_thai_tai_khoan");
            String vaiTro = rs.getString("vai_tro");
            String hoTen = rs.getString("ho_ten");

            // 1. Tài khoản bị khóa
            if (trangThai.equalsIgnoreCase("Bị khóa")) {
                JOptionPane.showMessageDialog(this,
                        "Tài khoản của bạn đã bị khóa!\nVui lòng liên hệ Thư viện.",
                        "Từ chối truy cập",
                        JOptionPane.ERROR_MESSAGE
                );

                rs.close();
                pstmt.close();
                con.close();
                return;
            }

            // 2. Độc giả ngoài đang chờ thủ thư duyệt
            if (trangThai.equalsIgnoreCase("Chờ duyệt")) {
                JOptionPane.showMessageDialog(this,
                        "Tài khoản đang CHỜ DUYỆT!\n"
                        + "Vui lòng mang CCCD và tiền cọc đến quầy Thủ thư để kích hoạt.",
                        "Từ chối truy cập",
                        JOptionPane.WARNING_MESSAGE
                );

                rs.close();
                pstmt.close();
                con.close();
                return;
            }

            // 3. Sinh viên nội bộ đăng nhập lần đầu, cần kích hoạt
            if (trangThai.equalsIgnoreCase("Chưa kích hoạt")) {

                if (!vaiTro.equalsIgnoreCase("Sinh viên")) {
                    JOptionPane.showMessageDialog(this,
                            "Tài khoản chưa kích hoạt nhưng không phải tài khoản sinh viên.\n"
                            + "Vui lòng liên hệ Thủ thư để kiểm tra.",
                            "Không thể kích hoạt tự động",
                            JOptionPane.WARNING_MESSAGE
                    );

                    rs.close();
                    pstmt.close();
                    con.close();
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Tài khoản sinh viên này chưa được kích hoạt.\n"
                        + "Bạn có muốn kích hoạt tài khoản thư viện ngay bây giờ không?",
                        "Kích hoạt tài khoản sinh viên",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm != JOptionPane.YES_OPTION) {
                    rs.close();
                    pstmt.close();
                    con.close();
                    return;
                }

                String updateSql = "UPDATE TaiKhoan "
                        + "SET trang_thai_tai_khoan = N'Đang hoạt động' "
                        + "WHERE ten_dang_nhap = ?";

                java.sql.PreparedStatement updateStmt = con.prepareStatement(updateSql);
                updateStmt.setString(1, username);

                int updated = updateStmt.executeUpdate();
                updateStmt.close();

                if (updated > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Kích hoạt tài khoản thành công!\n"
                            + "Xin chào " + hoTen + "!"
                    );

                    main.chuyenManHinh("cardTrangChuDocGia");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Kích hoạt thất bại, vui lòng thử lại!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

                rs.close();
                pstmt.close();
                con.close();
                return;
            }

            // 4. Tài khoản đang hoạt động
            if (trangThai.equalsIgnoreCase("Đang hoạt động")) {
                JOptionPane.showMessageDialog(this,
                        "Xin chào " + hoTen + "!\nVai trò: " + vaiTro
                );

                if (vaiTro.equalsIgnoreCase("Thủ thư")) {
                    main.chuyenManHinh("cardTrangChuThuThu");
                } else if (vaiTro.equalsIgnoreCase("Sinh viên") || vaiTro.equalsIgnoreCase("Độc giả ngoài")) {
                    main.chuyenManHinh("cardTrangChuDocGia");
                } else {
                    JOptionPane.showMessageDialog(this, "Vai trò tài khoản chưa được hỗ trợ: " + vaiTro);
                }

                rs.close();
                pstmt.close();
                con.close();
                return;
            }

            // 5. Trạng thái lạ
            JOptionPane.showMessageDialog(this,
                    "Trạng thái tài khoản không hợp lệ: " + trangThai,
                    "Lỗi trạng thái",
                    JOptionPane.WARNING_MESSAGE
            );

        } else {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
        }

        rs.close();
        pstmt.close();
        con.close();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
    }
}

    public void xoaTrangDuLieu() {
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
    }
}