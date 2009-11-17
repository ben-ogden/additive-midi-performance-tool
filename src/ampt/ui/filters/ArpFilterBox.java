package ampt.ui.filters;

import ampt.core.devices.ArpFilterDevice;
import javax.sound.midi.MidiUnavailableException;
/**
 * The display box for an arp filter
 * 
 * @author Rob
 */
public class ArpFilterBox extends MidiDeviceBox {

    public ArpFilterBox(ArpFilterDevice device) throws MidiUnavailableException {
        super(device);
    }
}
