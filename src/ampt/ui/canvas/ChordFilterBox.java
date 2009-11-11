/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ampt.ui.canvas;

import ampt.examples.filters.ChordFilterDevice;
import ampt.examples.filters.ChordFilterDevice.ChordInversion;
import ampt.examples.filters.ChordFilterDevice.ChordType;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Christopher
 */
public class ChordFilterBox extends MidiDeviceBox implements ActionListener{

    private JComboBox chordTypesComboBox;
    private JComboBox chordInversionsComboBox;

    public ChordFilterBox(ChordFilterDevice device){
        super(device);
        // Needed to override preferred size to be set by the components inside
        // this box
        this.setPreferredSize(null);
        // Needed to make sure that the paintComponent method used is from 
        // JPanel and not MidiDeviceBox
        overridePaintComponent = false;

        this.setBackground(Color.BLUE);
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Chord Filter", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.BELOW_TOP, null, Color.WHITE));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.BLUE);

        Vector<ChordType> chordTypes = new Vector<ChordType>();
        chordTypes.add(ChordType.MAJOR);
        chordTypes.add(ChordType.MINOR);
        chordTypes.add(ChordType.DIMINISHED);
        chordTypes.add(ChordType.AUGMENTED);
        chordTypesComboBox = new JComboBox(chordTypes);
        chordTypesComboBox.setSelectedItem(ChordType.MAJOR);
        device.setChordType(ChordType.MAJOR);
        chordTypesComboBox.addActionListener(this);
        chordTypesComboBox.setBorder(new TitledBorder(new LineBorder(Color.WHITE), "Chord Type", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.WHITE));
        chordTypesComboBox.setBackground(Color.BLUE);
        chordTypesComboBox.setForeground(Color.WHITE);
        centerPanel.add(chordTypesComboBox);

        Vector<ChordInversion> chordInversions = new Vector<ChordInversion>();
        chordInversions.add(ChordInversion.ROOT_POSITION);
        chordInversions.add(ChordInversion.FIRST_INVERSION);
        chordInversions.add(ChordInversion.SECOND_INVERSION);
        chordInversionsComboBox = new JComboBox(chordInversions);
        chordInversionsComboBox.setSelectedItem(ChordInversion.ROOT_POSITION);
        device.setChordInversion(ChordInversion.ROOT_POSITION);
        chordInversionsComboBox.addActionListener(this);
        chordInversionsComboBox.setBorder(new TitledBorder(new LineBorder(Color.WHITE), "Chord Inversion", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.WHITE));
        chordInversionsComboBox.setBackground(Color.BLUE);
        chordInversionsComboBox.setForeground(Color.WHITE);
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