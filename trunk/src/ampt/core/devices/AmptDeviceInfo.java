package ampt.core.devices;

import javax.sound.midi.MidiDevice.Info;

/**
 * A MidiDevice.Info object contains assorted data about a MidiDevice,
 * including its name, the company who created it, and descriptive text.
 *
 * @author Ben
 */
public class AmptDeviceInfo extends Info {

    // to simplify, the vendor and version will be static for all AMPT devices
    private static final String VENDOR = "AMPT";
    private static final String VERSION = "alpha"; // don't want to deal with this

    // the class is used to instantiate the AmptDevice
    private Class<? extends AmptDevice> _clazz;

    public AmptDeviceInfo(String name, String description, Class<? extends AmptDevice> clazz) {
        super(name, VENDOR, description, VERSION);
        _clazz = clazz;
    }

    public Class getDeviceClass() {
        return _clazz;
    }

} // class AmptDeviceInfo

