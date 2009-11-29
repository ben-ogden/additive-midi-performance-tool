package ampt.ui.filters;

import ampt.core.devices.ArpeggiatorFilterDevice;
import ampt.midi.note.Arpeggio;
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
 *
 * @author Rob
 */
public class ArpeggiatorFilterBox extends AmptMidiDeviceBox implements ActionListener {
    private JComboBox chordTypeComboBox;
    private JComboBox noteValueComboBox;
    private JComboBox arpTypeComboBox;

    public ArpeggiatorFilterBox(ArpeggiatorFilterDevice device) throws MidiUnavailableException {

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

        arpTypeComboBox = new JComboBox();
        arpTypeComboBox.addItem("Up");
        arpTypeComboBox.addItem("Down");
        arpTypeComboBox.addItem("Up and Down");
        arpTypeComboBox.addItem("Down and Up");
        arpTypeComboBox.setSelectedIndex(Arpeggio.ASCEND_DESCEND);
        device.setMotion(Arpeggio.ASCEND_DESCEND);
        arpTypeComboBox.addActionListener(this);
        arpTypeComboBox.setBorder(new TitledBorder(new LineBorder(Color.WHITE),
                "Motion", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, Color.WHITE));
        arpTypeComboBox.setBackground(Color.GREEN);
        arpTypeComboBox.setForeground(Color.WHITE);
        arpTypeComboBox.setKeySelectionManager(new EmptyKeySelectionManager());
        centerPanel.add(arpTypeComboBox);

        chordTypeComboBox = new JComboBox();
        chordTypeComboBox.addItem("Major");
        chordTypeComboBox.addItem("Minor");
        chordTypeComboBox.addItem("Augmented");
        chordTypeComboBox.addItem("Diminished");
        chordTypeComboBox.setSelectedIndex(Arpeggio.MAJOR);
        device.setChordType(Arpeggio.MAJOR);
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
            ArpeggiatorFilterDevice arp = (ArpeggiatorFilterDevice) midiDevice;
            arp.setChordType(chordTypeComboBox.getSelectedIndex());
        }
        if (e.getSource().equals(noteValueComboBox)) {
            ArpeggiatorFilterDevice arp = (ArpeggiatorFilterDevice) midiDevice;
            arp.setNoteValue((NoteValue)noteValueComboBox.getSelectedItem());
        }
        if (e.getSource().equals(arpTypeComboBox)) {
            ArpeggiatorFilterDevice arp = (ArpeggiatorFilterDevice) midiDevice;
            arp.setMotion(arpTypeComboBox.getSelectedIndex());
        }
    }
}
