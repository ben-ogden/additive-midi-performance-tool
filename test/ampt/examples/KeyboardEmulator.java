package ampt.examples;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.midi.*;
import javax.sound.midi.MidiDevice.Info;

public class KeyboardEmulator {

    private Synthesizer synthesizer;
    private Receiver receiver;
    
    /*
     * return an array of instruments that are currently
     * loaded in the synthesizer
     */
    public Instrument[] getLoadedInstruments() {
        return synthesizer.getLoadedInstruments();
    }

    /*
     * set synthesizer, obtain and set receiver,
     * and load available instruments
     */
    public void setSynthesizer(Synthesizer synthesizer)
        throws MidiUnavailableException {
        
        receiver = synthesizer.getReceiver();
        synthesizer.open();
        
        Instrument[] instruments = synthesizer.getAvailableInstruments();
        for(Instrument instrument : instruments)
            synthesizer.loadInstrument(instrument);
        
        this.synthesizer = synthesizer;
    }
    
    /*
     * sends a MIDI start note message
     */
    public void noteOn(int note) {
        ShortMessage msgNote = new ShortMessage();        
         try {
            msgNote.setMessage(ShortMessage.NOTE_ON, 0, note, 93);   
            receiver.send(msgNote, -1);
        } catch(InvalidMidiDataException e) {
        } 
    }
    
    /*
     * sends a MIDI stop note message
     */
    public void noteOff(int note) {
        ShortMessage msgNote = new ShortMessage();        
         try {
            msgNote.setMessage(ShortMessage.NOTE_OFF, 0, note, 93);   
            receiver.send(msgNote, -1);
        } catch(InvalidMidiDataException e) {
        }
    }
    
    /*
     * sends a MIDI change program message
     */
    public void changeInstrument(int program) {
        ShortMessage msgInstr = new ShortMessage();
        try {
            msgInstr.setMessage(ShortMessage.PROGRAM_CHANGE, 0, program, 0);
            receiver.send(msgInstr, -1);
        } catch(InvalidMidiDataException e) {
        }
    }

    public static void main(String[] args) {
    
        final KeyboardEmulator ke = new KeyboardEmulator();
        final JFrame jf = new JFrame();
        final JComboBox instrComboBox = new JComboBox();
        final JComboBox synthComboBox = new JComboBox();
        Container content = jf.getContentPane();
        
        /*
         * Create and populate the synthesizer combo box
         */
        Info[] allDevices = MidiSystem.getMidiDeviceInfo();
        for(Info info : allDevices) {
            try {
                if(MidiSystem.getMidiDevice(info) instanceof Synthesizer)
                    synthComboBox.addItem(info);
            } catch(MidiUnavailableException e) {
            }
        }
        
        /*
         * Create select synthesizer button and associated listener
         */
        JButton openSynthButton = new JButton("Select Synthesizer");
        openSynthButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    ke.setSynthesizer((Synthesizer)MidiSystem.getMidiDevice(
                        (Info)synthComboBox.getSelectedItem()));
                } catch(MidiUnavailableException e) {
                }
                
                Instrument[] instruments = ke.getLoadedInstruments();
                for(Instrument instrument : instruments)
                    instrComboBox.addItem(instrument);
                jf.pack();
            }
        });
        
        /*
         * Create select instrument button and associated listener
         */
         JButton selectInstrButton = new JButton("Select Instrument");
         selectInstrButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int program =
                    ((Instrument)instrComboBox.getSelectedItem())
                    .getPatch().getProgram();
                ke.changeInstrument(program);
            }
         });
        
        /*
         * Create play note 1 button and associated listener
         */
        JButton playNote1Button = new JButton("Play Note 1");
        playNote1Button.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent me) { ke.noteOn(60); }
            public void mouseReleased(MouseEvent me) { ke.noteOff(60); }
            public void mouseEntered(MouseEvent me) {}
            public void mouseExited(MouseEvent me) {}
            public void mouseClicked(MouseEvent me) {}
            
        });
        
        /*
         * Create play note 2 button and associated listener
         */
        JButton playNote2Button = new JButton("Play Note 2");
        playNote2Button.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent me) { ke.noteOn(100); }
            public void mouseReleased(MouseEvent me) { ke.noteOff(100); }
            public void mouseEntered(MouseEvent me) {}
            public void mouseExited(MouseEvent me) {}
            public void mouseClicked(MouseEvent me) {}
            
        });
        
        /*
         * Create exit button and associated listener
         */
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
        
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(2, 2));
        settingsPanel.add(new JLabel("Select Synthesizer"));
        settingsPanel.add(synthComboBox);
        settingsPanel.add(new JLabel("Select Instrument"));
        settingsPanel.add(instrComboBox);
        content.add(settingsPanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openSynthButton);
        buttonPanel.add(selectInstrButton);
        buttonPanel.add(exitButton);
        content.add(buttonPanel);
        
        JPanel keyboardPanel = new JPanel();
        keyboardPanel.add(playNote1Button);
        keyboardPanel.add(playNote2Button);
        content.add(keyboardPanel, BorderLayout.SOUTH);
        
        jf.pack();
        jf.setVisible(true);
    }
}