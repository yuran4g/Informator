package ui;

import Util.Settings;
import fileHelper.Entity;
import fileHelper.EntityList;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Андрей on 03.08.2016.
 */
public class NewArchiver extends JFrame {
    private final LinkedList<Row> rows = new LinkedList<Row>();
    private final NewArchiver instance = this;
    private boolean pressed;
    private int dy, dx, left, top;
    private final int WIDTH = 260;
    private ActionWindow dialogWindow = new ActionWindow();
    private final static Logger logger = Logger.getLogger(NewArchiver.class);
    private JPanel mainPanel;
    private Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    private InformatorButton informator;

    public NewArchiver(){
        setLayout(null);
        mainPanel=createMainPanel();
        add(mainPanel);
        left = (int) screenSize.getWidth() - WIDTH - 30;
        top = (int) screenSize.getHeight() - mainPanel.getHeight() - 30;
        setLocation(left - 40, top - 40);
        setSize(WIDTH,mainPanel.getHeight());
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setVisible(true);
    }

    private JPanel createMainPanel(){
        JPanel main=new JPanel(null);
        main.setBackground(new Color(255,255,255,0));
        JPanel dataPanel = createDataPanel();
        main.add(createExitButton());
        main.add(createAddButton());
        main.add(createSettingsButton());
        main.add(createInformatorButton());
        main.add(dataPanel);
        main.setSize(WIDTH, dataPanel.getHeight()+Row.TextAndIconSize);
        main.setVisible(true);
        main.addMouseListener(new MyMouseListener());
        main.addMouseMotionListener(new MyMouseMotionAdapter());
        return main;
    }

    private JLabel createInformatorButton(){
        if (informator==null) informator=new InformatorButton(0,rows.size()>0?(Row.HEIGHT*rows.size()+Row.TextAndIconSize/2):(20+Row.TextAndIconSize/2));
        else informator.setLocation(0,rows.size()>0?(Row.HEIGHT*rows.size()+Row.TextAndIconSize/2):(20+Row.TextAndIconSize/2));
        return informator;
    }

    private void fillRows(){
        rows.clear();
        for (int i = 0; i < EntityList.getEntities().size(); i++)
            rows.add(new Row(EntityList.getEntities().get(i).getName(),EntityList.getEntities().get(i).getLink(),i));
    }

    private JPanel[] getRowsPanels(){
        int top=5,left=5;
        JPanel[] ret = new JPanel[rows.size()];
        for (int i=0;i<rows.size();i++){
            ret[i]=rows.get(i).getPanel();
            ret[i].setLocation(left,top);
            ret[i].setVisible(true);
            top+=Row.HEIGHT;
        }
        return ret;
    }

    private JPanel createDataPanel() {
        JPanel ret = new JPanel(null);
        fillRows();
        for (JPanel rowPanel:getRowsPanels())
            ret.add(rowPanel);
        for (Component c: ret.getComponents())
            c.setVisible(true);
        ret.setVisible(true);
        if (rows.size()>0)
            ret.setSize(WIDTH,Row.HEIGHT*rows.size()+10);
        else
            ret.setSize(WIDTH,30);
        ret.setLocation(0,Row.TextAndIconSize/2);
        return ret;
    }

    private void updatePanel(){
        remove(mainPanel);
        mainPanel=createMainPanel();
        top = (int) screenSize.getHeight() - mainPanel.getHeight() - 30;
        setLocation(left - 40, top - 80);
        setSize(WIDTH,mainPanel.getHeight());
        add(mainPanel);
        invalidate();
        repaint();
    }

    private JLabel createExitButton(){
        JLabel exit = new JLabel(new ImageIcon(new ImageIcon("resources//exit.png").getImage().getScaledInstance(Row.TextAndIconSize, Row.TextAndIconSize, java.awt.Image.SCALE_SMOOTH)));
        exit.setBounds(WIDTH-Row.TextAndIconSize,0,Row.TextAndIconSize,Row.TextAndIconSize);
        exit.setVisible(true);
        exit.addMouseListener(new exitMouseListener());
        return exit;
    }

