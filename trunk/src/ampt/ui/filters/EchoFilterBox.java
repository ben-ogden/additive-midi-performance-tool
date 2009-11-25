/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ampt.ui.filters;

import ampt.core.devices.EchoFilterDevice;
import ampt.midi.note.NoteValue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 *
 * @author Ben
 */
public class EchoFilterBox extends MidiDeviceBox implements ActionListener, 
        ChangeListener {

    private static final Color FILTER_BGCOLOR = Color.BLACK;
    private static final Color FILTER_FGCOLOR = Color.WHITE;

    private JComboBox intervalComboBox;

    private JSlider durationSlider;



    public EchoFilterBox(EchoFilterDevice device) throws MidiUnavailableException {

        super(device);

        this.setPreferredSize(null);
        overridePaintComponent = false;

        this.setBackground(FILTER_BGCOLOR);
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder(new LineBorder(FILTER_FGCOLOR),
                                        "Echo Filter",
                                        TitledBorder.DEFAULT_JUSTIFICATION,
                                        TitledBorder.BELOW_TOP,
                                        null,
                                        FILTER_FGCOLOR));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(FILTER_BGCOLOR);

        // create duration slider
        durationSlider = createDurationSlider();
        centerPanel.add(durationSlider);

        // create note interval combo box
        intervalComboBox = createIntervalBox();
        centerPanel.add(intervalComboBox);


        // set default values
        intervalComboBox.setSelectedItem(NoteValue.EIGHTH_NOTE);

        this.add(centerPanel, BorderLayout.CENTER);

        if(hasReceiver()){
            JPanel westPanel = new JPanel();
            westPanel.setBackground(FILTER_BGCOLOR);
            westPanel.setLayout(new GridLayout(2,1));
            westPanel.add(new JLabel());
            BoxArrow boxArrow = new BoxArrow();
            boxArrow.setColor(FILTER_FGCOLOR);
            westPanel.add(boxArrow);
            this.add(westPanel, BorderLayout.WEST);
        }

        if(hasTransmitter()){
            JPanel eastPanel = new JPanel();
            eastPanel.setBackground(FILTER_BGCOLOR);
            eastPanel.setLayout(new GridLayout(2,1));
            eastPanel.add(new JLabel());
            BoxArrow boxArrow = new BoxArrow();
            boxArrow.setColor(FILTER_FGCOLOR);
            eastPanel.add(boxArrow);
            this.add(eastPanel, BorderLayout.EAST);
        }

    }

    /*
     * Create the Interval combo box
     */
    private JComboBox createIntervalBox() {

        Vector<NoteValue> noteValues = new Vector<NoteValue>();
        for(NoteValue noteValue : NoteValue.values()) {
            noteValues.add(noteValue);
        }

        JComboBox box = new JComboBox(noteValues);
        box.setBorder(new TitledBorder(new LineBorder(FILTER_FGCOLOR),
                "Interval", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, FILTER_FGCOLOR));
        box.setBackground(FILTER_BGCOLOR);
        box.setForeground(FILTER_FGCOLOR);
        // disable key handler
        box.setKeySelectionManager(new EmptyKeySelectionManager());
        // set actionlistener
        box.addActionListener(this);
        
        return box;
    }

    private JSlider createDurationSlider() {

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 20, 3);

        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);

        //slider.setPaintTrack(false);

        slider.setBackground(FILTER_BGCOLOR);
        slider.setForeground(FILTER_FGCOLOR);

        slider.setBorder(new TitledBorder(new LineBorder(FILTER_FGCOLOR),
                "Duration", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, FILTER_FGCOLOR));

        slider.addChangeListener(this);

        return slider;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        // set the interval from combo box
        if(e.getSource().equals(intervalComboBox)) {
            EchoFilterDevice device = (EchoFilterDevice) midiDevice;
            device.setInterval((NoteValue)intervalComboBox.getSelectedItem());
        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {

        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {

            int duration = (int)source.getValue();

            EchoFilterDevice device = (EchoFilterDevice) midiDevice;
            device.setDuration(duration);
        }
    }


}
