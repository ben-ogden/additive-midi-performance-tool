/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ampt.ui.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.swing.JPanel;

/**
 * This class is a box that is drawn on the canvas panel.  The box represents
 * a MidiDevice.
 *
 * @author Christopher Redding
 */
public class MidiDeviceBox extends JPanel {

    protected MidiDevice midiDevice;
    protected boolean hasTransmitter = false;
    protected boolean hasReceiver = false;
    protected String text;
    protected boolean overridePaintComponent = true;

    /**
     * Create the Box, and associate it with the correct MidiDevice.
     * @param deviceInfo
     */
    public MidiDeviceBox(MidiDevice device) {
        this.midiDevice = device;
        
        Info deviceInfo = device.getDeviceInfo();
        this.setPreferredSize(new Dimension(71, 71));
        try {
            this.text = deviceInfo.getName();
            if (midiDevice.getMaxReceivers() != 0) {
                hasReceiver = true;
            }
            if (midiDevice.getMaxTransmitters() != 0) {
                hasTransmitter = true;
            }
            this.setToolTipText("Name: " + deviceInfo.getName() + " Description: " + deviceInfo.getDescription() + " Vendor: " + deviceInfo.getVendor() + " Version: " + deviceInfo.getVersion());

        } catch (NullPointerException ex) {
            // Do Nothing
        }
        // Add a mouse adapter so we can move the box around the canvas panel.
        MyMouseAdapter mouseAdapter = new MyMouseAdapter(this);
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    /**
     * This is a convenience method for getting a transmitter.  It is
     * designed to be used by the MidiDeviceConnection class.
     */
    public Transmitter getTransmitter() throws MidiUnavailableException {
//        MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
        midiDevice.open();
        return midiDevice.getTransmitter();
    }

    /**
     * This is a convenience method for getting a receiver.  It is
     * designed to be used by the MidiDeviceConnection class.
     */
    public Receiver getReceiver() throws MidiUnavailableException {
//        MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
        midiDevice.open();
        return midiDevice.getReceiver();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(!overridePaintComponent){
            super.paintComponent(g);
            return;
        }

        Color color = g.getColor();
        g.setColor(Color.BLACK);

        // Draw the box
        g.drawRect(0, 0, 70, 70);

        // draw an arrow pointing in if there is a receiver
        if (hasReceiver) {
            g.drawLine(0, 35, 5, 35);
            g.drawLine(2, 32, 5, 35);
            g.drawLine(2, 38, 5, 35);
        }

        // draw an arrow pointing out if there is a transmitter
        if (hasTransmitter) {
            g.drawLine(65, 35, 70, 35);
            g.drawLine(67, 32, 70, 35);
            g.drawLine(67, 38, 70, 35);
        }

        // draw the text
        String[] tokens = text.split(" ", 4);
        int height = 12;
        for (String token : tokens) {
            g.drawString(token, 7, height);
            height += 15;
        }




        g.setColor(color);
    }

    public MidiDevice getMidiDevice() {
        return this.midiDevice;
    }

    /**
     * Mouse adapter that allows the box to be dragged around the screen.
     */
    private class MyMouseAdapter extends MouseAdapter {

        private MidiDeviceBox box;
        private Point firstPointOnBox = new Point(0, 0);

        public MyMouseAdapter(MidiDeviceBox box) {
            this.box = box;
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            int deltaX = e.getX() - firstPointOnBox.x;
            int x = box.getLocation().x + deltaX;
            int deltaY = e.getY() - firstPointOnBox.y;
            int y = box.getLocation().y + deltaY;

            x = x < 0 ? 0 : (x > box.getParent().getWidth() - box.getWidth() ? box.getParent().getWidth() - box.getWidth() : x);
            y = y < 0 ? 0 : (y > box.getParent().getHeight() - box.getHeight() ? box.getParent().getHeight() - box.getHeight() : y);

            box.setLocation(x, y);
            box.getParent().repaint();

        }

        @Override
        public void mousePressed(MouseEvent e) {
            firstPointOnBox = e.getPoint();
        }
    }
}
