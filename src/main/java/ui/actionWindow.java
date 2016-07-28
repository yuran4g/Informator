package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Created by Андрей on 28.07.2016.
 */
class actionWindow extends JFrame{
    private JTextField name,path,oldName;
    actionWindow(Archiver parent, int type){
        createUI(parent,type);
    }
    private int WIDTH,HEIGHT;
    private actionWindow instance=this;

    private void createUI(Archiver parent,int type) {
        WIDTH=parent.WIDTH;
        HEIGHT=parent.HEIGHT;
        setLayout(null);
        setBounds(parent.left+10,parent.top+10,WIDTH+10,HEIGHT);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        add(createPanel(type));
        for (Component c:getComponents()){
            c.setVisible(true);
        }
        setVisible(true);
    }

    private JPanel createPanel(int type){
        JPanel jp = new JPanel(null);
        jp.setBounds(0, 0, WIDTH, HEIGHT);
        JButton ok = new JButton("OK");
        ok.setBounds(WIDTH/2 - 30,HEIGHT - 70, 60, 20);
        jp.add(ok);
        switch (type) {
            case 1:
                name = new JTextField("Name");
                path = new JTextField("Path");
                name.setBounds(10, 10, WIDTH - 20, 20);
                path.setBounds(10, 40, WIDTH - 20, 20);
                jp.add(name);
                jp.add(path);
                break;
            case 2:
                oldName = new JTextField("Old Name");
                name = new JTextField("Name");
                path = new JTextField("Path");
                oldName.setBounds(10, 10, WIDTH - 20, 20);
                name.setBounds(10, 40, WIDTH - 20, 20);
                path.setBounds(10, 70, WIDTH - 20, 20);
                jp.add(name);
                jp.add(path);
                jp.add(oldName);
                break;
            case 3:
                name = new JTextField("Name");
                name.setBounds(10, 10, WIDTH - 20, 20);
                jp.add(name);
                break;
        }
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                instance.dispatchEvent(new WindowEvent(instance, WindowEvent.WINDOW_CLOSING));
            }
        });
        return jp;
    }
}
