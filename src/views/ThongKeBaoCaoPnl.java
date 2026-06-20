package views;

import components.ModernButton;
import components.RoundedPanel;
import controllers.ThongKeBaoCaoController;
import dto.SachQuaHanDTO;
import dto.TongQuanThongKeDTO;
import dto.TopSachMuonDTO;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
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

public class ThongKeBaoCaoPnl extends JPanel {
    
    private final ThongKeBaoCaoController thongKeBaoCaoController = new ThongKeBaoCaoController();

    private JLabel lblTongDauSach;
    private JLabel lblTongCuonSach;
    private JLabel lblSachSanSang;
    private JLabel lblSachDangMuon;
    private JLabel lblPhieuDangMuon;
    private JLabel lblPhieuPhatChuaThanhToan;
    private JLabel lblTienPhatChuaThu;
    private JLabel lblTienCocDangGiu;

    private JTable tblQuaHan;
    private JTable tblTopSach;

    public ThongKeBaoCaoPnl() {
        buildUI();
        taiDuLieuThongKe();
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
        topCard.setLayout(new BorderLayout());
        topCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Thống kê báo cáo");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel subtitle = new JLabel("Tổng quan tình trạng kho sách, mượn trả, vi phạm và tài chính thư viện");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        ModernButton btnLamMoi = new ModernButton("Làm mới", UITheme.CYAN, UITheme.CYAN_DARK);
        btnLamMoi.setPreferredSize(new Dimension(120, 42));
        btnLamMoi.addActionListener(e -> taiDuLieuThongKe());

        topCard.add(titleBox, BorderLayout.CENTER);
        topCard.add(btnLamMoi, BorderLayout.EAST);

        return topCard;
    }

    private JPanel buildCenterPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setOpaque(false);

        root.add(buildCardGridPanel(), BorderLayout.NORTH);
        root.add(buildTableAreaPanel(), BorderLayout.CENTER);

