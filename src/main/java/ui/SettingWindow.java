package ui;

import Util.Settings;
import Util.ZipPack;

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
        setBounds((screenSize.width-mainPanel.getWidth()-10)/2,(screenSize.height-mainPanel.getHeight()-20)/2,mainPanel.getWidth()+20,mainPanel.getHeight()+20);
        setVisible(true);
    }

    private JPanel createMainPanel(){
        JPanel mainPanel = new JPanel(null);
        for (Component c:getCheckBoxes())
            mainPanel.add(c);
        mainPanel.add(getCompLvlText());
        mainPanel.add(createComboBox());
        for(Component c:mainPanel.getComponents())
            c.setVisible(true);
        int height=0;
        for(Component c:mainPanel.getComponents())
            height+=c.getHeight()+5;
        mainPanel.setBounds(0,0, WIDTH+10,height+5);
        mainPanel.setVisible(true);
        return mainPanel;
    }

    private JLabel getCompLvlText(){
        JLabel text = new JLabel("Compression level");
        text.setBounds(5,65,150,20);
        text.setVisible(true);
        return text;
    }

    private JComboBox createComboBox(){
        final JComboBox level = new JComboBox(new String[]{"Default", "No compression", "Best speed", "Best compression"});
        int compLvl=ZipPack.getCompressionLevel();
        switch (compLvl){
            case -1:
                level.setSelectedIndex(0);
                break;
            case 0:
                level.setSelectedIndex(1);
                break;
            case 1:
                level.setSelectedIndex(2);
                break;
            case 9:
                level.setSelectedIndex(3);
                break;
        }
        level.setBounds(130,65,WIDTH-130,20);
        level.addActionListener(new switchLevelActionListener(level));
        level.setVisible(true);
        return level;
    }

    private JCheckBox[] getCheckBoxes(){
        JCheckBox[] ret = new JCheckBox[3];
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
        ret[2]=new JCheckBox("SaveCompressionLevel", Settings.getSaveCompressionLevel());
        ret[2].setBounds(5,45,WIDTH,20);
        ret[2].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.setSaveCompressionLevel(!Settings.getSaveCompressionLevel());
            }
        });
        return ret;
    }

    private class switchLevelActionListener implements ActionListener{
        private JComboBox level;
        switchLevelActionListener(JComboBox l){level=l;}
        public void actionPerformed(ActionEvent e) {
            switch (level.getSelectedIndex()){
                case 0:
                    ZipPack.setCompressionLevel(-1);
                    break;
                case 1:
                    ZipPack.setCompressionLevel(0);
                    break;
                case 2:
                    ZipPack.setCompressionLevel(1);
                    break;
                case 3:
                    ZipPack.setCompressionLevel(9);
                    break;
            }
        }
    }
}
