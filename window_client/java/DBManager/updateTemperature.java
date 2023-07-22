package DBManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class updateTemperature {
    private static final String DB_url = "jdbc:mysql://localhost:3306/temperature_db?useSSL=false&serverTimezone=Asia/Seoul";
    private static final String DB_user = "root";
    private static final String DB_password = "ejk127ekqls$";
    private static final String URL_STR = "http://165.246.116.20";

    public static void main(String[] args) {
        while (true) {
            try {
                URL url = new URL(URL_STR);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();

                if (responseCode != 200) {
                    System.out.println("HTTP connection failed : " + responseCode);
                } else {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();

                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                        response.append("\n");
                    }

                    reader.close();
                    String[] lines = response.toString().split("\n");
                    double celsius, fahrenheit;

                    if (lines.length >= 4) {
                        String ipline = lines[2];
                        String ip_address = ipline.split(": ")[1];

                        System.out.println("Your IP address : " + ip_address);
                        System.out.println(lines[3]);
                        System.out.println(lines[4]);

                        String[] values = lines[3].split(": ");
                        celsius = Double.parseDouble(values[1]);
                        values = lines[4].split(": ");
                        fahrenheit = Double.parseDouble(values[1]);

                        try (Connection conn = DriverManager.getConnection(DB_url, DB_user, DB_password)) {
                            String sql = "INSERT INTO temperature_data (celsius, fahrenheit) VALUES (?, ?)";
                            PreparedStatement stmt = conn.prepareStatement(sql);

                            stmt.setDouble(1, celsius);
                            stmt.setDouble(2, fahrenheit);
                            //System.out.println("SQL 연결 성공!");
                            stmt.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Response format error!");
                    }
                }

                connection.disconnect();
                Thread.sleep(1000); // 1초마다 반복
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
