package graph;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    String DB_url = "jdbc:mysql://localhost:3306/temperature_db?useSSL=false&serverTimezone=Asia/Seoul";
    String DB_user = "root";
    String DB_password = "ejk127ekqls$";

    public Database() {
        try (Connection conn = DriverManager.getConnection(DB_url, DB_user, DB_password)){
            System.out.println("MySQL 연결 성공");
        } catch (SQLException e) {
            System.out.println("MySQL 연결 실패");
            e.printStackTrace();
        }
    }

    public ArrayList<Double> getTemperatureData() {
        ArrayList<Double> temperatureData = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_url, DB_user, DB_password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT celsius FROM temperature_data")) {
            while (rs.next()) {
                double celsius = rs.getDouble("celsius");
                temperatureData.add(celsius);
            }
        } catch (SQLException e) {
            System.out.println("MySQL 데이터베이스 연결 오류");
            e.printStackTrace();
        }
        return temperatureData;
    }
}
