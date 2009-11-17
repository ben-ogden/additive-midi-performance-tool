package ampt.ui.filters;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.swing.JPanel;

/**
 * This class is a box that is drawn on the canvas panel.  The box represents
 * a MidiDevice.
 *
 * TODO need a way to remove box from canvas. that method should also close the
 * midiDevice
 *
 * @author Christopher Redding
 */
public class MidiDeviceBox extends JPanel {

    protected MidiDevice midiDevice;

    //TODO - maybe keep track of number of transmitters and receivers instead of
    //       if have any... also, this could change during runtime so perhaps
    //       code should call midiDevice to see how many if want to support
    //       drawing multiple arrows on the box
    private boolean hasTransmitter = false;
    private boolean hasReceiver = false;
    
    protected String deviceName;
    protected boolean overridePaintComponent = true;

    private static int PREFERRED_HEIGHT = 71;
    private static int PREFERRED_WIDTH = 71;
    
    /**
     * Create the Box, and associate it with the correct MidiDevice.
     *
     * @param deviceInfo
     */
    public MidiDeviceBox(MidiDevice device) throws MidiUnavailableException {

        this.midiDevice = device;

        // open the device once it is placed on the canvas
        midiDevice.open();

        Info deviceInfo = device.getDeviceInfo();
        this.setPreferredSize(new Dimension(PREFERRED_HEIGHT, PREFERRED_WIDTH));

        this.deviceName = deviceInfo.getName();

        if (midiDevice.getMaxReceivers() != 0) {
            hasReceiver = true;
        }
        if (midiDevice.getMaxTransmitters() != 0) {
            hasTransmitter = true;
        }

        // use description for tooltip, otherwise device name
        String description = deviceInfo.getDescription();
        if(null != description && description.length() > 0) {
            this.setToolTipText(description);
        } else {
            this.setToolTipText(deviceInfo.getName());
        }

        // Add a mouse adapter so we can move the box around the canvas panel.
        MyMouseAdapter mouseAdapter = new MyMouseAdapter(this);
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);

    }

    public void closeDevice(){
        midiDevice.close();
    }

    public Info getDeviceInfo(){
        return midiDevice.getDeviceInfo();
    }

    /**
     * Connect the output of this MidiDeviceBox to the input of another
     * MidiDeviceBox.
     *
     * @param anotherDevice The device that will be connected to
     *
     * @throws MidiUnavailableException
     */
    public void connectTo(MidiDeviceBox anotherDevice) throws MidiUnavailableException {
        //TODO - i would prefer if we could call AmptDevice.connectTo, but not all
        //       devices are AmptDevices.
        midiDevice.getTransmitter().setReceiver(anotherDevice.midiDevice.getReceiver());
    }

    /**
     * Disconnects the output of this MidiDeviceBox from the input of another
     * MidiDeviceBox.
     *
     * @param anotherDevice The device that will be disconnected
     * 
     * @throws MidiUnavailableException
     */
    public void disconnectFrom(MidiDeviceBox anotherDevice) throws MidiUnavailableException {
        for(Transmitter transmitter : midiDevice.getTransmitters()){
            for(Receiver receiver : anotherDevice.midiDevice.getReceivers()){
                if(transmitter.getReceiver().equals(receiver)){
                    transmitter.close();
                    return;
                }
            }
        }
    }

    public boolean hasTransmitter() {
        return hasTransmitter;
    }

    public boolean hasReceiver() {
        return hasReceiver;
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

        // draw the deviceName
        String[] tokens = deviceName.split(" ", 4);
        int height = 12;
        for (String token : tokens) {
            g.drawString(token, 7, height);
            height += 15;
        }

        g.setColor(color);
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