    private JLabel createAddButton(){
        JLabel add = new JLabel(new ImageIcon(new ImageIcon("resources//add.png").getImage().getScaledInstance(Row.TextAndIconSize, Row.TextAndIconSize, java.awt.Image.SCALE_SMOOTH)));
        add.setBounds(0,0,Row.TextAndIconSize,Row.TextAndIconSize);
        add.setVisible(true);
        add.addMouseListener(new addMouseListener());
        return add;
    }

    private JLabel createSettingsButton(){
        JLabel settings = new JLabel(new ImageIcon(new ImageIcon("resources//settings.png").getImage().getScaledInstance(Row.TextAndIconSize, Row.TextAndIconSize, java.awt.Image.SCALE_SMOOTH)));
        if (rows.size()>0)
            settings.setBounds(WIDTH-Row.TextAndIconSize,Row.HEIGHT*rows.size()+Row.TextAndIconSize/2,Row.TextAndIconSize,Row.TextAndIconSize);
        else
            settings.setBounds(WIDTH-Row.TextAndIconSize,20+Row.TextAndIconSize/2,Row.TextAndIconSize,Row.TextAndIconSize);
        settings.setVisible(true);
        settings.addMouseListener(new settingsMouseListener());
        return settings;
    }

    private void showMessageBox(String s){
        JOptionPane.showMessageDialog(this,s,"Error",JOptionPane.ERROR_MESSAGE);
    }

    class Row {
        static final int HEIGHT = 19, WIDTH = 250, TextAndIconSize=15,MARGIN=(HEIGHT-TextAndIconSize)/2;
        int number;
        String name,path;
        JPanel container;
        JLabel text,remove,change,clean,archive;
        final String[] icons={"delete.png","modify.png","clear.png","archive.png"};// add resources\
        Row(String Name, String Path, int Number) {
            name = Name;
            File f = new File(Path);
            if (f.isFile()) path = f.getAbsolutePath().replaceAll(f.getName(), "");
            else path = f.getAbsolutePath();
            number = Number;
            initLabels();
            createContainer();
        }

        private JPanel getPanel(){
            return container;
        }

        private void createContainer() {
            container=new JPanel(null);
            container.setBackground(new Color(255,255,255));
            container.setSize(WIDTH,HEIGHT);
            container.add(text);
            container.add(remove);
            container.add(change);
            container.add(clean);
            container.add(archive);
            for (Component c: container.getComponents())
                c.setVisible(true);
        }

        private void initLabels(){
            text=new JLabel();
            text.setVerticalTextPosition(SwingConstants.CENTER);
            text.addMouseListener(new doubleClickMouseListener());
            remove=new JLabel(scaledIcon("resources\\"+icons[0]));
            change=new JLabel(scaledIcon("resources\\"+icons[1]));
            clean=new JLabel(scaledIcon("resources\\"+icons[2]));
            archive=new JLabel(scaledIcon("resources\\"+icons[3]));
            setBounds();
            addListeners();
            text.setText(name);
        }

        private ImageIcon scaledIcon(String path){
            return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(TextAndIconSize, TextAndIconSize, java.awt.Image.SCALE_SMOOTH));
        }

        private void setBounds(){
            int top=HEIGHT-TextAndIconSize-MARGIN;
            archive.setSize(TextAndIconSize,TextAndIconSize);
            clean.setSize(TextAndIconSize,TextAndIconSize);
            change.setSize(TextAndIconSize,TextAndIconSize);
            remove.setSize(TextAndIconSize,TextAndIconSize);


            archive.setLocation(WIDTH-TextAndIconSize*4-MARGIN*4,top);
            clean.setLocation(WIDTH-TextAndIconSize*3-MARGIN*3,top);
            change.setLocation(WIDTH-TextAndIconSize*2-MARGIN*2,top);
            remove.setLocation(WIDTH-TextAndIconSize-MARGIN,top);
            text.setBounds(MARGIN,MARGIN,WIDTH-TextAndIconSize*4-MARGIN*6,TextAndIconSize);
        }

        private void addListeners(){
            remove.addMouseListener(new deleteMouseListener());
            change.addMouseListener(new changeMouseListener());
            clean.addMouseListener(new clearMouseListener());
            archive.addMouseListener(new archiveMouseListener());
        }

