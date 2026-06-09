USE QuanLyThuVien;
GO

/* =========================================================
   1. XÓA BẢNG CŨ THEO ĐÚNG THỨ TỰ
   Vì CuonSach phụ thuộc DauSach nên phải xóa CuonSach trước
   ========================================================= */

IF OBJECT_ID('CuonSach', 'U') IS NOT NULL
BEGIN
    DROP TABLE CuonSach;
END
GO

IF OBJECT_ID('DauSach', 'U') IS NOT NULL
BEGIN
    DROP TABLE DauSach;
END
GO


/* =========================================================
   2. TẠO BẢNG DAUSACH
   DauSach = thông tin chung của một tựa sách
   ========================================================= */

CREATE TABLE DauSach (
    id_dau_sach VARCHAR(50) PRIMARY KEY,

    ten_sach NVARCHAR(200) NOT NULL,

    tac_gia NVARCHAR(150) NULL,

    the_loai NVARCHAR(100) NULL,

    nha_xuat_ban NVARCHAR(150) NULL,

    nam_xuat_ban INT NULL,

    tom_tat NVARCHAR(500) NULL,

    trang_thai NVARCHAR(50) NOT NULL DEFAULT N'Còn phục vụ'
);
GO


/* =========================================================
   3. TẠO BẢNG CUONSACH
   CuonSach = từng quyển sách vật lý / bản sao cụ thể
   ========================================================= */

CREATE TABLE CuonSach (
    id_ca_biet VARCHAR(50) PRIMARY KEY,

    id_dau_sach VARCHAR(50) NOT NULL,

    trang_thai_luu_thong NVARCHAR(50) NOT NULL DEFAULT N'Sẵn sàng',

    tinh_trang_vat_ly NVARCHAR(50) NOT NULL DEFAULT N'Tốt',

    vi_tri_ke NVARCHAR(100) NULL,

    ngay_nhap_kho DATETIME NOT NULL DEFAULT GETDATE(),

    ghi_chu NVARCHAR(255) NULL,

    CONSTRAINT FK_CuonSach_DauSach
        FOREIGN KEY (id_dau_sach)
        REFERENCES DauSach(id_dau_sach)
);
GO


/* =========================================================
   4. THÊM DỮ LIỆU MẪU DAUSACH
   ========================================================= */

INSERT INTO DauSach
(id_dau_sach, ten_sach, tac_gia, the_loai, nha_xuat_ban, nam_xuat_ban, tom_tat, trang_thai)
VALUES
('DS001', N'Lập trình Java căn bản', N'Nguyễn Văn A', N'Công nghệ thông tin', N'NXB Giáo dục Việt Nam', 2022,
 N'Sách nhập môn lập trình Java, phù hợp cho sinh viên mới bắt đầu học lập trình hướng đối tượng.', N'Còn phục vụ'),

('DS002', N'Cấu trúc dữ liệu và giải thuật', N'Trần Văn B', N'Công nghệ thông tin', N'NXB Bách Khoa Hà Nội', 2021,
 N'Trình bày các cấu trúc dữ liệu cơ bản như danh sách liên kết, ngăn xếp, hàng đợi, cây và đồ thị.', N'Còn phục vụ'),

('DS003', N'Cơ sở dữ liệu', N'Lê Thị C', N'Công nghệ thông tin', N'NXB Đại học Quốc gia Hà Nội', 2020,
 N'Sách giới thiệu mô hình quan hệ, SQL, thiết kế cơ sở dữ liệu và chuẩn hóa dữ liệu.', N'Còn phục vụ'),

('DS004', N'Phân tích thiết kế hướng đối tượng', N'Phạm Văn D', N'Công nghệ phần mềm', N'NXB Khoa học và Kỹ thuật', 2023,
 N'Sách trình bày phương pháp phân tích thiết kế hệ thống theo hướng đối tượng với UML.', N'Còn phục vụ'),

('DS005', N'Kỹ thuật lập trình C/C++', N'Hoàng Văn E', N'Công nghệ thông tin', N'NXB Thanh Niên', 2019,
 N'Cung cấp kiến thức nền tảng về lập trình C/C++, con trỏ, hàm, mảng và cấu trúc dữ liệu cơ bản.', N'Còn phục vụ'),

('DS006', N'Hệ điều hành', N'Nguyễn Đức F', N'Hệ thống máy tính', N'NXB Bách Khoa Hà Nội', 2021,
 N'Trình bày các khái niệm tiến trình, luồng, quản lý bộ nhớ, hệ thống tệp và lập lịch CPU.', N'Còn phục vụ'),

('DS007', N'Mạng máy tính', N'Vũ Thị G', N'Mạng máy tính', N'NXB Thông tin và Truyền thông', 2022,
 N'Sách giới thiệu mô hình OSI, TCP/IP, địa chỉ IP, định tuyến và các giao thức mạng phổ biến.', N'Còn phục vụ'),

('DS008', N'Trí tuệ nhân tạo nhập môn', N'Đỗ Văn H', N'Trí tuệ nhân tạo', N'NXB Khoa học Tự nhiên và Công nghệ', 2023,
 N'Giới thiệu các khái niệm cơ bản về trí tuệ nhân tạo, tìm kiếm, học máy và hệ chuyên gia.', N'Còn phục vụ'),

('DS009', N'Kỹ năng viết CV và phỏng vấn', N'Nguyễn Thị I', N'Kỹ năng mềm', N'NXB Lao Động', 2020,
 N'Hướng dẫn xây dựng CV, chuẩn bị phỏng vấn và tìm kiếm việc làm cho sinh viên.', N'Còn phục vụ'),

