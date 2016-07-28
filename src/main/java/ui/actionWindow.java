package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Created by Андрей on 28.07.2016.
 */
class ActionWindow extends JFrame{
    ActionWindow(Archiver parent, int type){
        createUI(parent,type);
    }
    private int WIDTH,HEIGHT;
    private ActionWindow instance=this;

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

    private JPanel initTextField(JPanel panel,String fieldName, String fieldPath, String fieldOldName){
        JTextField name = new JTextField(fieldName);
        name.setBounds(10, 10, WIDTH - 20, 20);
        panel.add(name);
        if (!fieldPath.equals("")){
            JTextField path = new JTextField(fieldPath);
            path.setBounds(10, 70, WIDTH - 20, 20);
            panel.add(path);
        }
        if (!fieldOldName.equals("")){
            JTextField oldName = new JTextField(fieldOldName);
            oldName.setBounds(10, 40, WIDTH - 20, 20);
            panel.add(oldName);
        }
        return panel;
    }
    private JPanel createPanel(int type){
        JPanel jp = new JPanel(null);
        jp.setBounds(0, 0, WIDTH, HEIGHT);
        JButton ok = new JButton("OK");
        ok.setBounds(WIDTH/2 - 30,HEIGHT - 70, 60, 20);
        jp.add(ok);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                instance.dispatchEvent(new WindowEvent(instance, WindowEvent.WINDOW_CLOSING));
            }
        });
        switch (type) {
            case 1:
                return initTextField(jp,"Name", "Path", "");
            case 2:
                return initTextField(jp,"Name", "Path", "Old Name");
            case 3:
                return initTextField(jp,"Name", "", "");
        }
        return jp;
    }
}
