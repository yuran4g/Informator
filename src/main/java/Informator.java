import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by yksenofontov on 08.07.2016.
 */
public class Informator extends JFrame {
    int left,top,dy,dx;
    boolean pressed;
    ArrayList<String> params;
    public Informator() {
        setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        left = (int)screenSize.getWidth();
        top = (int)screenSize.getHeight();
        setLocation( left-40,  top-80);
        setSize(30, 30);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel jl = new JLabel(new ImageIcon(new ImageIcon("resources\\images.png").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
        jl.setVisible(true);
        jl.setBounds(0,0,30,30);
        JPanel jp = new JPanel(null);
        jp.setVisible(true);
        jp.setBounds(0,0,30,30);
        jp.setOpaque(false);
        jp.add(jl);
        params = new ArrayList<String>();
        for (String pr:Properties.allProperties){
            if (Properties.getInstance().getUserProperties().contains(pr)) params.add(pr);
        }
        jp.addMouseListener(new MyMouseAdapter());
        jp.addMouseMotionListener(new MyMouseMotionAdapter());
        setAlwaysOnTop(true);
        add(jp);
        setVisible(true);
    }
    private class CBActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String p = e.getActionCommand();
            boolean state = ((AbstractButton)e.getSource()).getModel().isSelected();
            if (state) params.add(p);
            else params.remove(p);
            Properties.getInstance().setUserProperties(params);
        }
    }

    class MyMouseMotionAdapter extends MouseMotionAdapter{
        public void mouseDragged(MouseEvent e){
            if (!pressed)return;
            top=top+(e.getY()-dy);
            left=left+(e.getX()-dx);
            setLocation(left,top);
        }
    }

    class MyMouseAdapter extends MouseAdapter{
        public void mousePressed(MouseEvent e){
            dy=e.getY();
            dx=e.getX();
            pressed=true;
        }

        public void mouseReleased(MouseEvent e){
            pressed=false;
        }

        public void mouseClicked(MouseEvent e) {
            ///1-left,3-right
            if (e.getButton()==1){
                String t = PCDataGrabber.getInstance().getPCdata(params);//error
                ClipboardAccess.getInstance().copyToClipboard(t);
            }
            else if (e.getButton()==3){
                JPopupMenu menu = new JPopupMenu();
                JMenuItem menuItem;
                JCheckBoxMenuItem cbMenuItem;
                for (String pr:Properties.allProperties) {
                    cbMenuItem = new JCheckBoxMenuItem(pr);
                    if (params.contains(pr)) cbMenuItem.setSelected(true);
                    else cbMenuItem.setSelected(false);
                    cbMenuItem.addActionListener(new CBActionListener());
                    menu.add(cbMenuItem);
                }
                menuItem = new JMenuItem("Exit");
                menuItem.addActionListener(new ActionListener()  {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });
                menu.add(menuItem);
                menu.setVisible(true);
                menu.show(e.getComponent(),e.getX(),e.getY());
            }
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
