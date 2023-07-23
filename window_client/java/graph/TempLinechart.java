package graph;

import javax.swing.*;
import java.awt.*;

public class TempLinechart extends JFrame{

    private static final String DB_url = "jdbc:mysql://localhost:3306/temperature_db?useSSL=false&serverTimezone=Asia/Seoul";
    private static final String DB_user = "root";
    private static final String DB_password = "ejk127ekqls$";

    public TempLinechart() {
        JFrame frm = new JFrame();
        JLabel label = new JLabel();

        frm.setTitle("온도 그래프");
        frm.setLocationRelativeTo(null); //기운데 배치
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //닫을 때 종료 설정
        frm.getContentPane().setLayout(null); //레이아웃
        frm.setBackground(Color.LIGHT_GRAY);
        frm.setSize(600,400);

        //버튼
        JButton btn_temp = new JButton("현재 온도");
        btn_temp.setBounds(300,300,120,30);
        frm.getContentPane().add(btn_temp);
        //버튼 동작
        JLabel btn_label1 = new JLabel();
        btn_temp.addActionListener(event -> {btn_label1.setText("현재온도는 : ");});
        btn_label1.setBounds(300, 330, 274, 50);
        frm.getContentPane().add(btn_label1);

        // 라벨
        label.setBounds(300, 30, 274, 50);
        label.setText("현재 온도 그래프");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        frm.getContentPane().add(label);

        frm.setVisible(true);

    }
    public static void main(String[] args) {
            new TempLinechart();
    }
}
