/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

import components.ModernButton;
import components.RoundedPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utils.UITheme;

public class ChaoMungPnl extends JPanel {
    private MainFrm main;

    public ChaoMungPnl(MainFrm mainFrm) {
        this.main = mainFrm;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_MAIN);

        JPanel root = new JPanel(new BorderLayout(45, 0));
        root.setOpaque(false);
        root.setBorder(javax.swing.BorderFactory.createEmptyBorder(50, 70, 50, 70));

        JPanel left = buildLeftPanel();
        JPanel right = buildRightPanel();

        root.add(left, BorderLayout.WEST);
        root.add(right, BorderLayout.CENTER);

        add(root, BorderLayout.CENTER);
    }

    private JPanel buildLeftPanel() {
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        left.setPreferredSize(new Dimension(610, 620));
        left.setMinimumSize(new Dimension(520, 520));

        JLabel badge = new JLabel("  HUST LIBRARY SYSTEM  ");
        badge.setOpaque(true);
        badge.setBackground(UITheme.PRIMARY_LIGHT);
        badge.setForeground(UITheme.PRIMARY_DARK);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 16));
        badge.setAlignmentX(LEFT_ALIGNMENT);
        badge.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 21, 10, 21));
        badge.setPreferredSize(new Dimension(300, 42));

        JLabel title1 = new JLabel("Hệ thống");
        title1.setFont(new Font("Segoe UI", Font.BOLD, 54));
        title1.setForeground(UITheme.TEXT_DARK);
        title1.setAlignmentX(LEFT_ALIGNMENT);

        JLabel title2 = new JLabel("quản lý thư viện");
        title2.setFont(new Font("Segoe UI", Font.BOLD, 54));
        title2.setForeground(UITheme.TEXT_DARK);
        title2.setAlignmentX(LEFT_ALIGNMENT);

        JLabel title3 = new JLabel("Tạ Quang Bửu");
        title3.setFont(new Font("Segoe UI", Font.BOLD, 54));
        title3.setForeground(UITheme.PRIMARY);
        title3.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "<html><div style='width:520px;'>"
                + "Ứng dụng hỗ trợ quản lý sách, độc giả, mượn trả, tài khoản "
                + "và báo cáo theo hướng trực quan, hiện đại và dễ sử dụng."
                + "</div></html>"
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitle.setForeground(UITheme.TEXT_NORMAL);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 14, 0));
        buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(560, 60));

        ModernButton btnLogin = new ModernButton(
                "Đăng nhập / Kích hoạt",
                UITheme.PRIMARY,
                UITheme.PRIMARY_DARK
        );
        btnLogin.setPreferredSize(new Dimension(200, 50));
        btnLogin.addActionListener(e -> main.chuyenManHinh("cardDangNhap"));

        ModernButton btnRegister = new ModernButton(
                "       Đăng ký       ",
                UITheme.PURPLE,
                UITheme.PURPLE_DARK
        );
        btnRegister.setPreferredSize(new Dimension(200, 48));
        btnRegister.addActionListener(e -> main.chuyenManHinh("cardDangKy"));

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);

        left.add(Box.createVerticalGlue());
        left.add(badge);
        left.add(Box.createVerticalStrut(35));
        left.add(title1);
        left.add(Box.createVerticalStrut(2));
        left.add(title2);
        left.add(Box.createVerticalStrut(2));
        left.add(title3);
        left.add(Box.createVerticalStrut(28));
        left.add(subtitle);
        left.add(Box.createVerticalStrut(34));
        left.add(buttonPanel);
        left.add(Box.createVerticalGlue());

        return left;
    }

    private JPanel buildRightPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        RoundedPanel card = new RoundedPanel(36, UITheme.PRIMARY);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(80, 50, 80, 50));

        JLabel icon = new JLabel("📚");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 88));
        icon.setForeground(Color.WHITE);
        icon.setAlignmentX(CENTER_ALIGNMENT);

        JLabel rightTitle = new JLabel("Library Management");
        rightTitle.setForeground(Color.WHITE);
        rightTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        rightTitle.setAlignmentX(CENTER_ALIGNMENT);

        JLabel rightText = new JLabel(
                "<html><div style='text-align:center; width:480px;'>"
                + "Tra cứu nhanh • Quản lý sách • Mượn trả • Báo cáo"
                + "</div></html>"
        );
        rightText.setForeground(new Color(226, 232, 240));
        rightText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rightText.setAlignmentX(CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(icon);
        card.add(Box.createVerticalStrut(28));
        card.add(rightTitle);
        card.add(Box.createVerticalStrut(18));
        card.add(rightText);
        card.add(Box.createVerticalGlue());

        wrapper.add(card, BorderLayout.CENTER);

        return wrapper;
    }
}
