package examples;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
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
 * This class opens a window which allows you to select a synthesizer and
 * another midi device to connect to each other.
 * 
 * I used this to connect my keyboard to the Java Synthesizer. I was able to
 * click on connect devices, and hear sound come from my PC when I pressed a key
 * on the keyboard.
 * 
 * NOTE: Not all devices in the 'MIDI Device' drop down box will allow you to 
 * access their transmitter.  This drop down is just a list of the devices 
 * on the system that are NOT synthesizers.
 * 
 */
@SuppressWarnings("serial")
public class ConnectDevices extends JFrame implements ActionListener {

	/*
	 * GUI Components
	 */
	JPanel mainPanel;
	JComboBox synthsComboBox;
	JComboBox midiDevicesComboBox;
	JButton connectDevicesButton;
	JButton exitButton;

	/**
	 * Create the GUI
	 */
	public ConnectDevices() {



		/*
		 * Populate the vectors for the combo boxes.
		 */
		Vector<Info> synths = new Vector<Info>();
		Vector<Info> midiDevices = new Vector<Info>();
		Info[] deviceInfos = MidiSystem.getMidiDeviceInfo();
		for (Info deviceInfo : deviceInfos) {
			try {
				MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
				if (device instanceof Synthesizer) {
					synths.add(deviceInfo);
				} else {
					midiDevices.add(deviceInfo);
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
		
		JLabel synthsLabel = new JLabel("Synthesizer");
		mainPanel.add(synthsLabel);
		synthsComboBox = new JComboBox(synths);
		mainPanel.add(synthsComboBox);
		JLabel midiDevicesLabel = new JLabel("MIDI Device");
		mainPanel.add(midiDevicesLabel);
		midiDevicesComboBox = new JComboBox(midiDevices);
		mainPanel.add(midiDevicesComboBox);

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
		new ConnectDevices();

	}

	/**
	 * This method gets the receiver from the synthesizer and connects it to the
	 * transmitter from the device creating the MIDI messages.
	 * 
	 * @param synth
	 *            - The synthesizer to use for producing sound
	 * @param device
	 *            - The device you want to connect to the synthesizer
	 * @throws MidiUnavailableException
	 */
	private void connectDevices(Synthesizer synth, MidiDevice device)
			throws MidiUnavailableException {
		Receiver synthRcvr = synth.getReceiver();

		Transmitter keyboardTrans = device.getTransmitter();

		keyboardTrans.setReceiver(synthRcvr);
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
				// Get the synthesizer from the combo box and open it.
				Synthesizer synth = (Synthesizer) MidiSystem
						.getMidiDevice((Info) synthsComboBox.getSelectedItem());
				synth.open();
				
				// Get the other MIDI device from the combo box and open it.
				MidiDevice device = MidiSystem
						.getMidiDevice((Info) midiDevicesComboBox
								.getSelectedItem());
				device.open();
				
				// Connect the devices together so the synthesizer can receive the MIDI messages.
				connectDevices(synth, device);
			} catch (MidiUnavailableException ex) {
				JOptionPane
						.showMessageDialog(this,
								"MidiUnavailableException thrown: "
										+ ex.getMessage(),
								"Midi Unavailable Exception",
								JOptionPane.ERROR_MESSAGE);
			}
		}

		/***************
		 * Exit Button *
		 ***************/
		
		if (event.getSource().equals(exitButton)) {
			System.exit(0);
		}

	}

}
