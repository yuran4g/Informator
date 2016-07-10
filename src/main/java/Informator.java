import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by yksenofontov on 08.07.2016.
 */
public class Informator extends JFrame {
    private int left, top, dy, dx;
    private boolean pressed, ready;
    private static JPopupMenu menu;
    private static ArrayList<String> params;
    private static JLabel jl;

    public Informator() {
        createUI();
        params = new ArrayList<String>(Properties.allProperties);
        initMenu();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    PCDataGrabber.getInstance().grabData(Properties.allProperties);
                    ready = true;
                    Check();
                    jl.setIcon(new ImageIcon(new ImageIcon("resources\\images.png").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
                    try {
                        Thread.sleep(300000);//reload data every 5min
                    } catch (InterruptedException e) {
                        System.out.println("Can not sleep the thread");
                    }
                }
            }
        });
        thread.start();
    }

    private void createUI(){
        setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        left = (int) screenSize.getWidth();
        top = (int) screenSize.getHeight();
        setLocation(left - 40, top - 80);
        setSize(30, 30);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jl = new JLabel(new ImageIcon(new ImageIcon("resources\\Red_icon.png").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
        jl.setVisible(true);
        jl.setBounds(0, 0, 30, 30);
        JPanel jp = new JPanel(null);
        jp.setVisible(true);
        jp.setBounds(0, 0, 30, 30);
        jp.setOpaque(false);
        jp.add(jl);
        jp.addMouseListener(new MyMouseAdapter());
        jp.addMouseMotionListener(new MyMouseMotionAdapter());
        setAlwaysOnTop(true);
        add(jp);
        setVisible(true);
    }

    private void initMenu(){
        menu = new JPopupMenu();
        JMenuItem menuItem;
        JCheckBoxMenuItem cbMenuItem;
        for (String pr : Properties.allProperties) {
            cbMenuItem = new JCheckBoxMenuItem(pr);
            cbMenuItem.setSelected(true);
            cbMenuItem.addActionListener(new CBActionListener());
            cbMenuItem.setUI(new StayOpenCheckBoxMenuItemUI());
            menu.add(cbMenuItem);
        }
        menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(menuItem);
    }

    private static void Check(){
        for (String s:Properties.allProperties){
            JCheckBoxMenuItem curr = ((JCheckBoxMenuItem)(menu.getSubElements()[Properties.allProperties.indexOf(s)]));
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

    private class MyMouseAdapter extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (e.getButton()!=1) return;
            dy = e.getY();
            dx = e.getX();
            pressed = true;
        }

        public void mouseReleased(MouseEvent e) {
            pressed = false;
        }

        private void showTooltip(MouseEvent e,String message) {
            JComponent component = (JComponent) e.getSource();
            component.setToolTipText(message);
            MouseEvent phantom = new MouseEvent(component, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, 0, 0, 0, false);
            ToolTipManager.sharedInstance().mouseMoved(phantom);
        }

        public void mouseClicked(MouseEvent e) {
            ///1-left,3-right
            if (e.getButton() == 1) {
                if (ready) {
                    String grabbedData = PCDataGrabber.getGrabbedData(params);
                    ClipboardAccess.getInstance().copyToClipboard(grabbedData);
                    showTooltip(e,"Done");
                }
                else {
                    showTooltip(e,"Data isn't ready");
                }
            } else if (e.getButton() == 3) {
                menu.setVisible(true);
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}

class StayOpenCheckBoxMenuItemUI extends BasicCheckBoxMenuItemUI {

    @Override
    protected void doClick(MenuSelectionManager msm) {
        menuItem.doClick(0);
    }
}