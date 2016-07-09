import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by yksenofontov on 08.07.2016.
 */
public class Informator extends JFrame {
    ArrayList<String> params;
    public Informator() {
        setLayout(null);
        setLocation(10, 10);
        setSize(20, 20);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel jl = new JLabel(new ImageIcon(new ImageIcon("resources\\images.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        jl.setVisible(true);
        jl.setBounds(0,0,20,20);
        JPanel jp = new JPanel(null);
        jp.setVisible(true);
        jp.setBounds(0,0,20,20);
        jp.setOpaque(false);
        jp.add(jl);
        params = new ArrayList<String>();
        for (String pr:Properties.allProperties){
            if (Properties.getInstance().getUserProperties().contains(pr)) params.add(pr);
        }
        jp.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ///1-left,3-right
                if (e.getButton()==1){
                    String t = PCDataGrabber.getInstance().getPCdata(params);
                    /*
                    error in line
                    if (param.equals("Java")) result = result + "Java version: " + getJavaVersion().get(0) + "\n";
                    getJavaVersion() return empty list
                    java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
                    version of Windows is correct
                     */
                    ClipboardAccess.getInstance().copyToClipboard(t);
                }
                else if (e.getButton()==3){
                    JPopupMenu menu;
                    JMenuItem menuItem;
                    JCheckBoxMenuItem cbMenuItem;
                    menu = new JPopupMenu();
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
        });
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
}
