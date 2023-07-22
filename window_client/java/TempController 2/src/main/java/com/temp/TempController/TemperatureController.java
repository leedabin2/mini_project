package com.temp.TempController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class TemperatureController {

    @GetMapping("/temperatures")
    public ResponseEntity<TemperatureData> getTemperatureData() {
        TemperatureData temperatureData = retrieveLatestTemperatureData();
        return ResponseEntity.ok(temperatureData);
    }

    private TemperatureData retrieveLatestTemperatureData() {
        String dbUrl = "jdbc:mysql://localhost:3306/temperature_db?useSSL=false&serverTimezone=Asia/Seoul";
        String dbUser = "root";
        String dbPassword = "ejk127ekqls$";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            // 온도 데이터 조회
            String sql = "SELECT celsius, fahrenheit FROM temperature_data ORDER BY id DESC LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double celsius = rs.getDouble("celsius");
                double fahrenheit = rs.getDouble("fahrenheit");
                return new TemperatureData(celsius, fahrenheit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // 데이터가 없을 경우 또는 오류 발생 시 null 반환
    }
}
