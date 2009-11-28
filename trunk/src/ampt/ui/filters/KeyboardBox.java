package ampt.ui.filters;

import ampt.core.devices.KeyboardDevice;
import ampt.ui.keyboard.KeyboardPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class extends MidiDeviceBox in order to have the keyboard and its
 * properties be part of the canvas box.
 * 
 * @author Christopher
 */
public class KeyboardBox extends MidiDeviceBox implements ChangeListener, ActionListener{

    private static final int VELOCITY_MIN = 0;
    private static final int VELOCITY_MAX = 127;
    private static final int STARTING_VELOCITY = 64;

    private JSlider velocitySlider;
    private JComboBox channelComboBox;
    private JComboBox octaveComboBox;

    /**
     * Constructor to create the box.
     * @param device
     */
    public KeyboardBox(KeyboardDevice device, boolean extended) throws MidiUnavailableException {

        super(device);
        
        // We need to change the preferred size to null since it was changed in
        // MidiDeviceBox.  This way, the components will determine the size of 
        // this panel.
        this.setPreferredSize(null);
        overridePaintComponent = false;

        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Software Keyboard", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.BELOW_TOP));
        this.setBackground(Color.CYAN);

        // Make the center panel contain the keyboard
        JPanel centerPanel = new JPanel();
        KeyboardPanel keyboardPanel = new KeyboardPanel(device, extended);
        keyboardPanel.setBackground(Color.CYAN);
        centerPanel.add(keyboardPanel);
        centerPanel.setBackground(Color.CYAN);
        this.add(centerPanel, BorderLayout.CENTER);



        // Make a south panel to contain the velocity slider
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.CYAN);
        velocitySlider = new JSlider(VELOCITY_MIN, VELOCITY_MAX, STARTING_VELOCITY);
        velocitySlider.setBackground(Color.CYAN);
        device.setVelocity(STARTING_VELOCITY);
        velocitySlider.addChangeListener(this);
        velocitySlider.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Velocity"));
        southPanel.add(velocitySlider);
        this.add(southPanel, BorderLayout.SOUTH);

        // Make an east panel to contain the channel and octave selecters
        JPanel eastPanel = new JPanel();
        eastPanel.setBackground(Color.CYAN);
        eastPanel.setLayout(new BorderLayout());
        JPanel centerEastPanel = new JPanel();
        centerEastPanel.setBackground(Color.CYAN);
        centerEastPanel.setLayout(new BoxLayout(centerEastPanel, BoxLayout.Y_AXIS));
        Integer[] channels = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        channelComboBox = new JComboBox(channels);
        channelComboBox.setBackground(Color.CYAN);
        channelComboBox.addActionListener(this);
        channelComboBox.setSelectedIndex(0);
//        device.setChannel(channels[0]);
        channelComboBox.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Chan."));
        channelComboBox.setKeySelectionManager(new EmptyKeySelectionManager());
        centerEastPanel.add(channelComboBox);

        Integer[] octaves = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        octaveComboBox = new JComboBox(octaves);
        octaveComboBox.setBackground(Color.CYAN);
        octaveComboBox.addActionListener(this);
        octaveComboBox.setSelectedIndex(5);
//        device.setOctave(octaves[5]);
        octaveComboBox.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Octave"));
        octaveComboBox.setKeySelectionManager(new EmptyKeySelectionManager());
        centerEastPanel.add(octaveComboBox);

        eastPanel.add(centerEastPanel, BorderLayout.CENTER);

        if(hasTransmitter()){
            JPanel eastEastPanel = new JPanel();
            eastEastPanel.setBackground(Color.CYAN);
            eastEastPanel.setLayout(new GridLayout(2,1));
            eastEastPanel.add(new JLabel());
            eastEastPanel.add(new BoxArrow());
            eastPanel.add(eastEastPanel, BorderLayout.EAST);
        }

        if(hasReceiver()){
            JPanel westPanel = new JPanel();
            westPanel.setBackground(Color.CYAN);
            westPanel.setLayout(new GridLayout(2,1));
            westPanel.add(new JLabel());
            westPanel.add(new BoxArrow());
            this.add(westPanel, BorderLayout.WEST);
        }

        this.add(eastPanel, BorderLayout.EAST);
    }

    /**
     * Override this method to return false, so connections to
     * a keyboard box cannot be made.
     * @return false
     */
    @Override
    public boolean hasReceiver() {
        return false;
    }



    /**
     * Listener for velocity slider
     * @param e
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource().equals(velocitySlider)){
            KeyboardDevice keyboard = (KeyboardDevice) midiDevice;
            keyboard.setVelocity(velocitySlider.getValue());
        }
    }

    /**
     * Listener for channel combo box and octave combo box
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(channelComboBox)){
            KeyboardDevice keyboard = (KeyboardDevice) midiDevice;
            keyboard.setChannel((Integer)channelComboBox.getSelectedItem() - 1);
        }
        else if(e.getSource().equals(octaveComboBox)){
            KeyboardDevice keyboard = (KeyboardDevice) midiDevice;
            keyboard.setOctave((Integer) octaveComboBox.getSelectedItem());
        }
    }



}