package ampt.ui.filters;

import ampt.core.devices.ArpeggiatorFilterDevice;
import ampt.midi.note.Arpeggio;
import ampt.midi.note.NoteValue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Rob
 */
public class ArpeggiatorFilterBox extends AmptMidiDeviceBox implements ActionListener {

    private static final long serialVersionUID = -230533765353151289L;
	
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
        centerPanel.setLayout(new GridLayout(6, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(2, 12, 2, 12));
        centerPanel.setBackground(Color.GREEN);

        JLabel motionLabel = new JLabel("Motion", JLabel.LEFT);
        motionLabel.setFont(new Font("SanSerif", Font.PLAIN, 12));
        motionLabel.setForeground(Color.WHITE);
        motionLabel.setHorizontalAlignment(JLabel.LEADING);
        centerPanel.add(motionLabel);

        arpTypeComboBox = new JComboBox();
        arpTypeComboBox.addItem("Up");
        arpTypeComboBox.addItem("Down");
        arpTypeComboBox.addItem("Up and Down");
        arpTypeComboBox.addItem("Down and Up");
        arpTypeComboBox.addItem("Random");
        arpTypeComboBox.setSelectedIndex(Arpeggio.ASCEND_DESCEND);
        device.setMotion(Arpeggio.ASCEND_DESCEND);
        arpTypeComboBox.addActionListener(this);
        arpTypeComboBox.setBackground(Color.GREEN);
        arpTypeComboBox.setForeground(Color.WHITE);
        arpTypeComboBox.setKeySelectionManager(new EmptyKeySelectionManager());
        centerPanel.add(arpTypeComboBox);

        JLabel typeLabel = new JLabel("Chord Type", JLabel.LEFT);
        typeLabel.setFont(new Font("SanSerif", Font.PLAIN, 12));
        typeLabel.setForeground(Color.WHITE);
        typeLabel.setHorizontalAlignment(JLabel.LEADING);
        centerPanel.add(typeLabel);

        chordTypeComboBox = new JComboBox();
        chordTypeComboBox.addItem("Major");
        chordTypeComboBox.addItem("Minor");
        chordTypeComboBox.addItem("Augmented");
        chordTypeComboBox.addItem("Diminished");
        chordTypeComboBox.setSelectedIndex(Arpeggio.MAJOR);
        device.setChordType(Arpeggio.MAJOR);
        chordTypeComboBox.addActionListener(this);
        chordTypeComboBox.setBackground(Color.GREEN);
        chordTypeComboBox.setForeground(Color.WHITE);
        chordTypeComboBox.setKeySelectionManager(new EmptyKeySelectionManager());
        centerPanel.add(chordTypeComboBox);

        JLabel noteLabel = new JLabel("Note Duration", JLabel.LEFT);
        noteLabel.setFont(new Font("SanSerif", Font.PLAIN, 12));
        noteLabel.setForeground(Color.WHITE);
        noteLabel.setHorizontalAlignment(JLabel.LEADING);
        centerPanel.add(noteLabel);

        Vector<NoteValue> noteValues = new Vector<NoteValue>();
        for(NoteValue noteValue : NoteValue.values())
            noteValues.add(noteValue);
        noteValueComboBox = new JComboBox(noteValues);
        noteValueComboBox.setSelectedItem(NoteValue.EIGHTH_NOTE);
        device.setNoteValue(NoteValue.EIGHTH_NOTE);
        noteValueComboBox.addActionListener(this);
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
