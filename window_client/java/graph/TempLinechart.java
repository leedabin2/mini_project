package graph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TempLinechart {

    private static final String DB_url = "jdbc:mysql://localhost:3306/temperature_db?useSSL=false&serverTimezone=Asia/Seoul";
    private static final String DB_user = "root";
    private static final String DB_password = "ejk127ekqls$";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() { //displays the graph
        JFrame frame = new JFrame("Temperature Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 400));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset(); //store data
        JFreeChart lineChart = ChartFactory.createLineChart(
                "실시간 온도 그래프",
                "Time", "Temperature",
                dataset, PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        frame.add(chartPanel);

        JLabel temperatureLabel = new JLabel("현재 온도 : ");
        frame.add(temperatureLabel);

        JButton updateButton = new JButton("현재온도");
        updateButton.setPreferredSize(new Dimension(100, 30));
        frame.add(updateButton);

        frame.setLayout(new GridLayout(1,3)); // 그래프와 레이블을 옆으로 나란히 배치
        frame.pack();
        frame.setVisible(true);

        updateGraph(dataset,temperatureLabel);

        // 버튼에 액션 리스너 등록
        updateButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updateTemperatureLabel(temperatureLabel);
            }
        });
    }

    private static void updateGraph(DefaultCategoryDataset dataset, JLabel temperatureLabel) {
        Thread updateThread = new Thread(() -> {
            while (true) {
                try (Connection conn = DriverManager.getConnection(DB_url, DB_user, DB_password)) {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT celsius FROM temperature_data");

                    while (rs.next()) {
                        double celsius = rs.getDouble("celsius");
                        dataset.addValue(celsius, "Temperature", String.valueOf(System.currentTimeMillis()));

                       // temperatureLabel.setText("현재온도 : " + celsius + " C");
                    }

                    rs.close();
                    stmt.close();

                    Thread.sleep(10000); // update 10sec
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        updateThread.start();
    }
    // 버튼 클릭 시 온도 레이블 업데이트
    private static void updateTemperatureLabel(JLabel temperatureLabel) {
        try (Connection conn = DriverManager.getConnection(DB_url, DB_user, DB_password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT celsius FROM temperature_data ORDER BY id DESC LIMIT 1");

            if (rs.next()) {
                double celsius = rs.getDouble("celsius");
                temperatureLabel.setText("현재온도 : " + celsius + "°C");
            } else {
                temperatureLabel.setText("현재온도를 가져올 수 없습니다.");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}





