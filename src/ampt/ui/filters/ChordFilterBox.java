package ampt.ui.filters;

import ampt.core.devices.ChordFilterDevice;
import ampt.midi.chord.ChordInversion;
import ampt.midi.chord.ChordType;

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
 * @author Christopher
 */
public class ChordFilterBox extends AmptMidiDeviceBox implements ActionListener{

    private static final long serialVersionUID = -2784740246353685410L;
	
    private JComboBox chordTypesComboBox;
    private JComboBox chordInversionsComboBox;

    public ChordFilterBox(ChordFilterDevice device) throws MidiUnavailableException{
        
        super(device, null, Color.BLUE, Color.WHITE);

        // Needed to override preferred size to be set by the components inside
        // this box
        this.setPreferredSize(null);
        // Needed to make sure that the paintComponent method used is from 
        // JPanel and not MidiDeviceBox
        overridePaintComponent = false;

        this.setBorder(new TitledBorder(new LineBorder(Color.WHITE), "Chord Filter",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.BELOW_TOP, null, Color.WHITE));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(2, 12, 2, 12));
        centerPanel.setBackground(Color.BLUE);

        JLabel typeLabel = new JLabel("Chord Type", JLabel.LEFT);
        typeLabel.setFont(new Font("SanSerif", Font.PLAIN, 12));
        typeLabel.setForeground(Color.WHITE);
        typeLabel.setHorizontalAlignment(JLabel.LEADING);
        centerPanel.add(typeLabel);

        Vector<ChordType> chordTypes = new Vector<ChordType>();
        chordTypes.add(ChordType.MAJOR);
        chordTypes.add(ChordType.MINOR);
        chordTypes.add(ChordType.DIMINISHED);
        chordTypes.add(ChordType.AUGMENTED);
        chordTypesComboBox = new JComboBox(chordTypes);
        chordTypesComboBox.setSelectedItem(ChordType.MAJOR);
        device.setChordType(ChordType.MAJOR);
        chordTypesComboBox.addActionListener(this);
        chordTypesComboBox.setBackground(Color.BLUE);
        chordTypesComboBox.setForeground(Color.WHITE);
        chordTypesComboBox.setKeySelectionManager(new EmptyKeySelectionManager());
        centerPanel.add(chordTypesComboBox);

        JLabel invLabel = new JLabel("Chord Inversion", JLabel.LEFT);
        invLabel.setFont(new Font("SanSerif", Font.PLAIN, 12));
        invLabel.setForeground(Color.WHITE);
        invLabel.setHorizontalAlignment(JLabel.LEFT);
        centerPanel.add(invLabel);

        Vector<ChordInversion> chordInversions = new Vector<ChordInversion>();
        chordInversions.add(ChordInversion.ROOT_POSITION);
        chordInversions.add(ChordInversion.FIRST_INVERSION);
        chordInversions.add(ChordInversion.SECOND_INVERSION);
        chordInversionsComboBox = new JComboBox(chordInversions);
        chordInversionsComboBox.setSelectedItem(ChordInversion.ROOT_POSITION);
        device.setChordInversion(ChordInversion.ROOT_POSITION);
        chordInversionsComboBox.addActionListener(this);
        chordInversionsComboBox.setBackground(Color.BLUE);
        chordInversionsComboBox.setForeground(Color.WHITE);
        chordInversionsComboBox.setKeySelectionManager(new EmptyKeySelectionManager());
        centerPanel.add(chordInversionsComboBox);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(chordTypesComboBox)){
            ChordFilterDevice chordDevice = (ChordFilterDevice) midiDevice;
            chordDevice.setChordType((ChordType)chordTypesComboBox.getSelectedItem());
        } else if (e.getSource().equals(chordInversionsComboBox)){
            ChordFilterDevice chordDevice = (ChordFilterDevice) midiDevice;
            chordDevice.setChordInversion((ChordInversion) chordInversionsComboBox.getSelectedItem());
        }
    }

}
