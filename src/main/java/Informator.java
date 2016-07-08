package src.main.java;

import com.sun.deploy.Environment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by yksenofontov on 08.07.2016.
 */
public class Informator extends JFrame {
    public Informator() {
        setLayout(null);
        setLocation(10, 10);
        setSize(20, 20);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel jl = new JLabel(new ImageIcon(new ImageIcon("resources\\images.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        jl.setVisible(true);
        jl.setBounds(0,0,20,20);
        JPanel jp = new JPanel(null);
        jp.setVisible(true);
        jp.setBounds(0,0,20,20);
        jp.setOpaque(false);
        jp.add(jl);
        jp.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ///1-left,3-right
                if (e.getButton()==1){

                }
                else if (e.getButton()==3){
                    JMenu menu;
                    JMenuItem menuItem;
                    JCheckBoxMenuItem cbMenuItem;
                    menu = new JMenu();
                    for (String pr:Properties.allProperties) {
                        cbMenuItem = new JCheckBoxMenuItem(pr);
                        cbMenuItem.setSelected(Properties.getInstance().getUserProperties().contains(pr));
                        menu.add(cbMenuItem);
                    }
                    menuItem = new JMenuItem("Exit");
                    menuItem.addActionListener(new MenuActionListener()  {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                        }
                    });
                    menu.add(menuItem);
                    menu.setVisible(true);
                }
            }
        });
        setAlwaysOnTop(true);
        add(jp);
        setVisible(true);

    }
    class MenuActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //закрыть приложение
        }
    }



//
//    readProperties
//
//    saveProperties
//     getPCData
//
//    CopyToClipboard
}
