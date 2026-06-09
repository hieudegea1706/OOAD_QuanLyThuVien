USE QuanLyThuVien;
GO

-- Tạo bảng Đầu Sách
CREATE TABLE DauSach (
    id_dau_sach VARCHAR(20) PRIMARY KEY,
    ten_sach NVARCHAR(100),
    tac_gia NVARCHAR(100),
    the_loai NVARCHAR(50)
);
GO

-- Thêm 3 cuốn sách mẫu để lát nữa Java có cái mà hiển thị
INSERT INTO DauSach (id_dau_sach, ten_sach, tac_gia, the_loai) VALUES 
('DS01', N'Nhập môn Lập trình', N'Nguyễn Văn A', N'Giáo trình'),
('DS02', N'Mạng Máy Tính', N'Trần Văn B', N'Giáo trình'),
('DS03', N'Sherlock Holmes', N'Arthur Conan Doyle', N'Văn học');
GO