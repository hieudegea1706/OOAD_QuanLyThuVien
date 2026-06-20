/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author HIEU
 */
public class DatabaseHelper {
    public static Connection getConnection() throws SQLException {
        
        String connectionUrl = "jdbc:sqlserver://localhost:1433;"
                + "databaseName=QuanLyThuVien;"
                + "user=sa;"
                + "password=Ducphuc30705;"
                + "encrypt=true;"
                + "trustServerCertificate=true;";
        
        return DriverManager.getConnection(connectionUrl);
    }
}
