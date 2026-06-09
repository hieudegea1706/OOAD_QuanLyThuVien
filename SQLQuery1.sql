USE QuanLyThuVien;
GO

-- Tạo bảng Tài khoản theo đúng Thẻ CRC
CREATE TABLE TaiKhoan (
    ten_dang_nhap VARCHAR(50) PRIMARY KEY,
    mat_khau VARCHAR(255) NOT NULL,
    vai_tro_role NVARCHAR(50),
    trang_thai_tai_khoan NVARCHAR(50)
);
GO

-- Thêm một tài khoản Thủ thư mẫu để lát nữa test đăng nhập
INSERT INTO TaiKhoan (ten_dang_nhap, mat_khau, vai_tro_role, trang_thai_tai_khoan)
VALUES ('admin', '123456', N'Thủ thư', N'Đang hoạt động');
GO