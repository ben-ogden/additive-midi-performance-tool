/*
 * CanvasPanel.java
 *
 * Created on Oct 21, 2009, 2:50:53 PM
 */
package ampt.ui.canvas;

import ampt.ui.filters.MidiDeviceBox;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

/**
 * This is the canvas that the ampt system is visually represented on.
 *
 * @author Christopher
 */
public class CanvasPanel extends javax.swing.JPanel {

    Vector<MidiDeviceBox> midiDeviceBoxes;
    Vector<MidiDeviceConnection> midiDeviceConnections;

//    private JPopupMenu popupMenu;
    /** Creates new form CanvasPanel */
    public CanvasPanel() {
        initComponents();
        this.setLayout(null);
        midiDeviceBoxes = new Vector<MidiDeviceBox>();
        midiDeviceConnections = new Vector<MidiDeviceConnection>();
        CanvasMotionMouseAdapter mouseAdapter = new CanvasMotionMouseAdapter(this);
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    /**
     * Closes all devices on the canvas.
     */
    public void closeAllDevices() {
        for(MidiDeviceBox box : midiDeviceBoxes) {
            box.closeDevice();
        }
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
        if (c instanceof MidiDeviceBox) {
            MidiDeviceBox box = (MidiDeviceBox) c;
            midiDeviceBoxes.add(box);
            final JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem addConnectionMenuItem = new JMenuItem("Add Filter Connection");
            addConnectionMenuItem.addActionListener(new AddConnectionActionListener(box, this));
            popupMenu.add(addConnectionMenuItem);
            if (box.hasTransmitter() == false) {
                addConnectionMenuItem.setEnabled(false);
            }
            this.add(popupMenu);
            final JMenu removeConnectionMenu = new JMenu("Remove Filter Connection");
            removeConnectionMenu.setEnabled(false);
            popupMenu.add(removeConnectionMenu);
            popupMenu.add(new JSeparator());
            JMenuItem deleteBoxMenuItem = new JMenuItem("Remove MIDI Device");
            deleteBoxMenuItem.addActionListener(new RemoveBoxActionListener(box, this));
            popupMenu.add(deleteBoxMenuItem);
            final CanvasPanel thisPanel = this;
            MouseListener popupListener = new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    maybeShowPopup(e);
                }

                public void mouseReleased(MouseEvent e) {
                    maybeShowPopup(e);
                }

                private void maybeShowPopup(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        /** Fist we need to add the remove connection menu if
                         * there are any active connections to this device
                         */
                        removeConnectionMenu.removeAll();
                        boolean hasConnections = false;
                        for (final MidiDeviceConnection conn : midiDeviceConnections) {
                            if (conn.getFrom().equals(e.getSource())) {
                                JMenuItem menuItem = new JMenuItem("To: " + conn.getTo().getDeviceInfo().getName());
                                menuItem.addActionListener(new RemoveConnectionActionListener(conn, thisPanel));
                                removeConnectionMenu.add(menuItem);
                                hasConnections = true;
                            } else if (conn.getTo().equals(e.getSource())) {
                                JMenuItem menuItem = new JMenuItem("From: " + conn.getFrom().getDeviceInfo().getName());
                                menuItem.addActionListener(new RemoveConnectionActionListener(conn, thisPanel));
                                removeConnectionMenu.add(menuItem);
                                hasConnections = true;
                            }
                        }
                        // if there are connections, enable the menu
                        if (hasConnections) {
                            removeConnectionMenu.setEnabled(true);
                        } // if there are no connections, disable the menu
                        else {
                            removeConnectionMenu.setEnabled(false);
                        }
                        popupMenu.show(e.getComponent(),
                                e.getX(), e.getY());
                    }
                }
            };
            box.addMouseListener(popupListener);

        } else if (c instanceof MidiDeviceConnection) {
            MidiDeviceConnection conn = (MidiDeviceConnection) c;
            if(midiDeviceConnections.contains(conn)){
                return c;
            }
            midiDeviceConnections.add(conn);
        }
        super.add(c);
        return c;
    }

    @Override
    public void remove(Component comp) {
        if (comp instanceof MidiDeviceBox) {
            MidiDeviceBox box = (MidiDeviceBox) comp;
            for (MidiDeviceConnection conn : midiDeviceConnections.toArray(new MidiDeviceConnection[0])) {
                if (conn.getTo().equals(box) || conn.getFrom().equals(box)) {
                    this.remove(conn);
                }
            }
            
            box.closeDevice();

            midiDeviceBoxes.remove(box);
            super.remove(comp);
        }
        if (comp instanceof MidiDeviceConnection) {
            try {
                MidiDeviceConnection conn = (MidiDeviceConnection) comp;
                conn.getFrom().disconnectFrom(conn.getTo());
                midiDeviceConnections.remove(conn);
                super.remove(comp);
            } catch (MidiUnavailableException ex) {
                JOptionPane.showMessageDialog(null,
                        "Unable to disconnect these devices. " + ex.getLocalizedMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            super.remove(comp);
        }
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (MidiDeviceConnection conn : midiDeviceConnections) {
            conn.paintOnCanvas(g);
        }
    }

    private class AddConnectionActionListener implements ActionListener {

        private MidiDeviceBox box;
        private CanvasPanel panel;

        public AddConnectionActionListener(MidiDeviceBox box, CanvasPanel panel) {
            this.box = box;
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MyMouseAdapter mouseAdapter = new MyMouseAdapter(box, panel);
            for (MidiDeviceBox toBox : midiDeviceBoxes) {
                if (toBox.hasReceiver()) {
                    toBox.addMouseListener(mouseAdapter);
                }
            }
            panel.addMouseListener(mouseAdapter);
        }
    }

    private class RemoveConnectionActionListener implements ActionListener {

        private MidiDeviceConnection conn;
        private CanvasPanel panel;

        public RemoveConnectionActionListener(MidiDeviceConnection conn, CanvasPanel panel) {
            this.conn = conn;
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            panel.remove(conn);
            panel.repaint();
        }
    }

    private class RemoveBoxActionListener implements ActionListener {

        private MidiDeviceBox box;
        private CanvasPanel panel;

        public RemoveBoxActionListener(MidiDeviceBox box, CanvasPanel panel) {
            this.box = box;
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            panel.remove(box);
            panel.repaint();
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
                            "Unable to connect these devices. " + ex.getLocalizedMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

            }

        }
    }

    /**
     * Mouse adapter that allows the canvas to be scrolled by clicking and dragging it.
     */
    private class CanvasMotionMouseAdapter extends MouseAdapter {

        private Point firstPointOnCanvas = new Point(0, 0);
        private CanvasPanel canvas;

        public CanvasMotionMouseAdapter(CanvasPanel canvas) {
            this.canvas = canvas;
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            int deltaX = firstPointOnCanvas.x - e.getX();
            int deltaY = firstPointOnCanvas.y - e.getY();

            Rectangle initialVisibleRectangle = canvas.getVisibleRect();
            Rectangle newVisibleRectangle = new Rectangle((int)initialVisibleRectangle.getX() + deltaX, (int)initialVisibleRectangle.getY() + deltaY, (int)initialVisibleRectangle.getWidth(), (int)initialVisibleRectangle.getHeight());
            canvas.scrollRectToVisible(newVisibleRectangle);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            firstPointOnCanvas = e.getPoint();
        }
    }

}