('DS010', N'Toán rời rạc ứng dụng', N'Phạm Minh K', N'Toán học', N'NXB Giáo dục Việt Nam', 2018,
 N'Trình bày logic, tập hợp, quan hệ, tổ hợp, đồ thị và các ứng dụng trong khoa học máy tính.', N'Còn phục vụ');
GO


/* =========================================================
   5. THÊM DỮ LIỆU MẪU CUONSACH
   Mỗi DauSach có vài bản sao vật lý
   ========================================================= */

INSERT INTO CuonSach
(id_ca_biet, id_dau_sach, trang_thai_luu_thong, tinh_trang_vat_ly, vi_tri_ke, ghi_chu)
VALUES
-- DS001 - Lập trình Java căn bản
('CB001', 'DS001', N'Sẵn sàng', N'Tốt', N'Kệ A1 - Tầng 1', N'Bản sao 1'),
('CB002', 'DS001', N'Sẵn sàng', N'Tốt', N'Kệ A1 - Tầng 1', N'Bản sao 2'),
('CB003', 'DS001', N'Đang cho mượn', N'Tốt', N'Kệ A1 - Tầng 1', N'Đang được độc giả mượn'),

-- DS002 - Cấu trúc dữ liệu và giải thuật
('CB004', 'DS002', N'Sẵn sàng', N'Tốt', N'Kệ A2 - Tầng 1', N'Bản sao 1'),
('CB005', 'DS002', N'Sẵn sàng', N'Rách nhẹ', N'Kệ A2 - Tầng 1', N'Cần theo dõi tình trạng vật lý'),
('CB006', 'DS002', N'Đang cho mượn', N'Tốt', N'Kệ A2 - Tầng 1', N'Đang được mượn'),

-- DS003 - Cơ sở dữ liệu
('CB007', 'DS003', N'Sẵn sàng', N'Tốt', N'Kệ B1 - Tầng 2', N'Bản sao 1'),
('CB008', 'DS003', N'Sẵn sàng', N'Tốt', N'Kệ B1 - Tầng 2', N'Bản sao 2'),

-- DS004 - Phân tích thiết kế hướng đối tượng
('CB009', 'DS004', N'Sẵn sàng', N'Tốt', N'Kệ B2 - Tầng 2', N'Bản sao 1'),
('CB010', 'DS004', N'Sẵn sàng', N'Tốt', N'Kệ B2 - Tầng 2', N'Bản sao 2'),
('CB011', 'DS004', N'Đang cho mượn', N'Tốt', N'Kệ B2 - Tầng 2', N'Đang được mượn'),

-- DS005 - Kỹ thuật lập trình C/C++
('CB012', 'DS005', N'Sẵn sàng', N'Tốt', N'Kệ C1 - Tầng 2', N'Bản sao 1'),
('CB013', 'DS005', N'Đã thanh lý', N'Hư hỏng', N'Kho lưu trữ', N'Sách cũ, hỏng gáy'),

-- DS006 - Hệ điều hành
('CB014', 'DS006', N'Sẵn sàng', N'Tốt', N'Kệ C2 - Tầng 3', N'Bản sao 1'),
('CB015', 'DS006', N'Sẵn sàng', N'Tốt', N'Kệ C2 - Tầng 3', N'Bản sao 2'),

-- DS007 - Mạng máy tính
('CB016', 'DS007', N'Sẵn sàng', N'Tốt', N'Kệ D1 - Tầng 3', N'Bản sao 1'),
('CB017', 'DS007', N'Đang cho mượn', N'Tốt', N'Kệ D1 - Tầng 3', N'Đang được mượn'),

-- DS008 - Trí tuệ nhân tạo nhập môn
('CB018', 'DS008', N'Sẵn sàng', N'Tốt', N'Kệ D2 - Tầng 3', N'Bản sao 1'),
('CB019', 'DS008', N'Sẵn sàng', N'Tốt', N'Kệ D2 - Tầng 3', N'Bản sao 2'),

-- DS009 - Kỹ năng viết CV và phỏng vấn
('CB020', 'DS009', N'Sẵn sàng', N'Tốt', N'Kệ E1 - Tầng 1', N'Bản sao 1'),

-- DS010 - Toán rời rạc ứng dụng
('CB021', 'DS010', N'Sẵn sàng', N'Tốt', N'Kệ E2 - Tầng 2', N'Bản sao 1'),
('CB022', 'DS010', N'Sẵn sàng', N'Rách nhẹ', N'Kệ E2 - Tầng 2', N'Bản sao hơi cũ');
GO


/* =========================================================
   6. KIỂM TRA DỮ LIỆU
   ========================================================= */

SELECT * FROM DauSach;
GO

SELECT * FROM CuonSach;
GO

SELECT 
    ds.id_dau_sach,
    ds.ten_sach,
    COUNT(cs.id_ca_biet) AS so_luong_ban_sao,
    SUM(CASE WHEN cs.trang_thai_luu_thong = N'Sẵn sàng' THEN 1 ELSE 0 END) AS so_sach_san_sang,
    SUM(CASE WHEN cs.trang_thai_luu_thong = N'Đang cho mượn' THEN 1 ELSE 0 END) AS so_sach_dang_muon,
    SUM(CASE WHEN cs.trang_thai_luu_thong = N'Đã thanh lý' THEN 1 ELSE 0 END) AS so_sach_thanh_ly
FROM DauSach ds
LEFT JOIN CuonSach cs ON ds.id_dau_sach = cs.id_dau_sach
GROUP BY ds.id_dau_sach, ds.ten_sach
ORDER BY ds.id_dau_sach;
GO