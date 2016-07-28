package ui;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import fileHelper.*;

/**
 * Created by Андрей on 27.07.2016.
 */
public class Archiver extends JFrame {
    private final static Logger logger = Logger.getLogger(Archiver.class);
    int left, top;
    final int HEIGHT=180,WIDTH=260;
    private Archiver instance = this;

    public Archiver(){
        createUI();
    }

    private void createUI() {
        setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        left = (int) screenSize.getWidth();
        top = (int) screenSize.getHeight();
        left = left - WIDTH - 30;
        top = top - 80 - HEIGHT - 20;
        setBounds(left, top, WIDTH + 10, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        add(createPanel());
        setVisible(true);
    }

    private JPanel createPanel(){
        JPanel jp = new JPanel(null);
        jp.setVisible(true);
        jp.setBounds(0, 0, WIDTH, HEIGHT);
        JTextField name = new JTextField("Name");
        JTextField path = new JTextField("Path");
        name.setBounds(10,10,WIDTH-20,20);
        path.setBounds(10,40,WIDTH-20,20);
        jp.add(name);
        jp.add(path);
        JButton archive = new JButton("Archive"),add=new JButton("Add"),change=new JButton("Change"),delete=new JButton("Delete"),clear=new JButton("Clear");
        add.setBounds(10,70,70,20);
        change.setBounds(90,70,80,20);
        delete.setBounds(180,70,70,20);
        archive.setBounds(10,100,WIDTH/2-20,20);
        clear.setBounds(WIDTH/2+10,100,WIDTH/2-20,20);
        archive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ActionWindow(instance,1);
            }
        });
        change.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ActionWindow(instance,2);
            }
        });
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ActionWindow(instance,3);
            }
        });
        jp.add(archive);
        jp.add(clear);
        jp.add(add);
        jp.add(change);
        jp.add(delete);
        return jp;
    }
}
