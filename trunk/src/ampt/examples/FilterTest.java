/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ampt.examples;

import ampt.examples.filters.ChordFilter;
import ampt.examples.filters.FlutterFilterVariation;
import ampt.ui.keyboard.KeyboardPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class can be used to test filters.  In order to be able to test a filter
 * using this class, you need to drop it into the filters vector.  The filter 
 * must implement Transmitter and Receiver for the filter to be compatible with
 * this class.
 * 
 * This class uses the ui.keyboard.KeyboardPanel for the input, and allows the 
 * user to select a single filter and a single receiver to process the MIDI 
 * messages.
 *
 * @author Chris Redding
 */
public class FilterTest extends JFrame{

    private Receiver currentReceiver;
    private KeyboardPanel keyboard;
    private final JComboBox receiverCombo, filterCombo;

    public FilterTest(){
        super("Filter Test");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        /**
         * Create Options Panel
         */

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(2,2));
        JLabel filterLabel = new JLabel("Filter: ");
        optionsPanel.add(filterLabel);
        Vector<Receiver> filters = new Vector<Receiver>();
        filters.add(new ChangeNoteFilter(null, 12));
        filters.add(new ChordFilter());
        filters.add(new FlutterFilter());
        filters.add(new FlutterFilterVariation());
        /******************************
         * Add any other filters here *
         ******************************/
        filterCombo = new JComboBox(filters);
        filterCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Receiver filterReceiver = (Receiver)filterCombo.getSelectedItem();
                keyboard.setReceiver(filterReceiver);
                Transmitter filterTransmitter = (Transmitter) filterCombo.getSelectedItem();

                filterTransmitter.setReceiver(currentReceiver);

            }
        });
        optionsPanel.add(filterCombo);
        JLabel receiverLabel = new JLabel("Receiver");
        optionsPanel.add(receiverLabel);
        Vector<Info> receivers = new Vector<Info>();
        Info[] deviceInfos = MidiSystem.getMidiDeviceInfo();
        for(Info info: deviceInfos){
            try{
                MidiDevice device = MidiSystem.getMidiDevice(info);
                device.getReceiver();
                receivers.add(info);
            } catch (MidiUnavailableException ex){
                // Fail silently
            }
        }
        receiverCombo = new JComboBox(receivers);
        receiverCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Info deviceInfo = (Info) receiverCombo.getSelectedItem();
                try {
                    MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
                    device.open();
                    Transmitter filterTransmitter = (Transmitter) filterCombo.getSelectedItem();
                    currentReceiver.close();
                    currentReceiver = device.getReceiver();
                    filterTransmitter.setReceiver(currentReceiver);
                } catch (MidiUnavailableException ex) {
                   ex.printStackTrace();
                }
            }
        });
        optionsPanel.add(receiverCombo);


        mainPanel.add(optionsPanel, BorderLayout.NORTH);

        /**
         * Create Keyboard Panel
         */

        keyboard = new KeyboardPanel(1);
        mainPanel.add(keyboard, BorderLayout.SOUTH);
        
        this.add(mainPanel);

        Receiver filterReceiver = (Receiver)filterCombo.getSelectedItem();
        keyboard.setReceiver(filterReceiver);
        Transmitter filterTransmitter = (Transmitter)filterCombo.getSelectedItem();
        Info deviceInfo= (Info) receiverCombo.getSelectedItem();
        try {
            MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
            device.open();
            currentReceiver = device.getReceiver();
            filterTransmitter.setReceiver(currentReceiver);
        } catch (MidiUnavailableException ex) {
           ex.printStackTrace();
        }
    }

    /**
     * Starts the application
     */
    public static void main(String[] args){
        FilterTest test = new FilterTest();
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.pack();
        test.setLocationRelativeTo(null);
        test.setVisible(true);
    }

}
