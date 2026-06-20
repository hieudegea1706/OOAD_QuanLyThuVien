package views;

import components.ModernButton;
import components.ModernPasswordField;
import components.ModernTextField;
import components.RoundedPanel;
import controllers.AuthController;
import dto.KetQuaDangNhap;
import utils.AppSession;
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
    private final AuthController authController = new AuthController();

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
    String username = txtTenDangNhap.getText().trim();
    String password = new String(txtMatKhau.getPassword());

    KetQuaDangNhap ketQua = authController.dangNhap(username, password);

    if (!ketQua.isThanhCong()) {
        JOptionPane.showMessageDialog(this, ketQua.getThongBao());
        return;
    }

    AppSession.dangNhap(
            ketQua.getTenDangNhap(),
            ketQua.getHoTen(),
            ketQua.getVaiTro()
    );

    JOptionPane.showMessageDialog(this,
            "Xin chào " + ketQua.getHoTen() + "!\nVai trò: " + ketQua.getVaiTro()
    );

    if ("Thủ thư".equalsIgnoreCase(ketQua.getVaiTro())) {
        main.chuyenManHinh("cardTrangChuThuThu");

    } else if ("Sinh viên".equalsIgnoreCase(ketQua.getVaiTro())
            || "Độc giả ngoài".equalsIgnoreCase(ketQua.getVaiTro())) {
        main.chuyenManHinh("cardTrangChuDocGia");

    } else {
        JOptionPane.showMessageDialog(this,
                "Vai trò tài khoản chưa được hỗ trợ: " + ketQua.getVaiTro()
        );
    }
}

    public void xoaTrangDuLieu() {
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
    }
}