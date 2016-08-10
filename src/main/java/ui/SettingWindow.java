package ui;

import Util.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Андрей on 06.08.2016.
 */
class SettingWindow extends JDialog{
    private final int WIDTH=280;
    SettingWindow(){
        setLayout(null);
        JPanel mainPanel = createMainPanel();
        add(mainPanel);
        setAlwaysOnTop(true);
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-mainPanel.getWidth()-10)/2,(screenSize.height-mainPanel.getHeight()-20)/2,mainPanel.getWidth()+20,mainPanel.getHeight()+40);
        setVisible(true);
    }

    private JPanel createMainPanel(){
        JPanel mainPanel = new JPanel(null);
        for (Component c:getCheckBoxes())
            mainPanel.add(c);
        for(Component c:mainPanel.getComponents())
            c.setVisible(true);
        int height=0;
        for(Component c:mainPanel.getComponents())
            height+=c.getHeight()+5;
        mainPanel.setBounds(0,0, WIDTH+10,height+5);
        mainPanel.setVisible(true);
        return mainPanel;
    }


    private JCheckBox[] getCheckBoxes(){
        JCheckBox[] ret = new JCheckBox[2];
        ret[0]=new JCheckBox("SaveEntityOnClose", Settings.getSaveEntityOnClose());
        ret[0].setBounds(5,5,WIDTH,20);
        ret[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.setSaveEntityOnClose(!Settings.getSaveEntityOnClose());
            }
        });
        ret[1]=new JCheckBox("DeleteTempFolderOnClose", Settings.getDeleteTempFolderOnClose());
        ret[1].setBounds(5,25,WIDTH,20);
        ret[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.setDeleteTempFolderOnClose(!Settings.getDeleteTempFolderOnClose());
            }
        });
        return ret;
    }
}
