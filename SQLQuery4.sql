USE QuanLyThuVien;
GO

/* =========================================================
   XÓA BẢNG CŨ NẾU CÓ
   Phải xóa ChiTietPhieuMuon trước vì nó phụ thuộc PhieuMuon
   ========================================================= */

IF OBJECT_ID('ChiTietPhieuMuon', 'U') IS NOT NULL
BEGIN
    DROP TABLE ChiTietPhieuMuon;
END
GO

IF OBJECT_ID('PhieuMuon', 'U') IS NOT NULL
BEGIN
    DROP TABLE PhieuMuon;
END
GO


/* =========================================================
   TẠO BẢNG PHIEUMUON
   ========================================================= */

CREATE TABLE PhieuMuon (
    id_phieu_muon INT IDENTITY(1,1) PRIMARY KEY,

    ten_dang_nhap VARCHAR(50) NOT NULL,

    ngay_muon DATETIME NOT NULL DEFAULT GETDATE(),

    han_tra DATETIME NOT NULL,

    trang_thai_phieu NVARCHAR(50) NOT NULL DEFAULT N'Đang mượn',

    ghi_chu NVARCHAR(255) NULL,

    CONSTRAINT FK_PhieuMuon_TaiKhoan
        FOREIGN KEY (ten_dang_nhap)
        REFERENCES TaiKhoan(ten_dang_nhap)
);
GO


/* =========================================================
   TẠO BẢNG CHITIETPHIEUMUON
   ========================================================= */

CREATE TABLE ChiTietPhieuMuon (
    id_chi_tiet INT IDENTITY(1,1) PRIMARY KEY,

    id_phieu_muon INT NOT NULL,

    id_ca_biet VARCHAR(50) NOT NULL,

    ngay_tra_thuc_te DATETIME NULL,

    trang_thai_chi_tiet NVARCHAR(50) NOT NULL DEFAULT N'Đang mượn',

    ghi_chu NVARCHAR(255) NULL,

    CONSTRAINT FK_ChiTietPhieuMuon_PhieuMuon
        FOREIGN KEY (id_phieu_muon)
        REFERENCES PhieuMuon(id_phieu_muon),

    CONSTRAINT FK_ChiTietPhieuMuon_CuonSach
        FOREIGN KEY (id_ca_biet)
        REFERENCES CuonSach(id_ca_biet)
);
GO

/* =========================================================
   DỮ LIỆU MẪU PHIẾU MƯỢN
   ========================================================= */

DECLARE @idPhieuMuon INT;

INSERT INTO PhieuMuon
(ten_dang_nhap, ngay_muon, han_tra, trang_thai_phieu, ghi_chu)
VALUES
('20234010', GETDATE(), DATEADD(DAY, 14, GETDATE()), N'Đang mượn', N'Phiếu mượn mẫu');

SET @idPhieuMuon = SCOPE_IDENTITY();

INSERT INTO ChiTietPhieuMuon
(id_phieu_muon, id_ca_biet, trang_thai_chi_tiet, ghi_chu)
VALUES
(@idPhieuMuon, 'CB001', N'Đang mượn', N'Chi tiết mượn mẫu');

UPDATE CuonSach
SET trang_thai_luu_thong = N'Đang cho mượn'
WHERE id_ca_biet = 'CB001';
GO


USE QuanLyThuVien;
GO

IF OBJECT_ID('PhieuPhat', 'U') IS NOT NULL
BEGIN
    DROP TABLE PhieuPhat;
END
GO

CREATE TABLE PhieuPhat (
    id_phieu_phat INT IDENTITY(1,1) PRIMARY KEY,

    id_phieu_muon INT NOT NULL,

    id_ca_biet VARCHAR(50) NOT NULL,

    ten_dang_nhap VARCHAR(50) NOT NULL,

    loai_vi_pham NVARCHAR(100) NOT NULL,
    so_ngay_qua_han INT NOT NULL DEFAULT 0,

    mo_ta_vi_pham NVARCHAR(255) NULL,

    so_tien_phat DECIMAL(12, 0) NOT NULL DEFAULT 0,

    trang_thai_thanh_toan NVARCHAR(50) NOT NULL DEFAULT N'Chưa thanh toán',

    ngay_lap DATETIME NOT NULL DEFAULT GETDATE(),

    ghi_chu NVARCHAR(255) NULL,

    CONSTRAINT FK_PhieuPhat_PhieuMuon
        FOREIGN KEY (id_phieu_muon)
        REFERENCES PhieuMuon(id_phieu_muon),

    CONSTRAINT FK_PhieuPhat_CuonSach
        FOREIGN KEY (id_ca_biet)
        REFERENCES CuonSach(id_ca_biet),

    CONSTRAINT FK_PhieuPhat_TaiKhoan
        FOREIGN KEY (ten_dang_nhap)
        REFERENCES TaiKhoan(ten_dang_nhap)
);
GO