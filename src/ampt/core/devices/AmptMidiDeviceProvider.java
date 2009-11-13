package ampt.core.devices;

import java.util.HashMap;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.spi.MidiDeviceProvider;

/**
 * This is the MidiDeviceProvider for AMPT. All devices supported by AMPT should
 * be registered here.
 *
 */
public class AmptMidiDeviceProvider extends MidiDeviceProvider{

    private HashMap<Info, Class> deviceMap;


    /**
     * Constructor which creates the list of supported devices.
     */
    public AmptMidiDeviceProvider(){

        deviceMap = new HashMap<Info, Class>();

        /*
         * Register all AMPT devices here with the Info and device Class
         */
        deviceMap.put(new ChordFilterDevice().getDeviceInfo(), ChordFilterDevice.class);
        deviceMap.put(new KeyboardDevice().getDeviceInfo(), KeyboardDevice.class);
        deviceMap.put(new NoteViewerDevice().getDeviceInfo(), NoteViewerDevice.class);
        
    }

    /**
     * Returns an array of MidiDevice.Info which contains the Info for each
     * supported device.
     *
     * @return array of Info
     */
    @Override
    public Info[] getDeviceInfo() {
        return deviceMap.keySet().toArray(new Info[0]);
    }

    /**
     * Get the MidiDevice that is represented by info.
     *
     * @param info the info describing the desired MidiDevice.
     * @return a MidiDevice matching the provided Info
     */
    @Override
    public MidiDevice getDevice(Info info) {

        // get the class for this device and attempt to instantiate it
        Class amptDeviceClass = deviceMap.get(info);

        if(null == amptDeviceClass) {
            throw new IllegalArgumentException(
                    "Device not supported by this provider.");
        }

        try {

            //TODO - preferable to use a static getInstance method instead of
            //       calling constructor in case some filters are singleton

            return (MidiDevice) amptDeviceClass.newInstance();

        } catch (InstantiationException ie) {
            throw new RuntimeException(ie);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }

    }

}
