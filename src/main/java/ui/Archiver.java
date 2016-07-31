package ui;

import Util.Cleaner;
import Util.ZipPack;
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
    int left, top;
    private final int HEIGHT=180,WIDTH=260;
    private ActionWindow dialogWindow;

    public Archiver(){
        createUI();
        dialogWindow=new ActionWindow(this);
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
        JButton archive = new JButton("Archive"),add=new JButton("Add"),change=new JButton("Change"),delete=new JButton("Delete"),clear=new JButton("Clear");
        add.setBounds(10,10,70,20);
        change.setBounds(90,10,80,20);
        delete.setBounds(180,10,70,20);
        archive.setBounds(10,40,WIDTH/2-20,20);
        clear.setBounds(WIDTH/2+10,40,WIDTH/2-20,20);
        archive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Cleaner.Delete();
            }
        });
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Cleaner.Delete();
            }
        });
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogWindow.Show('a');
            }
        });
        change.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogWindow.Show('c');
            }
        });
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogWindow.Show('r');
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
