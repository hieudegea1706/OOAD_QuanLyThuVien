/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

import components.ModernButton;
import components.RoundedPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utils.UITheme;

public class TrangChuDocGiaPnl extends JPanel {
    private MainFrm main;

    public TrangChuDocGiaPnl(MainFrm mainFrm) {
        this.main = mainFrm;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_MAIN);

        JPanel root = new JPanel(new GridBagLayout());
        root.setOpaque(false);
        root.setBorder(javax.swing.BorderFactory.createEmptyBorder(35, 35, 35, 35));

        RoundedPanel card = new RoundedPanel(32, UITheme.BG_CARD);
        card.setLayout(new GridBagLayout());
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel icon = new JLabel("👋", JLabel.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 70));

        JLabel title = new JLabel("Chào mừng độc giả", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel subtitle = new JLabel(
                "<html><div style='text-align:center;'>Khu vực độc giả sẽ phát triển các chức năng tra cứu sách, lịch sử mượn trả và thông tin tài khoản.</div></html>",
                JLabel.CENTER
        );
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);

        ModernButton btnDangXuat = new ModernButton("Đăng xuất", UITheme.RED, UITheme.RED_DARK);
        btnDangXuat.addActionListener(e -> main.moManHinhDangNhapAnToan());

        gbc.gridy = 0;
        card.add(icon, gbc);

        gbc.gridy = 1;
        card.add(title, gbc);

        gbc.gridy = 2;
        card.add(subtitle, gbc);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(25, 0, 10, 0);
        card.add(btnDangXuat, gbc);

        GridBagConstraints rootGbc = new GridBagConstraints();
        rootGbc.gridx = 0;
        rootGbc.gridy = 0;
        rootGbc.weightx = 1;
        rootGbc.weighty = 1;

        card.setPreferredSize(new java.awt.Dimension(560, 420));
        root.add(card, rootGbc);

        add(root, BorderLayout.CENTER);
    }
}
