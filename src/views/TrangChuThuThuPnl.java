package views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import utils.UITheme;

public class TrangChuThuThuPnl extends JPanel {
    private MainFrm main;

    private JPanel pnlNoiDung;
    private CardLayout contentLayout;

    private QuanLyDocGiaPnl quanLyDocGiaPnl;

    private JButton btnSach;
    private JButton btnDocGia;
    private JButton btnMuonTra;
    private JButton btnPhieuPhat;
    private JButton btnThongKe;
    private JButton btnDangXuat;

    private JPanel pnlSubDocGia;
    private boolean docGiaExpanded = false;

    private JButton btnDocGiaTatCa;
    private JButton btnDocGiaHoatDong;
    private JButton btnDocGiaChoDuyet;
    private JButton btnDocGiaBiKhoa;

    public TrangChuThuThuPnl(MainFrm mainFrm) {
        this.main = mainFrm;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_MAIN);

        JPanel sidebar = buildSidebar();

        contentLayout = new CardLayout();
        pnlNoiDung = new JPanel(contentLayout);
        pnlNoiDung.setBackground(UITheme.BG_MAIN);
        pnlNoiDung.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));

        quanLyDocGiaPnl = new QuanLyDocGiaPnl();

        pnlNoiDung.add(new QuanLySachPnl(), "cardQuanLySach");
        pnlNoiDung.add(quanLyDocGiaPnl, "cardQuanLyDocGia");
        pnlNoiDung.add(new MuonTraSachPnl(), "cardMuonTraSach");
        pnlNoiDung.add(new QuanLyPhieuPhatPnl(), "cardPhieuPhat");
        pnlNoiDung.add(new ThongKeBaoCaoPnl(), "cardThongKeBaoCao");

        add(sidebar, BorderLayout.WEST);
        add(pnlNoiDung, BorderLayout.CENTER);

        contentLayout.show(pnlNoiDung, "cardQuanLySach");
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new javax.swing.BoxLayout(sidebar, javax.swing.BoxLayout.Y_AXIS));
        sidebar.setBackground(UITheme.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 18, 24, 18));

        JLabel logo = new JLabel("<html><b>📚 Library</b><br><span style='font-size:10px;'>Quản lý thư viện</span></html>");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setAlignmentX(LEFT_ALIGNMENT);

        btnSach = createSidebarButton("  Quản lý kho sách", true);
        btnDocGia = createSidebarButton("  Quản lý độc giả     ▼", false);
        btnMuonTra = createSidebarButton("  Mượn / Trả sách", false);
        btnPhieuPhat = createSidebarButton("  Quản lý phiếu phạt", false);
        btnThongKe = createSidebarButton("  Thống kê báo cáo", false);
        btnDangXuat = createSidebarButton("Đăng xuất", false);
        btnDangXuat.setBackground(UITheme.RED);
        btnDangXuat.setHorizontalAlignment(SwingConstants.CENTER);

        pnlSubDocGia = buildSubDocGiaPanel();
        pnlSubDocGia.setVisible(false);

        btnSach.addActionListener(e -> {
            setActiveMain(btnSach);
            contentLayout.show(pnlNoiDung, "cardQuanLySach");
        });

        btnDocGia.addActionListener(e -> {
            toggleDocGiaMenu();
        });

        btnMuonTra.addActionListener(e -> {
            setActiveMain(btnMuonTra);
            contentLayout.show(pnlNoiDung, "cardMuonTraSach");
        });
        
        btnPhieuPhat.addActionListener(e -> {
            setActiveMain(btnPhieuPhat);
            contentLayout.show(pnlNoiDung, "cardPhieuPhat");
        });
        
        btnThongKe.addActionListener(e -> {
            setActiveMain(btnThongKe);
            contentLayout.show(pnlNoiDung, "cardThongKeBaoCao");
        });

        btnDangXuat.addActionListener(e -> main.moManHinhDangNhapAnToan());

        sidebar.add(logo);
        sidebar.add(javax.swing.Box.createVerticalStrut(35));

        sidebar.add(btnSach);
        sidebar.add(javax.swing.Box.createVerticalStrut(12));

        sidebar.add(btnDocGia);
        sidebar.add(javax.swing.Box.createVerticalStrut(6));
        sidebar.add(pnlSubDocGia);
        sidebar.add(javax.swing.Box.createVerticalStrut(12));

        sidebar.add(btnMuonTra);
        sidebar.add(javax.swing.Box.createVerticalStrut(12));

        sidebar.add(btnPhieuPhat);
        sidebar.add(javax.swing.Box.createVerticalStrut(12));

        sidebar.add(btnThongKe);
        
        sidebar.add(javax.swing.Box.createVerticalGlue());
        sidebar.add(btnDangXuat);

        return sidebar;
    }

    private JPanel buildSubDocGiaPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 176));

        btnDocGiaTatCa = createSubButton("     Tất cả tài khoản");
        btnDocGiaHoatDong = createSubButton("     Đang hoạt động");
        btnDocGiaChoDuyet = createSubButton("     Chờ duyệt & thu cọc");
        btnDocGiaBiKhoa = createSubButton("     Bị khóa");

        btnDocGiaTatCa.addActionListener(e -> moQuanLyDocGia("ALL", btnDocGiaTatCa));
        btnDocGiaHoatDong.addActionListener(e -> moQuanLyDocGia("DANG_HOAT_DONG", btnDocGiaHoatDong));
        btnDocGiaChoDuyet.addActionListener(e -> moQuanLyDocGia("CHO_DUYET", btnDocGiaChoDuyet));
        btnDocGiaBiKhoa.addActionListener(e -> moQuanLyDocGia("BI_KHOA", btnDocGiaBiKhoa));

        panel.add(btnDocGiaTatCa);
        panel.add(javax.swing.Box.createVerticalStrut(6));
        panel.add(btnDocGiaHoatDong);
        panel.add(javax.swing.Box.createVerticalStrut(6));
        panel.add(btnDocGiaChoDuyet);
        panel.add(javax.swing.Box.createVerticalStrut(6));
        panel.add(btnDocGiaBiKhoa);

        return panel;
    }

    private JButton createSidebarButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setPreferredSize(new Dimension(205, 48));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setBackground(active ? UITheme.SIDEBAR_ACTIVE : UITheme.SIDEBAR);
        btn.setAlignmentX(LEFT_ALIGNMENT);
        return btn;
    }

    private JButton createSubButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setPreferredSize(new Dimension(205, 38));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(226, 232, 240));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setBackground(new Color(30, 41, 59));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        return btn;
    }

    private void toggleDocGiaMenu() {
        docGiaExpanded = !docGiaExpanded;
        pnlSubDocGia.setVisible(docGiaExpanded);

        if (docGiaExpanded) {
            btnDocGia.setText("  Quản lý độc giả     ▲");
            setActiveMain(btnDocGia);
        } else {
            btnDocGia.setText("  Quản lý độc giả     ▼");
        }

        revalidate();
        repaint();
    }

    private void moQuanLyDocGia(String cheDo, JButton selectedSubButton) {
        setActiveMain(btnDocGia);
        setActiveSub(selectedSubButton);

        quanLyDocGiaPnl.setCheDo(cheDo);
        contentLayout.show(pnlNoiDung, "cardQuanLyDocGia");
    }

    private void setActiveMain(JButton selected) {
        btnSach.setBackground(UITheme.SIDEBAR);
        btnDocGia.setBackground(UITheme.SIDEBAR);
        btnMuonTra.setBackground(UITheme.SIDEBAR);
        btnPhieuPhat.setBackground(UITheme.SIDEBAR);
        btnThongKe.setBackground(UITheme.SIDEBAR);

        selected.setBackground(UITheme.SIDEBAR_ACTIVE);
    }

    private void setActiveSub(JButton selected) {
        btnDocGiaTatCa.setBackground(new Color(30, 41, 59));
        btnDocGiaHoatDong.setBackground(new Color(30, 41, 59));
        btnDocGiaChoDuyet.setBackground(new Color(30, 41, 59));
        btnDocGiaBiKhoa.setBackground(new Color(30, 41, 59));

        selected.setBackground(UITheme.PRIMARY_DARK);
    }
}


// hihi haha