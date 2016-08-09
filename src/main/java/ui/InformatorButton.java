package ui;

import org.apache.log4j.Logger;
import osData.PCDataGrabber;
import osData.Properties;

import javax.swing.*;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import Util.*;

public class InformatorButton extends JLabel {
    private final static Logger logger = Logger.getLogger(InformatorButton.class);
    private boolean ready;
    private static JPopupMenu menu;
    private static ArrayList<String> params;
    private int left=0,top;

    private static final int IMAGE_WIDTH = NewArchiver.Row.TextAndIconSize,IMAGE_HEIGHT=NewArchiver.Row.TextAndIconSize;;
    private static final String ReadyIcon="resources\\images.png",NotReadyIcon="resources\\Red_icon.png";

    public InformatorButton(int Left,int Top) {
        top=Top;
        left=Left;
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
                    setIcon(scaledIcon(ReadyIcon));
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

    private void createUI(){
        setLocation(left, top);
        setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
        setBackground(new Color(0, 0, 0, 0));
        setIcon(scaledIcon(NotReadyIcon));
        addMouseListener(new TooltipListener());
        setVisible(true);
    }

    private ImageIcon scaledIcon(String path){
        return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, java.awt.Image.SCALE_SMOOTH));
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

        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
    }
}

class StayOpenCheckBoxMenuItemUI extends BasicCheckBoxMenuItemUI {
    @Override
    protected void doClick(MenuSelectionManager msm) {
        menuItem.doClick(0);
    }
}