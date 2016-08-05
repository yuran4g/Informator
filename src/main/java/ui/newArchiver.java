package ui;

import fileHelper.EntityList;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Андрей on 03.08.2016.
 */
public class NewArchiver extends JFrame {
    private LinkedList<Row> rows = new LinkedList<Row>();
    private NewArchiver instance = this;
    private boolean pressed;
    private int dy, dx, left, top;
    private final int WIDTH = 260;
    private ActionWindow dialogWindow = new ActionWindow();
    private final static Logger logger = Logger.getLogger(NewArchiver.class);
    private JPanel mainPanel;
    private Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();

    public NewArchiver(){
        setLayout(null);
        mainPanel=createMainPanel();
        add(mainPanel);
        left = (int) screenSize.getWidth() - WIDTH - 30;
        top = (int) screenSize.getHeight() - mainPanel.getHeight() - 30;
        setLocation(left - 40, top - 80);
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
        main.add(dataPanel);
        main.setSize(WIDTH, dataPanel.getHeight()+Row.TextAndIconSize/2);
        main.setVisible(true);
        main.addMouseListener(new MyMouseListener());
        main.addMouseMotionListener(new MyMouseMotionAdapter());
        return main;
    }

    private JPanel createDataPanel() {
        JPanel ret = new JPanel(null);
        rows.clear();
        for (int i = 0; i < EntityList.getEntities().size(); i++)
            rows.add(new Row(EntityList.getEntities().get(i).getName(),EntityList.getEntities().get(i).getLink(),i));
        int top=5,left=5;
        for (Row r:rows){
            JPanel element = r.getPanel();
            element.setLocation(left,top);
            ret.add(element);
            top+=Row.HEIGHT;
        }
        for (Component c: ret.getComponents())
            c.setVisible(true);
        ret.setVisible(true);
        ret.setSize(WIDTH,top+5);
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

    private void showMessageBox(String s){
        JOptionPane.showMessageDialog(this,s,"Error",JOptionPane.ERROR_MESSAGE);
    }

    private class Row {
        static final int HEIGHT = 19, WIDTH = 250, TextAndIconSize=15,MARGIN=(HEIGHT-TextAndIconSize)/2;
        int number;
        String name,path;
        JPanel container;
        JLabel text,remove,change,clean,archive;
        final String[] icons={"clear.png","modify.png","clean.png","archive.png"};// add resources\
        Row(String Name, String Path, int Number){
            name=Name;
            path=Path;
            number=Number;
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
            //text.setFont(new Font(null,0,TextAndIconSize*10));
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
            remove.setSize(TextAndIconSize,TextAndIconSize);
            change.setSize(TextAndIconSize,TextAndIconSize);
            clean.setSize(TextAndIconSize,TextAndIconSize);
            archive.setSize(TextAndIconSize,TextAndIconSize);
            change.setLocation(WIDTH-TextAndIconSize*4-MARGIN*4,top);
            remove.setLocation(WIDTH-TextAndIconSize*3-MARGIN*3,top);
            archive.setLocation(WIDTH-TextAndIconSize*2-MARGIN*2,top);
            clean.setLocation(WIDTH-TextAndIconSize-MARGIN,top);
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
                EntityList.removeEntity(EntityList.getEntities().get(number));
                updatePanel();
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
                            EntityList.getEntities().get(number).clean();
                        } catch (Exception ex) {
                            logger.error("Can't clean entity: ", ex);
                            showMessageBox("Can't clean path");
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
                            String pathToArchive = EntityList.getEntities().get(number).archive();
                            Runtime.getRuntime().exec("explorer "+pathToArchive.replaceAll("\\\\[^\\\\]*zip$", ""));
                        } catch (IOException ex) {
                            logger.error("Can not archive path = " + path);
                            showMessageBox("Can not archive path");
                        }
                    }
                }).start();
            }

            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        }
    }

    private class addMouseListener implements MouseListener{
        public void mouseClicked(MouseEvent e) {
            dialogWindow.ShowAdd();
            while (dialogWindow.isVisible()) {
                try {
                    Thread.currentThread().wait(100);
                } catch (InterruptedException ex) {
                    logger.error("Can't add entity: ",ex);
                    showMessageBox(ex.toString());
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
                EntityList.saveEntityList();
            }
            catch (Exception ex){
                logger.error(ex);
            }
            instance.dispatchEvent(new WindowEvent(instance, WindowEvent.WINDOW_CLOSING));
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
