package ui;

import fileHelper.Entity;
import fileHelper.EntityList;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Андрей on 28.07.2016.
 */
class ActionWindow extends JDialog{
    ActionWindow(){
        createUI();
    }
    private final int HEIGHT=150,WIDTH=260;
    private JTextField name,path;
    private JButton ok;
    private JPanel panel;
    private String changeName;
    private Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();

    private static Logger logger = Logger.getLogger(ActionWindow.class);

    private void createUI() {
        setLayout(null);
        setBounds((screenSize.width-WIDTH+10)/2,(screenSize.height-HEIGHT)/2,WIDTH+10,HEIGHT);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        add(createPanel());
        setModal(true);
    }

    void ShowAdd(){
        setType('a');
        setVisible(true);
    }

    void ShowChange(Entity e){
        setType('c');
        changeName=e.getName();
        setTextBoxValues(e);
        setVisible(true);
    }

    private void setTextBoxValues(Entity e){
        name.setText(e.getName());
        path.setText(e.getLink());
    }

    private JPanel initTextField(JPanel panel){
        name = new JTextField("Name");
        name.setBounds(10, 10, WIDTH - 20, 20);
        panel.add(name);
        path = new JTextField("Path");
        path.setBounds(10, 40, WIDTH - 20, 20);
        panel.add(path);
        return panel;
    }
    private JPanel createPanel(){
        panel = new JPanel(null);
        panel.setBounds(0, 0, WIDTH, HEIGHT);
        ok = new JButton("OK");
        ok.setBounds(WIDTH/2 - 30,HEIGHT - 70, 60, 20);
        panel.add(ok);
        return initTextField(panel);
    }

    private void reset(){
        for (Component c: panel.getComponents())
            c.setVisible(false);
        ok.setVisible(true);
        for (ActionListener al:ok.getActionListeners()){
            ok.removeActionListener(al);
        }
    }

    private void setType(char type){
        reset();
        name.setVisible(true);
        path.setVisible(true);
        switch (type){
            case 'a':
                ok.addActionListener(new AddAction());
                break;
            case 'c':
                ok.addActionListener(new ChangeAction());
                break;
        }
    }

    private class AddAction implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{EntityList.addEntity(name.getText(),path.getText());}
            catch (Exception ex){logger.error(ex);}
            setVisible(false);
            //instance.dispatchEvent(new WindowEvent(instance, WindowEvent.WINDOW_CLOSING));
        }
    }
    private class ChangeAction implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{EntityList.updateEntity(EntityList.getEntity(changeName),name.getText(),path.getText());}
            catch (Exception ex){logger.error(ex);}
            setVisible(false);
        }
    }
}
