package views;

import components.ModernButton;
import components.ModernTextField;
import components.RoundedPanel;
import controllers.DocGiaHomeController;
import dto.LichSuMuonTraDocGiaDTO;
import dto.SachTraCuuDTO;
import dto.SachXuHuongDTO;
import dto.ThongTinTongQuanDocGiaDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import utils.AppSession;
import utils.UITheme;

public class TrangChuDocGiaPnl extends JPanel {

    private final MainFrm main;
    private final DocGiaHomeController docGiaHomeController = new DocGiaHomeController();

    // Thông tin tổng quan
    private JLabel lblXinChao;
    private JLabel lblVaiTro;
    private JLabel lblTrangThaiTaiKhoan;
    private JLabel lblTienCoc;

    // Chỉ số nhanh
    private JLabel lblSoSachDangMuon;
    private JLabel lblSoSachQuaHan;
    private JLabel lblSoPhieuPhat;
    private JLabel lblTienPhat;

    // Tab tài khoản
    private JLabel lblTkTenDangNhap;
    private JLabel lblTkHoTen;
    private JLabel lblTkVaiTro;
    private JLabel lblTkTrangThai;
    private JLabel lblTkTienCoc;

    // Tra cứu sách
    private ModernTextField txtTuKhoaSach;
    private JTable tblTraCuuSach;

