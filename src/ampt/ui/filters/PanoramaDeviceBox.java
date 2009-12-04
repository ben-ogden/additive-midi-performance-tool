package ampt.ui.filters;

import ampt.core.devices.PanoramaDevice;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Hashtable;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * PanoramaDeviceBox is the front end for the panorama filter.
 *
 * @author Ben
 */
public class PanoramaDeviceBox extends AmptMidiDeviceBox implements ChangeListener {

	private static final long serialVersionUID = 8307404198539430250L;

	private static final Color FILTER_BGCOLOR = Color.YELLOW;
    private static final Color FILTER_FGCOLOR = Color.BLACK;

    private JSlider _panSlider;


    /**
     * Create a new PanoramaDeviceBox.
     *
     * @param device the PanoramaDevice backing this component
     * @throws MidiUnavailableException if the device cannot be opened
     */
    public PanoramaDeviceBox(PanoramaDevice device) throws MidiUnavailableException {

        super(device, null, FILTER_BGCOLOR, FILTER_FGCOLOR);

        this.setPreferredSize(null);
        overridePaintComponent = false;

        this.setBorder(new TitledBorder(new LineBorder(FILTER_FGCOLOR),
                "Banana Panorama",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.BELOW_TOP,
                null,
                FILTER_FGCOLOR));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(FILTER_BGCOLOR);

        // create panorama _panSlider
        _panSlider = new JSlider(JSlider.HORIZONTAL, 0, 127, 64);
        _panSlider.setPaintTicks(false);
        _panSlider.setPaintLabels(true);
        _panSlider.setSnapToTicks(false);
        _panSlider.setBackground(FILTER_BGCOLOR);
        _panSlider.setForeground(FILTER_FGCOLOR);
        _panSlider.addChangeListener(this);

        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        labelTable.put( new Integer( 0 ), new JLabel("L") );
        labelTable.put( new Integer( 64 ), new JLabel("C") );
        labelTable.put( new Integer( 127 ), new JLabel("R") );
        _panSlider.setLabelTable( labelTable );
      
        JPanel sliderPanel = new JPanel();
        sliderPanel.setBackground(FILTER_BGCOLOR);
        sliderPanel.add(_panSlider);
        centerPanel.add(sliderPanel);

        // set default duration
        _panSlider.setValue(64);
        device.setPan(64);

        this.add(centerPanel, BorderLayout.CENTER);

        // override the debug checkbox
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(FILTER_BGCOLOR);
        this.add(emptyPanel, BorderLayout.SOUTH);
    }

    /**
     * Catch the change event from the slider.
     *
     * @param e the event
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        if(!_panSlider.getValueIsAdjusting()) {
            ((PanoramaDevice)midiDevice).setPan(_panSlider.getValue());
        }
    }
}
