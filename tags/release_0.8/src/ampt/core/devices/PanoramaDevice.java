package ampt.core.devices;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/**
 *
 * @author Ben
 */
public class PanoramaDevice extends AmptDevice {

    private static final String DEVICE_NAME = "Banana Panorama";
    private static final String DEVICE_DESCRIPTION = "Modifies the pan setting on attached devices.";

    // the pan setting to apply min 0, max 127
    private int _pan;

    public PanoramaDevice() {
        super(DEVICE_NAME, DEVICE_DESCRIPTION);

        _pan = 64;
    }

    public int getPan() {
        return _pan;
    }

    /**
     * Set the panorama. Each call to setPan results in the transmission of
     * pan messages to all transmitters on this device.
     *
     * @param pan, the panorama between 0 and 127
     */
    public void setPan(int pan) {
        if(pan < 0) {
            _pan = 0;
        } else if (pan > 127) {
            _pan = 127;
        } else {
            _pan = pan;
        }
        
        sendPanorama();
    }

    /*
     * Send pan message out on all transmitters.
     */
    private void sendPanorama() {
        ShortMessage msg = new ShortMessage();
        try {
            msg.setMessage(180, 10, _pan);
            sendNow(msg);
        } catch (InvalidMidiDataException ex) {
            log(DEVICE_NAME + " : Unable to send message. " + ex.getMessage());
        }
    }

    /**
     * PanoramaDevice does not accept MIDI input.
     *
     * @return 0, always
     */
    @Override
    public int getMaxReceivers() {
        return 0;
    }
    
    @Override
    protected void initDevice() throws MidiUnavailableException {
        // nothing to do
    }

    @Override
    protected void closeDevice() {
        // nothing to do
    }

    @Override
    protected Receiver getAmptReceiver() throws MidiUnavailableException {
        throw new UnsupportedOperationException("PanFilterDevice has no receivers.");
    }

}
