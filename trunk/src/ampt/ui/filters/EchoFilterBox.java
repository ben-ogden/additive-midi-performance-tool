package ampt.ui.filters;

import ampt.core.devices.EchoFilterDevice;
import ampt.midi.note.Decay;
import ampt.midi.note.NoteValue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * An EchoFilterBox is the GUI for an EchoFilterDevice.
 * 
 * @author Ben
 */
public class EchoFilterBox extends MidiDeviceBox implements ActionListener,
        ChangeListener {

    private static final Color FILTER_BGCOLOR = Color.BLACK;
    private static final Color FILTER_FGCOLOR = Color.WHITE;

    private JComboBox intervalComboBox;
    private JSlider durationSlider;

    private JPanel decayButtons;

    /*
     * Image labels for decay box
     */
    JLabel decayLabel = new JLabel();
    Icon noneIcon = getIcon("images/decay-none.png");
    Icon linearIcon = getIcon("images/decay-linear.png");
    Icon logIcon = getIcon("images/decay-log.png");
    Icon expIcon = getIcon("images/decay-exp.png");


    /**
     * Create a new EchoFilterBox.
     *
     * @param device the EchoFilterDevice backing this box
     * @throws MidiUnavailableException if the device cannot be opened
     */
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
        JPanel durationPanel = new JPanel();
        durationPanel.setBackground(FILTER_BGCOLOR);
        durationPanel.setBorder(new TitledBorder(new LineBorder(FILTER_FGCOLOR),
                "Duration", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, FILTER_FGCOLOR));

        durationPanel.add(durationSlider);
        centerPanel.add(durationPanel);
        // set default duration
        durationSlider.setValue(5);
        device.setDuration(5);

        JPanel innerCenterPanel = new JPanel();
        innerCenterPanel.setLayout(new GridLayout(1,2));
        innerCenterPanel.setBackground(FILTER_BGCOLOR);

        // create decay button group
        decayButtons = createDecayButtonPanel();
        innerCenterPanel.add(decayButtons);


        // create note interval combo box
        intervalComboBox = createIntervalBox();
        // set default values
        intervalComboBox.setSelectedItem(NoteValue.QUARTER_NOTE);
        device.setInterval(NoteValue.QUARTER_NOTE);

        JPanel intPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        intPanel.setBackground(FILTER_BGCOLOR);
        intPanel.add(intervalComboBox);
        intPanel.setBorder(new TitledBorder(new LineBorder(FILTER_FGCOLOR),
                "Interval", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, FILTER_FGCOLOR));

        innerCenterPanel.add(intPanel);

        centerPanel.add(innerCenterPanel);
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
     * Creates the panel for the decay icon and buttons
     */
    private JPanel createDecayButtonPanel() {

        // create the radio buttons
        JRadioButton noneButton = new JRadioButton();

        noneButton = new JRadioButton(Decay.NONE.toString(), false);

        noneButton.setActionCommand(Decay.NONE.toString());
        noneButton.setBackground(FILTER_BGCOLOR);
        noneButton.setForeground(FILTER_FGCOLOR);

        JRadioButton linearButton = new JRadioButton(Decay.LINEAR.toString(), true);
        linearButton.setActionCommand(Decay.LINEAR.toString());
        linearButton.setBackground(FILTER_BGCOLOR);
        linearButton.setForeground(FILTER_FGCOLOR);

        JRadioButton logButton = new JRadioButton(Decay.LOGARITHMIC.toString(), false);
        logButton.setActionCommand(Decay.LOGARITHMIC.toString());
        logButton.setBackground(FILTER_BGCOLOR);
        logButton.setForeground(FILTER_FGCOLOR);

        JRadioButton expButton = new JRadioButton(Decay.EXPONENTIAL.toString(), false);
        expButton.setActionCommand(Decay.EXPONENTIAL.toString());
        expButton.setBackground(FILTER_BGCOLOR);
        expButton.setForeground(FILTER_FGCOLOR);

        // create button group
        ButtonGroup group = new ButtonGroup();
        group.add(noneButton);
        group.add(linearButton);
        group.add(logButton);
        group.add(expButton);

        // set action listeners
        noneButton.addActionListener(this);
        linearButton.addActionListener(this);
        logButton.addActionListener(this);
        expButton.addActionListener(this);

        // create panel to hold the buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        panel.setBackground(FILTER_BGCOLOR);
        panel.setForeground(FILTER_FGCOLOR);
        panel.setBorder(new TitledBorder(new LineBorder(FILTER_FGCOLOR),
                "Decay", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, FILTER_FGCOLOR));

        decayLabel.setIcon(linearIcon);
        decayLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(decayLabel);

        panel.add(noneButton);
        panel.add(linearButton);
        panel.add(logButton);
        panel.add(expButton);

        return panel;
    }

    /*
     * Creates the Interval combo box
     */
    private JComboBox createIntervalBox() {

        Vector<NoteValue> noteValues = new Vector<NoteValue>();
        for(NoteValue noteValue : NoteValue.values()) {
            noteValues.add(noteValue);
        }

        JComboBox box = new JComboBox(noteValues);
        box.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        box.setBackground(FILTER_BGCOLOR);
        box.setForeground(FILTER_FGCOLOR);
        // disable key handler
        box.setKeySelectionManager(new EmptyKeySelectionManager());
        // set actionlistener
        box.addActionListener(this);

        return box;
    }

    /*
     * Creates the duration slider component.
     */
    private JSlider createDurationSlider() {

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 20, 0);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setBackground(FILTER_BGCOLOR);
        slider.setForeground(FILTER_FGCOLOR);
        slider.addChangeListener(this);

        return slider;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        // action for interval from combo box
        if(e.getSource().equals(intervalComboBox)) {
            EchoFilterDevice device = (EchoFilterDevice) midiDevice;
            device.setInterval((NoteValue)intervalComboBox.getSelectedItem());
        }
        // action for decay radio buttons
        else {

            EchoFilterDevice device = (EchoFilterDevice) midiDevice;

            if(Decay.NONE.toString().equals(e.getActionCommand())) {
                decayLabel.setIcon(noneIcon);
                device.setDecay(Decay.NONE);
            }
            else if(Decay.LINEAR.toString().equals(e.getActionCommand())) {
                decayLabel.setIcon(linearIcon);
                device.setDecay(Decay.LINEAR);
            }
            else if(Decay.LOGARITHMIC.toString().equals(e.getActionCommand())) {
                decayLabel.setIcon(logIcon);
                device.setDecay(Decay.LOGARITHMIC);
            }
            else if(Decay.EXPONENTIAL.toString().equals(e.getActionCommand())) {
                decayLabel.setIcon(expIcon);
                device.setDecay(Decay.EXPONENTIAL);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        JSlider source = (JSlider)e.getSource();

        // only process state change if slider is not moving
        if (!source.getValueIsAdjusting()) {

            int duration = (int)source.getValue();

            EchoFilterDevice device = (EchoFilterDevice) midiDevice;
            device.setDuration(duration);
        }
    }

    /*
     * Loads an Icon
     */
    private Icon getIcon(String path) {

        InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(path);
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(ImageIO.read(inStream));
        } catch (IOException ex) {
            // do nothing
        }
        return icon;
    }


}
