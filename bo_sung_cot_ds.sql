USE QuanLyThuVien;
GO

-- 1. Thêm cột trang_thai vào bảng DauSach, mặc định sách mới sẽ là 'Đang phục vụ'
ALTER TABLE DauSach 
ADD trang_thai NVARCHAR(30) DEFAULT N'Đang phục vụ';
GO

-- 2. Cập nhật lại toàn bộ các cuốn sách cũ đang có trong máy thành 'Đang phục vụ'
UPDATE DauSach SET trang_thai = N'Đang phục vụ';
GO