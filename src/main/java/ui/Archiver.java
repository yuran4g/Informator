package ui;

import Util.ZipPack;
import fileHelper.EntityList;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Андрей on 27.07.2016.
 */
public class Archiver extends JFrame {
    private final static Logger logger = Logger.getLogger(Archiver.class);
    int left, top;
    private final int HEIGHT=180,WIDTH=260;
    private ActionWindow dialogWindow;
    private JTable table;

    public Archiver(){
        createUI();
        dialogWindow=new ActionWindow(this);
    }

    private void createUI() {
        setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        left = (int) screenSize.getWidth();
        top = (int) screenSize.getHeight();
        left = left - WIDTH - 30;
        top = top - 80 - HEIGHT - 20;
        setBounds(left, top, WIDTH + 10, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        add(createPanel());
        setVisible(true);
    }

    private JPanel createPanel(){
        JPanel jp = new JPanel(null);
        jp.setVisible(true);
        jp.setBounds(0, 0, WIDTH, HEIGHT);
        jp=addButtons(jp);
        jp=addTable(jp);
        return jp;
    }

    private JPanel addTable(JPanel jp) {
        String[][] data = new String[EntityList.getEntities().size()][];
        for (int i=0;i<EntityList.getEntities().size();i++){
            data[i]=new String[2];
            data[i][0]=EntityList.getEntities().get(i).getName();
            data[i][1]=EntityList.getEntities().get(i).getLink();
        }
        DefaultTableModel mod = new DefaultTableModel(new Object[]{"Name", "Path"},data.length);
        mod.removeRow(0);
        for (String[] s:data){
            mod.addRow(s);
        }
        table = new JTable(mod);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVisible(true);
        scrollPane.setBounds(10,10,WIDTH-20,HEIGHT-120);
        scrollPane.setEnabled(true);
        jp.add(scrollPane);
        return jp;
    }

    private JPanel addButtons(JPanel jpanel){
        JButton archive = new JButton("Archive"),add=new JButton("Add"),change=new JButton("Change"),delete=new JButton("Delete"),clear=new JButton("Clear");
        add.setBounds(10,HEIGHT-90,70,20);
        change.setBounds(90,HEIGHT-90,80,20);
        delete.setBounds(180,HEIGHT-90,70,20);
        archive.setBounds(10,HEIGHT-60,WIDTH/2-20,20);
        clear.setBounds(WIDTH/2+10,HEIGHT-60,WIDTH/2-20,20);
        archive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ZipPack zp = new ZipPack();
                String path=EntityList.getEntities().get(table.getSelectedRow()).getLink();
                if (new File(path).isDirectory()){
                    zp.setPackDirectoryPath(path);
                    try{zp.packDirectory();}
                    catch (IOException ex){logger.error(ex);}
                }
                else{
                    zp.setPackFilePath(path);
                    try{zp.packFile();}
                    catch (IOException ex){logger.error(ex);}
                }
            }
        });
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{EntityList.getEntities().get(table.getSelectedRow()).clean();}
                catch (Exception ex){logger.error(ex);}
            }
        });
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogWindow.ShowAdd();
                while (dialogWindow.isVisible()){
                    try{Thread.currentThread().wait(100);}
                    catch (InterruptedException ex){logger.error(ex);}
                }
                String[] data = new String[]{EntityList.getEntities().get(EntityList.getEntities().size()-1).getName(),EntityList.getEntities().get(EntityList.getEntities().size()-1).getLink()};
                ((DefaultTableModel)table.getModel()).addRow(data);
            }
        });
        change.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                while (dialogWindow.isVisible()){
                    try{Thread.currentThread().wait(100);}
                    catch (InterruptedException ex){logger.error(ex);}
                }
                dialogWindow.ShowChange(EntityList.getEntities().get(table.getSelectedRow()));
                table.setValueAt(EntityList.getEntities().get(table.getSelectedRow()).getName(),table.getSelectedRow(),0);
                table.setValueAt(EntityList.getEntities().get(table.getSelectedRow()).getLink(),table.getSelectedRow(),1);
            }
        });
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    EntityList.removeEntity(EntityList.getEntities().get(table.getSelectedRow()));
                    ((DefaultTableModel)table.getModel()).removeRow(table.getSelectedRow());
                }
                catch (Exception ex){logger.error(ex);}
            }
        });
        jpanel.add(archive);
        jpanel.add(clear);
        jpanel.add(add);
        jpanel.add(change);
        jpanel.add(delete);
        return jpanel;
    }
}
