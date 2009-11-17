package ampt.examples.timing;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Here is a test similar to SoundTimingTest except that it sends the notes to
 * an external hardware synthesizer.
 *
 * @author Ben
 */
public class HardwareTimingTest extends JFrame implements ActionListener {

    MidiDevice outDevice;      // The midi port and channel to play the test on
    Integer channel;           // The midi channel
    Integer interval;          // The note interval
    Integer length;            // The note length
    // The thread running the test
    Thread testThread;

    /*
     * GUI Components
     */
    JPanel mainPanel;
    JComboBox receiversComboBox;
    JComboBox channelComboBox;
    JTextField intervalTextField;
    JTextField lengthTextField;
    JButton startTestButton;
    JButton exitButton;

    public HardwareTimingTest() {

        /*
         * Populate the vectors for the combo boxes.
         */
        Vector<Info> receivers = new Vector<Info>();

        /*
         * Build list of midi receivers
         */
        MidiDevice device;
        for (Info deviceInfo : MidiSystem.getMidiDeviceInfo()) {

            try {
                device = MidiSystem.getMidiDevice(deviceInfo);
                if (device.getMaxReceivers() != 0) {
                    device.getReceiver();
                    receivers.add(device.getDeviceInfo());
                }
            } catch (MidiUnavailableException ex) {
                throw new RuntimeException(ex);
            }

        }

        /*
         * Create the GUI
         */
        this.mainPanel = new JPanel();
        this.add(mainPanel);
        mainPanel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel receiversLabel = new JLabel("MIDI Out");
        mainPanel.add(receiversLabel);
        receiversComboBox = new JComboBox(receivers);
        mainPanel.add(receiversComboBox);

        JLabel channelLabel = new JLabel("MIDI Channel");
        mainPanel.add(channelLabel);
        channelComboBox = new JComboBox();
        for (int i = 1; i <= 16; i++) {
            channelComboBox.addItem(new Integer(i));
        }
        mainPanel.add(channelComboBox);

        JLabel intervalLabel = new JLabel("Note Interval (ms)");
        mainPanel.add(intervalLabel);
        intervalTextField = new JTextField("120");
        mainPanel.add(intervalTextField);

        JLabel lengthLabel = new JLabel("Note Length (ms)");
        mainPanel.add(lengthLabel);
        lengthTextField = new JTextField("60");
        mainPanel.add(lengthTextField);


        startTestButton = new JButton("Start");
        startTestButton.addActionListener(this);
        mainPanel.add(startTestButton);

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
     * Action Listener for the buttons.
     */
    @Override
    public void actionPerformed(ActionEvent event) {


        /***************
         * Start Button *
         ***************/
        if (event.getSource().equals(startTestButton)) {
            try {

                // disable the start button
                startTestButton.setEnabled(false);

                // Get the reciever from the combo box
                outDevice = MidiSystem.getMidiDevice((Info) receiversComboBox.getSelectedItem());
                channel = (Integer) channelComboBox.getSelectedItem();
                interval = Integer.parseInt(intervalTextField.getText());
                length = Integer.parseInt(lengthTextField.getText());

            } catch (MidiUnavailableException ex) {
                throw new RuntimeException(ex);
            }

            // start the test
            testThread = new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        // if open, close before starting test
                        if (outDevice.isOpen()) {
                            outDevice.close();
                        }
                        outDevice.open();

                        SoundTimingTest soundTimingTest = new SoundTimingTest();
                        soundTimingTest.playNotesAtInterval(outDevice.getReceiver(), channel, interval, length);
                    } catch (InvalidMidiDataException ex) {
                        throw new RuntimeException(ex);
                    } catch (MidiUnavailableException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            testThread.start();

        }

        /***************
         * Exit Button *
         ***************/
        if (event.getSource().equals(exitButton)) {


            // clean up
            if (outDevice.isOpen()) {
                outDevice.close();
            }
            System.exit(0);
        }
    }

    public static void main(String args[]) {

        new HardwareTimingTest();

    }
}
