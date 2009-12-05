package ampt.ui.filters;

import ampt.core.devices.KeyboardDevice;
import ampt.ui.keyboard.KeyboardPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSplitPaneUI.BasicVerticalLayoutManager;

/**
 * This class extends MidiDeviceBox in order to have the keyboard and its
 * properties be part of the canvas box.
 * 
 * @author Christopher
 */
public class KeyboardBox extends AmptMidiDeviceBox implements ChangeListener, ActionListener {


    private static final long serialVersionUID = -4364177791056312767L;
	
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

        super(device, null, Color.CYAN, Color.BLACK);
        
        // We need to change the preferred size to null since it was changed in
        // MidiDeviceBox.  This way, the components will determine the size of 
        // this panel.
        this.setPreferredSize(null);
        overridePaintComponent = false;

        this.setBorder(new TitledBorder(new LineBorder(Color.BLACK), device.getDeviceInfo().getName(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.BELOW_TOP));

        JPanel innerPanel = new JPanel(new BorderLayout());

        // Make the center panel contain the keyboard
        JPanel centerPanel = new JPanel();
        KeyboardPanel keyboardPanel = new KeyboardPanel(device, extended);
        keyboardPanel.setBackground(Color.CYAN);
        centerPanel.add(keyboardPanel);
        centerPanel.setBackground(Color.CYAN);
        innerPanel.add(centerPanel, BorderLayout.CENTER);

        // Make a south panel to contain the velocity slider
        JPanel southPanel = new JPanel(new GridLayout(2, 1));
        southPanel.setBackground(Color.CYAN);
        velocitySlider = new JSlider(VELOCITY_MIN, VELOCITY_MAX, STARTING_VELOCITY);
        velocitySlider.setBackground(Color.CYAN);
        device.setVelocity(STARTING_VELOCITY);
        velocitySlider.addChangeListener(this);

        JLabel vLabel = new JLabel("Velocity", JLabel.LEFT);
        vLabel.setFont(new Font("SanSerif", Font.PLAIN, 12));
        southPanel.add(vLabel);
        southPanel.add(velocitySlider);
        southPanel.setBorder(BorderFactory.createEmptyBorder(1, 10, 1, 10));
        innerPanel.add(southPanel, BorderLayout.SOUTH);

        // Make an east panel to contain the channel and octave selecters
        JPanel centerEastPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerEastPanel.setBackground(Color.CYAN);

        Integer[] channels = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        channelComboBox = new JComboBox(channels);
        channelComboBox.setBackground(Color.CYAN);
        channelComboBox.addActionListener(this);
        channelComboBox.setSelectedIndex(0);
        channelComboBox.setKeySelectionManager(new EmptyKeySelectionManager());

        Integer[] octaves = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        octaveComboBox = new JComboBox(octaves);
        octaveComboBox.setBackground(Color.CYAN);
        octaveComboBox.addActionListener(this);
        octaveComboBox.setSelectedIndex(5);
        octaveComboBox.setKeySelectionManager(new EmptyKeySelectionManager());

        JPanel intPanel = new JPanel(new GridLayout(4, 1));
        intPanel.setBackground(Color.CYAN);

        JLabel channelLabel = new JLabel("Channel", JLabel.LEFT);
        channelLabel.setFont(new Font("SanSerif", Font.PLAIN, 12));

        JLabel octaveLabel = new JLabel("Octave", JLabel.LEFT);
        octaveLabel.setFont(new Font("SanSerif", Font.PLAIN, 12));

        intPanel.add(channelLabel);
        intPanel.add(channelComboBox);
        intPanel.add(octaveLabel);
        intPanel.add(octaveComboBox);

        centerEastPanel.add(intPanel);
        //centerEastPanel.add(octaveComboBox);

        innerPanel.add(centerEastPanel, BorderLayout.EAST);

        this.add(innerPanel, BorderLayout.CENTER);
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