    // Bảng dữ liệu
    private JTable tblTopSach;
    private JTable tblLichSuMuonTra;

    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public TrangChuDocGiaPnl(MainFrm mainFrm) {
        this.main = mainFrm;
        buildUI();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                taiDuLieuDocGia();
            }
        });
    }

    private void buildUI() {
        setLayout(new BorderLayout(0, 16));
        setBackground(UITheme.BG_MAIN);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 22, 18, 22));

        add(buildHeaderPanel(), BorderLayout.NORTH);
        add(buildTabPanel(), BorderLayout.CENTER);
    }

    private JPanel buildHeaderPanel() {
        RoundedPanel header = new RoundedPanel(24, UITheme.BG_CARD);
        header.setLayout(new BorderLayout(20, 0));
        header.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Khu vực độc giả");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel subtitle = new JLabel("Tra cứu sách, theo dõi lịch sử mượn trả và xem xu hướng sách trong thư viện.");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        JPanel buttonBox = new JPanel();
        buttonBox.setOpaque(false);
        buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.X_AXIS));

        ModernButton btnLamMoi = new ModernButton("Làm mới", UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        btnLamMoi.setPreferredSize(new Dimension(120, 40));
        btnLamMoi.addActionListener(e -> taiDuLieuDocGia());

        ModernButton btnDangXuat = new ModernButton("Đăng xuất", UITheme.PURPLE, UITheme.PURPLE_DARK);
        btnDangXuat.setPreferredSize(new Dimension(120, 40));
        btnDangXuat.addActionListener(e -> main.moManHinhDangNhapAnToan());

        buttonBox.add(btnLamMoi);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(btnDangXuat);

        header.add(titleBox, BorderLayout.CENTER);
        header.add(buttonBox, BorderLayout.EAST);

        return header;
    }

    private JTabbedPane buildTabPanel() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 15));

        tabs.addTab("Trang chủ", buildTrangChuPanel());
        tabs.addTab("Tra cứu sách", buildTraCuuSachPanel());
        tabs.addTab("Lịch sử mượn trả", buildLichSuPanel());
        tabs.addTab("Tài khoản", buildTaiKhoanPanel());

        return tabs;
    }

    // ============================================================
    // TAB TRANG CHỦ
    // ============================================================

    private JPanel buildTrangChuPanel() {
        JPanel page = new JPanel(new BorderLayout(0, 16));
        page.setOpaque(false);
        page.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 0, 0, 0));

        page.add(buildTongQuanPanel(), BorderLayout.NORTH);
        page.add(buildTopSachPanel(), BorderLayout.CENTER);

        return page;
    }

    private JPanel buildTongQuanPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 14));
        wrapper.setOpaque(false);

        RoundedPanel infoCard = new RoundedPanel(22, UITheme.BG_CARD);
        infoCard.setLayout(new BorderLayout(12, 6));
        infoCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));

        lblXinChao = new JLabel("Xin chào, độc giả");
        lblXinChao.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblXinChao.setForeground(UITheme.TEXT_DARK);

        lblVaiTro = new JLabel("Vai trò: --");
        lblVaiTro.setFont(UITheme.FONT_SUBTITLE);
        lblVaiTro.setForeground(UITheme.TEXT_MUTED);

        lblTrangThaiTaiKhoan = new JLabel("Trạng thái tài khoản: --");
        lblTrangThaiTaiKhoan.setFont(UITheme.FONT_SUBTITLE);
        lblTrangThaiTaiKhoan.setForeground(UITheme.TEXT_MUTED);

        lblTienCoc = new JLabel("Tiền cọc: 0 VNĐ");
        lblTienCoc.setFont(UITheme.FONT_SUBTITLE);
        lblTienCoc.setForeground(UITheme.TEXT_MUTED);

        textBox.add(lblXinChao);
        textBox.add(Box.createVerticalStrut(5));
        textBox.add(lblVaiTro);
        textBox.add(Box.createVerticalStrut(3));
        textBox.add(lblTrangThaiTaiKhoan);
        textBox.add(Box.createVerticalStrut(3));
        textBox.add(lblTienCoc);

        infoCard.add(textBox, BorderLayout.CENTER);

        JPanel metricGrid = new JPanel(new GridLayout(1, 4, 14, 0));
        metricGrid.setOpaque(false);

        lblSoSachDangMuon = createMetricValueLabel();
        lblSoSachQuaHan = createMetricValueLabel();
        lblSoPhieuPhat = createMetricValueLabel();
        lblTienPhat = createMetricValueLabel();

        metricGrid.add(buildMetricCard("Sách đang mượn", lblSoSachDangMuon, "Tổng số cuốn chưa trả"));
        metricGrid.add(buildMetricCard("Sách quá hạn", lblSoSachQuaHan, "Cần trả sớm để tránh phạt"));
        metricGrid.add(buildMetricCard("Phiếu phạt", lblSoPhieuPhat, "Chưa thanh toán"));
        metricGrid.add(buildMetricCard("Tiền phạt", lblTienPhat, "Tổng tiền chưa thanh toán"));

        wrapper.add(infoCard, BorderLayout.NORTH);
        wrapper.add(metricGrid, BorderLayout.CENTER);

        return wrapper;
    }

    private JLabel createMetricValueLabel() {
        JLabel label = new JLabel("0");
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setForeground(UITheme.PRIMARY);
        return label;
    }

    private JPanel buildMetricCard(String titleText, JLabel valueLabel, String noteText) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel note = new JLabel(noteText);
        note.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        note.setForeground(UITheme.TEXT_MUTED);

        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(note);

        return card;
    }

    private JPanel buildTopSachPanel() {
        RoundedPanel card = new RoundedPanel(24, UITheme.BG_CARD);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Top sách được mượn nhiều");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel subtitle = new JLabel("Gợi ý xu hướng sách nổi bật dựa trên số lượt mượn trong hệ thống.");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        tblTopSach = new JTable();
        tblTopSach.setModel(createTableModel(new String[]{
            "Mã đầu sách",
            "Tên sách",
            "Tác giả",
            "Thể loại",
            "Lượt mượn",
            "Bản sẵn sàng"
        }));
        styleTable(tblTopSach);

        tblTopSach.getColumnModel().getColumn(0).setPreferredWidth(110);
        tblTopSach.getColumnModel().getColumn(1).setPreferredWidth(260);
        tblTopSach.getColumnModel().getColumn(2).setPreferredWidth(180);
        tblTopSach.getColumnModel().getColumn(3).setPreferredWidth(150);
        tblTopSach.getColumnModel().getColumn(4).setPreferredWidth(90);
        tblTopSach.getColumnModel().getColumn(5).setPreferredWidth(110);

        JScrollPane scrollPane = new JScrollPane(tblTopSach);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        card.add(titleBox, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    // ============================================================
    // TAB TRA CỨU SÁCH
    // ============================================================

    private JPanel buildTraCuuSachPanel() {
        JPanel page = new JPanel(new BorderLayout(0, 16));
        page.setOpaque(false);
        page.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 0, 0, 0));

        RoundedPanel card = new RoundedPanel(24, UITheme.BG_CARD);
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Tra cứu sách");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel subtitle = new JLabel("Tìm kiếm sách theo mã đầu sách, tên sách, tác giả, thể loại hoặc nhà xuất bản.");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        JPanel searchBox = new JPanel(new BorderLayout(10, 0));
        searchBox.setOpaque(false);

        txtTuKhoaSach = new ModernTextField();
        txtTuKhoaSach.setPreferredSize(new Dimension(420, 42));
        txtTuKhoaSach.addActionListener(e -> timKiemSachDocGia());

        ModernButton btnTimKiem = new ModernButton("Tìm kiếm", UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        btnTimKiem.setPreferredSize(new Dimension(120, 42));
        btnTimKiem.addActionListener(e -> timKiemSachDocGia());

        ModernButton btnLamMoi = new ModernButton("Làm mới", UITheme.CYAN, UITheme.CYAN_DARK);
        btnLamMoi.setPreferredSize(new Dimension(120, 42));
        btnLamMoi.addActionListener(e -> {
            txtTuKhoaSach.setText("");
            timKiemSachDocGia();
        });

        JPanel buttonBox = new JPanel();
        buttonBox.setOpaque(false);
        buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.X_AXIS));
        buttonBox.add(btnTimKiem);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(btnLamMoi);

        searchBox.add(txtTuKhoaSach, BorderLayout.CENTER);
        searchBox.add(buttonBox, BorderLayout.EAST);

        tblTraCuuSach = new JTable();
        tblTraCuuSach.setModel(createTableModel(new String[]{
            "Mã đầu sách",
            "Tên sách",
            "Tác giả",
            "Thể loại",
            "Nhà xuất bản",
            "Năm XB",
            "Bản sẵn sàng",
            "Vị trí kệ"
        }));
        styleTable(tblTraCuuSach);
        tblTraCuuSach.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tblTraCuuSach.getColumnModel().getColumn(0).setPreferredWidth(110);
        tblTraCuuSach.getColumnModel().getColumn(1).setPreferredWidth(280);
        tblTraCuuSach.getColumnModel().getColumn(2).setPreferredWidth(180);
        tblTraCuuSach.getColumnModel().getColumn(3).setPreferredWidth(140);
        tblTraCuuSach.getColumnModel().getColumn(4).setPreferredWidth(170);
        tblTraCuuSach.getColumnModel().getColumn(5).setPreferredWidth(80);
        tblTraCuuSach.getColumnModel().getColumn(6).setPreferredWidth(120);
        tblTraCuuSach.getColumnModel().getColumn(7).setPreferredWidth(140);

        JScrollPane scrollPane = new JScrollPane(tblTraCuuSach);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        JPanel topPanel = new JPanel(new BorderLayout(0, 14));
        topPanel.setOpaque(false);
        topPanel.add(titleBox, BorderLayout.NORTH);
        topPanel.add(searchBox, BorderLayout.CENTER);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        page.add(card, BorderLayout.CENTER);

        return page;
    }

    // ============================================================
    // TAB LỊCH SỬ MƯỢN TRẢ
    // ============================================================

    private JPanel buildLichSuPanel() {
        JPanel page = new JPanel(new BorderLayout(0, 16));
        page.setOpaque(false);
        page.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 0, 0, 0));

        RoundedPanel card = new RoundedPanel(24, UITheme.BG_CARD);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Lịch sử mượn trả");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel subtitle = new JLabel("Hiển thị các cuốn sách bạn đã mượn, đang mượn, đã trả và thông tin vi phạm nếu có.");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        tblLichSuMuonTra = new JTable();
        tblLichSuMuonTra.setModel(createTableModel(new String[]{
            "Mã phiếu",
            "Mã cá biệt",
            "Tên sách",
            "Ngày mượn",
            "Hạn trả",
            "Ngày trả",
            "Trạng thái",
            "Vi phạm",
            "Tiền phạt",
            "Thanh toán"
        }));
        styleTable(tblLichSuMuonTra);
        tblLichSuMuonTra.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tblLichSuMuonTra.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblLichSuMuonTra.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblLichSuMuonTra.getColumnModel().getColumn(2).setPreferredWidth(260);
        tblLichSuMuonTra.getColumnModel().getColumn(3).setPreferredWidth(150);
        tblLichSuMuonTra.getColumnModel().getColumn(4).setPreferredWidth(150);
        tblLichSuMuonTra.getColumnModel().getColumn(5).setPreferredWidth(150);
        tblLichSuMuonTra.getColumnModel().getColumn(6).setPreferredWidth(120);
        tblLichSuMuonTra.getColumnModel().getColumn(7).setPreferredWidth(160);
        tblLichSuMuonTra.getColumnModel().getColumn(8).setPreferredWidth(120);
        tblLichSuMuonTra.getColumnModel().getColumn(9).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(tblLichSuMuonTra);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        card.add(titleBox, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        page.add(card, BorderLayout.CENTER);

        return page;
    }

    // ============================================================
    // TAB TÀI KHOẢN
    // ============================================================

    private JPanel buildTaiKhoanPanel() {
        JPanel page = new JPanel(new BorderLayout(0, 16));
        page.setOpaque(false);
        page.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 0, 0, 0));

        RoundedPanel card = new RoundedPanel(24, UITheme.BG_CARD);
        card.setLayout(new BorderLayout(0, 18));
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Thông tin tài khoản");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel subtitle = new JLabel("Thông tin cá nhân và trạng thái sử dụng thư viện của độc giả.");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.TEXT_MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        JPanel infoGrid = new JPanel(new GridLayout(5, 1, 0, 12));
        infoGrid.setOpaque(false);

        lblTkTenDangNhap = new JLabel();
        lblTkHoTen = new JLabel();
        lblTkVaiTro = new JLabel();
        lblTkTrangThai = new JLabel();
        lblTkTienCoc = new JLabel();

        infoGrid.add(buildInfoLine("Tên đăng nhập", lblTkTenDangNhap));
        infoGrid.add(buildInfoLine("Họ tên", lblTkHoTen));
        infoGrid.add(buildInfoLine("Vai trò", lblTkVaiTro));
        infoGrid.add(buildInfoLine("Trạng thái tài khoản", lblTkTrangThai));
        infoGrid.add(buildInfoLine("Tiền cọc", lblTkTienCoc));

        card.add(titleBox, BorderLayout.NORTH);
        card.add(infoGrid, BorderLayout.CENTER);

        page.add(card, BorderLayout.NORTH);

        return page;
    }

    private JPanel buildInfoLine(String labelText, JLabel valueLabel) {
        RoundedPanel line = new RoundedPanel(18, Color.WHITE);
        line.setLayout(new BorderLayout(12, 0));
        line.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(UITheme.TEXT_DARK);
        label.setPreferredSize(new Dimension(190, 28));

        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        valueLabel.setForeground(UITheme.TEXT_NORMAL);

        line.add(label, BorderLayout.WEST);
        line.add(valueLabel, BorderLayout.CENTER);

        return line;
    }

    // ============================================================
    // LOAD DATA
    // ============================================================

    private void taiDuLieuDocGia() {
        if (!AppSession.daDangNhap()) {
            hienThiChuaDangNhap();
            return;
        }

        String tenDangNhap = AppSession.getTenDangNhap();

        try {
            ThongTinTongQuanDocGiaDTO tongQuan = docGiaHomeController.layTongQuanDocGia(tenDangNhap);
            hienThiTongQuan(tongQuan);

            List<SachXuHuongDTO> topSach = docGiaHomeController.layTopSachMuonNhieu();
            doTopSachLenBang(topSach);

            List<SachTraCuuDTO> sachTraCuu = docGiaHomeController.traCuuSach("");
            doSachTraCuuLenBang(sachTraCuu);

            List<LichSuMuonTraDocGiaDTO> lichSu = docGiaHomeController.layLichSuMuonTra(tenDangNhap);
            doLichSuLenBang(lichSu);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu độc giả: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void timKiemSachDocGia() {
        String tuKhoa = txtTuKhoaSach == null ? "" : txtTuKhoaSach.getText().trim();

        try {
            List<SachTraCuuDTO> danhSach = docGiaHomeController.traCuuSach(tuKhoa);
            doSachTraCuuLenBang(danhSach);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tra cứu sách: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void hienThiTongQuan(ThongTinTongQuanDocGiaDTO dto) {
        if (dto == null) {
            hienThiChuaDangNhap();
            return;
        }

        lblXinChao.setText("Xin chào, " + safe(dto.getHoTen()));
        lblVaiTro.setText("Vai trò: " + safe(dto.getVaiTro()));
        lblTrangThaiTaiKhoan.setText("Trạng thái tài khoản: " + safe(dto.getTrangThaiTaiKhoan()));
        lblTienCoc.setText("Tiền cọc: " + formatTien(dto.getTienCoc()));

        lblSoSachDangMuon.setText(String.valueOf(dto.getSoSachDangMuon()));
        lblSoSachQuaHan.setText(String.valueOf(dto.getSoSachQuaHan()));
        lblSoPhieuPhat.setText(String.valueOf(dto.getSoPhieuPhatChuaThanhToan()));
        lblTienPhat.setText(formatTien(dto.getTienPhatChuaThanhToan()));

        lblTkTenDangNhap.setText(safe(dto.getTenDangNhap()));
        lblTkHoTen.setText(safe(dto.getHoTen()));
        lblTkVaiTro.setText(safe(dto.getVaiTro()));
        lblTkTrangThai.setText(safe(dto.getTrangThaiTaiKhoan()));
        lblTkTienCoc.setText(formatTien(dto.getTienCoc()));
    }

    private void hienThiChuaDangNhap() {
        lblXinChao.setText("Chưa xác định độc giả");
        lblVaiTro.setText("Vai trò: --");
        lblTrangThaiTaiKhoan.setText("Trạng thái tài khoản: --");
        lblTienCoc.setText("Tiền cọc: 0 VNĐ");

        lblSoSachDangMuon.setText("0");
        lblSoSachQuaHan.setText("0");
        lblSoPhieuPhat.setText("0");
        lblTienPhat.setText("0 VNĐ");

        lblTkTenDangNhap.setText("--");
        lblTkHoTen.setText("--");
        lblTkVaiTro.setText("--");
        lblTkTrangThai.setText("--");
        lblTkTienCoc.setText("0 VNĐ");

        if (tblTopSach != null) {
            ((DefaultTableModel) tblTopSach.getModel()).setRowCount(0);
        }

        if (tblTraCuuSach != null) {
            ((DefaultTableModel) tblTraCuuSach.getModel()).setRowCount(0);
        }

        if (tblLichSuMuonTra != null) {
            ((DefaultTableModel) tblLichSuMuonTra.getModel()).setRowCount(0);
        }
    }

    private void doTopSachLenBang(List<SachXuHuongDTO> danhSach) {
        DefaultTableModel model = (DefaultTableModel) tblTopSach.getModel();
        model.setRowCount(0);

        for (SachXuHuongDTO sach : danhSach) {
            model.addRow(new Object[]{
                sach.getIdDauSach(),
                sach.getTenSach(),
                sach.getTacGia(),
                sach.getTheLoai(),
                sach.getSoLuotMuon(),
                sach.getSoBanSanSang()
            });
        }
    }

    private void doSachTraCuuLenBang(List<SachTraCuuDTO> danhSach) {
        DefaultTableModel model = (DefaultTableModel) tblTraCuuSach.getModel();
        model.setRowCount(0);

        for (SachTraCuuDTO sach : danhSach) {
            model.addRow(new Object[]{
                sach.getIdDauSach(),
                sach.getTenSach(),
                sach.getTacGia(),
                sach.getTheLoai(),
                sach.getNhaXuatBan(),
                sach.getNamXuatBan() == null ? "" : sach.getNamXuatBan(),
                sach.getSoBanSanSang(),
                safe(sach.getViTriKe())
            });
        }
    }

    private void doLichSuLenBang(List<LichSuMuonTraDocGiaDTO> danhSach) {
        DefaultTableModel model = (DefaultTableModel) tblLichSuMuonTra.getModel();
        model.setRowCount(0);

        for (LichSuMuonTraDocGiaDTO item : danhSach) {
            model.addRow(new Object[]{
                item.getIdPhieuMuon(),
                item.getIdCaBiet(),
                item.getTenSach(),
                formatNgay(item.getNgayMuon()),
                formatNgay(item.getHanTra()),
                formatNgay(item.getNgayTraThucTe()),
                safe(item.getTrangThaiChiTiet()),
                safe(item.getLoaiViPham()),
                formatTien(item.getSoTienPhat()),
                safe(item.getTrangThaiThanhToan())
            });
        }
    }

    // ============================================================
    // HELPER
    // ============================================================

    private DefaultTableModel createTableModel(String[] columns) {
        return new DefaultTableModel(new Object[][]{}, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(UITheme.TEXT_DARK);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 235, 245));
    }

    private String formatNgay(java.sql.Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }

        return dateFormat.format(timestamp);
    }

    private String formatTien(BigDecimal value) {
        if (value == null) {
            return "0 VNĐ";
        }

        return numberFormat.format(value) + " VNĐ";
    }

    private String safe(String value) {
        return value == null || value.trim().isEmpty() ? "--" : value;
    }
}