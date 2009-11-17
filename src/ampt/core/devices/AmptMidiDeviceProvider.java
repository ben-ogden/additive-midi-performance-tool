package ampt.core.devices;

import java.util.LinkedList;
import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.spi.MidiDeviceProvider;

/**
 * This is the MidiDeviceProvider for AMPT. All devices supported by AMPT should
 * be registered here.
 *
 */
public class AmptMidiDeviceProvider extends MidiDeviceProvider{

    /*
     * Synchronized, sorted set since multiple threads may be acting on the
     * device provider and since we want devices to always appear in the same
     * order.
     */
    private List<Info> deviceList;

    /**
     * Create a new AmptMidiDeviceProvider. This is called by MidiSystem and is
     * registered in META-INF.services/javax.sound.midi.spi.MidiDeviceProvider.
     */
    public AmptMidiDeviceProvider(){

        deviceList = new LinkedList<Info>();

        /*
         * Register all AMPT devices here with the device name and device Class
         */
        deviceList.add(new ChordFilterDevice().getDeviceInfo());
        deviceList.add(new KeyboardDevice().getDeviceInfo());
        deviceList.add(new NoteViewerDevice().getDeviceInfo());
        deviceList.add(new ArpFilterDevice().getDeviceInfo());

    }

    /**
     * Returns an array of MidiDevice.Info which contains the Info for each
     * supported device.
     *
     * @return array of Info
     */
    @Override
    public Info[] getDeviceInfo() {
        return deviceList.toArray(new Info[0]);
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
        Class amptDeviceClass = ((AmptDeviceInfo)info).getDeviceClass();

        if(null == amptDeviceClass) {
            throw new IllegalArgumentException(
                    "Device not supported by this provider.");
        }

        try {

            // create a new AmptMidiDevice
            return (MidiDevice) amptDeviceClass.newInstance();

        } catch (InstantiationException ie) {
            throw new RuntimeException(ie);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }

    }

}
