#!/usr/bin/env bash
# Build & chay QuanLyThuVien tren Ubuntu
set -e
cd "$(dirname "$0")"
DRIVER="lib/mssql-jdbc-13.4.0.jre11.jar"
echo "[1/2] Bien dich..."
rm -rf build/classes && mkdir -p build/classes
javac -encoding UTF-8 -cp "$DRIVER" -d build/classes $(find src -name '*.java')
echo "[2/2] Chay ung dung..."
java -cp "build/classes:$DRIVER" quanlythuvienproject.QuanLyThuVienProject
