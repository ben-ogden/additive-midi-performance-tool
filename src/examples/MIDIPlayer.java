package examples;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Vector;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This class opens a window which lets the user select a MIDI out 
 * device, a midi sequencer, and a midi song from the resources 
 * folder.  The MIDI out list contains receivers.  When the user 
 * presses the 'Play/Stop With Selected Devices' button, the 
 * class will connect the sequencer to the specified MIDI
 * out device, and then start playing the selected file.
 */
public class MIDIPlayer extends JFrame implements ActionListener{

    /*
     * GUI Components
     */
    JPanel mainPanel;
    JComboBox receiversComboBox;
    JComboBox sequencersComboBox;
    JComboBox songsComboBox;
    JButton connectDevicesButton;
    JButton exitButton;
    
    File[] midiFiles;
    boolean playing = false;
    Sequencer sequencer;
	
    /**
     * Create the GUI
     */
	public MIDIPlayer(){
        /*
         * Populate the vectors for the combo boxes.
         */
        Vector<Info> receivers = new Vector<Info>();
        Vector<Info> sequencerVector = new Vector<Info>();

        /*
         * Build list of midi receivers and transmitters
         */
        for (Info deviceInfo : MidiSystem.getMidiDeviceInfo()) {

            try {
            
                MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
                
                if(device instanceof Sequencer){
                	sequencerVector.add(device.getDeviceInfo());
                	continue;
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
         * Populate the Songs Combo Box from the resources folder
         */
        
        File resourcesFolder = new File("./resources");
        FilenameFilter midiFilter = new FilenameFilter() {
        	public boolean accept(File dir, String name){
        		if (name.endsWith(".mid"))
        			return true;
        		return false;
        	}
        };
        midiFiles = resourcesFolder.listFiles(midiFilter);
        

        /*
         * Create the GUI
         */
        this.mainPanel = new JPanel();
        this.add(mainPanel);
        mainPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel receiversLabel = new JLabel("MIDI Out");
        mainPanel.add(receiversLabel);
        receiversComboBox = new JComboBox(receivers);
        mainPanel.add(receiversComboBox);
        JLabel sequencersLabel = new JLabel("Sequencer");
        mainPanel.add(sequencersLabel);
        sequencersComboBox = new JComboBox(sequencerVector);
        mainPanel.add(sequencersComboBox);
        JLabel fileLabel = new JLabel("MIDI File");
        mainPanel.add(fileLabel);
        songsComboBox = new JComboBox(midiFiles);
        mainPanel.add(songsComboBox);

        connectDevicesButton = new JButton("Play/Stop With Selected Devices");
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
     * Action Listener for the buttons.
     */
    @Override
	public void actionPerformed(ActionEvent event){
        /**************************
         * Connect Devices Button *
         **************************/
        if (event.getSource().equals(connectDevicesButton)) {
        	
            try {
            	// if the file is already playing, stop it.
                if(playing){
            		sequencer.stop();
            		playing = false;
            		sequencersComboBox.setEnabled(true);
            		receiversComboBox.setEnabled(true);
            		songsComboBox.setEnabled(true);
            		return;
            	} else {

                // Get the reciever from the combo box and open it.
                MidiDevice receiver = MidiSystem.getMidiDevice((Info) receiversComboBox.getSelectedItem());

                // Get the other transmitter from the combo box and open it.
                sequencer = (Sequencer) MidiSystem.getMidiDevice((Info) sequencersComboBox.getSelectedItem());

	                // Connect the devices together so the synthesizer can receive the MIDI messages.
	                connectDevices(receiver, sequencer);
	                
	                //Play the selected file
	                File midiFile = midiFiles[songsComboBox.getSelectedIndex()];
	                Sequence midiSequence = MidiSystem.getSequence(midiFile);
	                sequencer.setSequence(midiSequence);
	                sequencer.start();
	                playing = true;
	                sequencersComboBox.setEnabled(false);
	                receiversComboBox.setEnabled(false);
	                songsComboBox.setEnabled(false);

            	}
            } catch (MidiUnavailableException ex) {
                JOptionPane.showMessageDialog(this,
                        "MidiUnavailableException thrown: " + ex.getMessage(),
                        "Midi Unavailable Exception",
                        JOptionPane.ERROR_MESSAGE);
            } catch (InvalidMidiDataException ex) {
                JOptionPane.showMessageDialog(this,
                        "InvalidMidiDataException thrown: " + ex.getMessage(),
                        "Invalid Midi Data Exception",
                        JOptionPane.ERROR_MESSAGE);
			} catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "IOException thrown: " + ex.getMessage(),
                        "IO Exception",
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
	
	public static void main(String[] args){
		new MIDIPlayer();
	}
	
}
