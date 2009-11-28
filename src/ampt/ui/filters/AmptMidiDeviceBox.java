package ampt.ui.filters;

import ampt.core.devices.AmptMidiDevice;
import ampt.core.devices.TimedDevice;
import ampt.ui.tempo.TempoEvent;
import ampt.ui.tempo.TempoListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.PrintStream;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * AmptMidiDeviceBox is a MidiDeviceBox with additional functionality
 * for all AmptMidiDevices. AmptMidiDeviceBox provides console logging,
 * tempo control, and custom ui components such as connection arrows and
 * debug checkbox.
 *
 * @author Ben
 */
public class AmptMidiDeviceBox extends MidiDeviceBox implements TempoListener {

    // A file or interactive log to which boxes can send debug or error messages.
    private PrintStream _logger;

    /**
     * Create the Box, and associate it with the correct MidiDevice. The caller
     * also provides a PrintStream for the box to use for logging. Default
     * colors are used.
     *
     * @param device the sMidiDevice backing this box
     * @param logger a PrintStream to write log messages
     */
    public AmptMidiDeviceBox(AmptMidiDevice device, PrintStream logger) throws MidiUnavailableException {
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
    public AmptMidiDeviceBox(AmptMidiDevice device, PrintStream logger,
            Color bgcolor, Color fgcolor) throws MidiUnavailableException {

        super(device, bgcolor, fgcolor);
        _logger = logger;

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
                AmptMidiDevice device = (AmptMidiDevice) midiDevice;
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    device.setMidiDebugEnabled(true);
                } else {
                    device.setMidiDebugEnabled(false);
                }
            }
        });

        this.add(debugCheckBox, BorderLayout.SOUTH);
    }

    /**
     * Set the OutputStream for this Box.
     *
     * @param out
     */
    public void setLogger(PrintStream out) {
        _logger = out;
    }

    /**
     * Write a message to the logger associated with this box.
     *
     * @param msgToLog
     */
    protected void log(String msgToLog) {
        if (null != _logger && null != msgToLog) {
            _logger.println(msgToLog);
        }
    }

    /**
     * Notify this listener of a change in tempo.
     *
     * @param event a TempoEvent
     */
    @Override
    public void tempoChanged(TempoEvent event) {
        // pass tempo along to device, but only if it is a TimedDevice
        if (midiDevice instanceof TimedDevice) {
            TimedDevice device = (TimedDevice) midiDevice;
            device.setTempo(event.getTempo());
        }
    }
}
