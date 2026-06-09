/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrm extends JFrame {
    private JPanel pnlCards;
    private CardLayout cardLayout;

    private ChaoMungPnl chaoMungPnl;
    private DangNhapPnl dangNhapPnl;
    private DangKyPnl dangKyPnl;
    private TrangChuThuThuPnl trangChuThuThuPnl;
    private TrangChuDocGiaPnl trangChuDocGiaPnl;

    public MainFrm() {
        setTitle("Hệ thống quản lý thư viện Tạ Quang Bửu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1180, 760);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        pnlCards = new JPanel(cardLayout);
        setContentPane(pnlCards);

        chaoMungPnl = new ChaoMungPnl(this);
        dangNhapPnl = new DangNhapPnl(this);
        dangKyPnl = new DangKyPnl(this);
        trangChuThuThuPnl = new TrangChuThuThuPnl(this);
        trangChuDocGiaPnl = new TrangChuDocGiaPnl(this);

        pnlCards.add(chaoMungPnl, "cardChaoMung");
        pnlCards.add(dangNhapPnl, "cardDangNhap");
        pnlCards.add(dangKyPnl, "cardDangKy");
        pnlCards.add(trangChuThuThuPnl, "cardTrangChuThuThu");
        pnlCards.add(trangChuDocGiaPnl, "cardTrangChuDocGia");

        chuyenManHinh("cardChaoMung");
    }

    public void chuyenManHinh(String tenCard) {
        cardLayout.show(pnlCards, tenCard);
    }

    public void moManHinhDangNhapAnToan() {
        dangNhapPnl.xoaTrangDuLieu();
        chuyenManHinh("cardDangNhap");
    }
}
