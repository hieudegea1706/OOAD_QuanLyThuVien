USE QuanLyThuVien;
GO

IF OBJECT_ID('DocGiaNgoai', 'U') IS NOT NULL
    DROP TABLE DocGiaNgoai;
GO

CREATE TABLE DocGiaNgoai (
    id_doc_gia_ngoai INT IDENTITY(1,1) PRIMARY KEY,

    ten_dang_nhap VARCHAR(50) NOT NULL UNIQUE,

    cccd VARCHAR(20) NOT NULL UNIQUE,

    so_dien_thoai VARCHAR(15),

    tien_coc DECIMAL(12, 0) NOT NULL DEFAULT 0,

    ngay_dang_ky DATETIME NOT NULL DEFAULT GETDATE(),

    ngay_kich_hoat DATETIME NULL,

    ghi_chu NVARCHAR(255) NULL,

    CONSTRAINT FK_DocGiaNgoai_TaiKhoan
        FOREIGN KEY (ten_dang_nhap)
        REFERENCES TaiKhoan(ten_dang_nhap)
);
GO

INSERT INTO DocGiaNgoai (
    ten_dang_nhap,
    cccd,
    so_dien_thoai,
    tien_coc,
    ngay_dang_ky,
    ngay_kich_hoat,
    ghi_chu
)
SELECT
    ten_dang_nhap,
    cccd,
    so_dien_thoai,
    0,
    GETDATE(),
    NULL,
    N'Tài khoản độc giả ngoài tạo từ dữ liệu TaiKhoan'
FROM TaiKhoan
WHERE vai_tro = N'Độc giả ngoài'
  AND cccd IS NOT NULL
  AND ten_dang_nhap NOT IN (
      SELECT ten_dang_nhap FROM DocGiaNgoai
  );
GO

SELECT * FROM TaiKhoan;
SELECT * FROM DocGiaNgoai;
GO