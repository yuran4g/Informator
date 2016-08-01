package ui;

import fileHelper.EntityList;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Андрей on 27.07.2016.
 */
public class Archiver extends JFrame {
    private final static Logger logger = Logger.getLogger(Archiver.class);
    int left, top;
    private final int HEIGHT = 180, WIDTH = 260;
    private ActionWindow dialogWindow;
    private JTable table;
    private JButton archive = new JButton("Archive"), add = new JButton("Add"), change = new JButton("Change"), delete = new JButton("Delete"), clear = new JButton("Clear");

    public Archiver() {
        createUI();
        dialogWindow = new ActionWindow(this);
    }

    private void createUI() {
        setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        left = (int) screenSize.getWidth() - WIDTH - 30;
        top = (int) screenSize.getHeight() - 80 - HEIGHT - 20;
        setBounds(left, top, WIDTH + 10, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        add(createPanel());
        setVisible(true);
    }

    private JPanel createPanel() {
        JPanel jp = new JPanel(null);
        jp.setVisible(true);
        jp.setBounds(0, 0, WIDTH, HEIGHT);
        jp = addButtons(jp);
        jp = addTable(jp);
        return jp;
    }

    private JPanel addTable(JPanel jp) {
        String[][] data = new String[EntityList.getEntities().size()][];
        for (int i = 0; i < EntityList.getEntities().size(); i++) {
            data[i] = new String[]{
                    EntityList.getEntities().get(i).getName(),
                    EntityList.getEntities().get(i).getLink()};
        }
        DefaultTableModel mod = new DefaultTableModel(new Object[]{"Name", "Path"}, data.length);
        mod.removeRow(0);
        for (String[] s : data) {
            mod.addRow(s);
        }
        table = new JTable(mod);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new tableListSelectionListener());
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVisible(true);
        scrollPane.setBounds(10, 10, WIDTH - 20, HEIGHT - 120);
        jp.add(scrollPane);
        return jp;
    }

    private void setButtonState(boolean state){
        clear.setEnabled(state);
        archive.setEnabled(state);
        delete.setEnabled(state);
        change.setEnabled(state);
    }

    private JPanel addButtons(JPanel jpanel) {
        initButtons();
        jpanel.add(archive);
        jpanel.add(clear);
        jpanel.add(add);
        jpanel.add(change);
        jpanel.add(delete);
        return jpanel;
    }

    private void initButtons(){
        setButtonState(false);
        add.setBounds(10, HEIGHT - 90, 70, 20);
        change.setBounds(90, HEIGHT - 90, 80, 20);
        delete.setBounds(180, HEIGHT - 90, 70, 20);
        archive.setBounds(10, HEIGHT - 60, WIDTH / 2 - 20, 20);
        clear.setBounds(WIDTH / 2 + 10, HEIGHT - 60, WIDTH / 2 - 20, 20);
        archive.addActionListener(new archiveActionListener());
        clear.addActionListener(new clearActionListener());
        add.addActionListener(new addActionListener());
        change.addActionListener(new changeActionListener());
        delete.addActionListener(new deleteActionListener());
    }

    private class deleteActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            try {
                EntityList.removeEntity(EntityList.getEntities().get(table.getSelectedRow()));
                ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
    }

    private class changeActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            while (dialogWindow.isVisible()) {
                try {
                    Thread.currentThread().wait(100);
                } catch (InterruptedException ex) {
                    logger.error(ex);
                }
            }
            dialogWindow.ShowChange(EntityList.getEntities().get(table.getSelectedRow()));
            table.setValueAt(EntityList.getEntities().get(table.getSelectedRow()).getName(), table.getSelectedRow(), 0);
            table.setValueAt(EntityList.getEntities().get(table.getSelectedRow()).getLink(), table.getSelectedRow(), 1);
        }
    }

    private class addActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            dialogWindow.ShowAdd();
            while (dialogWindow.isVisible()) {
                try {
                    Thread.currentThread().wait(100);
                } catch (InterruptedException ex) {
                    logger.error(ex);
                }
            }
            String[] data = new String[]{EntityList.getEntities().get(EntityList.getEntities().size() - 1).getName(), EntityList.getEntities().get(EntityList.getEntities().size() - 1).getLink()};
            ((DefaultTableModel) table.getModel()).addRow(data);
        }
    }

    private class clearActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            try {
                EntityList.getEntities().get(table.getSelectedRow()).clean();
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
    }

    private class archiveActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String path = EntityList.getEntities().get(table.getSelectedRow()).getLink();
            try {
                String pathToArchive = EntityList.getEntities().get(table.getSelectedRow()).archive();
                Runtime.getRuntime().exec("explorer "+pathToArchive.replaceAll("\\\\[^\\\\]*zip$", ""));
            } catch (IOException e1) {
                logger.error("Can not archive path = " + path);
            }
        }
    }

    private class tableListSelectionListener implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent e) {
            if (table.getSelectedRow()!=-1){
                setButtonState(true);
            }
            else{
                setButtonState(false);
            }
        }
    }
}
