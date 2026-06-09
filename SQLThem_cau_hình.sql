USE QuanLyThuVien;
GO

-- 1. XÓA BẢNG CŨ (Nếu dính khóa ngoại thì có thể phải xóa bảng PhieuMuon trước nhé)
IF OBJECT_ID('TaiKhoan', 'U') IS NOT NULL
    DROP TABLE TaiKhoan;
GO

-- 2. TẠO LẠI BẢNG MỚI CHUẨN CHỈNH
CREATE TABLE TaiKhoan (
    ten_dang_nhap VARCHAR(50) PRIMARY KEY, -- Chứa MSSV (Sinh viên), CCCD (Độc giả ngoài), hoặc ID tự tạo (Admin)
    mat_khau VARCHAR(255) NOT NULL,
    ho_ten NVARCHAR(100) NOT NULL,         -- Bắt buộc ai cũng phải có tên
    cccd VARCHAR(20),                      -- Có thể Null đối với Sinh viên/Thủ thư
    so_dien_thoai VARCHAR(15),             
    vai_tro NVARCHAR(50) NOT NULL,         -- Phân loại: 'Thủ thư', 'Sinh viên', 'Độc giả ngoài'
    trang_thai_tai_khoan NVARCHAR(50) DEFAULT N'Đang hoạt động' -- Mặc định là hoạt động, Độc giả ngoài sẽ bị ép thành 'Chờ duyệt' bằng code Java
);
GO

-- =======================================================
-- 3. ĐỔ DỮ LIỆU MẪU ĐỂ LÁT NỮA TEST ĐĂNG NHẬP THEO TỪNG LUỒNG
-- =======================================================

-- Mẫu 1: Tài khoản Thủ thư (Quyền cao nhất, hoạt động ngay)
INSERT INTO TaiKhoan (ten_dang_nhap, mat_khau, ho_ten, vai_tro, trang_thai_tai_khoan)
VALUES ('admin', '123456', N'Quản trị viên Thư viện', N'Thủ thư', N'Đang hoạt động');

-- Mẫu 2: Tài khoản Sinh viên Nội bộ (Dùng MSSV làm tên đăng nhập, hoạt động ngay)
INSERT INTO TaiKhoan (ten_dang_nhap, mat_khau, ho_ten, vai_tro, trang_thai_tai_khoan)
VALUES ('20231234', '123456', N'Nguyễn Văn Sinh Viên', N'Sinh viên', N'Đang hoạt động');

-- Mẫu 3: Tài khoản Độc giả ngoài (Vừa đăng ký qua Form, dùng CCCD làm tên đăng nhập, bị khóa trạng thái)
INSERT INTO TaiKhoan (ten_dang_nhap, mat_khau, ho_ten, cccd, so_dien_thoai, vai_tro, trang_thai_tai_khoan)
VALUES ('001203123456', '123456', N'Trần Khách Ngoài', '001203123456', '0901234567', N'Độc giả ngoài', N'Chờ duyệt');
GO