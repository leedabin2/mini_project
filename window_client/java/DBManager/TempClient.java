package DBManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TempClient {
    private static final String DB_url = "jdbc:mysql://localhost:3306/temperature_db?useSSL=false&serverTimezone=Asia/Seoul";
    private static final String DB_user = "root";
    private static final String DB_password = "ejk127ekqls$";

    public static void main(String[]args) throws MalformedURLException {
        String urlStr = "http://165.246.116.32";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("HTTP connection failed : " + responseCode);
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();

                String line;
                while((line = reader.readLine()) != null) {
                    response.append(line);
                    response.append("\n");
                }

                reader.close();
                String[] lines = response.toString().split("\n");
                if (lines.length >= 4) {
                    String ipline = lines[2];
                    String ip_address = ipline.split(": ")[1];
                    System.out.println("Your IP address : " + ip_address);
                    System.out.println(lines[3]);
                    System.out.println(lines[4]);
                } else {
                    System.out.println("Response format error!");
                }
            }

            connection.disconnect();
        } catch (IOException var11) {
            var11.printStackTrace();
        }

    }
}


