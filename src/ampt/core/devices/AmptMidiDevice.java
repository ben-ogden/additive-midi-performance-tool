package ampt.core.devices;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

/**
 * All AmptDevices should implement AmptMidiDevice.
 *
 * @author Ben
 */
public interface AmptMidiDevice extends MidiDevice {

    /**
     * Connect a transmitter (output) from this MidiDevice to a receiver (input)
     * of the given MidiDevice.
     *
     * @param device the MidiDevice to connect to
     * @throws MidiUnavailableException if the connection cannot be made
     */
    public void connectTo(MidiDevice device) throws MidiUnavailableException;

}
