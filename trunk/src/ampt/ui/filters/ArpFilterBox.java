package ampt.ui.filters;

import ampt.core.devices.ArpFilterDevice;
import ampt.midi.chord.ChordType;
import ampt.midi.note.NoteValue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 * The display box for an arp filter
 * 
 * @author Rob
 */
public class ArpFilterBox extends AmptMidiDeviceBox implements ActionListener {

    private JComboBox chordTypeComboBox;
    private JComboBox noteValueComboBox;
    private JComboBox arpTypeComboBox;

    public ArpFilterBox(ArpFilterDevice device) throws MidiUnavailableException {
        
        super(device, null, Color.GREEN, Color.WHITE);

        // Needed to override preferred size to be set by the components inside
        // this box
        this.setPreferredSize(null);
        // Needed to make sure that the paintComponent method used is from
        // JPanel and not MidiDeviceBox
        overridePaintComponent = false;

        this.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Arpeggiator",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.BELOW_TOP,
                null, Color.WHITE));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.GREEN);

        arpTypeComboBox = new JComboBox(ArpFilterDevice.arpTypes);
        arpTypeComboBox.setSelectedIndex(ArpFilterDevice.ASCEND_DESCEND);
        device.setArpType(ArpFilterDevice.ASCEND_DESCEND);
        arpTypeComboBox.addActionListener(this);
        arpTypeComboBox.setBorder(new TitledBorder(new LineBorder(Color.WHITE),
                "Motion", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, Color.WHITE));
        arpTypeComboBox.setBackground(Color.GREEN);
        arpTypeComboBox.setForeground(Color.WHITE);
        arpTypeComboBox.setKeySelectionManager(new EmptyKeySelectionManager());
        centerPanel.add(arpTypeComboBox);

        Vector<ChordType> chordTypes = new Vector<ChordType>();
        for(ChordType chordType : ChordType.values())
            chordTypes.add(chordType);
        chordTypeComboBox = new JComboBox(chordTypes);
        chordTypeComboBox.setSelectedItem(ChordType.MAJOR);
        device.setChordType(ChordType.MAJOR);
        chordTypeComboBox.addActionListener(this);
        chordTypeComboBox.setBorder(new TitledBorder(new LineBorder(Color.WHITE),
                "Chord Type", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, Color.WHITE));
        chordTypeComboBox.setBackground(Color.GREEN);
        chordTypeComboBox.setForeground(Color.WHITE);
        chordTypeComboBox.setKeySelectionManager(new EmptyKeySelectionManager());
        centerPanel.add(chordTypeComboBox);

        Vector<NoteValue> noteValues = new Vector<NoteValue>();
        for(NoteValue noteValue : NoteValue.values())
            noteValues.add(noteValue);
        noteValueComboBox = new JComboBox(noteValues);
        noteValueComboBox.setSelectedItem(NoteValue.EIGHTH_NOTE);
        device.setNoteValue(NoteValue.EIGHTH_NOTE);
        noteValueComboBox.addActionListener(this);
        noteValueComboBox.setBorder(new TitledBorder(new LineBorder(Color.WHITE),
                "Note Duration", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, Color.WHITE));
        noteValueComboBox.setBackground(Color.GREEN);
        noteValueComboBox.setForeground(Color.WHITE);
        noteValueComboBox.setKeySelectionManager(new EmptyKeySelectionManager());
        centerPanel.add(noteValueComboBox);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(chordTypeComboBox)) {
            ArpFilterDevice arp = (ArpFilterDevice) midiDevice;
            arp.setChordType((ChordType)chordTypeComboBox.getSelectedItem());
        }
        if (e.getSource().equals(noteValueComboBox)) {
            ArpFilterDevice arp = (ArpFilterDevice) midiDevice;
            arp.setNoteValue((NoteValue)noteValueComboBox.getSelectedItem());
        }
        if (e.getSource().equals(arpTypeComboBox)) {
            ArpFilterDevice arp = (ArpFilterDevice) midiDevice;
            arp.setArpType(arpTypeComboBox.getSelectedIndex());
        }
    }
}
