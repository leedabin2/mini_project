package graph;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;


public class TempLinechart extends JFrame {
    Showgraph showgraph;

    public TempLinechart(Showgraph shg) {
        showgraph = shg;

        setTitle("온도 그래프");
        setLocationRelativeTo(null); // 기운데 배치
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫을 때 종료 설정
        setLayout(null); // 레이아웃 설정
        setBackground(Color.LIGHT_GRAY);
        setSize(770, 600);
        setResizable(false);


        // 버튼
        JButton btn_temp = new JButton("현재 온도");
        btn_temp.setBounds(310, 400, 120, 30);
        getContentPane().add(btn_temp);

        // 버튼 동작
        JLabel btn_label1 = new JLabel();
        btn_temp.addActionListener(event -> {
            ArrayList<Double> temperatureData = showgraph.getTemperatureData();
            if (!temperatureData.isEmpty()) {
                double lastTemperature = temperatureData.get(temperatureData.size() - 1);
                btn_label1.setText("현재 온도는 : " + lastTemperature + " C");
            } else {
                btn_label1.setText("온도 데이터가 없습니다.");
            }
            createAndShowChart(temperatureData);
        });
        btn_label1.setBounds(315, 425, 274, 50);
        getContentPane().add(btn_label1,BorderLayout.SOUTH);

        // 라벨
        JLabel label = new JLabel();
        label.setBounds(310, 30, 274, 50);
        label.setText("현재 온도 그래프");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        getContentPane().add(label);

        // IP 주소 라벨
        JLabel ipLabel = new JLabel();
        try {
            InetAddress address = InetAddress.getLocalHost();
            String ipAddress = address.getHostAddress();
            ipLabel.setText("접속 IP 주소: " + ipAddress);
        } catch (Exception e) {
            ipLabel.setText("접속 IP 주소를 가져올 수 없습니다.");
        }
        ipLabel.setBounds(50, 500, 300, 30);
        getContentPane().add(ipLabel);



        setVisible(true);
    }

    // 그래프를 생성하고 데이터를 업데이트하는 메소드
    public void createAndShowChart(ArrayList<Double> temperatureData) {
        // 그래프 데이터셋 생성
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm");

        for (int i = 0; i < temperatureData.size(); i++) {
            double temperature = temperatureData.get(i);
            dataset.addValue(temperature, "온도", timeFormat.format(new Date()) + " (" + temperature + ")");
        }

        // 라인 차트 생성
        JFreeChart lineChart = ChartFactory.createLineChart("","시간", "온도 (섭씨)", dataset);

        // 배경을 투명하게 설정
        lineChart.setBackgroundPaint(new Color(0, 0, 0, 0));
        // 차트 배경을 투명하게 설정
        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0, 0, 0, 0));

        // 선 색깔 변경
        plot.setOutlinePaint(Color.PINK); // 아웃라인
        plot.getRenderer().setSeriesPaint(10, Color.PINK);

        // 차트 패널 생성하고 JFrame에 추가
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(600, 300)); // 그래프 패널 크기 조정
        chartPanel.setBounds(50, 80, 600, 300); // 레이아웃 크기 조정
        getContentPane().add(chartPanel);

    }
}
