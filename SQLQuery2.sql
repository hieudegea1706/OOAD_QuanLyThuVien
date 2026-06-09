IF NOT EXISTS (SELECT * FROM TaiKhoan WHERE ten_dang_nhap = '20234010')
BEGIN
    INSERT INTO TaiKhoan 
    (ten_dang_nhap, mat_khau, ho_ten, cccd, so_dien_thoai, vai_tro, trang_thai_tai_khoan)
    VALUES 
    ('20234010', '123456', N'Trần Trung Hiếu', NULL, '0987654321', N'Sinh viên', N'Chưa kích hoạt');
END
ELSE
BEGIN
    UPDATE TaiKhoan
    SET trang_thai_tai_khoan = N'Chưa kích hoạt',
        vai_tro = N'Sinh viên',
        mat_khau = '123456'
    WHERE ten_dang_nhap = '20234010';
END