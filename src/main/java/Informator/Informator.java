package Informator;

import org.apache.log4j.Logger;
import osData.PCDataGrabber;
import osData.Properties;

import javax.swing.*;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import Util.*;

public class Informator extends JFrame {
    private final static Logger logger = Logger.getLogger(Informator.class);
    private int left, top, dy, dx;
    private boolean pressed, ready;
    private static JPopupMenu menu;
    private static ArrayList<String> params;
    private static JLabel jl = new JLabel();

    private static final int IMAGE_WIDTH = 30,IMAGE_HEIGHT=30;
    private static final String ReadyIcon="resources\\images.png",NotReadyIcon="resources\\Red_icon.png";

    public Informator() {
        createUI();
        params = new ArrayList<String>(Properties.getInstance().getUserProperties());
        initMenu();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    logger.debug("Start grabbing OS data");
                    PCDataGrabber.getInstance().grabData(Properties.getInstance().getUserProperties());
                    logger.debug("OS data successfully grabbed");
                    ready = true;
                    Check();
                    jl.setIcon(scaledIcon(ReadyIcon));
                    try {
                        Thread.sleep(300000);//reload data every 5min
                    } catch (InterruptedException e) {
                        logger.error("Can not sleep the thread");
                    }
                }
            }
        });
        thread.start();
    }

    public Informator(String[] args){
        PCDataGrabber.getInstance().grabData(Properties.getInstance().getUserProperties());
        params = new ArrayList<String>(Properties.getInstance().getUserProperties());
        SaveToFile(args[0]);
    }

    private void createUI(){
        setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        left = (int) screenSize.getWidth();
        top = (int) screenSize.getHeight();
        setLocation(left - 40, top - 80);
        setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        add(createIconPanel());
        setVisible(true);
    }

    private ImageIcon scaledIcon(String path){
        return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, java.awt.Image.SCALE_SMOOTH));
    }

    private JPanel createIconPanel(){
        jl.setIcon(scaledIcon(NotReadyIcon));
        jl.setVisible(true);
        jl.setBounds(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        JPanel jp = new JPanel(null);
        jp.setVisible(true);
        jp.setBounds(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        jp.setOpaque(false);
        jp.add(jl);
        jp.addMouseListener(new TooltipListener());
        jp.addMouseMotionListener(new MyMouseMotionAdapter());
        return jp;
    }

    private void initMenu(){
        menu = new JPopupMenu();
        menu = addMenuItems(menu,Properties.getInstance().getUserProperties());
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.debug("Close program"); System.exit(0);
            }
        });
        menu.add(exit);
    }

    private JPopupMenu addMenuItems(JPopupMenu menu,ArrayList<String> params){
        JCheckBoxMenuItem cbMenuItem;
        for (String pr : params) {
            cbMenuItem = new JCheckBoxMenuItem(pr);
            cbMenuItem.setSelected(true);
            cbMenuItem.addActionListener(new CBActionListener());
            cbMenuItem.setUI(new StayOpenCheckBoxMenuItemUI());
            menu.add(cbMenuItem);
        }
        return menu;
    }

    private static void Check(){
        for (String s:Properties.getInstance().getUserProperties()){
            JCheckBoxMenuItem curr = ((JCheckBoxMenuItem)(menu.getSubElements()[Properties.getInstance().getUserProperties().indexOf(s)]));
            if (PCDataGrabber.Contains(s)) {
                curr.setEnabled(true);
            }
            else {
                params.remove(s);
                curr.setEnabled(false);
                curr.setSelected(false);
            }
        }
    }

    private class CBActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            menu.setVisible(true);
            if (((AbstractButton) e.getSource()).getModel().isSelected()) params.add(e.getActionCommand());
            else params.remove(e.getActionCommand());
            Properties.getInstance().setUserProperties(params);
        }
    }

    private class MyMouseMotionAdapter extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            if (!pressed) return;
            top = top + (e.getY() - dy);
            left = left + (e.getX() - dx);
            setLocation(left, top);
        }
    }

    private class TooltipListener implements MouseListener{
        TooltipListener(){
            ToolTipManager.sharedInstance().setInitialDelay(0);
        }

        public void mouseEntered(MouseEvent e){
            if(!ready) showTooltip(e,"Data isn't ready");
        }

        public void mouseExited(MouseEvent e){hideTooltip();}

        private void showTooltip(MouseEvent e,String message) {
            JComponent component = (JComponent) e.getSource();
            component.setToolTipText(message);
            MouseEvent phantom = new MouseEvent(component, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, 0, 10, 0, false);
            ToolTipManager.sharedInstance().setEnabled(true);
            ToolTipManager.sharedInstance().mouseMoved(phantom);
        }

        private void hideTooltip(){ToolTipManager.sharedInstance().setEnabled(false);}

        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == 1) {
                if (!ready) return;
                String grabbedData = PCDataGrabber.getGrabbedData(params);
                ClipboardAccess.getInstance().copyToClipboard(grabbedData);
                showTooltip(e,"Data copied to buffer");
                logger.debug("OS data successfully copied to clipboard");
            } else if (e.getButton() == 3) {
                logger.info("Show context menu");
                menu.setVisible(true);
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        public void mousePressed(MouseEvent e) {
            if (e.getButton()!=1) return;
            dy = e.getY();
            dx = e.getX();
            pressed = true;
        }

        public void mouseReleased(MouseEvent e) {
            pressed = false;
        }
    }

    private void SaveToFile(String Path){
        String Data = PCDataGrabber.getGrabbedData(params);
        try {
            PrintWriter writer = new PrintWriter(Path);
            writer.write(Data);
            writer.close();
        }
        catch (Exception e){
            logger.error("Can not save to file " + Path);
        }
    }
}

class StayOpenCheckBoxMenuItemUI extends BasicCheckBoxMenuItemUI {
    @Override
    protected void doClick(MenuSelectionManager msm) {
        menuItem.doClick(0);
    }
}