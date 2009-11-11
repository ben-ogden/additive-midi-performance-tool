/*
 * CanvasPanel.java
 *
 * Created on Oct 21, 2009, 2:50:53 PM
 */
package ampt.ui.canvas;

import ampt.ui.filters.MidiDeviceBox;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JOptionPane;

/**
 * This is the canvas that the ampt system is visually represented on.
 *
 * @author Christopher
 */
public class CanvasPanel extends javax.swing.JPanel {

    Vector<MidiDeviceBox> midiDeviceBoxes;
    Vector<MidiDeviceConnection> midiDeviceConnections;

    /** Creates new form CanvasPanel */
    public CanvasPanel() {
        initComponents();
        this.setLayout(null);
        midiDeviceBoxes = new Vector<MidiDeviceBox>();
        midiDeviceConnections = new Vector<MidiDeviceConnection>();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public Component add(Component c) {
        super.add(c);
        if (c instanceof MidiDeviceBox) {
            MidiDeviceBox box = (MidiDeviceBox) c;
            midiDeviceBoxes.add(box);
            if (box.hasTransmitter()) {
                MyActionListener myActionListener = new MyActionListener(box, this);
                final PopupMenu popupMenu = new PopupMenu();
                MenuItem menuItem = new MenuItem("Add Filter Connection");
                menuItem.addActionListener(myActionListener);
                popupMenu.add(menuItem);
                this.add(popupMenu);
                MouseListener popupListener = new MouseAdapter() {

                    public void mousePressed(MouseEvent e) {
                        maybeShowPopup(e);
                    }

                    public void mouseReleased(MouseEvent e) {
                        maybeShowPopup(e);
                    }

                    private void maybeShowPopup(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            popupMenu.show(e.getComponent(),
                                    e.getX(), e.getY());
                        }
                    }
                };
                box.addMouseListener(popupListener);
            }
        } else if (c instanceof MidiDeviceConnection) {
            MidiDeviceConnection conn = (MidiDeviceConnection) c;
            midiDeviceConnections.add(conn);
        }
        return c;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (MidiDeviceConnection conn : midiDeviceConnections) {
            conn.paintOnCanvas(g);
        }
    }

    private class MyActionListener implements ActionListener {

        private MidiDeviceBox box;
        private CanvasPanel panel;

        public MyActionListener(MidiDeviceBox box, CanvasPanel panel) {
            this.box = box;
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MyMouseAdapter mouseAdapter = new MyMouseAdapter(box, panel);
            for (MidiDeviceBox toBox : midiDeviceBoxes) {
                if(toBox.hasReceiver())
                    toBox.addMouseListener(mouseAdapter);
            }
            panel.addMouseListener(mouseAdapter);
        }
    }

    private class MyMouseAdapter extends MouseAdapter {

        private MidiDeviceBox sourceBox;
        private CanvasPanel panel;

        public MyMouseAdapter(MidiDeviceBox sourceBox, CanvasPanel panel) {
            this.sourceBox = sourceBox;
            this.panel = panel;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            panel.removeMouseListener(this);
            for (MidiDeviceBox box : midiDeviceBoxes) {
                box.removeMouseListener(this);
            }

            if (e.getSource() instanceof MidiDeviceBox) {
                MidiDeviceBox destBox = (MidiDeviceBox) e.getSource();

                // don't connect a box to itself
                if (destBox.equals(sourceBox)) {
                    return;
                }

                // add a new connection between boxes
                try {
                    panel.add(new MidiDeviceConnection(sourceBox, destBox));
                    panel.repaint();
                } catch (MidiUnavailableException ex) {
                    
                    JOptionPane.showMessageDialog(null,
                            "Unable to connect these devices. "
                            + ex.getLocalizedMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            }

        }
    }
}
