package graph;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class TempLinechart extends JFrame {
    Showgraph showgraph;

    public TempLinechart(Showgraph shg) {
        showgraph = shg;

        setTitle("온도 그래프");
        setLocationRelativeTo(null); // 기운데 배치
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫을 때 종료 설정
        setLayout(null); // 레이아웃 설정
        setBackground(Color.LIGHT_GRAY);
        setSize(600, 400);

        // 버튼
        JButton btn_temp = new JButton("현재 온도");
        btn_temp.setBounds(300, 300, 120, 30);
        getContentPane().add(btn_temp);

        // 버튼 동작
        JLabel btn_label1 = new JLabel();
        btn_temp.addActionListener(event -> {
            ArrayList<Double> temperatureData = showgraph.getTemperatureData();
            if (!temperatureData.isEmpty()) {
                double lastTemperature = temperatureData.get(temperatureData.size() - 1);
                btn_label1.setText("현재 온도는 : " + lastTemperature);
            } else {
                btn_label1.setText("온도 데이터가 없습니다.");
            }
            createAndShowChart(temperatureData);
        });
        btn_label1.setBounds(300, 330, 274, 50);
        getContentPane().add(btn_label1);

        // 라벨
        JLabel label = new JLabel();
        label.setBounds(300, 30, 274, 50);
        label.setText("현재 온도 그래프");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        getContentPane().add(label);

        setVisible(true);
    }

    // 그래프를 생성, 데이터 업데이트
    public void createAndShowChart(ArrayList<Double> temperatureData) {
        // 그래프 데이터셋
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < temperatureData.size(); i++) {
            dataset.addValue(temperatureData.get(i), "온도", Integer.toString(i + 1));
        }

        // 라인 차트
        JFreeChart lineChart = ChartFactory.createLineChart("온도 그래프", "시간", "온도 (섭씨)", dataset);

        // 차트 패널 생성하고 JFrame에 추가
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setBounds(50, 80, 500, 200);
        getContentPane().add(chartPanel);
    }
}
