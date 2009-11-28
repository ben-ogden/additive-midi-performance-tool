package ampt.ui.filters;

import ampt.core.devices.AmptDevice;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * This class is a box that is drawn on the canvas panel.  The box represents
 * a MidiDevice.
 *
 * @author Christopher Redding
 */
public class MidiDeviceBox extends JPanel {

    protected MidiDevice midiDevice;

    // A file or interactive log to which boxes can send debug or error messages.
    private PrintStream logger;

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
    
    // default colors for a box
    private static final Color DEFAULT_BG_COLOR = Color.WHITE;
    private static final Color DEFAULT_FG_COLOR = Color.BLACK;

    /**
     * Create the Box, and associate it with the correct MidiDevice.
     *
     * @param device the sMidiDevice backing this box
     */
    public MidiDeviceBox(MidiDevice device) throws MidiUnavailableException {
        this(device, null, DEFAULT_BG_COLOR, DEFAULT_FG_COLOR);
    }

    /**
     * Create the Box, and associate it with the correct MidiDevice. The caller
     * also provides a PrintStream for the box to use for logging. Default
     * colors are used.
     *
     * @param device the sMidiDevice backing this box
     * @param logger a PrintStream to write log messages
     */
    public MidiDeviceBox(MidiDevice device, PrintStream logger) throws MidiUnavailableException {
        this(device, logger, DEFAULT_BG_COLOR, DEFAULT_FG_COLOR);
    }
        
    /**
     * Create the Box, and associate it with the correct MidiDevice. The caller
     * also provides a PrintStream for the box to use for logging as well as
     * the desired colors to use.
     *
     * @param device the sMidiDevice backing this box
     * @param logger a PrintStream to write log messages
     * @param bgcolor the background color for the box
     * @param fgcolor the foreground color for the box
     */
    public MidiDeviceBox(MidiDevice device, PrintStream logger, Color bgcolor,
            Color fgcolor) throws MidiUnavailableException {

        this.midiDevice = device;
        this.logger = logger;

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

        // setup layout
        this.setLayout(new BorderLayout());
        this.setBackground(bgcolor);
        this.setForeground(fgcolor);

        // TODO - move custom code to a subclass of MidiDeviceBox that AMPT
        // device boxes inherit from
        if (midiDevice instanceof AmptDevice) {

            // setup arrows
            if (hasReceiver()) {
                JPanel westPanel = new JPanel();
                westPanel.setBackground(bgcolor);
                westPanel.setLayout(new GridLayout(2, 1));
                westPanel.add(new JLabel());
                BoxArrow boxArrow = new BoxArrow();
                boxArrow.setColor(fgcolor);
                westPanel.add(boxArrow);
                this.add(westPanel, BorderLayout.WEST);
            }

            if (hasTransmitter()) {
                JPanel eastPanel = new JPanel();
                eastPanel.setBackground(bgcolor);
                eastPanel.setLayout(new GridLayout(2, 1));
                eastPanel.add(new JLabel());
                BoxArrow boxArrow = new BoxArrow();
                boxArrow.setColor(fgcolor);
                eastPanel.add(boxArrow);
                this.add(eastPanel, BorderLayout.EAST);
            }


            // setup debug checkbox
            JCheckBox debugCheckBox = new JCheckBox("Debug");
            debugCheckBox.setBackground(bgcolor);
            debugCheckBox.setForeground(fgcolor);
            debugCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 10));
            debugCheckBox.setSelected(true);
            debugCheckBox.setHorizontalAlignment(JCheckBox.RIGHT);
            debugCheckBox.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            debugCheckBox.setBorder(new EmptyBorder(0, 0, 0, 10));

            // add listener for checkbox to enable/disable debugging
            debugCheckBox.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    AmptDevice device = (AmptDevice) midiDevice;
                    if (ItemEvent.SELECTED == e.getStateChange()) {
                        device.setMidiDebugEnabled(true);
                    } else {
                        device.setMidiDebugEnabled(false);
                    }
                }
            });

            this.add(debugCheckBox, BorderLayout.SOUTH);
            
        } // if (midiDevice instanceof AmptDevice)

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
     * Set the OutputStream for this Box.
     *
     * @param out
     */
    public void setLogger(PrintStream out) {
        logger = out;
    }

    /**
     * Write a message to the logger associated with this box.
     *
     * @param msgToLog
     */
    protected void log(String msgToLog) {
        if(null != logger && null != msgToLog) {
            logger.println(msgToLog);
        }
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

            x = x < 0 ? 0 : ((x + box.getWidth()) > box.getParent().getWidth() ? box.getParent().getWidth() - box.getWidth() : x);
            y = y < 0 ? 0 : ((y + box.getHeight()) > box.getParent().getHeight() ? box.getParent().getHeight() - box.getHeight() : y);

            // This was for changing the size of the canvas
//            if((box.getParent().getWidth() < x + box.getWidth()) || (box.getParent().getHeight() < y + box.getHeight())){
//                int panelX = x + box.getWidth() < box.getParent().getWidth() ? box.getParent().getWidth() : x + box.getWidth();
//                int panelY = y + box.getHeight() < box.getParent().getHeight() ? box.getParent().getHeight() : y + box.getHeight();
//                box.getParent().setPreferredSize(new Dimension(panelX, panelY));
//
//                ((JPanel)box.getParent()).revalidate();
//            }

            
            Rectangle visibleRectangle = box.getVisibleRect();
            box.setLocation(x, y);
            box.getParent().repaint();

            box.scrollRectToVisible(visibleRectangle);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            firstPointOnBox = e.getPoint();
        }
    }
}
