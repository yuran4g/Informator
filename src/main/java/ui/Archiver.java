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
    private int left, top;
    private static final int HEIGHT=240,WIDTH=260;
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
        JButton archive = new JButton("Archive"),add=new JButton("Add"),change=new JButton("Change"),delete=new JButton("Delete"),clear=new JButton("Clear");
        add.setBounds(10,70,70,20);
        change.setBounds(90,70,70,20);
        delete.setBounds(180,70,70,20);
        archive.setBounds(10,100,70,20);
        clear.setBounds(90,100,70,20);
        archive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

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