        private class deleteMouseListener implements MouseListener {
            public void mouseClicked(MouseEvent e) {
                try {
                    EntityList.removeEntity(EntityList.getEntities().get(number));
                    updatePanel();
                } catch (Exception ex) {
                    logger.error("Can't remove entity: ",ex);
                }
            }

            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        }

        private class changeMouseListener implements MouseListener{
            public void mouseClicked(MouseEvent e) {
                dialogWindow.ShowChange(EntityList.getEntities().get(number));
                while (dialogWindow.isVisible()) {
                    try {
                        Thread.currentThread().wait(100);
                    } catch (InterruptedException ex) {
                        logger.error("Can't change entity: ",ex);
                    }
                }
                updatePanel();
            }

            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        }

        private class clearMouseListener implements MouseListener{
            public void mouseClicked(MouseEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            clean.setEnabled(false);
                            EntityList.getEntities().get(number).clean();
                        } catch (Exception ex) {
                            logger.error("Can't clean entity: ", ex);
                            showMessageBox("Can't clean path");
                        }
                        finally {
                            clean.setEnabled(true);
                        }
                    }
                }).start();
            }

            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        }

        private class archiveMouseListener implements MouseListener{
            public void mouseClicked(MouseEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        String path = EntityList.getEntities().get(number).getLink();
                        try {
                            archive.setEnabled(false);
                            String pathToArchive = EntityList.getEntities().get(number).archive();
                            Runtime.getRuntime().exec("explorer "+pathToArchive.replaceAll("\\\\[^\\\\]*zip$", ""));
                        } catch (IOException ex) {
                            logger.error("Can not archive path = " + path);
                            showMessageBox("Can not archive path");
                        }
                        finally {
                            archive.setEnabled(true);
                        }
                    }
                }).start();
            }

            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        }

        private class doubleClickMouseListener implements MouseListener {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    try {
                        Runtime.getRuntime().exec("explorer "+path);
                    } catch (IOException e1) {
                        logger.error("Can't open in explorer path: "+path,e1);
                    }
                }
            }

            public void mousePressed(MouseEvent e) {

            }
            public void mouseReleased(MouseEvent e) {

            }
            public void mouseEntered(MouseEvent e) {

            }
            public void mouseExited(MouseEvent e) {

            }
        }
    }

    private class addMouseListener implements MouseListener{
        public void mouseClicked(MouseEvent e) {
            dialogWindow.ShowAdd();
            while (dialogWindow.isVisible()) {
                try {
                    Thread.currentThread().wait(100);
                } catch (InterruptedException ex) {
                    logger.error("Can't add entity: "+ex.getMessage());
                }
            }
            updatePanel();
        }

        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }

    private class exitMouseListener implements MouseListener{
        public void mouseClicked(MouseEvent e) {
            try {
                if (Settings.getSaveEntityOnClose())
                    EntityList.saveEntityList();
                else{
                    Entity.deleteFolder(new File(EntityList.ENTITYFILE));
                }
            } catch (Exception e1) {
                logger.error("Can not save entityList");
                logger.error(e1.getMessage());
            }
            try {
                if (Settings.getDeleteTempFolderOnClose())
                    Entity.deleteFolder(new File("TEMP"));
            } catch (Exception e2) {
                logger.error("Can not delete temp folder");
                logger.error(e2.getMessage());
            }
            Settings.SaveSettings();
            instance.dispatchEvent(new WindowEvent(instance, WindowEvent.WINDOW_CLOSING));
        }

        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }

    private class settingsMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            new SettingWindow();
        }

        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }

    private class MyMouseMotionAdapter extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            if (!pressed) return;
            top = top + (e.getY() - dy);
            left = left + (e.getX() - dx);
            setLocation(left, top);
        }
    }

    private class MyMouseListener implements MouseListener{
        public void mousePressed(MouseEvent e) {
            if (e.getButton()!=1) return;
            dy = e.getY();
            dx = e.getX();
            pressed = true;
        }

        public void mouseReleased(MouseEvent e) {
            pressed = false;
        }

        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }
}