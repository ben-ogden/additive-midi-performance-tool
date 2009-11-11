package ampt.examples;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.Synthesizer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This class opens a window which allows you to select an input and output
 * midi port to connect to each other. The list labeled MIDI Out contains
 * receivers, which could be software synthesizers or actual hardware MIDI Out
 * ports to which you can connect a hardware synthesizer (keyboard). The list
 * labeled MIDI In contains transmitters, which likely represent MIDI In
 * hardware ports.
 *
 * TODO disconnect the devices when changing routing
 * TODO close devices when exiting
 * 
 */
public class MIDILoopBack extends JFrame implements ActionListener {

    /*
     * GUI Components
     */
    JPanel mainPanel;
    JComboBox receiversComboBox;
    JComboBox transmittersComboBox;
    JButton connectDevicesButton;
    JButton exitButton;

    /**
     * Create the GUI
     */
    public MIDILoopBack() {

        /*
         * Populate the vectors for the combo boxes.
         */
        Vector<Info> receivers = new Vector<Info>();
        Vector<Info> transmitters = new Vector<Info>();

        /*
         * Build list of midi receivers and transmitters
         */
        for (Info deviceInfo : MidiSystem.getMidiDeviceInfo()) {

            try {
            
                MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);

                if(device instanceof Synthesizer) {

                    System.out.println(device.getDeviceInfo().getName() + " = " + ((Synthesizer)device).getLatency());
                }

                try {
                    device.getTransmitter();
                    transmitters.add(device.getDeviceInfo());
                } catch (MidiUnavailableException mue) {
                    // fail silently
                }

                try {
                    device.getReceiver();
                    receivers.add(device.getDeviceInfo());
                } catch (MidiUnavailableException mue) {
                    // fail silently
                }
                


            } catch (MidiUnavailableException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error Loading Midi Device: " + deviceInfo.getName(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        /*
         * Create the GUI
         */
        this.mainPanel = new JPanel();
        this.add(mainPanel);
        mainPanel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel receiversLabel = new JLabel("MIDI Out");
        mainPanel.add(receiversLabel);
        receiversComboBox = new JComboBox(receivers);
        mainPanel.add(receiversComboBox);
        JLabel transmittersLabel = new JLabel("MIDI In");
        mainPanel.add(transmittersLabel);
        transmittersComboBox = new JComboBox(transmitters);
        mainPanel.add(transmittersComboBox);

        connectDevicesButton = new JButton("Connect Devices");
        connectDevicesButton.addActionListener(this);
        mainPanel.add(connectDevicesButton);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        mainPanel.add(exitButton);

        /*
         * Pack and display the GUI
         */
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

    }

    /**
     * Starts the application
     *
     * @param args
     */
    public static void main(String[] args) {
        new MIDILoopBack();

    }

    /**
     * This method gets the receiver connects it to the
     * transmitter from the device creating the MIDI messages.
     *
     * @param device
     *            - The device you want to connect to the synthesizer
     * @throws MidiUnavailableException
     */
    private void connectDevices(MidiDevice device, MidiDevice device2)
            throws MidiUnavailableException {

        // is there a cleaner way to reset the connections?
        device.close();
        device2.close();
        device.open();
        device2.open();

        Receiver receiver = device.getReceiver();
        Transmitter transmitter = device2.getTransmitter();
        transmitter.setReceiver(receiver);
        
    }

    /**
     * Action Listener for the buttons.
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        /**************************
         * Connect Devices Button *
         **************************/
        if (event.getSource().equals(connectDevicesButton)) {
            try {
                // Get the reciever from the combo box and open it.
                MidiDevice receiver = MidiSystem.getMidiDevice((Info) receiversComboBox.getSelectedItem());
                receiver.open();

                // Get the other transmitter from the combo box and open it.
                MidiDevice transmitter = MidiSystem.getMidiDevice((Info) transmittersComboBox.getSelectedItem());
                transmitter.open();

                // Connect the devices together so the synthesizer can receive the MIDI messages.
                connectDevices(receiver, transmitter);
                
            } catch (MidiUnavailableException ex) {
                JOptionPane.showMessageDialog(this,
                        "MidiUnavailableException thrown: " + ex.getMessage(),
                        "Midi Unavailable Exception",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        /***************
         * Exit Button *
         ***************/
        if (event.getSource().equals(exitButton)) {

            //TODO close midi resources

            System.exit(0);
        }

    }
}