        return root;
    }

    private JPanel buildCardGridPanel() {
        JPanel grid = new JPanel(new java.awt.GridLayout(2, 4, 14, 14));
        grid.setOpaque(false);

        lblTongDauSach = createNumberLabel();
        lblTongCuonSach = createNumberLabel();
        lblSachSanSang = createNumberLabel();
        lblSachDangMuon = createNumberLabel();
        lblPhieuDangMuon = createNumberLabel();
        lblPhieuPhatChuaThanhToan = createNumberLabel();
        lblTienPhatChuaThu = createNumberLabel();
        lblTienCocDangGiu = createNumberLabel();

        grid.add(createStatCard("Tổng đầu sách", lblTongDauSach, UITheme.PRIMARY));
        grid.add(createStatCard("Tổng cuốn sách", lblTongCuonSach, UITheme.PURPLE));
        grid.add(createStatCard("Sách sẵn sàng", lblSachSanSang, UITheme.GREEN));
        grid.add(createStatCard("Sách đang mượn", lblSachDangMuon, UITheme.ORANGE));

        grid.add(createStatCard("Phiếu đang mượn", lblPhieuDangMuon, UITheme.PRIMARY_DARK));
        grid.add(createStatCard("Phạt chưa thanh toán", lblPhieuPhatChuaThanhToan, UITheme.RED));
        grid.add(createStatCard("Tiền phạt chưa thu", lblTienPhatChuaThu, UITheme.RED_DARK));
        grid.add(createStatCard("Tiền cọc đang giữ", lblTienCocDangGiu, UITheme.CYAN_DARK));

        return grid;
    }

    private RoundedPanel createStatCard(String titleText, JLabel valueLabel, Color accentColor) {
        RoundedPanel card = new RoundedPanel(22, UITheme.BG_CARD);
        card.setLayout(new BorderLayout());
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(UITheme.TEXT_MUTED);

        valueLabel.setForeground(accentColor);

        card.add(title, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JLabel createNumberLabel() {
        JLabel label = new JLabel("0");
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setForeground(UITheme.TEXT_DARK);
        return label;
    }

    private JPanel buildTableAreaPanel() {
        JPanel area = new JPanel(new java.awt.GridLayout(1, 2, 16, 0));
        area.setOpaque(false);

        area.add(buildQuaHanPanel());
        area.add(buildTopSachPanel());

        return area;
    }

    private JPanel buildQuaHanPanel() {
        RoundedPanel card = new RoundedPanel(24, UITheme.BG_CARD);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Sách đang quá hạn");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel subtitle = new JLabel("Các cuốn sách chưa trả và đã vượt hạn trả");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(UITheme.TEXT_MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);

        tblQuaHan = new JTable();
        tblQuaHan.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Mã phiếu",
                    "Độc giả",
                    "Mã sách",
                    "Tên sách",
                    "Hạn trả",
                    "Quá hạn"
                }
        ));

        styleTable(tblQuaHan);
        tblQuaHan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tblQuaHan.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblQuaHan.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblQuaHan.getColumnModel().getColumn(2).setPreferredWidth(90);
        tblQuaHan.getColumnModel().getColumn(3).setPreferredWidth(200);
        tblQuaHan.getColumnModel().getColumn(4).setPreferredWidth(150);
        tblQuaHan.getColumnModel().getColumn(5).setPreferredWidth(90);

        JScrollPane scrollPane = new JScrollPane(tblQuaHan);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        card.add(titleBox, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private JPanel buildTopSachPanel() {
        RoundedPanel card = new RoundedPanel(24, UITheme.BG_CARD);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Top sách được mượn nhiều");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel subtitle = new JLabel("Xếp hạng theo số lần xuất hiện trong chi tiết phiếu mượn");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(UITheme.TEXT_MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);

        tblTopSach = new JTable();
        tblTopSach.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Mã đầu sách",
                    "Tên sách",
                    "Tác giả",
                    "Thể loại",
                    "Số lượt mượn"
                }
        ));

        styleTable(tblTopSach);
        tblTopSach.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tblTopSach.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblTopSach.getColumnModel().getColumn(1).setPreferredWidth(220);
        tblTopSach.getColumnModel().getColumn(2).setPreferredWidth(150);
        tblTopSach.getColumnModel().getColumn(3).setPreferredWidth(130);
        tblTopSach.getColumnModel().getColumn(4).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tblTopSach);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        card.add(titleBox, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
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

    private void taiDuLieuThongKe() {
        taiSoLieuTongQuan();
        taiDanhSachQuaHan();
        taiTopSachMuonNhieu();
    }

    private void taiSoLieuTongQuan() {
    try {
        TongQuanThongKeDTO dto = thongKeBaoCaoController.layTongQuanThongKe();

        lblTongDauSach.setText(String.valueOf(dto.getTongDauSach()));
        lblTongCuonSach.setText(String.valueOf(dto.getTongCuonSach()));
        lblSachSanSang.setText(String.valueOf(dto.getSachSanSang()));
        lblSachDangMuon.setText(String.valueOf(dto.getSachDangMuon()));
        lblPhieuDangMuon.setText(String.valueOf(dto.getPhieuDangMuon()));
        lblPhieuPhatChuaThanhToan.setText(String.valueOf(dto.getPhieuPhatChuaThanhToan()));
        lblTienPhatChuaThu.setText(formatTien(dto.getTienPhatChuaThu()));
        lblTienCocDangGiu.setText(formatTien(dto.getTienCocDangGiu()));

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải số liệu tổng quan: " + e.getMessage());
    }
}

    private void taiDanhSachQuaHan() {
    DefaultTableModel model = (DefaultTableModel) tblQuaHan.getModel();
    model.setRowCount(0);

    try {
        List<SachQuaHanDTO> danhSach = thongKeBaoCaoController.layDanhSachQuaHan();

        for (SachQuaHanDTO item : danhSach) {
            model.addRow(new Object[]{
                item.getIdPhieuMuon(),
                item.getHoTen(),
                item.getIdCaBiet(),
                item.getTenSach(),
                item.getHanTra(),
                item.getSoNgayQuaHan()
            });
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách quá hạn: " + e.getMessage());
    }
}

    private void taiTopSachMuonNhieu() {
    DefaultTableModel model = (DefaultTableModel) tblTopSach.getModel();
    model.setRowCount(0);

    try {
        List<TopSachMuonDTO> danhSach = thongKeBaoCaoController.layTopSachMuonNhieu();

        for (TopSachMuonDTO item : danhSach) {
            model.addRow(new Object[]{
                item.getIdDauSach(),
                item.getTenSach(),
                item.getTacGia(),
                item.getTheLoai(),
                item.getSoLuotMuon()
            });
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải top sách mượn nhiều: " + e.getMessage());
    }
}

    private String formatTien(BigDecimal soTien) {
        if (soTien == null) {
            return "0";
        }

        return String.format("%,.0f", soTien.doubleValue());
    }
}