package ui;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Андрей on 27.07.2016.
 */
public class Archiver extends JFrame {
    private final static Logger logger = Logger.getLogger(Archiver.class);
    private int left, top;
    private static final int HEIGHT=140,WIDTH=160;
    private JTextField name,path;

    public Archiver(){
        createUI();
    }

    private void createUI() {
        setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        left = (int) screenSize.getWidth();
        top = (int) screenSize.getHeight();
        setBounds(left - WIDTH - 30, top - 80 - HEIGHT - 20, WIDTH + 10, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        add(createPanel());
        setVisible(true);
    }

    private JPanel createPanel(){
        JPanel jp = new JPanel(null);
        jp.setVisible(true);
        jp.setBounds(0, 0, WIDTH, HEIGHT);
        name=new JTextField("Name");
        path=new JTextField("Path");
        name.setBounds(10,10,WIDTH-20,20);
        path.setBounds(10,40,WIDTH-20,20);
        jp.add(name);
        jp.add(path);
        JButton ok = new JButton("Run");
        ok.setBounds(10,70,70,20);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Run();
            }
        });
        jp.add(ok);
        return jp;
    }

    private void Run() {
        String Name=name.getText(),Path=path.getText();
        //someFunction(Name,Path);
    }
}
