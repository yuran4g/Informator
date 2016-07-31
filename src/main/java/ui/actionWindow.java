package ui;

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
    ActionWindow(Archiver parent){
        createUI(parent);
    }
    private final int HEIGHT=180,WIDTH=260;
    private JTextField name,path,oldName;
    private JButton ok;
    private JPanel panel;

    private static Logger logger = Logger.getLogger(ActionWindow.class);

    private void createUI(Archiver parent) {
        setLayout(null);
        setBounds(parent.left+10,parent.top+10,WIDTH+10,HEIGHT);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        add(createPanel());
    }

    void Show(char type){
        setType(type);
        this.setModal(true);
        setVisible(true);
    }

    private JPanel initTextField(JPanel panel){
        name = new JTextField("Name");
        name.setBounds(10, 10, WIDTH - 20, 20);
        panel.add(name);
        path = new JTextField("Path");
        path.setBounds(10, 70, WIDTH - 20, 20);
        panel.add(path);
        oldName = new JTextField("OldName");
        oldName.setBounds(10, 40, WIDTH - 20, 20);
        panel.add(oldName);
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
        switch (type){
            case 'a':
                name.setVisible(true);
                path.setVisible(true);
                ok.addActionListener(new AddAction());
                break;
            case 'c':
                name.setVisible(true);
                path.setVisible(true);
                oldName.setVisible(true);
                ok.addActionListener(new ChangeAction());
                break;
            case 'r':
                name.setVisible(true);
                ok.addActionListener(new RemoveAction());
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
            try{EntityList.updateEntity(EntityList.getEntity(oldName.getText()),name.getText(),path.getText());}
            catch (Exception ex){logger.error(ex);}
            setVisible(false);
        }
    }
    private class RemoveAction implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{EntityList.removeEntity(EntityList.getEntity(name.getText()));}
            catch (Exception ex){logger.error(ex);}
            setVisible(false);
        }
    }
}
